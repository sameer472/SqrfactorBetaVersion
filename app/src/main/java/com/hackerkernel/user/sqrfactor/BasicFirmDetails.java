package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicFirmDetails extends AppCompatActivity {
    Toolbar toolbar;
    private Button addEmail, AddOccupation, SaveandNext;
    private ArrayList<CountryClass> countryClassArrayList = new ArrayList<>();
    private ArrayList<String> countryName = new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList = new ArrayList<>();
    private ArrayList<String> statesName = new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList = new ArrayList<>();
    private ArrayList<String> citiesName = new ArrayList<>();
    private EditText nextEmail1, nextEmail2, nextEmail3;
    private String[] companyFirm;
    private int countryId = 1;
    private String country_val = null, state_val = null, city_val = null, compnayFirm_val = null;
    private EditText nameOfCompany, mobileNumber, email, shortBio, firmCompanyName, registerNumber, facbook, linkedin, instagram, twitter;
    private Button save;
    private boolean email2 = false;
    private boolean email3 = false;
    private int count = 0;
    Spinner spin;
    UserClass userClass;
    UserData userData;
    Spinner countrySpinner, companySpinner, citySpinner, stateSpinner;
    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String> company = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_firm_details);

        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        userData= gson1.fromJson(json1, UserData.class);

        toolbar = (Toolbar) findViewById(R.id.basic_firm_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        countrySpinner = (Spinner) findViewById(R.id.company_Country);
        stateSpinner = (Spinner) findViewById(R.id.company_State);
        citySpinner = (Spinner) findViewById(R.id.company_City);

        companySpinner = (Spinner) findViewById(R.id.company_select);


        nameOfCompany = (EditText) findViewById(R.id.company_name_text);
        mobileNumber = (EditText) findViewById(R.id.company_mobile_Text);
        email = (EditText) findViewById(R.id.company_email_text);
        shortBio = (EditText) findViewById(R.id.company_shortBioText);
        firmCompanyName = (EditText) findViewById(R.id.company_firm_name_text);
        registerNumber = (EditText) findViewById(R.id.company_registerNumber_text);
        facbook = (EditText) findViewById(R.id.company_facebookLinktext);
        instagram = (EditText) findViewById(R.id.company_InstagramLinktext);
        linkedin = (EditText) findViewById(R.id.company_LinkedinLinktext);
        twitter = (EditText) findViewById(R.id.company_TwitterLinktext);
        save = (Button) findViewById(R.id.company_SaveandNext);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToServer();
            }
        });

        addEmail = (Button) findViewById(R.id.company_AddEmail);
        addEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AddEmailPopUp();
            }
        });

        companyFirm = getResources().getStringArray(R.array.Company);

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int position, long id) {
                compnayFirm_val = companyFirm[position];

            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, companyFirm);
        companySpinner.setAdapter(spin_adapter);
//        ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, countries);
//        countrySpinner.setAdapter(spin_adapter1);

//        ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, company);
//        companySpinner.setAdapter(spin_adapter2);


        BindDataTOviews();
        LoadCountryFromServer();
        LoadStateFromServer(1);
        LoadCitiesFromServer(1, 1);


        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int position, long id) {
                if (countryName.size() > 0) {
                    //country_val = countryName.get(position);
                    country_val=position+"";
//                    if(!userData.getCountry_id().equals("null"))
//                        countrySpinner.setSelection(Integer.parseInt(userData.getCountry_id()));
                    LoadStateFromServer(countryClassArrayList.get(position).getId());
                    countryId = countryClassArrayList.get(position).getId();


                }

                //Toast.makeText(getApplicationContext(),countryClassArrayList.get(position).getId()+" ",Toast.LENGTH_LONG).show();


            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int position, long id) {
                if (statesName.size() > 0) {
                    //state_val = statesName.get(position);
                    state_val=position+"";

//                    if(!userData.getState_id().equals("null"))
//                        stateSpinner.setSelection(Integer.parseInt(userData.getState_id()));


                    LoadCitiesFromServer(statesClassArrayList.get(position).getId(), countryId);
                }
                // Toast.makeText(getApplicationContext(),statesClassArrayList.get(position).getId()+" "+countryId,Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int position, long id) {
                if (citiesName.size() > 0)
                    // city_val = citiesName.get(position);
                    city_val=position+"";
//                if(!userData.getCity_id().equals("null"))
//                    citySpinner.setSelection(Integer.parseInt(userData.getCity_id()));
                // Toast.makeText(getApplicationContext(),city_val,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

    }

    public void LoadCountryFromServer() {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://archsqr.in/api/event/country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);
                        //Toast.makeText(BasicFirmDetails.this, "res" + s, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray countries = jsonObject.getJSONArray("countries");
                            countryName.clear();
                            countryClassArrayList.clear();
                            for (int i = 0; i < countries.length(); i++) {
                                CountryClass countryClass = new CountryClass(countries.getJSONObject(i));
                                countryClassArrayList.add(countryClass);
                                countryName.add(countryClass.getName());

                            }


                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, countryName);
                            countrySpinner.setAdapter(spin_adapter1);

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

        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


    }

    public void LoadStateFromServer(final int countryId) {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/state",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);
                        //  Toast.makeText(BasicFirmDetails.this, "res" + s, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray states = jsonObject.getJSONArray("states");
                            statesName.clear();
                            statesClassArrayList.clear();
                            for (int i = 0; i < states.length(); i++) {
                                StateClass stateClass = new StateClass(states.getJSONObject(i));
                                statesClassArrayList.add(stateClass);
                                statesName.add(stateClass.getName());
                            }


                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, statesName);
                            stateSpinner.setAdapter(spin_adapter2);
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
                params.put("country", countryId + "");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void LoadCitiesFromServer(final int stateId, final int countryId) {
        //Toast.makeText(BasicDetails.this,stateId,Toast.LENGTH_LONG).show();
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/city",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray cities = jsonObject.getJSONArray("cities");
                            citiesName.clear();
                            citiesClassArrayList.clear();
                            for (int i = 0; i < cities.length(); i++) {
                                CitiesClass citiesClass = new CitiesClass(cities.getJSONObject(i));
                                citiesClassArrayList.add(citiesClass);
                                citiesName.add(citiesClass.getName());
                            }
                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(BasicFirmDetails.this, android.R.layout.simple_list_item_1, citiesName);
                            citySpinner.setAdapter(spin_adapter3);


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
                params.put("state", stateId + "");
                params.put("country", countryId + "");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void AddEmailPopUp() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(BasicFirmDetails.this);
        final View promptsView = li.inflate(R.layout.addemailprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BasicFirmDetails.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        nextEmail1 = (EditText) promptsView
                .findViewById(R.id.nextEmailText1);
        nextEmail2 = (EditText) promptsView
                .findViewById(R.id.nextEmailText2);
        nextEmail3 = (EditText) promptsView
                .findViewById(R.id.nextEmailText3);
        final Button addPromptButton = (Button) promptsView
                .findViewById(R.id.AddPromptButton);

        final LinearLayout linearLayoutPrompt1 = (LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt1);
        linearLayoutPrompt1.setVisibility(View.GONE);

        final LinearLayout linearLayoutPrompt2 = (LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt2);
        linearLayoutPrompt2.setVisibility(View.GONE);

        final Button removePromptButton1 = (Button) promptsView
                .findViewById(R.id.removeEmailButton1);

        final Button removePromptButton2 = (Button) promptsView
                .findViewById(R.id.removeEmailButton2);

        //final TextInputLayout nextEmail=(TextInputLayout) promptsView.findViewById(R.id.nextEmail);

        addPromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                if (!email2 && count == 1) {
                    linearLayoutPrompt1.setVisibility(View.VISIBLE);
                    email2 = true;
                }
                if (!email3 && count == 2) {
                    linearLayoutPrompt2.setVisibility(View.VISIBLE);
                    email3 = true;
                }

            }
        });

        removePromptButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email2) {
                    linearLayoutPrompt1.setVisibility(View.GONE);
                    email2 = false;
                    count--;

                }
            }
        });
        removePromptButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email3) {
                    linearLayoutPrompt2.setVisibility(View.GONE);
                    email3 = false;
                    count--;
                }
            }
        });


        // set dialog message
                /*alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("SAVE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // here we will get the new email
                                        //
                                    }
                                })
                        .setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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

    public void saveDataToServer() {
        //https://archsqr.in/api/profile/edit(POST Method)

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/hire-organization-basic-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);
                        Toast.makeText(BasicFirmDetails.this, "Response" + s, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            nameOfCompany.setText("");
                            mobileNumber.setText("");
                            email.setText("");
                            shortBio.setText("");
                            firmCompanyName.setText("");
                            registerNumber.setText("");
                            facbook.setText("");
                            instagram.setText("");
                            linkedin.setText("");
                            twitter.setText("");



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
                if(!TextUtils.isEmpty(nameOfCompany.getText().toString()))
                {
                    params.put("name_of_the_company", nameOfCompany.getText().toString());
                }
                else {
                    params.put("name_of_the_company", null+"");
                }
                params.put("address", null+"");
                params.put("pin_code", null+"");
                // params.put("mobile",mobileNumber.getText().toString());
                // params.put("aadhar_id",email.getText().toString());
                //params.put("aadhar_id",nextEmail1.getText().toString());
//                params.put("aadhar_id",nextEmail2.getText().toString());
                params.put("business_description", null+"");
                params.put("webside", null+"");
                params.put("country", country_val);
                params.put("state", state_val);
                params.put("city", city_val);

                if(!TextUtils.isEmpty(shortBio.getText().toString()))
                {
                    params.put("short_bio", shortBio.getText().toString());
                }
                else {
                    params.put("short_bio", null+"");
                }

                if(!TextUtils.isEmpty(firmCompanyName.getText().toString()))
                {
                    params.put("firm_or_company_name", firmCompanyName.getText().toString());
                }
                else {
                    params.put("firm_or_company_name", null+"");
                }
                params.put("types_of_firm_company", compnayFirm_val);

                if(!TextUtils.isEmpty(registerNumber.getText().toString()))
                {
                    params.put("firm_or_company_registration_number", registerNumber.getText().toString());
                }
                else {
                    params.put("firm_or_company_registration_number", null+"");
                }
                if(!TextUtils.isEmpty(facbook.getText().toString()))
                {
                    params.put("facebook_link", facbook.getText().toString());
                }
                else {
                    params.put("facebook_link", null+"");

                }
                if(!TextUtils.isEmpty(twitter.getText().toString()))
                {
                    params.put("twitter_link", twitter.getText().toString());
                }
                else {
                    params.put("twitter_link", null+"");
                }
                if(!TextUtils.isEmpty(linkedin.getText().toString()))
                {
                    params.put("linkedin_link", linkedin.getText().toString());
                }
                else {
                    params.put("linkedin_link",null+"");
                }




                //params.put("date_of_birth",instagram.getText().toString());
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);
    }

    private void BindDataTOviews() {

        if (!userClass.getUser_name().equals("null")) {
            nameOfCompany.setText(userClass.getUser_name());
        }
        if (!userClass.getMobile().equals("null")) {
            mobileNumber.setText(userClass.getMobile());
        }
        if (!userClass.getEmail().equals("null")) {
            email.setText(userClass.getEmail());
        }
        if (!userData.getShot_bio().equals("null")) {
            shortBio.setText(userData.getShot_bio());
        }
        if (!userData.getFirm_or_company_name().equals("null")) {
            firmCompanyName.setText(userData.getFirm_or_company_name());
        }
        if (!userData.getFirm_or_company_registration_number().equals("null")) {
            registerNumber.setText(userData.getFirm_or_company_registration_number());
        }
        if (!userData.getFacebook_link().equals("null")) {
            facbook.setText(userData.getFacebook_link());
        }
        if (!userData.getLinkedin_link().equals("null")) {
            linkedin.setText(userData.getLinkedin_link());
        }
        if (!userData.getTwitter_link().equals("null")) {
            twitter.setText(userData.getTwitter_link());
        }
        if (!userData.getInstagram_link().equals("null")) {
            instagram.setText(userData.getInstagram_link());
        }

    }
}