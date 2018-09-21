package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class StatusFragment extends Fragment {
    private ArrayList<NewsFeedStatus> newsstatus = new ArrayList<>();
    private ImageView displayImage;
    private ImageView camera;// button
    public ImageButton mRemoveButton;
    RecyclerView recyclerView;
    private NewsFeedStatus newsFeedStatus;
    Button like, comment, share, like2;
    String token;
    SharedPreferences sharedPreferences;
    ImageView user_profile_photo;
    String message, encodedImage;
    private boolean isScrolling;
    int currentItems,totalItems,scrolledItems;
    private ProgressBar progressBar;
    Button btnSubmit;
    private ImageView newsProfileImage;
    EditText writePost;
    private ProgressDialog pDialog;
    public static String UPLOAD_URL = "https://archsqr.in/api/post";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    LinearLayoutManager layoutManager;
    private NewsFeedAdapter newsFeedAdapter;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    PullRefreshLayout layout;
    private Context context;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;
    private boolean isLoading=false;
    private static String nextPageUrl;
    private String oldUrl;

    @Override
    public void onStart() {
        super.onStart();
        context=getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fregment_status, container, false);

        recyclerView = rootView.findViewById(R.id.news_recycler);
        progressBar = rootView.findViewById(R.id.progress_bar_status);
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        newsFeedAdapter = new NewsFeedAdapter(newsstatus, this.getActivity());
        recyclerView.setAdapter(newsFeedAdapter);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();
        Log.v("status","hello");
        SharedPreferences mPrefs =getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);

        LoadNewsFeedDataFromServer();
        camera = rootView.findViewById(R.id.news_camera);
        displayImage = rootView.findViewById(R.id.news_upload_image);
        btnSubmit = rootView.findViewById(R.id.news_postButton);

        layout = rootView.findViewById(R.id.news_pullToRefresh);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //LoadNewsFeedDataFromServer();
                //layout.setRefreshing(false);
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        LoadNewsFeedDataFromServer();
                    }
                },1000);

            }
        });
        dialog = new ProgressDialog(this.getActivity());
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        final RelativeLayout relativeLayout = rootView.findViewById(R.id.rl);
//        relativeLayout.setVisibility(View.GONE);
        jsonObject = new JSONObject();



        sharedPreferences = this.getActivity().getSharedPreferences("PREF_NAME", this.getActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "sqr");
        Log.v("TokenOnStatus", token);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isLoading=false;
                //Toast.makeText(context,"moving down",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId=layoutManager.findLastVisibleItemPosition();
//                if(dy>0)
//                {
//                    Toast.makeText(context,"moving up",Toast.LENGTH_SHORT).show();
//                }
                if(dy>0 && lastId + 2 > layoutManager.getItemCount() && !isLoading)
                {
                    isLoading=true;
                    Log.v("rolling",layoutManager.getChildCount()+" "+layoutManager.getItemCount()+" "+layoutManager.findLastVisibleItemPosition()+" "+
                            layoutManager.findLastVisibleItemPosition());
                    FetchDataFromServer();

                }
            }
        });



        //RealTimeNotificationListner();
        ref.child("notification").child(userClass.getUserId()+"").child("all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HomeScreen.getnotificationCount();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.child("Chats").child(userClass.getUserId()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HomeScreen.getUnReadMsgCount();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;

    }
    public void LoadNewsFeedDataFromServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        newsstatus.clear();
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/news-feed",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed1", response);
//                         Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            nextPageUrl=jsonPost.getString("next_page_url");
                            JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                NewsFeedStatus newsFeedStatus1 = new NewsFeedStatus(jsonArrayData.getJSONObject(i));
                                newsstatus.add(newsFeedStatus1);
                            }

                            newsFeedAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Token" + error+"", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }

        };


        requestQueue.add(myReq);
    }

    public void FetchDataFromServer() {

        if(nextPageUrl!=null) {

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest myReq = new StringRequest(Request.Method.POST, nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("MorenewsFeedFromServer", response);
                            //Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonPost = jsonObject.getJSONObject("posts");
                                nextPageUrl = jsonPost.getString("next_page_url");
                                JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    NewsFeedStatus newsFeedStatus1 = new NewsFeedStatus(jsonArrayData.getJSONObject(i));
                                    newsstatus.add(newsstatus.size()-1, newsFeedStatus1);
                                    //messageClassArrayList.add(0,messageClassArrayList1.get(i));
                                    newsFeedAdapter.notifyItemInserted(newsstatus.size()-1);
                                }

                                //newsFeedAdapter.notifyDataSetChanged();
                                //newsFeedAdapter.setLoaded();
                                // progressBar.setVisibility(View.GONE);


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
                    params.put("Authorization", "Bearer " + TokenClass.Token);

                    return params;
                }

            };

            requestQueue.add(myReq);
        }
    }


}

