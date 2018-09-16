package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Credits extends AppCompatActivity {

    Toolbar toolbar;
    ImageView menu;
    String CurrentUrl="https://archsqr.in/api/profile/detail/sqrfactor/credits";
    String previousPageUrl=null;
    String nextPageUrl=null;
    CreditsAdapter creditsAdapter;
    private Button nextPage,prevPage,editProfile;
    private RecyclerView recyclerView;
    TextView bluePrint,portfolio,followers,following,bluePrint1,portfolio1,followers1,following1,userProfileName;
    ImageView userProfile;
    private boolean isScrolling=false;
    int currentItems, totalItems, scrolledItems;
    ArrayList<CreditsClass> creditsClassArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CreditsFragment()).commit();

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_credit);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(Credits.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        userProfileName=findViewById(R.id.userProfileName);
        userProfileName.setText(userClass.getUser_name());
        editProfile=(Button)findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });

        nextPage=(Button)findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageRefersh(nextPageUrl);
                prevPage.setVisibility(View.VISIBLE);
            }
        });
        prevPage=(Button)findViewById(R.id.prevPage);
        prevPage.setVisibility(View.GONE);
        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageRefersh(previousPageUrl);
                Toast.makeText(getApplicationContext(),previousPageUrl+" ",Toast.LENGTH_LONG).show();
                prevPage.setVisibility(View.GONE);
            }
        });
        //adding dummy data for testing
        creditsAdapter = new CreditsAdapter(this, creditsClassArrayList);
        recyclerView.setAdapter(creditsAdapter);

        userProfile = findViewById(R.id.userProfilePic);
                Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                        .into(userProfile);

        toolbar = (Toolbar) findViewById(R.id.credits_toolbar);
        toolbar.setTitle("Credits");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        PageRefersh(CurrentUrl);

        menu = (ImageView) findViewById(R.id.morebtn);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Credits.this, v);
                popupMenu.inflate(R.menu.profie_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        bluePrint = (TextView) findViewById(R.id.blueprint);

        bluePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Credits.this, ProfileActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();

            }
        });

        portfolio = (TextView) findViewById(R.id.portfolio);
        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Credits.this, PortfolioActivity.class);
                j.putExtra("UserName",userClass.getUser_name());
                startActivity(j);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();
            }
        });
        followers = (TextView) findViewById(R.id.followers);
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Credits.this, FollowersActivity.class);
                k.putExtra("UserName",userClass.getUser_name());
                startActivity(k);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();

            }
        });
        following = (TextView) findViewById(R.id.following);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(Credits.this, FollowingActivity.class);
                l.putExtra("UserName",userClass.getUser_name());
                startActivity(l);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();

            }
        });
        bluePrint1=findViewById(R.id.blueprint1);
        bluePrint1.setText(userClass.getBlueprintCount());

        portfolio1=findViewById(R.id.portfolio1);
        portfolio1.setText(userClass.getPortfolioCount());

        followers1=findViewById(R.id.followers1);
        followers1.setText(userClass.getFollowerCount());

        following1=findViewById(R.id.following1);
        following1.setText(userClass.getFollowingCount());
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                currentItems = layoutManager.getChildCount();
//                totalItems = layoutManager.getItemCount();
//                scrolledItems = layoutManager.findFirstVisibleItemPosition();
//                if (isScrolling && (currentItems + scrolledItems == totalItems)) {
//                    isScrolling = false;
//                    fetchData();
//                }
//            }
//        });

    }
    public void PageRefersh( String currentUrl) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, currentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        creditsClassArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject post = jsonObject.getJSONObject("posts");
                            nextPageUrl=post.getString("next_page_url");
                            previousPageUrl=post.getString("prev_page_url");
                            JSONArray jsonArrayData = post.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                CreditsClass creditsClass = new CreditsClass(jsonArrayData.getJSONObject(i));
                                creditsClassArrayList.add(creditsClass);
                            }
                            creditsAdapter.notifyDataSetChanged();

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}