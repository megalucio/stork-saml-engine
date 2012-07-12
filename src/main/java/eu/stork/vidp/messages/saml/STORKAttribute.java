/*
 * Copyright 2011 by Graz University of Technology, Austria
 * The Austrian STORK Modules have been developed by the E-Government
 * Innovation Center EGIZ, a joint initiative of the Federal Chancellery
 * Austria and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */


/**
 * 
 */
package eu.stork.vidp.messages.saml;

import javax.xml.namespace.QName;

import org.opensaml.saml2.core.Attribute;

import eu.stork.vidp.messages.common.STORKConstants;

/**
 * Interface extending original SAML Attribute for STORK with the XML attributeStatus attribute
 * {@inheritDoc} 
 * @author bzwattendorfer
 *
 */
public interface STORKAttribute extends Attribute {
	
	public static final String STORK_ATTRIBUTE_STATUS_ATTTRIB_NAME = "AttributeStatus";
	
	public static final QName DEFAULT_STORK_ATTRIBUTE_QNAME = new QName(STORKConstants.STORK10_NS, STORK_ATTRIBUTE_STATUS_ATTTRIB_NAME, STORKConstants.STORK10_PREFIX);
	
	public static final String ALLOWED_ATTRIBUTE_STATUS_AVAIL = "Available";
	public static final String ALLOWED_ATTRIBUTE_STATUS_NOT_AVAIL = "NotAvailable";
	public static final String ALLOWED_ATTRIBUTE_STATUS_WITHHELD = "Withheld";
	
	/**
	 * Sets the STORK attributeStatus
	 * @param attributeStatus
	 */
	public void setAttributeStatus(String attributeStatus);
	
	/**
	 * Gets the STORK attributeStatus
	 * @return
	 */
	public String getAttributeStatus();

}
