package com.hackerkernel.user.sqrfactor;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BasicDetails extends AppCompatActivity {

    private Button addEmail,AddOccupation,SaveandNext;
    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList=new ArrayList<>();
    private ArrayList<String> statesName=new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList=new ArrayList<>();
    private ArrayList<String> citiesName=new ArrayList<>();
    private EditText nextEmail1,nextEmail2,nextEmail3;

    private UserClass userClass;
    private UserData userData;
    private Spinner countrySpinner,gender,spin,stateSpinner,citySpinner;
    private String country_val=null,state_val=null,city_val=null,gender_val=null;
    private RadioGroup radioGroup;
    private EditText dateOfBirth,Occupation,fNametext,lNametext,Emailtext,mobileText,dateOfBirthText,UIDtext,shortBioTecxt,
            facebookLinktext,TwitterLinktext,LinkedinLinktext,InstagramLinktext,email;
    private LinearLayout linearLayout2;
    private boolean email2=false;
    private boolean email3=false;
    private int countryId=1;
    private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6;
    private int count=0;
    private Toolbar toolbar;
    ArrayList<String> countries = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);

        toolbar = (Toolbar) findViewById(R.id.basic_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        userData= gson1.fromJson(json1, UserData.class);

        Occupation=(EditText)findViewById(R.id.otherOccupationtext);
        email=(EditText)findViewById(R.id.Emailtext);
        InstagramLinktext=(EditText)findViewById(R.id.InstagramLinktext);
        LinkedinLinktext=(EditText)findViewById(R.id.LinkedinLinktext);
        TwitterLinktext=(EditText)findViewById(R.id.TwitterLinktext);
        facebookLinktext=(EditText)findViewById(R.id.facebookLinktext);
        shortBioTecxt=(EditText)findViewById(R.id.shortBioTecxt);
        fNametext=(EditText)findViewById(R.id.fNametext);
        lNametext=(EditText)findViewById(R.id.lNametext);
        Emailtext=(EditText)findViewById(R.id.Emailtext);
        mobileText=(EditText)findViewById(R.id.mobileText);
        dateOfBirthText=(EditText)findViewById(R.id.dateOfBirthText);
        UIDtext=(EditText)findViewById(R.id.UIDtext);
        SaveandNext=(Button)findViewById(R.id.SaveandNext);
        AddOccupation=(Button)findViewById(R.id.AddOccupation);

        SaveandNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToServer();
            }
        });

        addEmail = (Button) findViewById(R.id.AddEmail);
        linearLayout2 =(LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout2.setVisibility(View.GONE);
        addEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AddEmailPopup();
            }
        });



        final String[] gender = { "Male", "Female" };
        spin = (Spinner) findViewById(R.id.gender);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                gender_val = gender[position];

            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1, gender);
        spin.setAdapter(spin_adapter);


        countrySpinner = (Spinner) findViewById(R.id.Country);
        stateSpinner = (Spinner) findViewById(R.id.State);
        citySpinner = (Spinner) findViewById(R.id.City);

        BindDataTOviews();
        LoadCountryFromServer();
        LoadStateFromServer(1);
        LoadCitiesFromServer(1,1);



        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {

                if(countryName.size()>0)
                {
                    //country_val = countryName.get(position);
                    country_val=position+"";
//                    if(!userData.getCountry_id().equals("null"))
//                        countrySpinner.setSelection(Integer.parseInt(userData.getCountry_id()));
                    // Toast.makeText(getApplicationContext(),countryClassArrayList.get(position).getId()+" ",Toast.LENGTH_LONG).show();
                    LoadStateFromServer(countryClassArrayList.get(position).getId());
                    countryId=countryClassArrayList.get(position).getId();
                }



            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if(statesName.size()>0)
                {

                    // state_val = statesName.get(position);
                    state_val=position+"";
//                    if(!userData.getState_id().equals("null"))
//                        stateSpinner.setSelection(Integer.parseInt(userData.getState_id()));
                    //Toast.makeText(getApplicationContext(),statesClassArrayList.get(position).getId()+" "+countryId,Toast.LENGTH_LONG).show();
                    LoadCitiesFromServer(statesClassArrayList.get(position).getId(),countryId);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if (citiesName.size()>0)
                {

                    //city_val = citiesName.get(position);
                    city_val=position+"";
//                    if(!userData.getCity_id().equals("null"))
//                        citySpinner.setSelection(Integer.parseInt(userData.getCity_id()));
                    // Toast.makeText(getApplicationContext(),city_val,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });



        //final String[] Countries = { "India", "Pakistan","England","Australia" };
//        Locale[] locale = Locale.getAvailableLocales();
//
//        String country;
//        for( Locale loc : locale ){
//            country = loc.getDisplayCountry();
//            if( country.length() > 0 && !countries.contains(country) ){
//                countries.add( country );
//            }
//        }
//        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);




        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabe(myCalendar);
            }

        };

        dateOfBirthText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                new DatePickerDialog(BasicDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        checkBox1= (CheckBox) findViewById(R.id.checkbox1);
        checkBox2= (CheckBox) findViewById(R.id.checkbox2);
        checkBox3= (CheckBox) findViewById(R.id.checkbox3);
        checkBox4= (CheckBox) findViewById(R.id.checkbox4);
        checkBox5= (CheckBox) findViewById(R.id.checkbox5);
        checkBox6= (CheckBox) findViewById(R.id.checkbox6);

        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    linearLayout2.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearLayout2.setVisibility(View.GONE);
                }
            }
        });

        SaveandNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToServer();
            }
        });



    }

    public void LoadCountryFromServer()
    {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://archsqr.in/api/event/country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        //Toast.makeText(BasicDetails.this,"res"+s,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray countries=jsonObject.getJSONArray("countries");
                            countryName.clear();
                            countryClassArrayList.clear();
                            for (int i=0;i<countries.length();i++)
                            {
                                CountryClass countryClass=new CountryClass(countries.getJSONObject(i));
                                countryClassArrayList.add(countryClass);
                                countryName.add(countryClass.getName());

                            }



                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1,countryName);
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }

        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


    }

    public void LoadStateFromServer(final int countryId)
    {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/state",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        // Toast.makeText(BasicDetails.this,"res"+s,Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray states=jsonObject.getJSONArray("states");
                            statesName.clear();
                            statesClassArrayList.clear();
                            for (int i=0;i< states.length();i++)
                            {
                                StateClass stateClass=new StateClass(states.getJSONObject(i));
                                statesClassArrayList.add(stateClass);
                                statesName.add(stateClass.getName());
                            }



                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1,statesName);
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country",countryId+"");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void LoadCitiesFromServer(final int stateId,final int countryId)
    {
        //Toast.makeText(BasicDetails.this,stateId,Toast.LENGTH_LONG).show();
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/city",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray cities=jsonObject.getJSONArray("cities");
                            citiesName.clear();
                            citiesClassArrayList.clear();
                            for (int i=0;i< cities.length();i++)
                            {
                                CitiesClass citiesClass=new CitiesClass(cities.getJSONObject(i));
                                citiesClassArrayList.add(citiesClass);
                                citiesName.add(citiesClass.getName());
                            }
                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(BasicDetails.this, android.R.layout.simple_list_item_1,citiesName);
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state",stateId+"");
                params.put("country",countryId+"");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void AddEmailPopup()
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(BasicDetails.this);
        final View promptsView = li.inflate(R.layout.addemailprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BasicDetails.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        nextEmail1 = (EditText) promptsView
                .findViewById(R.id.nextEmailText1);
        nextEmail2 = (EditText) promptsView
                .findViewById(R.id.nextEmailText2);
        nextEmail3 = (EditText) promptsView
                .findViewById(R.id.nextEmailText3);

        final Button addPromptButton= (Button) promptsView
                .findViewById(R.id.AddPromptButton);

        final LinearLayout linearLayoutPrompt1=(LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt1);
        linearLayoutPrompt1.setVisibility(View.GONE);

        final LinearLayout linearLayoutPrompt2=(LinearLayout) promptsView.findViewById(R.id.linearLayoutprompt2);
        linearLayoutPrompt2.setVisibility(View.GONE);

        final Button removePromptButton1= (Button) promptsView
                .findViewById(R.id.removeEmailButton1);

        final Button removePromptButton2= (Button) promptsView
                .findViewById(R.id.removeEmailButton2);

        //final TextInputLayout nextEmail=(TextInputLayout) promptsView.findViewById(R.id.nextEmail);

        addPromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                if(!email2 && count==1)
                {
                    linearLayoutPrompt1.setVisibility(View.VISIBLE);
                    email2=true;
                }
                if(!email3 && count==2)
                {
                    linearLayoutPrompt2.setVisibility(View.VISIBLE);
                    email3=true;
                }

            }
        });

        removePromptButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email2)
                {
                    linearLayoutPrompt1.setVisibility(View.GONE);
                    email2=false;
                    count--;

                }
            }
        });
        removePromptButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email3)
                {
                    linearLayoutPrompt2.setVisibility(View.GONE);
                    email3=false;
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


    public void saveDataToServer()
    {
        //https://archsqr.in/api/profile/edit(POST Method)

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/edit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(BasicDetails.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            fNametext.setText("");
                            lNametext.setText("");
                            mobileText.setText("");
                            dateOfBirthText.setText("");
                            email.setText("");
                            UIDtext.setText("");
                            shortBioTecxt.setText("");
                            facebookLinktext.setText("");
                            LinkedinLinktext.setText("");
                            TwitterLinktext.setText("");
                            InstagramLinktext.setText("");


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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if(!TextUtils.isEmpty(fNametext.getText().toString()))
                {
                    params.put("first_name",fNametext.getText().toString());
                }
                else {
                    params.put("first_name",null+"");
                }
                if(!TextUtils.isEmpty(lNametext.getText().toString()))
                {
                    params.put("last_name",lNametext.getText().toString());
                }
                else {
                    params.put("last_name",null+"");
                }
                if(!TextUtils.isEmpty(UIDtext.getText().toString()))
                {
                    params.put("aadhar_id",UIDtext.getText().toString());
                }
                else {
                    params.put("aadhar_id",null+"");
                }
                if(!TextUtils.isEmpty(Occupation.getText().toString()))
                {
                    params.put("occupation",Occupation.getText().toString());
                }
                else {
                    params.put("occupation",null+"");
                }


                params.put("pin_code",null+"");
                params.put("looking_for_an_architect",null+"");
                params.put("address",null+"");



//                params.put("occupation",email.getText().toString());
//                params.put("pin_code",nextEmail1.getText().toString());
//                params.put("occupation",nextEmail2.getText().toString());
//                params.put("occupation",nextEmail3.getText().toString());

                if(country_val!=null)
                {
                    params.put("country",country_val);
                }
                else {
                    params.put("country",null+"");
                }
                if(state_val!=null)
                {
                    params.put("state",state_val);
                }
                else {
                    params.put("state",null+"");
                }
                if(city_val!=null)
                {
                    params.put("city",city_val);
                }
                else {
                    params.put("city",null+"");
                }


                if(!TextUtils.isEmpty(facebookLinktext.getText().toString()))
                {
                    params.put("facebook_link",facebookLinktext.getText().toString());
                }
                else {
                    params.put("facebook_link",null+"");
                }

                if(!TextUtils.isEmpty(TwitterLinktext.getText().toString()))
                {
                    params.put("twitter_link",TwitterLinktext.getText().toString());
                }
                else {
                    params.put("twitter_link",null+"");
                }

                if(!TextUtils.isEmpty(LinkedinLinktext.getText().toString()))
                {
                    params.put("linkedin_link",LinkedinLinktext.getText().toString());
                }
                else {
                    params.put("linkedin_link",null+"");
                }

                if(!TextUtils.isEmpty(InstagramLinktext.getText().toString()))
                {
                    params.put("instagram_link",InstagramLinktext.getText().toString());
                }
                else {
                    params.put("instagram_link",null+"");
                }
                if(!TextUtils.isEmpty(dateOfBirth.getText().toString()))
                {
                    params.put("date_of_birth",dateOfBirth.getText().toString());
                }
                else {
                    params.put("date_of_birth",null+"");
                }
                if(gender_val!=null)
                {
                    params.put("gender",gender_val);
                }
                else {
                    params.put("gender",null+"");
                }

                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);




    }

    private void updateLabe(Calendar myCalendar) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
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

    private void BindDataTOviews() {
        if (!userClass.getFirst_name().equals("null")) {
            fNametext.setText(userClass.getFirst_name());
        }
        if (!userClass.getLast_name().equals("null")) {
            lNametext.setText(userClass.getLast_name());
        }
//        if (!userData.getEmails().equals("null")) {
//            email.setText(userData.getEmails());
//        }
        if (!userData.getShot_bio().equals("null")) {
            shortBioTecxt.setText(userData.getShot_bio());
        }
        if (!userClass.getEmail().equals("null")) {
            email.setText(userClass.getEmail());
        }
        if (!userData.getDate_of_birth().equals("null")) {
            dateOfBirthText.setText(userData.getDate_of_birth());
        }
        if (!userData.getFacebook_link().equals("null")) {
            facebookLinktext.setText(userData.getFacebook_link());
        }
        if (!userData.getLinkedin_link().equals("null")) {
            LinkedinLinktext.setText(userData.getLinkedin_link());
        }
        if (!userData.getTwitter_link().equals("null")) {
            TwitterLinktext.setText(userData.getTwitter_link());
        }
        if (!userData.getInstagram_link().equals("null")) {
            InstagramLinktext.setText(userData.getInstagram_link());
        }

    }
}