package com.hackerkernel.user.sqrfactor;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationCountService  extends IntentService {
    public static DatabaseReference ref;
    public static FirebaseDatabase database;


    public NotificationCountService() {
        super("DisplayNotification");
    }
    public NotificationCountService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();



        ref.child("Notifications").child(MessageFragment.userId+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(getApplicationContext(),"real Notification changed",Toast.LENGTH_LONG).show();

                //getnotificationCount();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyIntent", "MyIntentService onCreate() method is invoked.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyIntentE", "MyIntentService onDestroy() method is invoked.");
    }
}
