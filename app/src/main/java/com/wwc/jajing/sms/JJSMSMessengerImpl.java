package com.wwc.jajing.sms;

import java.util.Timer;
import java.util.TimerTask;

import android.telephony.SmsManager;
import android.util.Log;

import com.wwc.jajing.listeners.CSLonUnavailable;

import io.realm.Realm;

public class JJSMSMessengerImpl implements JJSMSMessenger {

	private final String TAG = "JJSMSMessengerImpl";
	
	private String currentJJSMSStr = "";
	
	@Override
	public void sendRawSms(JJSMS jjsms, String phoneNumber) {
		String rawJJSMSstr = jjsms.getRawJJSMS();

		//do our check to prevent duplicate messages from being sent out within a 1 second time span
		if (this.isSendingSameSMSTwice(rawJJSMSstr)) {
			Log.d(TAG, "cancelling message!"); //cancels second message
			return;
		}



		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, rawJJSMSstr+" On "+ CSLonUnavailable.str_timewithdate +". To Download App goto: www.Dettach.com", null, null);
		
		Log.e(TAG, "A text message was sent to:" + phoneNumber);
		Log.e(TAG, "The text messsage body that was sent:" + rawJJSMSstr);
		
		//set the current jjsms string being sent
		this.currentJJSMSStr = rawJJSMSstr;
		//now reset the current jjsms string being sent after 1 second
		this.clearCurrentJJSMSstr();
		
		
	}
	
	/*
	 * This is a quick hacky solution to preven the messenger from sending jjsms messages twice
	 * within a very shor time period
	 */
	private boolean isSendingSameSMSTwice(String aJJSMSStr)
	{

		System.out.println("aJJSMSStr = " + aJJSMSStr);
		System.out.println("currentJJSMSStr = " + currentJJSMSStr);

		if (this.currentJJSMSStr.equalsIgnoreCase(aJJSMSStr)) {
			Log.d(TAG, "SENT THE SAME TEXT MESSAGE TWICE!");
			return true;
		} else {
			return false;

		}
		
	}
	
	private void clearCurrentJJSMSstr() {
		
		new Timer().schedule( 
		        new TimerTask() {
		            @Override
		            public void run() {
		            	JJSMSMessengerImpl.this.currentJJSMSStr = "";
		            	Log.d(TAG, "current jjsms string sent is now cleared");
		            }
		        }, 
		        1000 
		);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
