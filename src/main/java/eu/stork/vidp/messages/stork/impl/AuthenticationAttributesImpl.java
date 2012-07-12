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


package eu.stork.vidp.messages.stork.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

import eu.stork.vidp.messages.stork.AuthenticationAttributes;
import eu.stork.vidp.messages.stork.VIDPAuthenticationAttributes;

public class AuthenticationAttributesImpl extends
		AbstractValidatingXMLObject implements
		AuthenticationAttributes {
	
	private VIDPAuthenticationAttributes authenticationAttributes;
	
	
	protected AuthenticationAttributesImpl(String namespaceURI,
			String elementLocalName, String namespacePrefix) {
		super(namespaceURI, elementLocalName, namespacePrefix);		
	}

	public VIDPAuthenticationAttributes getVIDPAuthenticationAttributes() {
		return authenticationAttributes;
	}

	public void setVIDPAuthenticationAttributes(
			VIDPAuthenticationAttributes authenticationAttributes) {
		this.authenticationAttributes = authenticationAttributes;
	}
	

	public List<XMLObject> getOrderedChildren() {
		ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (authenticationAttributes != null) {
            children.add(authenticationAttributes);
        }
        
        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
	}

}
