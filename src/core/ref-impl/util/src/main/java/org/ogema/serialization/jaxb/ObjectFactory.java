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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.13 at 08:01:54 AM CEST 
//

package org.ogema.serialization.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * org.ogema.rest.xml package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _Resource_QNAME = new QName("http://www.ogema-source.net/REST", "resource");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
	 * org.ogema.rest.xml
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Resource }
	 * 
	 */
	public Resource createResource() {
		return new Resource();
	}

	/**
	 * Create an instance of {@link OpaqueResource }
	 * 
	 */
	public OpaqueResource createOpaqueResource() {
		return new OpaqueResource();
	}

	/**
	 * Create an instance of {@link FloatResource }
	 * 
	 */
	public FloatResource createFloatResource() {
		return new FloatResource();
	}

	/**
	 * Create an instance of {@link StringSchedule }
	 * 
	 */
	public StringSchedule createStringSchedule() {
		return new StringSchedule();
	}

	/**
	 * Create an instance of {@link BooleanSchedule }
	 * 
	 */
	public BooleanSchedule createBooleanSchedule() {
		return new BooleanSchedule();
	}

	/**
	 * Create an instance of {@link SampledTime }
	 * 
	 */
	public SampledTime createSampledTime() {
		return new SampledTime();
	}

	/**
	 * Create an instance of {@link SampledOpaque }
	 * 
	 */
	public SampledOpaque createSampledOpaque() {
		return new SampledOpaque();
	}

	/**
	 * Create an instance of {@link IntegerResource }
	 * 
	 */
	public IntegerResource createIntegerResource() {
		return new IntegerResource();
	}

	/**
	 * Create an instance of {@link IntegerSchedule }
	 * 
	 */
	public IntegerSchedule createIntegerSchedule() {
		return new IntegerSchedule();
	}

	/**
	 * Create an instance of {@link SampledFloat }
	 * 
	 */
	public SampledFloat createSampledFloat() {
		return new SampledFloat();
	}

	/**
	 * Create an instance of {@link StringResource }
	 * 
	 */
	public StringResource createStringResource() {
		return new StringResource();
	}

	/**
	 * Create an instance of {@link SampledInteger }
	 * 
	 */
	public SampledInteger createSampledInteger() {
		return new SampledInteger();
	}

	/**
	 * Create an instance of {@link BooleanResource }
	 * 
	 */
	public BooleanResource createBooleanResource() {
		return new BooleanResource();
	}

	/**
	 * Create an instance of {@link OpaqueSchedule }
	 * 
	 */
	public OpaqueSchedule createOpaqueSchedule() {
		return new OpaqueSchedule();
	}

	/**
	 * Create an instance of {@link SampledString }
	 * 
	 */
	public SampledString createSampledString() {
		return new SampledString();
	}

	/**
	 * Create an instance of {@link TimeSchedule }
	 * 
	 */
	public TimeSchedule createTimeSchedule() {
		return new TimeSchedule();
	}

	/**
	 * Create an instance of {@link ResourceLink }
	 * 
	 */
	public ResourceLink createResourceLink() {
		return new ResourceLink();
	}

	/**
	 * Create an instance of {@link SampledBoolean }
	 * 
	 */
	public SampledBoolean createSampledBoolean() {
		return new SampledBoolean();
	}

	/**
	 * Create an instance of {@link FloatSchedule }
	 * 
	 */
	public FloatSchedule createFloatSchedule() {
		return new FloatSchedule();
	}

	/**
	 * Create an instance of {@link TimeResource }
	 * 
	 */
	public TimeResource createTimeResource() {
		return new TimeResource();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Resource }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://www.ogema-source.net/REST", name = "resource")
	public JAXBElement<Resource> createResource(Resource value) {
		return new JAXBElement<Resource>(_Resource_QNAME, Resource.class, null, value);
	}

}