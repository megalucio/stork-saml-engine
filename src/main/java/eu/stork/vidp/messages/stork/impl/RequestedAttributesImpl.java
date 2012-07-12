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

package eu.stork.vidp.messages.stork.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

import eu.stork.vidp.messages.stork.RequestedAttributes;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.AudienceRestriction}.
 */
public class RequestedAttributesImpl extends AbstractValidatingXMLObject implements RequestedAttributes {

    /** List of the audiences. */
    private XMLObjectChildrenList<RequestedAttribute> requestedAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RequestedAttributesImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        requestedAttributes = new XMLObjectChildrenList<RequestedAttribute>(this);
    }

    /** {@inheritDoc} */
    public List<RequestedAttribute> getRequestedAttributes() {
        return requestedAttributes;
    }
    
    
    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(requestedAttributes);

        return Collections.unmodifiableList(children);
    }

	public void setRequestedAttributes(
			List<RequestedAttribute> requestedAttributes) {
		this.requestedAttributes = (XMLObjectChildrenList<RequestedAttribute>) requestedAttributes;
		
	}

	

	
}