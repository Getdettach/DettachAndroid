package com.wwc.jajing.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wwc.jajing.activities.WantToDisturbActivity;

public class OutgoingCallReceiver extends BroadcastReceiver {

	String formattedNumber;
	Context context;
	//public static String str_closeprogress;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("OutgoingCallReceiver","Onreceivecalled");
		/*if(WantToDisturbActivity.str_connectingcall=="connect")
		{
			WantToDisturbActivity.str_connectingcall=" ";
			str_closeprogress="close";

		}*/
		this.context = context;
		//outgoing phone number
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        formattedNumber = this.formatIncomingNumber(number);
        

	}
	
	/*
	 * This method is used to prevent
	 * REFACTOR this code, very inefficient way of formatting incoming number
	 */
	private String formatIncomingNumber(String incomingNumber)
	{
		if(incomingNumber.length() == 11){
			return incomingNumber;
		} else {
			if (!Character.toString(incomingNumber.charAt(0)).equalsIgnoreCase("1")){
				return ("1" + incomingNumber);
			}
		}
		return null;
		
	}


}
