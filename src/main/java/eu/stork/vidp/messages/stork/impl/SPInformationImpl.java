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

import eu.stork.vidp.messages.stork.SPAuthRequest;
import eu.stork.vidp.messages.stork.SPCertEnc;
import eu.stork.vidp.messages.stork.SPCertSig;
import eu.stork.vidp.messages.stork.SPID;
import eu.stork.vidp.messages.stork.SPInformation;

public class SPInformationImpl extends
		AbstractValidatingXMLObject implements
		SPInformation {
	
	private SPID spID;
	
	private SPCertSig spCertSig;
	
	private SPCertEnc spCertEnc;
	
	private SPAuthRequest spAuthRequest;

	protected SPInformationImpl(String namespaceURI,
			String elementLocalName, String namespacePrefix) {
		super(namespaceURI, elementLocalName, namespacePrefix);		
	}

	public SPAuthRequest getSPAuthRequest() {
		return spAuthRequest;
	}

	public SPCertEnc getSPCertEnc() {
		return spCertEnc;
	}

	public SPCertSig getSPCertSig() {
		return spCertSig;
	}

	public SPID getSPID() {
		return spID;
	}

	public void setSPAuthRequest(SPAuthRequest spAuthRequest) {
		this.spAuthRequest = spAuthRequest;
	}

	public void setSPCertEnc(SPCertEnc spCertEnc) {
		this.spCertEnc = spCertEnc;
	}

	public void setSPCertSig(SPCertSig spCertSig) {
		this.spCertSig = spCertSig;
	}

	public void setSPID(SPID spID) {
		this.spID = spID;
	}

	public List<XMLObject> getOrderedChildren() {
		ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (spID != null) {
            children.add(spID);
        }
        
        if (spCertSig != null) {
            children.add(spCertSig);
        }
        
        if (spCertEnc != null) {
            children.add(spCertEnc);
        }
        
        if (spAuthRequest != null) {
            children.add(spAuthRequest);
        }
        
        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
	}

	

	

	
}
