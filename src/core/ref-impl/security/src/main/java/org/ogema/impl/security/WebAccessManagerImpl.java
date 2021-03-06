/**
 * This file is part of OGEMA.
 *
 * OGEMA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OGEMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OGEMA. If not, see <http://www.gnu.org/licenses/>.
 */
package org.ogema.impl.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ogema.accesscontrol.SessionAuth;
import org.ogema.core.administration.AdministrationManager;
import org.ogema.core.application.AppID;
import org.ogema.core.security.WebAccessManager;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.slf4j.Logger;

public class WebAccessManagerImpl implements WebAccessManager {

	private static final long serialVersionUID = 2191312216443580107L;

	protected static final String OLDREQ_ATTR_NAME = "requestBeforeLogin";

	private static final String LOGIN_PATH = "/web/login.html";

	private static final long LOGIN_TIMEOUT = 60000;

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	HttpService http;
	// FIXME: we should not depend on the implementation... rather on the interface
	// to allow easy replacement of implementation ...
	PermissionManagerImpl permMan;

	// app name vs. context
	ConcurrentHashMap<String, OgemaHttpContext> contextRegs;

	byte[] scratch;

	AdministrationManager admin;

	private RestHttpContext restContext;

	private M2MLogin m2mLogin;

	// app id vs. list of resource names. Note that the resource names are not unique compared to the aliases. This
	// information is needed in {@link OgemaHttpContext.getResource} to get the
	// requested resource from the according bundle file.
	// ConcurrentHashMap<AppID, ArrayList<String>> resourcesNames;

	public WebAccessManagerImpl(PermissionManagerImpl pm, AdministrationManager admin) {
		// this.rnd = new Random(System.currentTimeMillis());
		this.http = pm.http;
		this.permMan = pm;
		this.contextRegs = new ConcurrentHashMap<>();

		scratch = new byte[512];

		this.admin = admin;
		this.restContext = new RestHttpContext();
		new RestAccess(permMan, admin);
		this.m2mLogin = new M2MLogin(permMan);
		try {
			this.http.registerResources("/login", "/web", null);
			LoginServlet loginServlet = new LoginServlet(permMan, permMan.accessMan.usrAdmin);
			this.http.registerServlet(LoginServlet.LOGIN_SERVLET_PATH, loginServlet, null, null);
//			registerWebResource(LoginServlet.LOGIN_SERVLET_PATH, loginServlet);
			// this.http.registerServlet("/rest", this.restAccess, null, restContext);
			this.http.registerServlet("/m2mLogin", this.m2mLogin, null, restContext);
		} catch (NamespaceException | ServletException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unregisterWebResource(String alias) {

		// alias = aliasToConvention(alias);

		// Remove the resource from the list of registered resources
		AppID app = admin.getContextApp(getClass());
		if (app == null) {
			logger.error("Tried to unregister web resource {}, but could not determine app", alias);
			return;
		}
		OgemaHttpContext ctx = contextRegs.get(app.getIDString());
		ctx.resources.remove(alias);
		ctx.servlets.remove(alias);

		try {
			http.unregister(alias);
		} catch (IllegalArgumentException iae) {
			logger.error("No registration found " + alias);
		}

		// If the list is empty the http context could be unregistered too
		if (ctx.resources.isEmpty() && ctx.servlets.isEmpty())
			contextRegs.remove(alias);
	}

	/**
	 * This Method adds "/ogema" to the beginning of the alias
	 */
	private String aliasToConvention(String alias) {
		if (!alias.startsWith("/ogema")) {
			if (alias.startsWith("/")) {
				alias = "/ogema" + alias;
			}
			else {
				alias = "/ogema/" + alias;
			}
		}
		return alias;
	}

	/**
	 * This method is called by the application or a osgi bundle. The registration is delegated to the http service with
	 * a OgemaHttpContext each App/Bundle. Bundle reference and the alias are registered by WebResourceManager so these
	 * information can be provided to security handling in OgemaHttpContext.
	 */
	@Override
	public String registerWebResource(String alias, String name) {

		// alias = aliasToConvention(alias);

		String result = null;
		// Check if the bundle already registereusrd any resources
		// In this case we use the HttpContext already created for this bundle.
		// Oterwise we need a new one.
		AppID app = admin.getContextApp(getClass());
		// If no App found, the registration couldn't be accomplished.
		if (app == null)
			return null;
		String appid = app.getIDString();
		OgemaHttpContext httpCon = this.contextRegs.get(appid);
		if (httpCon == null) {
			httpCon = new OgemaHttpContext(permMan, app);
		}

		try {
			// 1. register Resource to the http service
			http.registerResources(alias, name, httpCon);
		} catch (NamespaceException e) {
			// the alias is already used, generate another one and try it again
			result = generateAlias(alias, app);
		}
		try {
			if (result != null) // if a unique alias had been generated
				http.registerResources(result, name, httpCon);
			else
				result = alias;
		} catch (NamespaceException e) {
			// this could happen is the app restarted or updated.
			// In this case nothing is to do.
		}

		contextRegs.put(appid, httpCon);
		httpCon.resources.put(result, name);
		return result;
	}

	/**
	 * Like registerWebResource above but registerServlet instead of registerResource
	 */
	@Override
	public String registerWebResource(String alias, Servlet servlet) {

		// alias = aliasToConvention(alias);

		// Check if the app already registered any resources
		// In this case we use the HttpContext already created for this app
		// Otherwise we need a new one.
		AppID app = admin.getContextApp(getClass());
		// If no App found, the registration couldn't be accomplished.
		if (app == null)
			return null;
		String appid = app.getIDString();
		OgemaHttpContext httpCon = this.contextRegs.get(appid);
		if (httpCon == null) {
			httpCon = new OgemaHttpContext(permMan, app);
		}

		String result = null;

		try {
			// 1. register Resource to the http service
			http.registerServlet(alias, servlet, null, httpCon);
		} catch (NamespaceException e) {
			// the alias is already used, generate another one and try it again
			result = generateAlias(alias, app);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		try {
			if (result != null)
				http.registerServlet(result, servlet, null, httpCon);
			else
				result = alias;
		} catch (NamespaceException e) {
			// this should not happen
			// if it happens though generateAlias doesn't work well
			logger.warn("Non-unique alias is generated: " + alias);
			e.printStackTrace();
			throw new RuntimeException();

		} catch (ServletException e) {
			e.printStackTrace();
			e.getRootCause().printStackTrace();
			throw new RuntimeException();
		}

		contextRegs.put(appid, httpCon);
		httpCon.servlets.put(result, app);

		return result;
	}

	String generateAlias(String alias, AppID id) {
		return alias + id.getIDString().hashCode();
	}

	synchronized SessionAuth handleNewSession(HttpServletRequest request, HttpServletResponse response) {
		SessionAuth result = null;
		HttpSession ses = request.getSession(true);
		if (Configuration.DEBUG) {
			logger.info("Creation time: " + ses.getCreationTime());
			logger.info("SessionID: " + ses.getId());
		}

		/*
		 * If the session is new send the login page
		 */
		if (Configuration.DEBUG) {
			logger.debug("Unknown session: send login request.");
		}
		InputStream is;
		OutputStream bout;
		int len = 0;
		try {
			is = getClass().getResource(LOGIN_PATH).openStream();
			bout = response.getOutputStream();
			do {
				len = is.read(scratch);
				if (len == -1) // This check is needed due to jetty's own OutputStream implementation.
					break;
				bout.write(scratch, 0, len);
				// NOTE: Jetty server has its own OutputStream class its write method doesn't throw this
				// IndexOutOfBoundsException

			} while (true);
			response.flushBuffer();
			try {
				wait(LOGIN_TIMEOUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// result = sessions.get(ses.getId());
			result = (SessionAuth) ses.getAttribute(SessionAuth.AUTH_ATTRIBUTE_NAME);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// If the Session is not new or the authentication was successful
		if (result != null) {
			if (Configuration.DEBUG) {
				logger.debug("Authentication succeded.");
			}
		}
		else if (Configuration.DEBUG) {
			logger.debug("Authentication failed or timed out.");
		}
		return result;
	}

	public String registerOTP(AppID app, HttpSession ses) {
		SessionAuth auth = (SessionAuth) ses.getAttribute(SessionAuth.AUTH_ATTRIBUTE_NAME);
		if (auth == null)
			return null;
		String otp = auth.registerAppOtp(app);
		return otp;
	}

	@Override
	public boolean authenticate(HttpSession ses, String usr, String pwd) {

		/*
		 * Is there any authorization object on the current session?
		 */
		SessionAuth sa = (SessionAuth) ses.getAttribute("ogemaAuth");
		if (sa != null) {
			String tmpotp = sa.getOtpList().get(usr);
			/*
			 * If the app is already registered with this one time password the access is permitted.
			 */
			if (tmpotp != null && tmpotp.equals(pwd)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, String> getRegisteredResources(AppID appid) {
		OgemaHttpContext cntx = contextRegs.get(appid.getIDString());
		if (cntx == null)
			return Collections.emptyMap();
		else
			return cntx.resources;
	}
}
