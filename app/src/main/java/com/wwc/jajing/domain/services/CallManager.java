package com.wwc.jajing.domain.services;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;
import com.wwc.jajing.domain.entity.MissedCallLog;
import com.wwc.jajing.domain.entity.MissedMessageLog;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

public class CallManager extends CallManagerAbstract {
	
	
	private MissedCallLog recentCallLog;
	private MissedMessageLog recentMessageLog;
	
	public static final String TAG = "CallManager";
	private Context context;
	
	
	public CallManager()
	{
		context = (Context) JJSystemImpl.getInstance().getSystemService(
				Services.CONTEXT);
	}

	public void disconnectCall() {
		// froyo and beyond trigger on buttonUp instead of buttonDown
	    Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
	    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
	            KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
	    context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
	    new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				CallManager.this.sendCallToVoicemail();
				CallManager.this.sendCallToVoicemail();

			}
		}, 0);
	}

	/*
	 * This is currently working when used in CSLonUnavailable
	 * 
	 */
	@Override
	public void sendCallToVoicemail() {
		Log.d(TAG, "send call to voicemail called");
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Class clazz = Class.forName(tm.getClass().getName());
			Method method = clazz.getDeclaredMethod("getITelephony");
			method.setAccessible(true);
			ITelephony telephonyService = (ITelephony) method.invoke(tm);
			telephonyService.endCall();
			Log.d(TAG, "Sent call to voicemail successfully.");
		} catch (Exception e) {
			Log.d(TAG,
					"Something went wrong when trying to send call to voicemail");
		}
	}

	/*
	 * 
	 * Silence ringer not working using telephony service hack because of permission error.
	 * MODIFY_PHONE_STATE is a system only permission
	 * 
	 * Instead we use AudiManager to silence the phone.
	 * 
	 */
	public void silenceRinger(Boolean silence) {
        if (Build.VERSION.SDK_INT < 23) {
            if (silence) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                am.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                Log.d(TAG, "Ringer mode set to silent");
            } else {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                Log.d(TAG, "Ringer mode set to Normal");
            }
        }else if( Build.VERSION.SDK_INT >= 23 ) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // if user granted access else ask for permission
            if ( notificationManager.isNotificationPolicyAccessGranted()) {
                if (silence) {
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    //am.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                    Log.d(TAG, "Ringer mode set to silent");
                } else {
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    //am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    Log.d(TAG, "Ringer mode set to Normal");
                }
            }
        }
	}

	/*
	 *Lazy load our missed call log
	 */
	@Override
	public MissedCallLog getRecentMissedCallLog() {
		if(this.recentCallLog == null) {
			this.recentCallLog = MissedCallLog.getInstance();
			return this.recentCallLog;
		}
		return this.recentCallLog;
	}

	/*
	 * Lazy load our missed message log
	 */
	@Override
	public MissedMessageLog getRecentMissedMessageLog() {
		if(this.recentMessageLog == null) {
			this.recentMessageLog = MissedMessageLog.getInstance();
			return this.recentMessageLog;
		}
		return this.recentMessageLog;
	}


	private void requestMutePermissions() {
		try {
			if (Build.VERSION.SDK_INT < 23) {
				AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			} else if( Build.VERSION.SDK_INT >= 23 ) {
				this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
			}
		} catch ( SecurityException e ) {

		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// if user granted access else ask for permission
		if ( notificationManager.isNotificationPolicyAccessGranted()) {
			AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}

		/*else{
			// Open Setting screen to ask for permisssion
			Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
			startActivityForResult( intent, ON_DO_NOT_DISTURB_CALLBACK_CODE );
		}*/
	}

	/*@RequiresApi(api = Build.VERSION_CODES.M)
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE ) {
			this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
		}
	}*/

}
