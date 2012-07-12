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


package eu.stork.vidp.messages.stork;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.xml.validation.ValidatingXMLObject;

import eu.stork.vidp.messages.common.STORKConstants;

public interface RequestedAttributes extends
		ValidatingXMLObject {
	
	/** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "RequestedAttributes";
    
    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(STORKConstants.STORKP10_NS, DEFAULT_ELEMENT_LOCAL_NAME,
    		STORKConstants.STORKP10_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestedAttributesType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(STORKConstants.STORKP10_NS, TYPE_LOCAL_NAME,
    		STORKConstants.STORKP10_PREFIX);
    
    public void setRequestedAttributes(List<RequestedAttribute> requestedAttributes);
    
    public List<RequestedAttribute> getRequestedAttributes();
    
}
