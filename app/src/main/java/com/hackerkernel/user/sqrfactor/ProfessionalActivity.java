package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfessionalActivity extends AppCompatActivity {

    private Button add,remove,add1,remove1,add2,remove2,save;
    private boolean stDate=false;
    private boolean edDate=false;
    private LinearLayout newForm,newForm1;
    private Boolean isClicked=false;
    private Boolean isClicked1=false;
    private UserData userData;
    private ArrayList<ProfessionalDetailsClass> professionalDetailsClassArrayList=new ArrayList<>();
    private EditText startDateText,endDateText,COARgistration,YearsSinceServiceText,RoleText,CompanyText,SalaryText,RoleText1,CompanyText1,
            StartDateText1,EndDateText1,SalaryText1,RoleText2,CompanyText2,
            StartDateText2,EndDateText2,SalaryText2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);


        toolbar = (Toolbar) findViewById(R.id.professional_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        startDateText= (EditText)findViewById(R.id.StartDateText);
        StartDateText1= (EditText)findViewById(R.id.StartDateText1);
        StartDateText2= (EditText)findViewById(R.id.StartDateText2);
        endDateText=(EditText)findViewById(R.id.EndDateText);
        EndDateText1=(EditText)findViewById(R.id.EndDateText1);
        EndDateText2=(EditText)findViewById(R.id.EndDateText2);

        SalaryText=(EditText)findViewById(R.id.SalaryText);
        SalaryText1=(EditText)findViewById(R.id.SalaryText1);
        SalaryText2=(EditText)findViewById(R.id.SalaryText2);
        CompanyText=(EditText)findViewById(R.id.CompanyText);
        CompanyText1=(EditText)findViewById(R.id.CompanyText1);
        CompanyText2=(EditText)findViewById(R.id.CompanyText2);
        RoleText=(EditText)findViewById(R.id.RoleText);
        RoleText1=(EditText)findViewById(R.id.RoleText1);
        RoleText2=(EditText)findViewById(R.id.RoleText2);



        COARgistration=(EditText)findViewById(R.id.COARgistration);
        YearsSinceServiceText=(EditText)findViewById(R.id.YearsSinceServiceText);




        add=(Button)findViewById(R.id.Add);
        remove=(Button)findViewById(R.id.Remove);
        add1 =(Button)findViewById(R.id.Add1);
        remove2=(Button)findViewById(R.id.Remove2);
        newForm=(LinearLayout) findViewById(R.id.linear);
        newForm1=(LinearLayout) findViewById(R.id.linear2);
        save=(Button)findViewById(R.id.save_professinal);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToServer();
            }
        });




        newForm.setVisibility(View.GONE);
        newForm1.setVisibility(View.GONE);
        //add.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked)
                {
                    newForm.setVisibility(View.VISIBLE);
                    isClicked=true;
                }

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newForm!=null && isClicked)
                {
                    newForm.setVisibility(View.GONE);
                    isClicked=false;
                }
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked1)
                {
                    newForm1.setVisibility(View.VISIBLE);
                    isClicked1=true;
                }

            }
        });

        remove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newForm1!=null && isClicked1)
                {
                    newForm1.setVisibility(View.GONE);
                    isClicked1=false;
                }
            }
        });

        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        startDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stDate=true;
                new DatePickerDialog(ProfessionalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edDate=true;
                new DatePickerDialog(ProfessionalActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        GetDataFromServerAndBindTOView();

    }

    public void  GetDataFromServerAndBindTOView()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/profile/edit/professional-details",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray professionalDetail=jsonObject.getJSONArray("allProfessionalDetail");
                            //Toast.makeText(getApplicationContext(),professionalDetail.length(),Toast.LENGTH_LONG).show();
                            for(int i=0;i<professionalDetail.length();i++)
                            {
                                ProfessionalDetailsClass professionalDetailsClass=new ProfessionalDetailsClass(professionalDetail.getJSONObject(i));
                                // Toast.makeText(getApplicationContext(),professionalDetailsClass.getRole(),Toast.LENGTH_LONG).show();
                                professionalDetailsClassArrayList.add(professionalDetailsClass);

                            }

                            BindDataToView(professionalDetailsClassArrayList);

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

    public void BindDataToView(ArrayList<ProfessionalDetailsClass> professionalDetailsArrayList)
    {

        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("UserData", "");
        userData= gson1.fromJson(json1, UserData.class);
        for(int i=0;i<professionalDetailsArrayList.size();i++)
        {
            if(i==0)
            {
                COARgistration.setText(userData.getCoa_registration());
                YearsSinceServiceText.setText(userData.getYear_in_service());
                RoleText.setText(professionalDetailsArrayList.get(i).getRole());
                CompanyText.setText(professionalDetailsArrayList.get(i).getCompany_firm_or_college_university());
                startDateText.setText(professionalDetailsArrayList.get(i).getStart_date());
                endDateText.setText(professionalDetailsArrayList.get(i).getEnd_date_of_working_currently());
                SalaryText.setText(professionalDetailsArrayList.get(i).getSalary_stripend());
            }
            else if(i==1 && !isClicked)
            {

                newForm.setVisibility(View.VISIBLE);
                isClicked=true;
                RoleText1.setText(professionalDetailsArrayList.get(i).getRole());
                CompanyText1.setText(professionalDetailsArrayList.get(i).getCompany_firm_or_college_university());
                StartDateText1.setText(professionalDetailsArrayList.get(i).getStart_date());
                EndDateText1.setText(professionalDetailsArrayList.get(i).getEnd_date_of_working_currently());
                SalaryText1.setText(professionalDetailsArrayList.get(i).getSalary_stripend());
            }
            else  if(i==2 && !isClicked1)
            {
                newForm1.setVisibility(View.VISIBLE);
                isClicked1=true;
                RoleText2.setText(professionalDetailsArrayList.get(i).getRole());
                CompanyText2.setText(professionalDetailsArrayList.get(i).getCompany_firm_or_college_university());
                StartDateText2.setText(professionalDetailsArrayList.get(i).getStart_date());
                EndDateText2.setText(professionalDetailsArrayList.get(i).getEnd_date_of_working_currently());
                SalaryText2.setText(professionalDetailsArrayList.get(i).getSalary_stripend());
            }
        }
    }


    public void saveDataToServer()
    {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/parse/work-professional-details",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);

                        Toast.makeText(ProfessionalActivity.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            YearsSinceServiceText.setText("");
                            COARgistration.setText("");
                            startDateText.setText("");
                            StartDateText1.setText("");
                            StartDateText2.setText("");
                            endDateText.setText("");
                            EndDateText1.setText("");
                            EndDateText2.setText("");
                            CompanyText.setText("");
                            CompanyText1.setText("");
                            CompanyText2.setText("");
                            SalaryText.setText("");
                            SalaryText1.setText("");
                            SalaryText2.setText("");





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

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

                if(!TextUtils.isEmpty(COARgistration.getText().toString()))
                {
                    params.put("coa_registration[0]",COARgistration.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(YearsSinceServiceText.getText().toString()))
                {
                    params.put("years_since_service[0]",YearsSinceServiceText.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
//                if(!TextUtils.isEmpty(RoleText.getText().toString()))
//                {
//                    params.put("years_sinc",RoleText.getText().toString());
//                    //courseArray.add(courseText.getText().toString());
//                }
                if(!TextUtils.isEmpty(CompanyText.getText().toString()))
                {
                    params.put("company_firm_or_college_university[0]",CompanyText.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(CompanyText1.getText().toString()))
                {
                    params.put("company_firm_or_college_university[1]",CompanyText1.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(CompanyText2.getText().toString()))
                {
                    params.put("company_firm_or_college_university[2]",CompanyText2.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }

                if(!TextUtils.isEmpty(startDateText.getText().toString()))
                {
                    params.put("start_date[0]",startDateText.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(StartDateText1.getText().toString()))
                {
                    params.put("start_date[1]",StartDateText1.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(StartDateText2.getText().toString()))
                {
                    params.put("start_date[2]",StartDateText2.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                //params.put("",YearsSinceServiceText.getText().toString());
                //params.put("",RoleText.getText().toString());
                //params.put("",CompanyText.getText().toString());
                // params.put("",startDateText.getText().toString());

                if(!TextUtils.isEmpty(endDateText.getText().toString()))
                {
                    params.put("end_date_of_working_currently[0]",endDateText.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(EndDateText1.getText().toString()))
                {
                    params.put("end_date_of_working_currently[1]",EndDateText1.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(EndDateText2.getText().toString()))
                {
                    params.put("end_date_of_working_currently[2]",EndDateText2.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(SalaryText.getText().toString()))
                {
                    params.put("salary_stripend[0]",SalaryText.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(SalaryText1.getText().toString()))
                {
                    params.put("salary_stripend[1]",SalaryText1.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
                if(!TextUtils.isEmpty(SalaryText2.getText().toString()))
                {
                    params.put("salary_stripend[2]",SalaryText2.getText().toString());
                    //courseArray.add(courseText.getText().toString());
                }
//                params.put("",endDateText.getText().toString());
//                params.put("",SalaryText.getText().toString());
                //post remainig data

                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(stDate==true)
            startDateText.setText(sdf.format(myCalendar.getTime()));
        if(edDate==true)
            endDateText.setText(sdf.format(myCalendar.getTime()));
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