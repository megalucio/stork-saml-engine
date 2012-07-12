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


package eu.stork.vidp.messages.validation;

import java.util.regex.Pattern;

import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.saml2.metadata.validator.RequestedAttributeSchemaValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.validation.ValidationException;

import eu.stork.vidp.messages.common.STORKConstants;

public class StorkRequestedAttributeValidator extends
		RequestedAttributeSchemaValidator {
	
	private static final String PATTERN_ISAGEOVER = "^[0-9]{1,3}$";

	public StorkRequestedAttributeValidator() {

		super();
	}

	@Override
	public void validate(RequestedAttribute attr) throws ValidationException {

		super.validate(attr);

		if (attr.getName() == null) {

			throw new ValidationException("Name is required.");
		}

		if (attr.getNameFormat() == null) {

			throw new ValidationException("NameFormat is required.");
		}
		
		if (!STORKConstants.FULL_STORK_ATTRIBUTE_SET.contains(attr.getName()) && attr.isRequired()) {
			throw new ValidationException("Unknown attribute " + attr.getName() + " requested mandatory.");
		}
		
		if (attr.getName().equals(STORKConstants.STORK_ATTRIBUTE_ISAGEOVER)) {
			if (attr.getAttributeValues().isEmpty()) {
				throw new ValidationException("isAgeOver requires attribute value");
			}
			
			XMLObject attrValueObject = attr.getAttributeValues().get(0);
			
			if (attrValueObject instanceof XSString) {
				if (!Pattern.matches(PATTERN_ISAGEOVER, ((XSString) attr.getAttributeValues().get(0)).getValue())) {
					throw new ValidationException("Value for isAgeOver has incorrect format.");
				}
			} else if (attrValueObject instanceof XSAny) {
				if (!Pattern.matches(PATTERN_ISAGEOVER, ((XSAny) attrValueObject).getTextContent())) {
					throw new ValidationException("Value for isAgeOver has incorrect format.");
				}				
				
			} else {
				throw new ValidationException("Value for isAgeOver has incorrect format.");
			}
			
		}
		
	}

}
