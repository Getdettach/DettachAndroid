package com.wwc.jajing.system;

import android.content.Context;
import android.content.Intent;

import com.wwc.jajing.domain.entity.CallerImpl;
import com.wwc.jajing.domain.entity.User;
import com.wwc.jajing.domain.entity.UserImpl;
import com.wwc.jajing.domain.services.CallManager;
import com.wwc.jajing.domain.services.CallManagerAbstract;
import com.wwc.jajing.permissions.CallerPermission;
import com.wwc.jajing.permissions.PermissionManager;
import com.wwc.jajing.settings.time.DailyNotifier;
import com.wwc.jajing.sms.JJSMSHelper;
import com.wwc.jajing.sms.JJSMSManager;
import com.wwc.jajing.sms.JJSMSManagerImpl;
import com.wwc.jajing.sms.JJSMSMessenger;
import com.wwc.jajing.sms.JJSMSMessengerImpl;
import com.wwc.jajing.sms.JJSMSResponseDispatcher;
import com.wwc.jajing.sms.JJSMSResponseDispatcherImpl;
import com.wwc.jajing.sms.JJSMSValidator;
import com.wwc.jajing.sms.JJSMSValidatorImpl;
import com.wwc.jajing.sms.command.JJCommandFactory;
import com.wwc.jajing.sms.command.JJCommandFactoryImpl;

import android.util.Log;

/*
 * Singleton
 */

public class JJSystemImpl implements JJSystem {

	public enum Services {
		SMS_MANAGER,CALL_MANAGER, CONTEXT, USER,
	}

	public static final String INTENT_INIT_COMPLETE = "com.exmaple.jajingprototype.system.event.INIT_COMPLETE";
	private static final JJRegistry jjregistry = JJRegistryImpl.getInstance();
	private static final JJSystem instance = new JJSystemImpl();
	private static boolean hasBeenInitialized;

	private JJSystemImpl() {
		
	}
	
	public static JJSystem getInstance()
	{
		return instance;
	}

	@Override
	public Object getSystemService(Services service) {

		return jjregistry.getFromRegistry(service);
	}
	
	public static boolean hasBeenIntialized()
	{
		if(hasBeenInitialized == true) {
			return true;
		}
		return false;
	}

	/*
	 * Here we perform any initial bootstrap work for our system. 
	 * ** registering global services into our registry. **
	 */
	public void initSystem(Context context) {
		//try
		//	{
                Log.d("Application", "initSystem - entered!");
				JJSMSHelper jjsmsHelper = new JJSMSHelper();
                Log.d("Application", "initSystem - 1!");
				JJSMSValidator jjsmsValidator = new JJSMSValidatorImpl();
                Log.d("Application", "initSystem - 2!");
				JJSMSMessenger jjsmsMessenger = new JJSMSMessengerImpl();
                Log.d("Application", "initSystem - 3!");
				JJCommandFactory jjCommandFactory = new JJCommandFactoryImpl();
                Log.d("Application", "initSystem - 4!");
				JJSMSResponseDispatcher jjDispatcher = new JJSMSResponseDispatcherImpl();
                Log.d("Application", "initSystem - 5!");
				JJSMSManager jjsmsManager = new JJSMSManagerImpl(jjsmsHelper, jjsmsValidator, jjsmsMessenger, jjCommandFactory, jjDispatcher);
                Log.d("Application", "initSystem - 6!");
				//Context must be added before any of the other services are added to our registry
				//add Context to our registry
				this.jjregistry.addToRegistry(Services.CONTEXT, context);
                Log.d("Application", "initSystem - 7!");
				//add SMS manager to registry
				this.jjregistry.addToRegistry(Services.SMS_MANAGER, jjsmsManager);
                Log.d("Application", "initSystem - 8!");
				//add User to registry
				//this restores the state of the user if the user is defined


                Log.d("Application", "initSystem - 9!");
				User user = UserImpl.findById(UserImpl.class, Long.parseLong("1"));
                //User user = null;
                Log.d("Application", "initSystem - 10!");
				this.jjregistry.addToRegistry(Services.USER, (user == null)? UserImpl.getInstance():user);
                Log.d("Application", "initSystem - 11!");

				//add CallManager to registry
				CallManagerAbstract callManger = new CallManager();
                Log.d("Application", "initSystem - 12!");
				this.jjregistry.addToRegistry(Services.CALL_MANAGER, callManger);
                Log.d("Application", "initSystem - 13!");


				//fire event letting components know system is done initializing
				Intent intent = new Intent();
                Log.d("Application", "initSystem - 14!");
				intent.setAction(this.INTENT_INIT_COMPLETE);
                Log.d("Application", "initSystem - 15!");
				context.sendBroadcast(intent);
                Log.d("Application", "initSystem - 16!");
				this.hasBeenInitialized = true;
                Log.d("Application", "initSystem - Done!");
		//	}catch (Exception e)
		//{
		//	e.printStackTrace();
		//}
	}
}
