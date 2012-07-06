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
package org.ogema.frameworkadministration.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tgries
 */
public class UserJsonPermissionCondition implements Serializable {

	private static final long serialVersionUID = 6625360065961582803L;

	private String name;
	private String mode;
	private UserJsonCondition condition;
	private List<UserJsonPermission> permissions = new ArrayList<UserJsonPermission>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public UserJsonCondition getCondition() {
		return condition;
	}

	public void setCondition(UserJsonCondition condition) {
		this.condition = condition;
	}

	public List<UserJsonPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserJsonPermission> permissions) {
		this.permissions = permissions;
	}

}