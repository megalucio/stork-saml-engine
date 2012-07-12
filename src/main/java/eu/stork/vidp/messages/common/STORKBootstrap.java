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


package eu.stork.vidp.messages.common;

import java.io.InputStream;

import org.opensaml.DefaultBootstrap;
import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLConfigurator;

/**
 * Class extending the default bootstrap mechanism of OpenSAML
 * @author bzwattendorfer
 *
 */
public class STORKBootstrap extends DefaultBootstrap {
	
	/**
	 * Extends the default bootstrap mechanism of OpenSAML
	 * Adds STORK schemata and extension elements
	 * @throws ConfigurationException
	 */
	public static synchronized void bootstrap() throws ConfigurationException {
		
		SAMLSchemaBuilder.addExtensionSchema("stork-schema-assertion-1.0.xsd");
		SAMLSchemaBuilder.addExtensionSchema("stork-schema-protocol-1.0.xsd");
		
		DefaultBootstrap.bootstrap();
       
        initStorkConfig("saml2-stork-config.xml");        
        
    }
	
	/**
	 * Initializes OpenSAML with config
	 * @param xmlConfig XML Config for STORK and SAML2
	 * @throws ConfigurationException
	 */
	private static void initStorkConfig(String xmlConfig) throws ConfigurationException {		
		
		XMLConfigurator configurator = new XMLConfigurator();
		
		InputStream is = STORKBootstrap.class.getClassLoader().getResourceAsStream(xmlConfig);

		configurator.load(is);		
	}
		

}
