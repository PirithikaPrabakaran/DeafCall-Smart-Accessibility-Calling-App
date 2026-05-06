package com.example.deafcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            // Call started
            Intent serviceIntent = new Intent(context, CallService.class);
            context.startForegroundService(serviceIntent);
        }

        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            // Call ended
            Intent serviceIntent = new Intent(context, CallService.class);
            context.stopService(serviceIntent);
        }
    }
}
