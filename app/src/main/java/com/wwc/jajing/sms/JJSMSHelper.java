package com.wwc.jajing.sms;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class JJSMSHelper {


	public String getMessageBodyFromSMS(Intent intent)
	{

		Bundle extras = intent.getExtras();
        Object[] pdus = (Object[]) extras.get("pdus");
        SmsMessage sms;
        String rawSmsStr = "";
 
        for (Object pdu : pdus) {
            sms = SmsMessage.createFromPdu((byte[]) pdu);
            rawSmsStr +=sms.getMessageBody();
            Log.e("JJSMSHelper","getMessageBodyFromSMS"+rawSmsStr);
        }
		return rawSmsStr;
	}
	
	public String getPhoneNumberFromIncomingSMS(Intent intent)
	{
		Bundle extras = intent.getExtras();
        Object[] pdus = (Object[]) extras.get("pdus");
        SmsMessage sms;
        String phoneNUmber = "";
 
        for (Object pdu : pdus) {
            sms = SmsMessage.createFromPdu((byte[]) pdu);
            phoneNUmber += sms.getOriginatingAddress();
            Log.e("JJSMSHelper","getPhoneNumberFromIncomingSMS"+phoneNUmber);
            break;

        }
		return phoneNUmber;
	}
}
