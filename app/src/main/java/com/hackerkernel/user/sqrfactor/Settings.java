package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Settings extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView menu,profilePic;
    private TextView text1,text2,text3,text4,text5,profileUserName;
    private TextView bluePrint1,portfolio1,followers1,following1;
    private TextView bluePrint,portfolio,followers,following;
    private ArrayList<UserData> userDataArrayList = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profilePic=(ImageView)findViewById(R.id.userProfilePic);

        profileUserName=(TextView)findViewById(R.id.userProfileName);
        bluePrint =(TextView)findViewById(R.id.blueprint);
        portfolio =(TextView)findViewById(R.id.portfolio);
        followers =(TextView)findViewById(R.id.followers);
        following =(TextView)findViewById(R.id.following);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SettingsFragment()).commit();

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profilePic);
       // Log.v("image",userClass.getProfile());
        profileUserName.setText(userClass.getUser_name());
        toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        menu = (ImageView)findViewById(R.id.morebtn);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Settings.this,v);
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

        bluePrint1 = (TextView)findViewById(R.id.blueprint1);
        bluePrint1.setText(userClass.getBlueprintCount());


        bluePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, ProfileActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();

            }
        });

        portfolio1 = (TextView)findViewById(R.id.portfolio1);
        portfolio1.setText(userClass.getPortfolioCount());


        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Settings.this, PortfolioActivity.class);
                j.putExtra("UserName",userClass.getUser_name());
                startActivity(j);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();
            }
        });

        followers1 = (TextView)findViewById(R.id.followers1);
        followers1.setText(userClass.getFollowerCount());


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Settings.this, FollowersActivity.class);
                k.putExtra("UserName",userClass.getUser_name());
                startActivity(k);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio())
                //.addToBackStack(null).commit();

            }
        });
        following1 = (TextView)findViewById(R.id.following1);

        following1.setText(userClass.getFollowingCount());


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(Settings.this, FollowingActivity.class);
                l.putExtra("UserName",userClass.getUser_name());
                startActivity(l);

            }
        });

        text1 = (TextView)findViewById(R.id.basic_details);
        text2 = (TextView)findViewById(R.id.edu_details);
        text3 = (TextView)findViewById(R.id.prof_details);
        text4 = (TextView)findViewById(R.id.other_details);
        text4.setVisibility(View.GONE);
        text5 = (TextView)findViewById(R.id.change_password);


        //Toast.makeText(this,"type"+userClass.getUserType(),Toast.LENGTH_LONG).show();

        getUserDataFromServer();

        if(userClass.getUserType().equals("work_individual"))
        {

            text1.setText("Basic Details");
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Settings.this,BasicDetails.class);
                    startActivity(i);
                }
            });

            text2.setText("Education Details");
            text2.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Intent j = new Intent(Settings.this,EducationDetailsActivity.class);
                    startActivity(j);
                }
            });

            text3.setText("Professional Details");
            text3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent k = new Intent(Settings.this,ProfessionalActivity.class);
                    startActivity(k);
                }
            });

            text4.setVisibility(View.VISIBLE);
            text4.setText("Other Details");
            text4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent l = new Intent(Settings.this,OtherDetailsActivity.class);
                    startActivity(l);
                }
            });
            text5.setText("Change Password");
            text5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent l = new Intent(Settings.this,ChangePassword.class);
                    startActivity(l);
                }
            });

        }
        else if(userClass.getUserType().equals("work_architecture_college")) {
            text1.setText("Basic Details");
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Settings.this, CollegeBasicDetails.class);
                    startActivity(i);
                }
            });

            text2.setText("College/University Details");
            text2.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Intent j = new Intent(Settings.this, CollegeDetails.class);
                    startActivity(j);
                }
            });

            text3.setText("Faculty Details");
            text3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent k = new Intent(Settings.this, EmployeeMemberDetails.class);
                    startActivity(k);
                }
            });

            text4.setVisibility(View.GONE);

            text5.setText("Change Password");
            text5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent l = new Intent(Settings.this, ChangePassword.class);
                    startActivity(l);
                }
            });
        }
        else
        {
            text1.setText("Basic Details");
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Settings.this,BasicFirmDetails.class);
                    startActivity(i);
                }
            });

            text2.setText("Company Firm Details");
            text2.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Intent j = new Intent(Settings.this,CompanyFirmDetails.class);
                    startActivity(j);
                }
            });

            text3.setText("Employee/Member Details");
            text3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent k = new Intent(Settings.this,EmployeeMemberDetails.class);
                    startActivity(k);
                }
            });

            text4.setVisibility(View.GONE);

            text5.setText("Change Password");
            text5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent l = new Intent(Settings.this,ChangePassword.class);
                    startActivity(l);
                }
            });

        }
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

    private void getUserDataFromServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/profile/edit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("User Data");
                            UserData  userData = new UserData(jsonArray.getJSONObject(0));
                           // userDataArrayList.add(userData);
                            Toast.makeText(getApplicationContext(), userData.getId()+userData.getName_of_the_company()+userData.getAddress(), Toast.LENGTH_LONG).show();

                            mPrefs = getSharedPreferences("userData", MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(userData);
                            prefsEditor.putString("UserData", json);
                            prefsEditor.commit();

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
