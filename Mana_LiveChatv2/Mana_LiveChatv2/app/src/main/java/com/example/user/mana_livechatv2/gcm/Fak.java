package com.example.user.mana_livechatv2.gcm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;

import java.io.IOException;

/**
 * Created by user on 26/07/2016.
 */
public class Fak extends Activity {
    private static final String TAG = GcmIntentService.class.getSimpleName();

    public void jancuk (String topic, String token) {
        try {
            GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
            //InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            Log.d("debug_subs_narasumber", token);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Narasumber has subscribed to topic: " + topic);
            } else {
                Log.e(TAG, "error: narasumber gcm registration id is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "narasumber Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
