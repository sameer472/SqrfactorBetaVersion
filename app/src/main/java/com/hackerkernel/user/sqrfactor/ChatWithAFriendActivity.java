package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatWithAFriendActivity extends AppCompatActivity {
    static int id;
    MessageClass messageClass=null;
    String friendProfile, friendName;
    private ArrayList<MessageClass> messageClassArrayList = new ArrayList<>();
    private ChatWithAFriendActivityAdapter chatWithAFriendActivityAdapter;
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private TextView friendNametext;
    private EditText messageToSend;
    private ImageButton sendMessageButton;
    private PullRefreshLayout layout ;
    private boolean isLoading=false,isVisibleBottpmArrow=false,isFirstTimeLoading=true;
    public Context context;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;
    private Toolbar toolbar;
    private ImageView bottom_arrow;
    private String isOnline;
    private  String nextPageUrl;
    private UserClass userClass;
    private int loadCount=0;



    //private EndlessRecyclerOnScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_afriend);

        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //friendNametext = (TextView) findViewById(R.id.friendName);
        messageToSend = (EditText) findViewById(R.id.messageToSend);
        sendMessageButton = (ImageButton) findViewById(R.id.sendButton);
        //toolbar=(Toolbar)findViewById(R.id.toolbar);


        Intent intent = getIntent();
        id = intent.getExtras().getInt("FriendId");
        friendProfile = intent.getExtras().getString("FriendProfileUrl");
        friendName = intent.getExtras().getString("FriendName");
        isOnline = intent.getExtras().getString("isOnline");

        actionBar.setTitle(friendName);
        if (isOnline.equals("True")) {
            actionBar.setSubtitle("Online");
        } else {
            actionBar.setSubtitle("Offline");
        }

        database= FirebaseDatabase.getInstance();
        ref = database.getReference();
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler.setLayoutManager(layoutManager);
        chatWithAFriendActivityAdapter = new ChatWithAFriendActivityAdapter(messageClassArrayList, getApplicationContext(), id, friendProfile, friendName);
        recycler.setAdapter(chatWithAFriendActivityAdapter);
        bottom_arrow=(ImageView)findViewById(R.id.bottom_arrow);

        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/myallMSG/getChat/" + id,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        messageClassArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl=jsonObject.getString("nextChats");
                            JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData=jsonObjectChat.getJSONArray("data");
                           // Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                messageClassArrayList.add(messageClass);
                            }

                            Collections.reverse(messageClassArrayList);
                            chatWithAFriendActivityAdapter.notifyDataSetChanged();

                            FirebaseListner();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(myReq);

        bottom_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisibleBottpmArrow) {
                    bottom_arrow.setVisibility(View.GONE);
                    isVisibleBottpmArrow = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(messageClassArrayList.size() - 1);

                        }
                    }, 1);
                }
            }
        });


        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isLoading=false;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId=layoutManager.findLastVisibleItemPosition();

                if(dy>0 &&lastId==messageClassArrayList.size()-3 && isVisibleBottpmArrow)
                {
                    bottom_arrow.setVisibility(View.GONE);
                    isVisibleBottpmArrow=false;
                }
                if(dy<0 && lastId<=18 &&! isVisibleBottpmArrow)
                {
                    bottom_arrow.setVisibility(View.VISIBLE);
                    isVisibleBottpmArrow=true;
                }
                if(dy<0 && lastId==10 && !isLoading)
                {
                    isLoading=true;
                    Log.v("rolling",layoutManager.getChildCount()+" "+layoutManager.getItemCount()+" "+layoutManager.findLastVisibleItemPosition()+" "+
                            layoutManager.findLastVisibleItemPosition());

                    fetchMoreChatDataFromServer();

                }
            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //
                SendMessageToServer();
                final LastMessage lastMessage = new LastMessage(userClass.getUserId(), messageToSend.getText().toString(), userClass.getUser_name());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MessageFragment.ref.child("Chats").child(id + "").setValue(lastMessage);
                    }
                }, 1000);

                Toast.makeText(ChatWithAFriendActivity.this, "Messeage sent..", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCount=0;

    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    public void fetchMoreChatDataFromServer() {

        if(nextPageUrl!=null)
        {
            final ArrayList<MessageClass> messageClassArrayList1=new ArrayList<>();
            final ArrayList<MessageClass> finalmessageClassArrayList=new ArrayList<MessageClass>(messageClassArrayList);
            Log.v("ArraySize111",finalmessageClassArrayList.size()+"");
            StringRequest myReq = new StringRequest(Request.Method.POST,nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("MorenewsFeedFromServer", response);
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            messageClassArrayList1.clear();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                nextPageUrl=jsonObject.getString("nextChats");
                                JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                                JSONArray jsonArrayData=jsonObjectChat.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    //Log.v("Response",response);
                                    MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                    messageClassArrayList1.add(messageClass);
                                }

                                Log.v("ArraySize333",messageClassArrayList1.size()+"");
                                Collections.reverse(messageClassArrayList1);
                                for(int i=0;i<messageClassArrayList1.size();i++)
                                {
                                    messageClassArrayList.add(0,messageClassArrayList1.get(i));
                                    chatWithAFriendActivityAdapter.notifyItemInserted(0);
                                }

//
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //isLoading=false;
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer "+TokenClass.Token);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            requestQueue.add(myReq);
        }

    }



    public void FirebaseListner() {

            ref.child("Chats").child(userClass.getUserId() + "").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    LastMessage lastMessage = dataSnapshot.getValue(LastMessage.class);
                    if (loadCount!=0 && lastMessage != null && id == lastMessage.getSenderId()) {


                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();


                        if (messageClassArrayList.size() != 0) {
                            messageClass =
                                    new MessageClass(messageClassArrayList.get(messageClassArrayList.size() - 1).getMessageId() + 1, id, userClass.getUserId(), lastMessage.getMessage(), "1", formatter.format(date), formatter.format(date));

                        }

                        messageClassArrayList.add(messageClass);
                        chatWithAFriendActivityAdapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recycler.smoothScrollToPosition(messageClassArrayList.size() - 1);
                            }
                        }, 1);


                        //  Toast.makeText(ChatWithAFriendActivity.this,"MessageFromServer"+lastMessage.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    else {
                        loadCount=1;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }





    public  void SendMessageToServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/chat/sendChat"
                ,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        MessageClass messageClass=
                                new MessageClass(messageClassArrayList.get(messageClassArrayList.size()-1).getMessageId()+1,userClass.getUserId(),id,messageToSend.getText().toString(),"1",formatter.format(date),formatter.format(date));

                        messageClassArrayList.add(messageClass);
                        messageToSend.setText("");
                        chatWithAFriendActivityAdapter.notifyItemInserted(messageClassArrayList.size()-1);
                        if(isVisibleBottpmArrow)
                        {
                            isVisibleBottpmArrow=false;
                            bottom_arrow.setVisibility(View.GONE);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recycler.smoothScrollToPosition(messageClassArrayList.size()-1);
                            }
                        }, 1);



                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId",userClass.getUserId()+"");
                params.put("friendId",id+"" );
                params.put("message",messageToSend.getText().toString() );

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
        };

        requestQueue.add(myReq);

    }

}