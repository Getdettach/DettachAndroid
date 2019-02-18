package com.wwc.jajing.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.wwc.jajing.component.GetJSONResponse;
import com.wwc.jajing.interfaces.VolleyInterface;
import com.wwc.jajing.listeners.CSLonUnavailable;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.receivers.OutgoingCallReceiver;
import com.wwc.jajing.services.MyFirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.RealmResults;

import static com.wwc.jajing.system.JJApplication.getRealmInstance;

public class PleaseWait extends Activity{

	public ProgressDialog pd;
	int mProgressStatus = 0;
	String number,myNumber,id;

	private Timer timer = new Timer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		String title  = i.getStringExtra("title");
		String description  = i.getStringExtra("description");
		myNumber = i.getStringExtra("mynumber");
		id = i.getStringExtra("id");
		number = i.getStringExtra("number");

		pd = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK).show(this, title, description);

		pd.show();

		Log.e("PleaseWait", "oncreatecall");

		Log.e("PleaseWait", "title=="+i.getStringExtra("title"));
		Log.e("PleaseWait", "description=="+i.getStringExtra("description"));
		Log.e("PleaseWait", "mynumber=="+i.getStringExtra("mynumber"));
		Log.e("PleaseWait", "id=="+i.getStringExtra("id"));
		Log.e("PleaseWait", "number==="+i.getStringExtra("number"));

		getServerResponse();
		/*RealmResults<RegisterDeviceModel> deviceValues = getRealmInstance().where(RegisterDeviceModel.class).findAll();

		if(deviceValues.size()>0){

			Constants.accessToken  = deviceValues.get(0).getAccestoken();

			ExceuteHandler();

		}*/

	}

	public void ExceuteHandler(){

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				getServerResponse();

			}
		}, 0, 5*1000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e("Pleasewait", "Onpausecalled====");
		Log.e("Pleasewait", "MyFirebaseMessagingService.str_closeprogress===="+MyFirebaseMessagingService.str_closeprogress);

		if(MyFirebaseMessagingService.str_closeprogress=="close")
		{
			Log.e("Pleasewait", "OnStopcalled if====");
			MyFirebaseMessagingService.str_closeprogress=" ";
			pd.dismiss();
			this.finish();
		}else
		{
			Log.e("Pleasewait", "OnStopcalled else====");
		}


	}
	
	/*@Override
	protected void onStop() {
		super.onStop();
		Log.e("Pleasewait", "OnStopcalled====");

	}*/

	public void getServerResponse(){

		Map<String, String> params = new HashMap<>();
		params.put("AccessToken",Constants.accessToken);
		params.put("Caller",myNumber);
		params.put("Receiver",number);
		params.put("Scheduler","0");
		params.put("AllowDenyStatus","");
		params.put("StatusId",id);


		new GetJSONResponse(getApplicationContext()).RequestJsonToServer(getApplicationContext(),"DetachCallStatus", params, new VolleyInterface() {

			@Override
			public void onSuccessResponse(final JSONObject result) {

				System.out.println("result in Splash Activity = " + result);
				Log.e("Pleasewait", "getServerResponse===="+result);

				try{

					if(result.getString("AllowDenyStatus").compareTo("A") == 0){

						Intent i = new Intent(PleaseWait.this, AboutToBreakThrough.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra("jjsms", number);
						startActivity(i);
						finish();



						timer.cancel();
					}else if(result.getString("AllowDenyStatus").compareTo("D") == 0){

						Intent i = new Intent(PleaseWait.this, DoNotDisturbOptions.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra("JJSMS", number);
						startActivity(i);
						finish();


						timer.cancel();
					}



				}catch(Exception e){
					e.printStackTrace();
				}



			}

			@Override
			public void onErroeResponse(VolleyError result) {

				System.out.println("ERROE IN SPLASH "+result);

			}

			@Override
			public void onSuccessResponse(JSONArray result) {

			}
		});
	}
	
	public static void showPleaseWaitDialog(Context aContext, String aTitle, String aDescription)
	{
		Intent i = new Intent(aContext, PleaseWait.class);
		i.putExtra("title", aTitle);
		i.putExtra("description", aDescription);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		aContext.startActivity(i);

	}
}
