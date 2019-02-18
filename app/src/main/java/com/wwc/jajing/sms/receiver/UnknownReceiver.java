package com.wwc.jajing.sms.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wwc.jajing.activities.OptionsToCallingParty;
import com.wwc.jajing.activities.PleaseWait;
import com.wwc.jajing.domain.services.CallManager;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.sms.JJSMS;
import com.wwc.jajing.sms.listeners.UserStatusJJSMSResponseListener;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

public class UnknownReceiver implements  CommandReceiver{

	public static final String TAG = "UnknownReceiver";
	private Context context;
	private JJSMS jjsms;
	
	private ShowOptionsToCallingPartyReceiver aShowOptionsToCallingPartyReceiver;
	private CallManager cm;

	

	
	public UnknownReceiver(Context aContext, JJSMS aJJSMS) {
		this.jjsms = aJJSMS;
		this.context = aContext;
		this.cm = (CallManager) JJSystemImpl.getInstance().getSystemService(Services.CALL_MANAGER);
		
		this.cm.disconnectCall();
	}
	
	/*public Boolean isInitialMessage()
	{
		if(jjsms.getRawJJSMS().equalsIgnoreCase(JJSMS.INITIAL_MESSAGE)) {
			return true;
		}
		else {
			return false;
		}
	}*/
	
	public Boolean isInitialMessage()
	{
		//Log.e(TAG, "We got an initial Message.");
		//Log.e(TAG, "We got an initial Message."+jjsms.getRawJJSMS().substring(0, 41));
		Log.e(TAG, "JJSMS.INITIAL_MESSAGE."+JJSMS.INITIAL_MESSAGE);

		//jjsms.getRawJJSMS().substring(0, 41)
		if(jjsms.getRawJJSMS().substring(0, 33).equalsIgnoreCase(JJSMS.INITIAL_MESSAGE)) {
			Log.e(TAG, "We got an initial Message.");

			return true;
		}
		else {
			Log.e(TAG, "We did not get an initial Message.Instead:" + jjsms.getRawJJSMS());
			return false;
		}
	}
	
	public void handleInitialMessage()
	{
		//PleaseWait.showPleaseWaitDialog(context, "JaJing!", this.jjsms.getSendersPhoneNumber().toString() + " has jajing! please wait while we attempt to connect...");
		
		String[] reasonAndAvailabilityTime = this.getReasonAndAvailabilityTime();
		this.jjsms.addToExtras(OptionsToCallingParty.REASON, reasonAndAvailabilityTime[0]);
		this.jjsms.addToExtras(OptionsToCallingParty.TIME_BACK, reasonAndAvailabilityTime[1]);
		
		//new up the receiver
		this.aShowOptionsToCallingPartyReceiver = new ShowOptionsToCallingPartyReceiver(context, jjsms);
		this.aShowOptionsToCallingPartyReceiver.showCallReceivingIsBusyOptionsMenu();

		Log.e(TAG, "Finished Handling Initial Message");
		
	}
	
	private String[] getReasonAndAvailabilityTime()
	{

		String [] reasonSecondHalf = this.jjsms.getRawJJSMS().split(JJSMS.INITIAL_MESSAGE);
		String [] reason = reasonSecondHalf[1].split(" and will be available at ");
		return new String[] {reason[0], reason[1]};
	}
	
	
	
	public boolean isResponse(){
		
		return this.jjsms.isResponse();
	}

	
	
}
