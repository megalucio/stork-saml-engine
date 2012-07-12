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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.validator.AttributeSchemaValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.validation.ValidationException;

import eu.stork.vidp.messages.common.STORKConstants;
import eu.stork.vidp.messages.saml.STORKAttribute;

public class StorkAttributeValidator extends AttributeSchemaValidator {
	
	private static final String PATTERN_EIDENTIFIER = "^[A-Z]{2}/[A-Z]{2}/[A-Za-z0-9+/=\r\n]+$";
	private static final String PATTERN_GENDER = "^[MF]{1}$";	
	private static final String PATTERN_COUNTRYCODEOFBIRTH = "^[A-Z]{2}|[A-Z]{4}$";
	private static final String PATTERN_COUNTRYCODE = "^[A-Z]{2}$";
	private static final String PATTERN_MARTIALSTATUS = "^[SMPDW]{1}$";
	private static final String PATTERN_EMAIL = "^[-+.\\w]{1,64}@[-.\\w]{1,64}\\.[-.\\w]{2,6}$";
	private static final String PATTERN_AGE = "^[0-9]{1,3}$";
	private static final int MAX_AGE = 120;
	private static final String PATTERN_ISAGEOVER = PATTERN_AGE;
	private static final String PATTERN_CITIZENQAALEVEL = "^[1-4]{1}$";

		
	/**
	 * Constructor
	 * 
	 */
	public StorkAttributeValidator() {
		
		super();
	}
	
	@Override
    public void validate(Attribute attr) throws ValidationException {

		super.validate(attr);
	
		if(attr.getName() == null) {
			
			throw new ValidationException("Name is required.");
		}
		
		if(attr.getNameFormat() == null) {
			
			throw new ValidationException("NameFormat is required.");
		}
		
		
		if(attr.getUnknownAttributes() != null) {
			
			AttributeMap map = attr.getUnknownAttributes();
			
			String value = map.get(STORKAttribute.DEFAULT_STORK_ATTRIBUTE_QNAME);
			
			if (value == null || value.equals(STORKAttribute.ALLOWED_ATTRIBUTE_STATUS_AVAIL)) {
				//if AttributeStatus not present, default is "Available" thus AttributeValue must be present
				if (attr.getAttributeValues().isEmpty()) {
					//isAgeOver can have no value
					if (!attr.getName().equals(STORKConstants.STORK_ATTRIBUTE_ISAGEOVER)) {
						throw new ValidationException("AttributeStatus indicates that attribute is available but no AttributeValue is present.");
					}
				}					
				
				//throw new ValidationException("AttributeStatus not present.");
				
			} else if(!value.equals(STORKAttribute.ALLOWED_ATTRIBUTE_STATUS_AVAIL) &&
					!value.equals(STORKAttribute.ALLOWED_ATTRIBUTE_STATUS_NOT_AVAIL) &&
					!value.equals(STORKAttribute.ALLOWED_ATTRIBUTE_STATUS_WITHHELD)) {
				
				throw new ValidationException("AttributeStatus is invalid.");
			}
					
		}
		
		if (!attr.getAttributeValues().isEmpty()) {
			//validate individual attributes if present
			XMLObject attrValueObject = attr.getAttributeValues().get(0);
							
			if (!(attrValueObject instanceof XSString)) {
				//Only validate String attributes 
				return;
			}
			
			String value = ((XSString) attr.getAttributeValues().get(0)).getValue();
			String attrName = attr.getName();
			
			//only isAgeOver can be empty if provided
			if (value == null) { 
				//only isAgeOver can be empty if provided
				if (attrName.equals(STORKConstants.STORK_ATTRIBUTE_ISAGEOVER)) {
					return;
				} else {
					throw new ValidationException("Provided AttributeValue is empty");
				}
			}
			
			//validate eIdentifier		
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_EIDENTIFIER, PATTERN_EIDENTIFIER);
			
			//validate gender
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_GENDER, PATTERN_GENDER);
			
			//validate dateOfBirth
			if (attrName.equals(STORKConstants.STORK_ATTRIBUTE_DATEOFBIRTH))  {
				verifyDate(value);
			}
			
			//validate countryCode of birth
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_COUNTRYCODEOFBIRTH, PATTERN_COUNTRYCODEOFBIRTH);
			
			//validate countryCode 
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_NATIONALITYCODE, PATTERN_COUNTRYCODE);
			
			//validate martialStatus 
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_MARTIALSTATUS, PATTERN_MARTIALSTATUS);
			
			//validate email
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_EMAIL, PATTERN_EMAIL);
			
			//validate age and isAgeOver
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_AGE, PATTERN_AGE);
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_ISAGEOVER, PATTERN_ISAGEOVER);
			if (attr.getName().equals(STORKConstants.STORK_ATTRIBUTE_AGE) || attr.getName().equals(STORKConstants.STORK_ATTRIBUTE_ISAGEOVER)) {
				if (Integer.valueOf(((XSString) attr.getAttributeValues().get(0)).getValue()) > MAX_AGE) {
					throw new ValidationException("Maximum age reached");
				}
			}
			
			validateAttributeValueFormat(value, attrName, STORKConstants.STORK_ATTRIBUTE_CITIZENQAALEVEL, PATTERN_CITIZENQAALEVEL);
		}
		
	}
	
	private void validateAttributeValueFormat(String value, String currentAttrName, String attrNameToTest, String pattern) throws ValidationException {
		if (currentAttrName.equals(attrNameToTest)) {
			if (!Pattern.matches(pattern, value)) {
				throw new ValidationException(attrNameToTest + " has incorrect format.");
			}		
		}
		
	}
	
	private static void verifyDate(String pepsDate) throws ValidationException {		
		DateTimeFormatter fmt = null;
		
		switch (pepsDate.length()) {
		case 4:
			fmt = DateTimeFormat.forPattern("yyyy");
			break;
		case 6:
			fmt = DateTimeFormat.forPattern("yyyyMM");
			break;
		case 8:
			fmt = DateTimeFormat.forPattern("yyyyMMdd");
			break;
		default:
			throw new ValidationException("Date has wrong format");			
		}	
		
		try {
			fmt.parseDateTime(pepsDate);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Date has wrong format");
		}
		

	}
	
	
	
	
}
