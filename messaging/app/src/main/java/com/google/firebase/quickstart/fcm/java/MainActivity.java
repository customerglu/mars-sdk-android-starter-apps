/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm.java;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.content.res.AssetManager;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.quickstart.fcm.R;
import com.google.firebase.quickstart.fcm.java.persistance.OfferRepository;
import com.google.firebase.quickstart.fcm.java.persistance.db.dao.OfferDao;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;
import com.google.firebase.quickstart.fcm.java.persistance.viewmodel.OfferViewModel;
import ai.marax.android.sdk.core.RudderClient;
import ai.marax.android.sdk.core.RudderMessageBuilder;
import ai.marax.android.sdk.core.RudderProperty;
import ai.marax.android.sdk.core.RudderTraits;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import static com.google.firebase.quickstart.fcm.java.MainApplication.getRudderClient;


public class MainActivity extends AppCompatActivity {
    private OfferRepository mRepository;



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.indexOf("google.com") > -1) {
                // This is my website, so do not override; let my WebView load the page
                Log.i("Hello", "World2");
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            Log.i("Hello", "World");
            startActivity(intent);
            return true;
        }
    }


    private static final String TAG = "MainActivity";
    private String tempDeviceId = FirebaseInstanceId.getInstance().getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }



        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }


        Button drawWebViewButton = findViewById(R.id.drawWebView);
        drawWebViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView myWebView = getRudderClient().drawOfferView(MainActivity.this);
                setContentView(myWebView);
                Log.i("Running", "NOWWWW");
            }
        });

        Button orderCompleted50 = findViewById(R.id.order_completed_50);
        orderCompleted50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("order_value", 50);
                getRudderClient().track("ORDER_COMPLETED", new RudderProperty().putValue(properties));

            }
        });

        Button orderCompleted600 = findViewById(R.id.order_completed_600);
        orderCompleted600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("order_value", 600);
                getRudderClient().track("ORDER_COMPLETED", new RudderProperty().putValue(properties));

            }
        });

        Button randomEventTest = findViewById(R.id.random_event_test);
        randomEventTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("something", "test");
                getRudderClient().track("RANDOM_EVENT", new RudderProperty().putValue(properties));

            }
        });

        Button randomEventBad = findViewById(R.id.random_event_bad);
        randomEventBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("something", "bad");
                getRudderClient().track("RANDOM_EVENT", new RudderProperty().putValue(properties));

            }
        });

        Button updateAttributes = findViewById(R.id.update_attributes);
        updateAttributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> properties = new HashMap<>();
                properties.put("email", "sjkdfhkjh@gmail.com");
                properties.put("phone", "+9193389393");
                getRudderClient().updateAttributes(properties);

            }
        });
    }


}
