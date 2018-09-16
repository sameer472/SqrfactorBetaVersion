package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsActivity extends ToolbarActivity {

    //TextView text, text2;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NotificationClass> notificationsClassArrayList = new ArrayList<>();
    private NotificationsAdapter notificationsAdapter;
    private Button send;
    private boolean isLoading = false;
    private Context context;
    private  String nextPageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        String s = getApplicationContext().getResources().getString(R.string.noti_string);
        //text.setText(Html.fromHtml(s));
        //text2.setText(Html.fromHtml(s));

        recycler = (RecyclerView)findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(this);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);

        notificationsAdapter = new NotificationsAdapter(notificationsClassArrayList,this);
        recycler.setAdapter(notificationsAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(recycler.getContext(), linearLayoutManager.getOrientation());
        recycler.addItemDecoration(decoration);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isLoading=false;
                //Toast.makeText(context,"moving down",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId=linearLayoutManager.findLastVisibleItemPosition();
//                if(dy>0)
//                {
//                    Toast.makeText(context,"moving up",Toast.LENGTH_SHORT).show();
//                }
                if(dy>0 && lastId + 3 > linearLayoutManager.getItemCount() && !isLoading)
                {
                    isLoading=true;
                    Log.v("rolling",linearLayoutManager.getChildCount()+" "+linearLayoutManager.getItemCount()+" "+linearLayoutManager.findLastVisibleItemPosition()+" "+
                            linearLayoutManager.findLastVisibleItemPosition());
                    LoadMoreNotification();

                }
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/notifications",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl=jsonObject.getString("nextpage");
                            JSONArray jsonArrayData = jsonObject.getJSONArray("notifications");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Log.v("Response",response);
                                NotificationClass notificationsClass = new NotificationClass(jsonArrayData.getJSONObject(i));
                                notificationsClassArrayList.add(notificationsClass);
                            }
                            notificationsAdapter.notifyDataSetChanged();


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

        requestQueue.add(myReq);

    }

    public void LoadMoreNotification(){
        if(nextPageUrl!= null){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/notifications",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayData = jsonObject.getJSONArray("notifications");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Log.v("Response",response);
                                NotificationClass notificationsClass = new NotificationClass(jsonArrayData.getJSONObject(i));
                                notificationsClassArrayList.add(notificationsClass);
                            }
                            notificationsAdapter.notifyDataSetChanged();


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

        requestQueue.add(myReq);

    }
    }

}

