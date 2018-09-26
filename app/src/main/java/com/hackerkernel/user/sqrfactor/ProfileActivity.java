package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends ToolbarActivity {
    private ArrayList<ProfileClass1> profileClassList = new ArrayList<>();
//    private ImageView displayImage;
//    private ImageView camera;// button
//    public ImageButton mRemoveButton;
    private ProfileAdapter profileAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    private ImageView morebtn, btn,coverImage,profileImage,profileStatusImage,profile_status_image;
    private TextView profileName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    Button btnSubmit,editProfile;
    EditText writePost;
    Bitmap bitmap;
    boolean flag = false;
    LinearLayoutManager layoutManager;
    TextView blueprint, portfolio, followers, following;
    private UserClass userClass;
    private static String nextPageUrl;
    private boolean isLoading=false;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.profile_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        profileAdapter = new ProfileAdapter(profileClassList,this);
        recyclerView.setAdapter(profileAdapter);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        profile_status_image=(ImageView)findViewById(R.id.profile_status_image);
        writePost = (EditText)findViewById(R.id.profile_profile_write_post);
        writePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        editProfile = (Button)findViewById(R.id.profile_editprofile);
        coverImage = (ImageView) findViewById(R.id.profile_cover_image);
        profileImage = (ImageView) findViewById(R.id.profile_profile_image);
        profileName =(TextView)findViewById(R.id.profile_profile_name);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profile_status_image);
        if(userClass.getFirst_name().equals("null"))
        {
            profileName.setText(userClass.getUser_name());
        }

        else {
            profileName.setText(userClass.getFirst_name()+"" +userClass.getLast_name());
        }


        profileStatusImage = (ImageView) findViewById(R.id.profile_status_image);
        followCnt = (TextView) findViewById(R.id.profile_followerscnt);
        followingCnt = (TextView) findViewById(R.id.profile_followingcnt);
        portfolioCnt = (TextView) findViewById(R.id.profile_portfoliocnt);
        bluePrintCnt = (TextView) findViewById(R.id.profile_blueprintcnt);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Settings.class);

                startActivity(intent);
            }
        });

        btn = (ImageView)findViewById(R.id.profile_morebtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.home_menu, pop.getMenu());
                pop.show();
            }
        });

        morebtn = (ImageView)findViewById(R.id.profile_about_morebtn);
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.more_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.about:
                                Intent i = new Intent(getApplicationContext(), About.class);
                                startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        blueprint = (TextView)findViewById(R.id.profile_blueprintClick);
        portfolio = (TextView)findViewById(R.id.profile_portfolioClick);
        followers = (TextView)findViewById(R.id.profile_followersClick);
        following = (TextView)findViewById(R.id.profile_followingClick);

        blueprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ProfileActivity.this, BlueprintActivity.class);
//                startActivity(i);
                LoadData();
            }
        });

        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, PortfolioActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowersActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowingActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        LoadData();

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

                if(dy>0 && lastId + 2 > layoutManager.getItemCount() && !isLoading)
                {
                    isLoading=true;
                    Log.v("rolling",layoutManager.getChildCount()+" "+layoutManager.getItemCount()+" "+layoutManager.findLastVisibleItemPosition()+" "+
                            layoutManager.findLastVisibleItemPosition());
                    LoadMoreDataFromServer();

                }
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        //LoadData();
    }

    public void LoadData(){

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/detail/"+userClass.getUser_name(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl = jsonObject.getString("nextPage");
                            followCnt.setText(jsonObject.getString("followerCnt"));
                            followingCnt.setText(jsonObject.getString("followingCnt"));
                            bluePrintCnt.setText(jsonObject.getString("blueprintCnt"));
                            portfolioCnt.setText(jsonObject.getString("portfolioCnt"));

                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            if (jsonPost!=null)
                            {
                                JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                    profileClassList.add(profileClass1);
                                }
                                profileAdapter.notifyDataSetChanged();
                            }



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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", userClass.getUser_name() );
                return params;
            }

        };


        requestQueue.add(myReq);


    }
    public void LoadMoreDataFromServer(){
        if(nextPageUrl!=null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest myReq = new StringRequest(Request.Method.GET, nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                nextPageUrl = jsonObject.getString("nextPage");
                                JSONObject jsonPost = jsonObject.getJSONObject("posts");
                                if (jsonPost != null) {
                                    JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                        profileClassList.add(profileClass1);
                                    }
                                    profileAdapter.notifyDataSetChanged();
                                }


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