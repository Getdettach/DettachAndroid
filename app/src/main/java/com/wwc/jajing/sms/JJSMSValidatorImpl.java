package com.wwc.jajing.sms;

public class JJSMSValidatorImpl implements JJSMSValidator {

	/*
	 * This constant is used for debugging purposes on LogCat.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "JJSMSValidatorImpl";

	/*
	 * Conditions that must be met for raw sms messages to be considered valid
	 * jjsms message strings.
	 */
	@Override
	public boolean isValidRawJJSMSStr(String rawSmsStr) {

		if (hasJJSMSSignature(rawSmsStr))
			return true;
		else
			return false;
	}

	/*
	 * Tells us if a raw sms string has "#jj/" as the first characters of an sms
	 * message.
	 */
	private boolean hasJJSMSSignature(String rawSmsStr) {


		if (rawSmsStr.length() < 3) {
			return false;
		}

		String firstFourCharacters = rawSmsStr.substring(0, 3);

		if (firstFourCharacters.equalsIgnoreCase("#D/")) {
			return true;
		} else {
			return false;
		}

	}

}
