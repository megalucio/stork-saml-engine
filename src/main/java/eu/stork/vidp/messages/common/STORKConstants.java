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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Interface encapuslating relevant STORK constants such as namespace, attribute names, etc.
 * @author bzwattendorfer
 *
 */
public interface STORKConstants {
	
	/**
	 * STORK namespace
	 */
	public static final String STORK10_NS = "urn:eu:stork:names:tc:STORK:1.0:assertion";
	
	/**
	 * STORK namespace prefix
	 */
	public static final String STORK10_PREFIX = "stork";
	
	/**
	 * STORK protocol namespace
	 */
	public static final String STORKP10_NS = "urn:eu:stork:names:tc:STORK:1.0:protocol";
	
	/**
	 * STORK protocol namespace prefix
	 */
	public static final String STORKP10_PREFIX = "storkp";
		
	/**
	 * STORK attribute name prefix
	 */
	final static String STORK_ATTRIBUTE_NAME_PREFIX = "http://www.stork.gov.eu/1.0/";
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_EIDENTIFIER = "eIdentifier";
	final static String STORK_ATTRIBUTE_EIDENTIFIER = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_EIDENTIFIER;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_GIVENAME = "givenName";
	final static String STORK_ATTRIBUTE_GIVENNAME = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_GIVENAME;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_SURNAME = "surname";
	final static String STORK_ATTRIBUTE_SURNAME = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_SURNAME;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_INHERITED_FAMILYNAME = "inheritedFamilyName";
	final static String STORK_ATTRIBUTE_INHERITED_FAMILYNAME = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_INHERITED_FAMILYNAME;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_ADOPTED_FAMILYNAME = "adoptedFamilyName";
	final static String STORK_ATTRIBUTE_ADOPTED_FAMILYNAME = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_ADOPTED_FAMILYNAME;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_GENDER = "gender";
	final static String STORK_ATTRIBUTE_GENDER = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_GENDER;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_DATEOFBIRTH = "dateOfBirth";
	final static String STORK_ATTRIBUTE_DATEOFBIRTH = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_DATEOFBIRTH;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_COUNTRYOFBIRTH = "countryCodeOfBirth";
	final static String STORK_ATTRIBUTE_COUNTRYCODEOFBIRTH = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_COUNTRYOFBIRTH;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_NATIONALITYCODE = "nationalityCode";
	final static String STORK_ATTRIBUTE_NATIONALITYCODE = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_NATIONALITYCODE;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_MARTIALSTATUS = "maritalStatus";
	final static String STORK_ATTRIBUTE_MARTIALSTATUS = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_MARTIALSTATUS;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_TEXT_RESIDENCE_ADDRESS = "textResidenceAddress";
	final static String STORK_ATTRIBUTE_TEXT_RESIDENCE_ADDRESS = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_TEXT_RESIDENCE_ADDRESS;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_CANONICAL_RESIDENCE_ADDRESS = "canonicalResidenceAddress";
	final static String STORK_ATTRIBUTE_TEXT_CANONICAL_ADDRESS = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_CANONICAL_RESIDENCE_ADDRESS;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_EMAIL = "eMail";
	final static String STORK_ATTRIBUTE_EMAIL = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_EMAIL;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_TITLE = "title";
	final static String STORK_ATTRIBUTE_TITLE = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_TITLE;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_RESIDENCE_PERMIT = "residencePermit";
	final static String STORK_ATTRIBUTE_RESIDENCE_PERMIT = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_RESIDENCE_PERMIT;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_PSEUDONYM = "pseudonym";
	final static String STORK_ATTRIBUTE_PSEUDONYM = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_PSEUDONYM;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_AGE = "age";
	final static String STORK_ATTRIBUTE_AGE = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_AGE;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_ISAGEOVER = "isAgeOver";
	final static String STORK_ATTRIBUTE_ISAGEOVER = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_ISAGEOVER;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_SIGNED_DOC = "signedDoc";	
	final static String STORK_ATTRIBUTE_SIGNEDDOC = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_SIGNED_DOC;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_FISCALNUMBER = "fiscalNumber";
	final static String STORK_ATTRIBUTE_FISCALNUMBER = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_FISCALNUMBER;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_CITIZENQAALEVEL_OLD = "citizenQAAlevel";
	final static String STORK_ATTRIBUTE_CITIZENQAALEVEL_OLD = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_CITIZENQAALEVEL_OLD;
	
	final static String STORK_ATTRIBUTE_NAME_SUFFIX_CITIZENQAALEVEL = "citizenQAALevel";
	final static String STORK_ATTRIBUTE_CITIZENQAALEVEL = STORK_ATTRIBUTE_NAME_PREFIX + STORK_ATTRIBUTE_NAME_SUFFIX_CITIZENQAALEVEL;
	
	/**
	 * Full Set of accepted STORK attributes
	 */
	public final Set<String> FULL_STORK_ATTRIBUTE_SET = new HashSet<String>(Arrays.asList(new String[] {STORK_ATTRIBUTE_EIDENTIFIER,
			STORK_ATTRIBUTE_GIVENNAME,
			STORK_ATTRIBUTE_SURNAME,
			STORK_ATTRIBUTE_INHERITED_FAMILYNAME,
			STORK_ATTRIBUTE_ADOPTED_FAMILYNAME,
			STORK_ATTRIBUTE_GENDER,
			STORK_ATTRIBUTE_DATEOFBIRTH,
			STORK_ATTRIBUTE_COUNTRYCODEOFBIRTH,
			STORK_ATTRIBUTE_NATIONALITYCODE,
			STORK_ATTRIBUTE_MARTIALSTATUS,
			STORK_ATTRIBUTE_TEXT_RESIDENCE_ADDRESS,
			STORK_ATTRIBUTE_TEXT_CANONICAL_ADDRESS,
			STORK_ATTRIBUTE_TEXT_CANONICAL_ADDRESS,
			STORK_ATTRIBUTE_EMAIL,
			STORK_ATTRIBUTE_TITLE,
			STORK_ATTRIBUTE_RESIDENCE_PERMIT,
			STORK_ATTRIBUTE_PSEUDONYM,
			STORK_ATTRIBUTE_AGE,
			STORK_ATTRIBUTE_ISAGEOVER,
			STORK_ATTRIBUTE_SIGNEDDOC,
			STORK_ATTRIBUTE_FISCALNUMBER,
			STORK_ATTRIBUTE_CITIZENQAALEVEL_OLD,
			STORK_ATTRIBUTE_CITIZENQAALEVEL}));

	/**
	 * Default set of STORK attributes to be requested (without signedDoc)
	 */
	Set<String> DEFAULT_STORK_REQUESTED_ATTRIBUTE_SET = new HashSet<String>(Arrays.asList(new String[] {
			STORK_ATTRIBUTE_EIDENTIFIER,
			STORK_ATTRIBUTE_GIVENNAME,
			STORK_ATTRIBUTE_SURNAME,
			STORK_ATTRIBUTE_DATEOFBIRTH,}));
	
	/**
	 * Default required set of returned STORK attributes
	 */
	Set<String> DEFAULT_STORK_RETURNED_ATTRIBUTE_SET = new HashSet<String>(Arrays.asList(new String[] {
			STORK_ATTRIBUTE_EIDENTIFIER,
			STORK_ATTRIBUTE_GIVENNAME,
			STORK_ATTRIBUTE_SURNAME,
			STORK_ATTRIBUTE_DATEOFBIRTH,
			STORK_ATTRIBUTE_SIGNEDDOC}));

}
