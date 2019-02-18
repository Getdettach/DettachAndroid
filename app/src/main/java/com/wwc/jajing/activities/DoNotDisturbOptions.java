package com.wwc.jajing.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wwc.R;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.sms.JJSMS;
import com.wwc.jajing.sms.JJSMSManager;
import com.wwc.jajing.system.JJSystemImpl;
import com.wwc.jajing.system.JJSystemImpl.Services;

public class DoNotDisturbOptions extends Activity {

	private static final String TAG = "DoNotDisturbOptions";
	private User user;
//	private JJSMS jjsms;

	private String number;
	boolean bl_unabletoreach;
	TextView txtv_unabletoreach;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options_to_calling_party_do_not_disturb);
		txtv_unabletoreach=(TextView)findViewById(R.id.txtv_unabletoreach);

		this.user = (User) JJSystemImpl.getInstance().getSystemService(
				Services.USER);
		bl_unabletoreach=getIntent().getExtras().getBoolean("unabletotakecall");

		/*if(bl_unabletoreach)
		{
			bl_unabletoreach=false;
			txtv_unabletoreach.setVisibility(View.GONE);
		}else
		{
			txtv_unabletoreach.setVisibility(View.VISIBLE);
		}*/

	}

	@Override
	protected void onStart() {
		super.onStart();

//		this.jjsms = (JJSMS) getIntent().getSerializableExtra(
//				OptionsToCallingParty.JJSMS);

		Bundle b = getIntent().getExtras();
		number = b.getString("JJSMS");

	}

	@Override
	protected void onStop() {
		super.onStop();
//		this.finish();
	}

	public void leaveVoiceMail(View view) {
		PleaseWait.showPleaseWaitDialog(this, "Please Wait", "sending you to voicemail...");
		// let call-receiver know that this caller wants to leave voicemail
		JJSMSManager jjsmsManager = (JJSMSManager) JJSystemImpl.getInstance()
				.getSystemService(Services.SMS_MANAGER);

//		jjsmsManager.getMessenger().sendRawSms(new JJSMS("#D/LEAVE_VOICEMAIL"),this.jjsms.getSendersPhoneNumber().toString());
//		Log.d(TAG, this.jjsms.getSendersPhoneNumber().toString());

		jjsmsManager.getMessenger().sendRawSms(new JJSMS("#D/LEAVE_VOICEMAIL"),number);
		Log.d(TAG, number);

		this.finish();

	}
	
	public void hangUp(View view) {
		// let the call receiver know that this caller wants to hang up
		JJSMSManager jjsmsManager = (JJSMSManager) JJSystemImpl.getInstance()
				.getSystemService(Services.SMS_MANAGER);

//		jjsmsManager.getMessenger().sendRawSms(new JJSMS("#D/HANG_UP"),this.jjsms.getSendersPhoneNumber().toString());
//		Log.d(TAG, this.jjsms.getSendersPhoneNumber().toString());

		jjsmsManager.getMessenger().sendRawSms(new JJSMS("#D/HANG_UP"),number);
		Log.d(TAG, number);

		Intent main=new Intent(DoNotDisturbOptions.this,StatusActivity.class);
		startActivity(main);
		this.finish();


	}

	public void willCallBack(View view) {
		// let the call receiver know that this caller wants to hang up
		JJSMSManager jjsmsManager = (JJSMSManager) JJSystemImpl.getInstance()
				.getSystemService(Services.SMS_MANAGER);

//		jjsmsManager.getMessenger().sendRawSms(new JJSMS("#D/WILL_CALL_BACK"),this.jjsms.getSendersPhoneNumber().toString());
//		Log.d(TAG, this.jjsms.getSendersPhoneNumber().toString());

		jjsmsManager.getMessenger().sendRawSms(new JJSMS("#D/WILL_CALL_BACK"),number);
		Log.d(TAG, number);


		this.finish();
	}
	
	
	public void leaveTextMessage(View view)
	{
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//		smsIntent.putExtra("address", this.jjsms.getSendersPhoneNumber().toString());
//		smsIntent.setData(Uri.parse("smsto:" + this.jjsms.getSendersPhoneNumber().toString()));

		smsIntent.putExtra("address", number);
		smsIntent.setData(Uri.parse("smsto:" + number));
		startActivity(smsIntent);

		/*Intent main=new Intent(DoNotDisturbOptions.this,StatusActivity.class);
		startActivity(main);
		this.finish();*/

	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.e(TAG,"Onbackpressed called");
		Intent main=new Intent(DoNotDisturbOptions.this,StatusActivity.class);
		startActivity(main);
		this.finish();


				}
}
