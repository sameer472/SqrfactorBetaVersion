package com.hackerkernel.user.sqrfactor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Bitmap bitmap;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
//
        //Toast.makeText(this, "message" + remoteMessage.getData().getB, Toast.LENGTH_LONG).show();


//        Log.v("notify", remoteMessage.getData().get("senderId"));
//
//        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPrefs.getString("MyObject", "");
//        UserClass userClass = gson.fromJson(json, UserClass.class);
//
//        if(remoteMessage.getData().get("senderID")!=userClass.getUserId()+"")
        SendNotification(remoteMessage.getData().get("notificationSenderUrl"), remoteMessage.getData().get("postTitle")
                , remoteMessage.getData().get("postType"), remoteMessage.getData().get("username"), remoteMessage.getData().get("type"));
//
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//       // Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//           //og.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                //heduleJob();
//            } else {
//                // Handle message within 10 seconds
//              //ndleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            SendNotification(remoteMessage.getNotification().getBody());
//            //g.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
    }


//    public void handleIntent(RemoteMessage remoteMessage) {
//

//    }

    private void SendNotification(String profileUrl, String postTitle, String postType, String userName, String type)
    {
        Intent intent=new Intent(getApplicationContext(),HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        Uri defaultUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

      // bitmap=getBitmapfromUrl(profileUrl);
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_round);
        if(type.equals("like_post"))
        {
            notificationBuilder.setContentTitle(userName+ " liked your "+postType);
        }
        else if(type.equals("comment")) {

            notificationBuilder.setContentTitle(userName+ " commented on your "+postType);
        }
        else {
            notificationBuilder.setContentTitle(userName+ " started following you ");
        }

        notificationBuilder.setContentText(postTitle);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultUri);
        notificationBuilder.setContentIntent(pendingIntent);
        int num = (int) System.currentTimeMillis();

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(num,notificationBuilder.build());


    }
//
    public void CustomNotification(String profileUrl, String postDescription, String postType, String userName, String type) {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);


        // Open NotificationView Class on Notification Click
//        Intent intent = new Intent(this, NotificationView.class);
//        // Send data to NotificationView Class
//        intent.putExtra("title", strtitle);
//        intent.putExtra("text", strtext);
//        // Open NotificationView.java Activity
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        //remoteViews.setImageViewResource(R.id.comapnyLogo,getBitmapfromUrl(profileUrl));
        remoteViews.setBitmap(R.id.profileImage, "", getBitmapfromUrl(profileUrl));

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.UserName, "sameer");
        remoteViews.setTextViewText(R.id.body, postDescription);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.ic_share_square)
                // Set Ticker Message
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                // Set RemoteViews into Notification
                .setContent(remoteViews);


        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }


    private Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL("https://archsqr.in/" + imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}



