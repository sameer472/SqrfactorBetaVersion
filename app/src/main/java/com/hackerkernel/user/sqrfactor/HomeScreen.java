package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeScreen extends ToolbarActivity {

    Toolbar toolbar;
    static TabLayout tabLayout;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ImageView profileImage;
    private boolean notificationVisible = false;
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();
    private List<BadgeView> mBadgeViews;
    private int count = 0;
    private SearchResultAdapter searchResultAdapter;
    LinearLayout linearLayout;
    private EditText searchEditText;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;
    private ArrayList<SearchResultClass> searchResultClasses=new ArrayList<>();
    private RecyclerView recyclerView;
    BadgeView badge7;
    View v;
    private FragmentTabHost mTabHost;
    static int count1;
    private UserClass userClass;
    static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
//        toolbar.setNavigationIcon(R.drawable.profilepic);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
//        actionBar.setLogo(R.drawable.profilepic);
        actionBar.setDisplayShowTitleEnabled(false);

        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        profileImage = findViewById(R.id.toolbar_profile);
        drawerLayout = findViewById(R.id.drawer_layout);

        linearLayout=(LinearLayout)findViewById(R.id.mainfrag);
        searchEditText=(EditText)findViewById(R.id.user_search);
        recyclerView=(RecyclerView)findViewById(R.id.search_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);



        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        searchResultAdapter = new SearchResultAdapter( this,searchResultClasses);
        recyclerView.setAdapter(searchResultAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Toast.makeText(getApplicationContext(),s+"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(),s+"",Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                FetchSearchedDataFromServer(s+"");


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "sqr");
        TokenClass.Token = token;
        TokenClass tokenClass = new TokenClass(token);
        Log.v("Token1", token);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainfrag, new NewsFeedFragment()).commit();


        tabLayout = (TabLayout)findViewById(R.id.tabs);

        //tabLayout.setupWithViewPager(pager);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.newsfeeed3color));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.chatmsg));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notify4));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.toggle));
        //getnotificationCount();
       // RealTimeNotificationListner();




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){

                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfrag, new NewsFeedFragment()).commit();
                        tab.setIcon(R.drawable.newsfeeed3color);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.chatmsgcolor);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfrag, new MessageFragment()).commit();

                        if(tab.getCustomView()!=null)
                            v = tab.getCustomView().findViewById(R.id.badgeCotainer);
                        if(v != null) {
                            v.setVisibility(View.GONE);
                        }
                        break;

                    case 2:
                        tab.setIcon(R.drawable.notifycolor1);
                        if(tab.getCustomView()!=null)
                         v = tab.getCustomView().findViewById(R.id.badgeCotainer);
                        if(v != null) {
                            v.setVisibility(View.GONE);
                        }
                        ref.child("notification").child(userClass.getUserId()+"").child("unread").removeValue();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfrag, new NotificationsFragment()).commit();
                        break;


                    case 3:
                        tab.setIcon(R.drawable.toggle1color);
                        new ModalSheet().show(getSupportFragmentManager(), "");
                        break;

                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        tab.setIcon(R.drawable.newsfeeed4);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.chatmsg);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.notify4);
                        break;

                    case 3:
                        tab.setIcon(R.drawable.toggle);

                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {



            }
        });
        navigationView = findViewById(R.id.navigation_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.navigation_drawer);
        ImageView profileImage = (ImageView) headerLayout.findViewById(R.id.profile_image);
        headerLayout.setVisibility(View.VISIBLE);

        Toast.makeText(this, userClass.getProfile(), Toast.LENGTH_SHORT).show();
        Glide.with(this).load("https://archsqr.in/" + userClass.getProfile())
                .into(profileImage);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.navigation_profile) {
                    Intent i = new Intent(HomeScreen.this, ProfileActivity.class);
                    //i.putExtra("Activity", "1");
                    startActivity(i);

                }
                if (id == R.id.navigation_credits) {
                    Intent j = new Intent(HomeScreen.this, Credits.class);
                    //j.putExtra("Activity", "2");
                    startActivity(j);

                }
                if (id == R.id.navigation_settings) {
                    Intent intent = new Intent(HomeScreen.this, Settings.class);
                    //intent.putExtra("Activity", "3");
                    startActivity(intent);

                }
                if (id == R.id.navigation_logout) {

//
//                    //call api here for logout
//
                    SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("MyObject", "");
                    final UserClass userClass = gson.fromJson(json, UserClass.class);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications" + userClass.getUserId());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("chats" + userClass.getUserId());

                    RequestQueue requestQueue = Volley.newRequestQueue(HomeScreen.this);

                    StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/logout",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("ReponseFeed", response);
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date();
                                    IsOnline isOnline=new IsOnline("False",formatter.format(date));
//                            HashMap<String,String> status=new HashMap<>();
//                            status.put("android","True");
//                            status.put("web","False");
//                            status.put("time",formatter.format(date) );
                                    ref.child("Status").child(userClass.getUserId()+"").child("android").setValue(isOnline);

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


                    Intent intent = new Intent(HomeScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();

                }


                menuItem.setChecked(false);
                drawerLayout.closeDrawers();
                //Toast.makeText(HomeScreen.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }




    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //navigationView.getMenu().getItem(0).setCheckable(false);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.action_search:
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void FetchSearchedDataFromServer(final String search) {

        RequestQueue requestQueue = Volley.newRequestQueue(HomeScreen.this);
        // "https://archsqr.in/api/profile/detail/
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/parse/search",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        searchResultClasses.clear();
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject users=jsonObject.getJSONObject("users");
                            JSONArray dataArray=users.getJSONArray("data");
                            for(int i=0;i<dataArray.length();i++)
                            {
                                SearchResultClass searchResultClass=new SearchResultClass(dataArray.getJSONObject(i));
                                searchResultClasses.add(searchResultClass);
                            }
                            searchResultAdapter.notifyDataSetChanged();

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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search",search);
                return params;
            }

        };
        requestQueue.add(myReq);

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.context=getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.context=getApplicationContext();
    }

    public static void  getnotificationCount(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/notificationcount",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            count1 =jsonObject.getInt("count");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(count1!=0) {
                            TabLayout.Tab tab1 = tabLayout.getTabAt(2);
                            if (tab1.getCustomView() == null)
                                tab1.setCustomView(R.layout.badage);

                            if (tab1 != null && tab1.getCustomView() != null) {
                                TextView b = (TextView) tab1.getCustomView().findViewById(R.id.badge);
                                if (b != null) {
                                    b.setText(count1 + "");
                                }
                                View v = tab1.getCustomView().findViewById(R.id.badgeCotainer);
                                if (v != null) {
                                    v.setVisibility(View.VISIBLE);
                                }
                            }
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


    public static void getUnReadMsgCount(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/unread_counts",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            count1 =jsonObject.getInt("Unread Messages");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(count1!=0) {
                            TabLayout.Tab tab1 = tabLayout.getTabAt(1);
                            if (tab1.getCustomView() == null)
                                tab1.setCustomView(R.layout.badage);

                            if (tab1 != null && tab1.getCustomView() != null) {
                                TextView b = (TextView) tab1.getCustomView().findViewById(R.id.badge);
                                if (b != null) {
                                    b.setText(count1 + "");
                                }
                                View v = tab1.getCustomView().findViewById(R.id.badgeCotainer);
                                if (v != null) {
                                    v.setVisibility(View.VISIBLE);
                                }
                            }
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