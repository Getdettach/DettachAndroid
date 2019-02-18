package com.wwc.jajing.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.VolleyError;
import com.wwc.jajing.activities.BaseActivity;
import com.wwc.jajing.activities.Constants;
import com.wwc.jajing.activities.OptionsToCallingParty;
import com.wwc.jajing.activities.SetStatusActivity;
import com.wwc.jajing.activities.SplashActivity;
import com.wwc.jajing.activities.WantToDisturbActivity;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.domain.entity.Caller;
import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.MissedCall;
import com.wwc.jajing.domain.entity.TimeSetting;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.domain.value.PhoneNumber;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.permissions.CallerPermission;
import com.wwc.jajing.permissions.PermissionManager.Permissions;
import com.wwc.jajing.realmDB.CallerModel;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.settings.time.TimeSettingTaskManager;
import com.wwc.jajing.sms.JJSMS;
import com.wwc.jajing.sms.JJSMSManager;
import com.wwc.jajing.sms.JJSMSMessenger;
import com.wwc.jajing.sms.JJSMSService;
import com.wwc.jajing.sms.SMSReceiver;
import com.wwc.jajing.system.JJSystem;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

/*
 * Responsible for controlling the workflow of calls coming in and going out
 * 
 */
public class CSLonUnavailable extends PhoneStateListener {

	private static final String TAG = "CSLonUnavailable";
	private User user;
	private JJSMSManager jjsmsManger;
	
	private JJSMSMessenger jjsmsMessenger;
	
	private CallManagerAbstract callManager;
	
	public  Context context;

	String availableTime="";

	String number;
	Long timeId=0L;
	TimeSetting aTimeSettings;

	boolean allowCal = true;

	String callerName;

	boolean callStat = true;

	Date dateFormat;

	String isServiceStart = "0";
	public static String str_timewithdate;


	//60035162
	
	public CSLonUnavailable() 
	{
		super();
		//**CACHE OUR SYSTEM, FOR PERFORMANCE...
		JJSystem jjSystem = JJSystemImpl.getInstance();
		this.user = (User) jjSystem.getSystemService(
				Services.USER);
		
		this.jjsmsManger = (JJSMSManager) jjSystem.getSystemService(Services.SMS_MANAGER);
		
		if(this.jjsmsManger == null) {
			throw new IllegalStateException("jjsmsManager should not be null!");
		}
		this.jjsmsMessenger  = jjsmsManger.getMessenger();
		
		this.callManager = (CallManagerAbstract) jjSystem.getSystemService(Services.CALL_MANAGER);
		
		this.context =  (Context) jjSystem.getSystemService(Services.CONTEXT);


	}


    public CSLonUnavailable(Context context){

        this.context = context;

    }


	public boolean hasTimeSettingForToday(String day) {

		String dayName= TimeSetting.Days.values()[this.getTodaysOrdinal()].toString();

		return dayName.startsWith(day);
	}

	private int getTodaysOrdinal() {

//        Log.d(TAG, "Toadays ordinal:" + this.convertCalendarToOrdinal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
		return (this.convertCalendarToOrdinal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));

	}


	private int convertOrdinalToCalendar(int anOrdinal) {
		return anOrdinal + 1;
	}

	private int convertCalendarToOrdinal(int aCalendarDay) {
		return aCalendarDay - 1;

	}
	//8124024365

	/*
	 * 
	 * This listener is registered when our JJOnAwayService is connected
	 * This listener is unregistered when our JJOnAwayService is disconnected.
	 * 
	 * This listener is responsible for Managing the workflow of incoming calls when user is "Unavailable"
	 * 
	 * 
	 */
	@Override
	public void onCallStateChanged(int state, final String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

//		callManager.silenceRinger(false);

//		System.out.println("incomingNumber = " + incomingNumber);

		Log.e(TAG, "onCallStateChanged");
		Log.e(TAG, "onCallStateChanged user.getUserStatus().getAvailabilityStatus()"+user.getUserStatus().getAvailabilityStatus()+"callStat=="+callStat);

		if(user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE")!=0 && callStat) {

			callStat = false;

			//Formatting number because it was a souce of a MAJOR bug
			if (incomingNumber.length() == 0)
				return; //possible bug in android, sometimes incoming number is empty
			final String formattedIncomingNumber = this.formatIncomingNumber(incomingNumber);

			number = formattedIncomingNumber;

			Log.e(TAG, "formatted incoming number:" + formattedIncomingNumber);


			SimpleDateFormat dateFormatterTIME = new SimpleDateFormat("hh:mm a");


			List<TimeSetting> timeList = TimeSetting.listAll(TimeSetting.class);

			for (TimeSetting aTimeSetting : timeList) {

				if (aTimeSetting.getId() != 1 && aTimeSetting.isOn()) {
					aTimeSettings = TimeSetting.findById(TimeSetting.class, aTimeSetting.getId());
//					System.out.println(" is ON = " + aTimeSettings.getId());

					timeId = aTimeSettings.getId();
					availableTime = aTimeSettings.getEndTime();

					break;
				}

			}
			String startTime = "";
			String endTime = "";


			if (aTimeSettings != null) {

				startTime = aTimeSettings.getStartTime();
				endTime = aTimeSettings.getEndTime();

			}

			final RealmResults<ExtendTimeModel> extendTimeModel = getRealmInstance().where(ExtendTimeModel.class).equalTo("timeId",timeId.toString()).findAll();

			if (extendTimeModel.size() > 0) {

				isServiceStart = extendTimeModel.get(0).getStartId();

			}
			//7358209318

//			RealmResults<RepeatDaysModel> daysList = getRealmInstance().where(RepeatDaysModel.class).findAll();
//			System.out.println("daysList  = " + daysList);

			RealmResults<RepeatDaysModel> repeatDays = getRealmInstance().where(RepeatDaysModel.class).equalTo("id", timeId.toString()).findAll();

			if (repeatDays.size() > 0) {

				String[] daySplit = repeatDays.get(0).getDays().split(" | ");

				for (int i = 0; i < daySplit.length; i++) {

					String day = daySplit[i];

					if (hasTimeSettingForToday(day)) {
						allowCal = false;
						break;
					}
				}

			} else {
				if (user.getUserStatus().getAvailabilityStatus().compareTo("AVAILABLE") != 0) {
					allowCal = false;
				}
			}


//			System.out.println("check the alllow call " + allowCal);


			try {
//            Date endTime = dateFormatterTIME.parse(startTime);
//
//            System.out.println("endTime = " + endTime);

				Date now = dateFormatterTIME.parse(dateFormatterTIME.format((new Date())));
				Date startT = dateFormatterTIME.parse(startTime);
				Date endT = dateFormatterTIME.parse(endTime);

//				if ((now.equals(startT) || now.after(startT)) && (now.equals(endT) || now.before(endT)) && !allowCal) {

				if(isServiceStart.compareTo("1")==0 && !allowCal){

					switch (state) {

						case TelephonyManager.CALL_STATE_RINGING:
							Log.e(TAG, "CALL_STATE_RINGING");
							PackageManager pm = context.getPackageManager();
							ApplicationInfo appInfo = pm.getApplicationInfo(this.context.getPackageName(), 0);

							try {
								ActivityManager am = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
								List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
								ComponentName componentInfo = taskInfo.get(0).topActivity;
								String packageName = componentInfo.getPackageName();

//								System.out.println("Package Name === " + packageName);
//								System.out.println("Context === " + this.context);
//								System.out.println("getAppli = " + appInfo);

								if (packageName.compareTo("com.wwc") == 0) {

									/*Intent mveInt = new Intent();
									mveInt.setAction(Intent.ACTION_MAIN);
									mveInt.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
									mveInt.addCategory(Intent.CATEGORY_HOME);
									this.context.startActivity(mveInt);*/
								}else{

								}
							}catch(Exception e){
								e.printStackTrace();
							}


							Log.e(TAG, "Ringing!");
							Log.e(TAG, "user.isAvailable()"+user.isAvailable());
							Log.e(TAG, "user.isMakingCall()"+user.isMakingCall());

							if (!user.isAvailable() && !this.user.isMakingCall()) {

								if (CallerImpl.isCallerPersisted(formattedIncomingNumber)) {
									Caller caller = (Caller) CallerImpl.getCallerByPhoneNumber(formattedIncomingNumber);

									if (CallerImpl.checkPermission(formattedIncomingNumber)) {

										callManager.silenceRinger(false);
//										this.user.denyPermission(caller, Permissions.SEND_CALL);

//										System.out.println("TESTING SOUND");

										return;

									}

									//REGARDLESS IF THE CALLERS DOES OR DOES NOT HAVE APP, WE NEED TO DISCONNECT THE CALL IMMEDIATELY
									// THIS IS FOR CALL RECEIVING END
									callManager.disconnectCall();
									callManager.silenceRinger(true);

									//SEND THE INITIAL MESSAGE IF WE ARE DEALING WITH A CALLER WITH NO APP
									Log.e(TAG, "Caller does NOT have the app");

									String availabilityTime = this.user.getUserStatus().getavailabilityTime();

//									System.out.println("availabilityTime before = " + availabilityTime);
//
//									if (TimeSetting.hasEndTimePassed(availabilityTime))
//										availabilityTime = "00:00 --";

									availabilityTime = availabilityTime + " " + this.user.getAvailabilityTime().getTimeZoneString();

//									System.out.println("availabilityTime after = " + availabilityTime);

									RealmResults<HasAppModel> hasAppModel = getRealmInstance().where(HasAppModel.class).endsWith("number", number).findAll();

//									System.out.println("hasAppModel CLS CLass = " + hasAppModel);

//									if (hasAppModel.size() > 0) {
//
//										if(hasAppModel.get(0).getIsActive().compareTo("true")==0 && hasAppModel.get(0).getCalAllowStatus().compareTo("0")==0){
//											callManager.disconnectCall();
//										}
//
//									}else{
//										sendRequestToServer(availabilityTime,incomingNumber);
//									}
									DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
									Realm realm = Realm.getInstance(context);
									RealmResults<SetStatusModel> statusModels=realm.where(SetStatusModel.class).findAll();
									for(int i=0;i<statusModels.size();i++)
									{
										Date str_dateandtime=statusModels.get(i).getEndTime();
										str_timewithdate = formatter.format(str_dateandtime);
										Log.e(TAG, "str_dateandtime*********>>>>>>>>"+str_timewithdate);
									}

									// This indirectly check if other user has app installed, and forces callers who have the app to query for user status
									jjsmsMessenger.sendRawSms(new JJSMS(JJSMS.INITIAL_MESSAGE + this.user.getUserStatus().getAvailabilityStatus() + " and will be available at " + availabilityTime.toUpperCase() ), formattedIncomingNumber);
									Log.e(TAG, "JJSMS Initial Message Sent.");

									//Log the missed call
									this.logMissedCall(formattedIncomingNumber);

									return;


								} else {

									//Persist this caller
									Log.e(TAG, "Caller is NOT persisted! Persisting Caller...");
									//TODO - we don't want to assign every caller an anonmyous we need to give them a name later.


									CallerImpl aCaller = new CallerImpl(context, "Anonymous", formattedIncomingNumber);
									Log.e(TAG, "persisted phone number:" + formattedIncomingNumber);
									aCaller.save();
									Log.e(TAG, "Finished Persisting Caller! Incoming number was:" + formattedIncomingNumber);

									//Disconnect incoming call
									callManager.disconnectCall();
									callManager.silenceRinger(true);

									String availabilityTime = this.user.getUserStatus().getavailabilityTime();

//									System.out.println("availabilityTime before = " + availabilityTime);
//
//									if (TimeSetting.hasEndTimePassed(availabilityTime))
//										availabilityTime = "00:00 --";
									availabilityTime = availabilityTime + " " + this.user.getAvailabilityTime().getTimeZoneString();

//									System.out.println("availabilityTime sfter = " + availabilityTime);

//									String number = incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;
//
//									RealmResults<HasAppModel> hasAppModel = getRealmInstance().where(HasAppModel.class).endsWith("number", number).findAll();
//
////									System.out.println("hasAppModel = " + hasAppModel);
//
//									if (hasAppModel.size() > 0) {
//
//										if(hasAppModel.get(0).getIsActive().compareTo("true")==0 && hasAppModel.get(0).getCalAllowStatus().compareTo("0")==0){
//											callManager.disconnectCall();
//										}
//
//									}else{
//										sendRequestToServer(availabilityTime,incomingNumber);
//									}




									// This indirectly check if other user has app installed, and forces callers who have the app to query for user status
									jjsmsMessenger.sendRawSms(new JJSMS(JJSMS.INITIAL_MESSAGE + this.user.getUserStatus().getAvailabilityStatus() + " and will be available at " +availabilityTime.toUpperCase()), formattedIncomingNumber);
									Log.e(TAG, "The Default JJSMS Initial Message Was Sent.");

									//log missed call
									this.logMissedCall(formattedIncomingNumber);

								}

							} else {
								Log.e(TAG, "User is AVAILABLE! OR is MAKING A CALL!");
								this.user.setIsMakingCall(false);
								//LET THE CALL GO THROUGH
							}
							Log.e(TAG, "RINGING. CSLonUnavailable. User Availability Status:" + user.getUserStatus().getAvailabilityStatus());

							break;

						case TelephonyManager.CALL_STATE_IDLE:
							Log.e(TAG, "call state idle");
							this.user.setIsMakingCall(false);

//							String number = incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;
//
//							RealmResults<HasAppModel> hasAppModel = getRealmInstance().where(HasAppModel.class).endsWith("number", number).findAll();
//
//							if (hasAppModel.size() > 0) {
//
////								if(hasAppModel.get(0).getIsActive().compareTo("true")==0 && hasAppModel.get(0).getCalAllowStatus().compareTo("0")==0){
////									callManager.disconnectCall();
////								}
//
//							}else{
//								sendRequestToServer("",incomingNumber);
//							}


//							try {
//
//								int showLimit = 20;
//
//								ActivityManager mgr = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
//								List<ActivityManager.RunningTaskInfo> allTasks = mgr.getRunningTasks(showLimit);
///* Loop through all tasks returned. */
//								for (ActivityManager.RunningTaskInfo aTask : allTasks) {
//									if (aTask.baseActivity.getClassName().startsWith("com.wwc")) {
//										Intent intent = new Intent(this.context, SplashActivity.class);
//										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//										intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
//										this.context.startActivity(intent);
//									}
//
//								}
//							}catch(Exception e){
//								e.printStackTrace();
//							}


							break;
						case TelephonyManager.CALL_STATE_OFFHOOK:
							Log.e(TAG, "call state offhook");

							try {

//								String numberH = incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;
//
//								RealmResults<HasAppModel> hasAppModelH = getRealmInstance().where(HasAppModel.class).endsWith("number", numberH).findAll();
//
////								System.out.println("hasAppModel = " + hasAppModel);
//
//								if (hasAppModelH.size() > 0) {
//
//									if(hasAppModelH.get(0).getIsActive().compareTo("true")==0 && hasAppModelH.get(0).getCalAllowStatus().compareTo("0")==0){
////										callManager.disconnectCall();
//
//										showCallReceivingIsBusyOptionsMenu();
//									}
//
//								}else{
////									sendRequestToServer("",incomingNumber);
//								}
							}catch (Exception e){
								e.printStackTrace();
							}


							this.user.setIsMakingCall(false);

							break;
					}

				} else {

					callManager.silenceRinger(false);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(incomingNumber.compareTo("")!=0){

			callStat = false;
			Log.e(TAG, "Ringing! else"+state);
			Log.e(TAG, "Ringing! else incomingNumber"+incomingNumber);
			switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:



					Log.e(TAG, "Ringing! else");
					break;

				case TelephonyManager.CALL_STATE_IDLE:

					Log.e(TAG, "call state idle else");

					if(WantToDisturbActivity.str_allowstatus=="allow")
					{

						WantToDisturbActivity.str_allowstatus=" ";
						String numberH = incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;

						RealmResults<CallsModel> callsModel = getRealmInstance().where(CallsModel.class).endsWith("number",numberH).findAll();

						if(callsModel.size()>0)
						{
							String incoming_Number = callsModel.get(0).getNumber();
						}

						Log.e(TAG, "Finished giving caller permissions"+incomingNumber);

						List<CallerImpl> callerImpl = CallerImpl.find(CallerImpl.class,"number=?",incomingNumber);

						if(callerImpl.size()>0){
							CallerImpl callerperm = callerImpl.get(0);
							callerperm.setAllowPermission(0);
							callerperm.save();


							callManager.silenceRinger(true);
						}

					}

					//					String numberI = incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;
//
//					RealmResults<HasAppModel> hasAppModel = getRealmInstance().where(HasAppModel.class).endsWith("number", numberI).findAll();
//
//					if (hasAppModel.size() > 0) {
//
//						if(hasAppModel.get(0).getIsActive().compareTo("true")==0 && hasAppModel.get(0).getCalAllowStatus().compareTo("0")==0){
//
//							String[] endTime =  hasAppModel.get(0).getAvailTime().split(" ");
//
//							showCallReceivingIsBusyOptionsMenu(hasAppModel.get(0).getStatusName(),incomingNumber,endTime[1]+" "+endTime[2]);
//						}
//
//					}else{
//						sendRequestToServer("",incomingNumber);
//					}

					this.user.setIsMakingCall(false);

					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:

					Log.e(TAG, "call state offhook else "+incomingNumber);

//					try {
//
//						String number = incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;
//
//						RealmResults<HasAppModel> hasAppModel = getRealmInstance().where(HasAppModel.class).endsWith("number", number).findAll();
//
//						System.out.println("hasAppModel = " + hasAppModel);
//
//						if (hasAppModel.size() > 0) {
//
//							if(hasAppModel.get(0).getIsActive().compareTo("true")==0 && hasAppModel.get(0).getCalAllowStatus().compareTo("0")==0){
//								callManager.disconnectCall();
//							}
//
//						}else{
//							sendRequestToServer("",incomingNumber);
//						}

//					}catch (Exception e){
//						e.printStackTrace();
//					}
//
//					this.user.setIsMakingCall(false);

					break;
			}
		}

		System.out.println("state = " + state);
		
	}

	public void sendRequestToServer(final String availTime,final String number){

//		System.out.println("availTime = " + availTime);

		RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

		if(deviceValues.size()>0){
			Constants.accessToken  = deviceValues.get(0).getAccestoken();
		}

		JSONObject userData;
		JSONObject userData1 = new JSONObject();
		JSONArray userDataArray = new JSONArray();

		try {

			userData = new JSONObject();
			userData.put("UserName", "Check");
			userData.put("MobileNumber", number);

			userDataArray.put(userData);

			userData1.put("Contacts", userDataArray);
			userData1.put("AcessToken", Constants.accessToken);
		}catch(Exception e){
			e.printStackTrace();
		}

//		Map<String, String> params = new HashMap<>();
//		params.put("AccessToken",Constants.accessToken);
//		params.put("MobileNumber",number);


		new GetJSONResponse(context).RequestJsonToServerArray(context, "DetachContacts", userData1, new VolleyInterface() {

			@Override
			public void onSuccessResponse(final JSONArray result) {

				System.out.println("Service Result = " + result);

				try {

					if(availTime.compareTo("")!=0 ) {

						if (result.length() != 0) {

							JSONObject jsonObj = result.getJSONObject(0);

							if (String.valueOf(jsonObj.getBoolean("IsActive")).compareTo("true") == 0) {
								jjsmsMessenger.sendRawSms(new JJSMS(JJSMS.INITIAL_MESSAGE+ user.getUserStatus().getAvailabilityStatus() + " and will be available at " + availTime.toLowerCase()), number);
								Log.e(TAG, "JJSMS Initial Message Sent.");
							}
						}else if(result.length() == 0){
							jjsmsMessenger.sendRawSms(new JJSMS(JJSMS.INITIAL_MESSAGE+ user.getUserStatus().getAvailabilityStatus() + " and will be available at " + availTime.toLowerCase() ), number);
							Log.e(TAG, "JJSMS Initial Message Sent.");
						}

					}else{

						if (result.length() != 0) {

							JSONObject jsonObj = result.getJSONObject(0);

							System.out.println("jsonObj = " + jsonObj);

							if(String.valueOf(jsonObj.getBoolean("IsActive")).compareTo("true")==0){

								System.out.println("availTime is empty for message");

//							callManager.disconnectCall();
								String statusName = jsonObj.getString("Status");
								String[] endTime =  jsonObj.getString("EndTime").split(" ");

								showCallReceivingIsBusyOptionsMenu(statusName,number,endTime[1]+" "+endTime[2]);

							}else{
								System.out.println("ELSE availTime is empty for message");
							}
						}


					}

				}catch(Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onErroeResponse(VolleyError result) {

				System.out.println("ERROE IN SPLASH "+result);

				if(!Constants.haveNetworkConnection(context)){

					if(availTime.compareTo("")!=0){
						jjsmsMessenger.sendRawSms(new JJSMS(JJSMS.INITIAL_MESSAGE + user.getUserStatus().getAvailabilityStatus() + " and will be available at " + availTime.toLowerCase() ), number);
						Log.d(TAG, "JJSMS Initial Message Sent.");
					}
				}
			}

			@Override
			public void onSuccessResponse(JSONObject result) {

			}
		});


	}

	public void showCallReceivingIsBusyOptionsMenu(String status,String number,String endTime) {

		//String rawSMSstr = "#D/The person you are calling is "+ status+ " and will be available at " + "01/05/2018" +endTime+". To Download App goto: www.Dettach.com";
		String rawSMSstr = "#D/The person you are trying to reach has set status "+ status+ " and will be available at "+endTime+" on DATE. To Download App goto: www.Dettach.com";
		Log.e(TAG,"rawSMSstr*********************"+rawSMSstr);

//		System.out.println("rawSMSstr = " + rawSMSstr);
//
//		System.out.println("number = " + number);

		Intent i = new Intent(context, JJSMSService.class);
		i.putExtra("jjsmsStr", rawSMSstr);
		i.putExtra("phoneNumber", number);
		context.startService(i);


//		Intent i = new Intent(this.context, OptionsToCallingParty.class);
//		i.putExtra(OptionsToCallingParty.JJSMS, "TESTING WORK ON PROGRESS");
//
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(i);

	}





	private void logMissedCall(final String incomingNumber)
	{
		//Log the missed call
		//MissedCall mc = new MissedCall(this.context, new PhoneNumber(incomingNumber), new Date(), MissedCall.Actions.NO_APP,this.user.getUserStatus().getAvailabilityStatus());
		//mc.save();


		//List<MissedCall> misdCal = MissedCall.listAll(MissedCall.class);

		//System.out.println("misdCal = " + misdCal.size());

		final Calendar c = Calendar.getInstance();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		final RealmResults<CallsModel> calldetails = getRealmInstance().where(CallsModel.class).equalTo("number",incomingNumber).equalTo("type",user.getUserStatus().getAvailabilityStatus()).equalTo("calldate",df.format(c.getTime())).findAll();

		callerName="Unknown";

//		System.out.println("calldetails = " + calldetails);
//		System.out.println("incomingNumber "+incomingNumber);


		try{

			String number =  incomingNumber.length() > 8 ? incomingNumber.substring(incomingNumber.length() - 8) : incomingNumber;

			RealmResults<ContactModel> contactNumber = getRealmInstance().where(ContactModel.class).endsWith("number",number).findAll();

			if(contactNumber.size()>0){
				callerName = contactNumber.get(0).getName();
			}

			dateFormat = df.parse(df.format(c.getTime()));

		}catch(Exception e){

			e.printStackTrace();
		}


		/*if(calldetails.size() == 0) {

//			System.out.println("callerName = " + callerName);

			getRealmInstance().executeTransaction(new Realm.Transaction() {
				@Override
				public void execute(Realm realm) {
					CallsModel callsModel = realm.createObject(CallsModel.class); // Create managed objects directly
					if (incomingNumber != null) {
						callsModel.setNumber(incomingNumber);
						callsModel.setType(user.getUserStatus().getAvailabilityStatus());
						callsModel.setCalldate(df.format(c.getTime()));
						callsModel.setTime(String.valueOf(System.currentTimeMillis()));
						callsModel.setCallerName(callerName);
						callsModel.setCallDateFormat(dateFormat);
					}
				}
			});

		}
		else
		{
			getRealmInstance().beginTransaction();
			calldetails.get(0).setTime(String.valueOf(System.currentTimeMillis()));
			calldetails.get(0).setCallerName(callerName);
			getRealmInstance().commitTransaction();

		}*/

		getRealmInstance().executeTransaction(new Realm.Transaction() {
			@Override
			public void execute(Realm realm) {
				CallsModel callsModel = realm.createObject(CallsModel.class); // Create managed objects directly
				if (incomingNumber != null) {
					callsModel.setNumber(incomingNumber);
					callsModel.setType(user.getUserStatus().getAvailabilityStatus());
					callsModel.setCalldate(df.format(c.getTime()));
					callsModel.setTime(String.valueOf(System.currentTimeMillis()));
					callsModel.setCallerName(callerName);
					callsModel.setCallDateFormat(dateFormat);
				}
			}
		});

//		RealmResults<CallsModel> callerList = getRealmInstance().where(CallsModel.class).findAll();
//		System.out.println("call history= " + callerList);
		
	}
	
	
	/*
	 * This method is used to prevent
	 * REFACTOR this code, very inefficient way of formatting incoming number
	 * This code is duplicated in JJSMSService....
	 */
	private String formatIncomingNumber(String incomingNumber)
	{
		if(incomingNumber.length() == 11){
			return incomingNumber;
		}else if (incomingNumber.startsWith("+91",0)) {

			return incomingNumber;

		} else {
				//now check if first character  is +
			if(Character.toString(incomingNumber.charAt(0)).equalsIgnoreCase("+")) {
				return incomingNumber.replace("+", " ");
			}
			if (!Character.toString(incomingNumber.charAt(0)).equalsIgnoreCase("1")){
				Log.d(TAG, "first character was:" + Character.toString(incomingNumber.charAt(0)));
				return ("1" + incomingNumber);
			}

//			System.out.println("incomingNumber = " + incomingNumber);

            return incomingNumber;


		}



	}



}
