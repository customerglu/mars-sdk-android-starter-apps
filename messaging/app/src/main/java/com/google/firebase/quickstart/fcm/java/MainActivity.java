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

//import io.swagger.client.api.PetApi;
//import io.swagger.client.model.Pet;
//import io.swagger.client.ApiException;

import io.swagger.client.ApiClient;
import io.swagger.client.api.UserOffersApi;
import io.swagger.client.api.UserApi;
import io.swagger.client.model.InlineResponse2001;
import io.swagger.client.model.UserOfferObject;
import io.swagger.client.model.Body;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.ApiException;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import java.util.List;
import android.app.Application;


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

        final AssetManager assets = this.getAssets();
        final ApiClient marsDefaultClient = new ApiClient(tempDeviceId);
        Log.i("The API Client is ", marsDefaultClient.getDeviceId());

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
        // [END handle_data_extras]
//drawWebView
        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Subscribing to weather topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END subscribe_topics]
            }
        });

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                // [START retrieve_current_token]
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END retrieve_current_token]
            }
        });


        Button drawWebViewButton = findViewById(R.id.drawWebView);
        drawWebViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView myWebView = new WebView(MainActivity.this);
                WebSettings webSettings = myWebView.getSettings();
                myWebView.addJavascriptInterface(new WebAppInterface(MainActivity.this), "Android");
                webSettings.setJavaScriptEnabled(true);
//                String unencodedHtml =
//                        "&lt;html&gt;&lt;body&gt;'%23' is the percent code for ‘#‘ &lt;/body&gt;&lt;/html&gt;";
//                String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                        Base64.NO_PADDING);
//                myWebView.setWebViewClient(new MyWebViewClient());
//                myWebView.loadData(encodedHtml, "text/html", "base64");
//                String summary = "<html><body>You scored <b>192</b> points.</body></html>";
//                InputStream stream= assets.open("index.html", AssetManager.ACCESS_BUFFER);
//                myWebView.loadData(summary, "text/html", null);
//                myWebView.loadUrl("file:///android_asset/index2.html");
                myWebView.loadUrl("https://webview-demo.marax.ai/offers");
                setContentView(myWebView);
//                myWebView.loadUrl("https://www.google.com");
                Log.i("Running", "NOWWWW");
                // [END retrieve_current_token]
            }
        });


        Button showAllOffersButton = findViewById(R.id.showAllOffers);
        showAllOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = marsDefaultClient.getDeviceId();
                Log.i("DeviceId From SDK is ", d);
                Log.i("mansion", "About to Start a new intent");
                Intent myIntent = new Intent(MainActivity.this, OfferList.class);
                startActivity(myIntent);
            }
        });




    }

    public class WebAppInterface {
        Context mContext;


        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {

            mContext = c;
        }
        /** Show a toast from the web page */
        @JavascriptInterface
        public void killView(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            UserOffersApi apiInstance = new UserOffersApi();
//            UserApi userInstance = new UserApi();
            UserOfferObject body = new UserOfferObject();
//            Body newUserBody = new Body();
//            newUserBody = newUserBody.deviceId("dark");
//            PetApi apiInstance = new PetApi();
//            Pet body = new Pet(); // Pet | Pet object that needs to be added to the store


            try {
//                apiInstance.addPet(body);
                InlineResponse200 res = apiInstance.fetchOffer("5def66ed53bb2e00d88d92e5", "5def6acaf713800130d70957");
                Offer offer = new Offer("approved", res.getData().getUserId(), res.getData().getOfferId(), res.getData().getCreatedAt(), res.getData().getUpdatedAt(), res.getData().getTemplateUrl());


//                Offer offer = new Offer("started", "abc1", "ME100", "2019-12-16 13:15:22", "2019-12-16 13:30:20", "https://marax.ai/go/home");
//                mOfferViewModel = new ViewModelProvider(this).get(OfferViewModel.class);
//                OfferRepository mOfferRep = new OfferRepository();
//                mRepository.insert(offer);
//                mOfferViewModel.insert(offer);
//                InlineResponse2001 res = userInstance.createUser(newUserBody);
                Log.i("BodyTry", res.toString());
            } catch (ApiException e) {
                System.err.println("");
                e.printStackTrace();
            }
//            } catch (InterruptedException e) {
//                System.err.println("Exception in interupted Execution");
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }
            finish();
        }
    }

}
