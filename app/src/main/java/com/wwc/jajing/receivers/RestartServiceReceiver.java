package com.wwc.jajing.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wwc.jajing.services.JJOnAwayService;

/**
 * Created by infmac1 on 09/12/16.
 */

public class RestartServiceReceiver extends BroadcastReceiver
{

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context.getApplicationContext(), JJOnAwayService.class));
        }
    }
}
