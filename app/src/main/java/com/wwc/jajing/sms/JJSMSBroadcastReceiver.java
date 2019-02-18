package com.wwc.jajing.sms;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BlockedNumberContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.MissedMessageModel;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/*
 * Responsible for listening to raw JJSMS messages
 */
public class JJSMSBroadcastReceiver extends BroadcastReceiver{

	private final String TAG = "JJSMSBroadcastReceiver";
	
	
	private JJSMSManager jjsmsManager;
	private JJSMSHelper jjsmsHelper;
	private JJSMSValidator jjsmsValidator;
	private User user;

	private CallManagerAbstract callManager;

	Date dateFormat;

	private String callerName="Unknown";


	final SmsManager sms = SmsManager.getDefault();
	
	public JJSMSBroadcastReceiver()
	{
		//CACHE SYSTEM FOR PERFORMANCE
		JJSystem jjSystem = JJSystemImpl.getInstance();
		this.user = (User) jjSystem.getSystemService(Services.USER);
		this.jjsmsManager = (JJSMSManager) jjSystem.getSystemService(Services.SMS_MANAGER);
		//BUG CRASHING AT THIS LINE JJSMS MANAGER is null
		this.jjsmsHelper = jjsmsManager.getHelper();
		this.jjsmsValidator = jjsmsManager.getValidator();

		this.callManager = (CallManagerAbstract) jjSystem.getSystemService(Services.CALL_MANAGER);
		
	}
	/*
	 * This method will check to see if we received any raw jjsms strings
	 * If so it will create these messages into a JJSMS representations, and use these messages to execute commands.
	 *
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e("JJSMSBroadcastReceiver","Onreceivecalled");
		final String rawSMSstr = jjsmsHelper.getMessageBodyFromSMS(intent);
		final String phoneNumber = jjsmsHelper.getPhoneNumberFromIncomingSMS(intent);
		Log.d(TAG, "BR got it" + rawSMSstr);

		if(jjsmsValidator.isValidRawJJSMSStr(rawSMSstr)){
			Log.e("JJSMSBroadcastReceiver","user  isValidRawJJSMSStr ");
			Intent i = new Intent(context, JJSMSService.class);
			i.putExtra("jjsmsStr", rawSMSstr);
			i.putExtra("phoneNumber", phoneNumber);
			context.startService(i);

			if (!this.user.isAvailable()) {
				callManager.silenceRinger(true);
			}
		
			abortBroadcast();
			
		} else {
			Log.e(TAG, "A invalid jjsms string was received.The rawSMSstr is:" + rawSMSstr);
			// "Invalid" messages are just normal sms messages sent to the user by his contacts
			// We broadcast an intent, and attach associated data to the intent to deliver the messages another time
			// let the SMSReceiver to do the work
			
			//check if the user is unavailable before aborting the message
			if (!this.user.isAvailable()) {
				Log.e("JJSMSBroadcastReceiver","user is unavailable ");
				/*Intent smsIntent = new Intent().setAction(SMSReceiver.INTENT_ACTION);
				smsIntent.putExtra("textMessage", rawSMSstr);
				smsIntent.putExtra("phoneNumber", phoneNumber);
				context.sendBroadcast(smsIntent);*/
				Log.e(TAG, "phoneNumber=="+phoneNumber);

//				System.out.println("rawSMSstr = " + rawSMSstr.length());
				Bundle pudsBundle = intent.getExtras();
				Object[] pdus = (Object[]) pudsBundle.get("pdus");
				SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);
				Log.e(TAG,  messages.getMessageBody());
				if(messages.getMessageBody().equals("Hi")) {
					Log.e(TAG, "messages=="+messages.getMessageBody());
					abortBroadcast();
				}

				final String textMessage;
				if(rawSMSstr.length()!=0 && rawSMSstr.length()>35){
					textMessage = rawSMSstr.substring(0,30)+".......";
					Log.e(TAG, "textMessage if=="+textMessage);
				}else{
					textMessage = rawSMSstr;
					Log.e(TAG, "textMessage else=="+textMessage);
				}

//				System.out.println("phoneNumber = " + phoneNumber);


				try{
					RealmResults<ContactModel> contactList = getRealmInstance().where(ContactModel.class).findAll();
//					System.out.println("contactList = " + contactList);

					for(int i=0;i<contactList.size();i++){
						if(phoneNumber.contains(contactList.get(i).getNumber())){
							callerName = contactList.get(i).getName();
//							System.out.println("name = " + callerName);
							break;
						}
					}
					final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					final Calendar c = Calendar.getInstance();

					dateFormat = df.parse(df.format(c.getTime()));

				}catch(Exception e){
					e.printStackTrace();
				}
                //blocking sms for matched number

			/*	if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
					final String numberCall = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);;

                  //reject call if number is matched to our blocking number
					if(numberCall.equals(phoneNumber)){
						disconnectPhoneItelephony(context);
					}
				}
				//blocking sms for matched number

				if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
					Bundle bundle = intent.getExtras();
					Object messages[] = (Object[]) bundle.get("pdus");
					SmsMessage smsMessage[] = new SmsMessage[messages.length];

					for (int n = 0; n < messages.length; n++) {
						smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
					}

					final String numberSms = smsMessage[0].getOriginatingAddress();
					//final String messageSms = smsMessage[0].getDisplayMessageBody();
					//long dateTimeSms = smsMessage[0].getTimestampMillis();
					Log.e(TAG, "numberSms=="+numberSms);
					//block sms if number is matched to our blocking number
					if(numberSms.equals(phoneNumber)){
						abortBroadcast();
					}
				}*/


				//prevent these sms messages from going into user's inbox

				getRealmInstance().executeTransaction(new Realm.Transaction() {
					@Override
					public void execute(Realm realm) {
						MissedMessageModel msgModel = realm.createObject(MissedMessageModel.class); // Create managed objects directly

						final Calendar c = Calendar.getInstance();
						final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

						if (phoneNumber != null) {
							msgModel.setNumber(phoneNumber);
							msgModel.setType(user.getUserStatus().getAvailabilityStatus());
							msgModel.setDate(df.format(c.getTime()));
							msgModel.setMessage(textMessage);
							msgModel.setCallerName(callerName);
							msgModel.setTime(String.valueOf(System.currentTimeMillis()));
							msgModel.setCallDateFormat(dateFormat);
						}
						Log.e(TAG, "setMessage=="+msgModel.getMessage());
					}
				});

				callManager.silenceRinger(true);

			}

			abortBroadcast();

			
		}

	}



}
