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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;

import eu.stork.vidp.messages.stork.SPAuthRequest;

public class SPAuthRequestUnmarshaller extends AbstractXMLObjectUnmarshaller {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractSAMLObjectUnmarshaller.class);

    /** Constructor. */
    public SPAuthRequestUnmarshaller() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
    	SPAuthRequest spAuthRequest = (SPAuthRequest) parentXMLObject;

    	spAuthRequest.getUnknownXMLObjects().add(childXMLObject);
    }

    /**
     * {@inheritDoc}
     */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        log.debug("Ignorning unknown attribute {}", attribute.getLocalName());
    }

    /**
     * {@inheritDoc}
     */
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
        log.debug("Ignoring element content {}", elementContent);
    }
}
