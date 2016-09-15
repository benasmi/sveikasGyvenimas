package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Benas on 9/15/2016.
 */
public class GcmOnTokenRefresh extends InstanceIDListenerService {

    public GcmOnTokenRefresh(){
        Log.i("TEST", "GcmOnTokenRefresh");
    }


    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegisterTokenService.class));
        super.onTokenRefresh();
    }
}