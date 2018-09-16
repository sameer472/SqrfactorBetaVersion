package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompanyFirmDetails extends AppCompatActivity {
    Toolbar toolbar;
    private EditText yearInService,firmSize,serviceOffered,assetsServed,cityServed,awardName,projectName;
    private Button saveAllChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_firm_details);

        toolbar = (Toolbar) findViewById(R.id.company_firm_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        yearInService= (EditText)findViewById(R.id.years_in_service_text);
        firmSize=(EditText)findViewById(R.id.firm_size_text);
        serviceOffered=(EditText)findViewById(R.id.service_offered_text);
        assetsServed=(EditText)findViewById(R.id.assets_served_text);
        cityServed=(EditText)findViewById(R.id.city_served_text);
        awardName=(EditText)findViewById(R.id.award_name_text);
        projectName=(EditText)findViewById(R.id.project_name_text);
        saveAllChanges=(Button) findViewById(R.id.save_changes);
        BindDataTOVIew();
        saveAllChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToServer();
            }
        });
    }

    private void BindDataTOVIew()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserData","");
        UserData userData = gson.fromJson(json,UserData.class);
        if (!userData.getName_of_the_company().equals("null")){
            yearInService.setText(userData.getYear_in_service());
        }
        if (!userData.getName_of_the_company().equals("null")){
            firmSize.setText(userData.getFirm_size());
        }
        if (!userData.getName_of_the_company().equals("null")){
            serviceOffered.setText(userData.getServices_offered());
        }
        if (!userData.getName_of_the_company().equals("null")){
            serviceOffered.setText(userData.getServices_offered());
        }
        if (!userData.getName_of_the_company().equals("null")){
            assetsServed.setText(userData.getAsset_served());
        }
        if (!userData.getName_of_the_company().equals("null")){
            cityServed.setText(userData.getCity_served());
        }
        if (!userData.getName_of_the_company().equals("null")){
            awardName.setText(userData.getAward_name());
        }
        if (!userData.getName_of_the_company().equals("null")){
            projectName.setText(userData.getProject_name());
        }

    }


    public void saveDataToServer() {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/edit/company-firm-details",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);
                        Toast.makeText(CompanyFirmDetails.this,"Response"+s,Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            yearInService.setText("");
                            serviceOffered.setText("");
                            firmSize.setText("");
                            assetsServed.setText("");
                            cityServed.setText("");
                            awardName.setText("");
                            projectName.setText("");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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

                if (!TextUtils.isEmpty(yearInService.getText().toString())) {
                    params.put("year_in_service", yearInService.getText().toString());
                } else {
                    params.put("year_in_service", null+"");
                }
                if (!TextUtils.isEmpty(firmSize.getText().toString())) {
                    params.put("firm_size", firmSize.getText().toString());
                } else {
                    params.put("firm_size", null+"");
                }
                if (!TextUtils.isEmpty(yearInService.getText().toString())) {
                    params.put("services_offered", serviceOffered.getText().toString());
                } else {
                    params.put("services_offered", null+"");
                }
                if (!TextUtils.isEmpty(assetsServed.getText().toString())) {
                    params.put("asset_served", assetsServed.getText().toString());
                } else {
                    params.put("asset_served", null+"");
                }
                if (!TextUtils.isEmpty(cityServed.getText().toString())) {
                    params.put("city_served", cityServed.getText().toString());
                } else {
                    params.put("city_served", null+"");
                }
                if (!TextUtils.isEmpty(awardName.getText().toString())) {
                    params.put("award_name", awardName.getText().toString());
                } else {
                    params.put("award_name", null+"");
                }
                if (!TextUtils.isEmpty(projectName.getText().toString())) {
                    params.put("project_name", projectName.getText().toString());
                } else {
                    params.put("project_name", null+"");
                }

                return params;

            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


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
}