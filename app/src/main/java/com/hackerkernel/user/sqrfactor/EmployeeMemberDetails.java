package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeMemberDetails extends AppCompatActivity {
    Toolbar toolbar;
    private EditText firstName,lastName,role,phoneNumber,aadhaarId,email;
    private TextView employeeAttachment;
    private Button employeeAddbtn;
    private int countryId=1;
    private ImageView attachedImage,attachment;
    private Spinner employee_CountrySpinner,employee_StateSpinner,employee_CitySpinner;
    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private ArrayList<StateClass> statesClassArrayList=new ArrayList<>();
    private ArrayList<String> statesName=new ArrayList<>();
    private ArrayList<CitiesClass> citiesClassArrayList=new ArrayList<>();
    private ArrayList<String> citiesName=new ArrayList<>();
    private String country_val=null,state_val=null,city_val=null,gender_val=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_member_details);

        toolbar = (Toolbar) findViewById(R.id.employee_details_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        employee_CountrySpinner=(Spinner)findViewById(R.id.employee_Country);
        employee_StateSpinner=(Spinner)findViewById(R.id.employee_State);
        employee_CitySpinner=(Spinner)findViewById(R.id.employee_City);
        employeeAttachment=(TextView)findViewById(R.id.employee_attachment);
        attachedImage=(ImageView)findViewById(R.id.employee_attachment_image);
        attachment = (ImageView)findViewById(R.id.employee_attachment_icon);
        //attachmentImage = (ImageView)findViewById(R.id.employee_attachment_image);
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        firstName =(EditText)findViewById(R.id.employee_firstName_text);
        lastName =(EditText)findViewById(R.id.employee_firstLast_text);
        role =(EditText)findViewById(R.id.employee_role_text);
        phoneNumber =(EditText)findViewById(R.id.employee_number_text);
        aadhaarId =(EditText)findViewById(R.id.employee_aadhaar_text);
        email =(EditText)findViewById(R.id.employee_email_text);
        employeeAddbtn =(Button)findViewById(R.id.employee_add_button);

        employeeAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToServer();
            }
        });

        BindDataTOViews();
        LoadCountryFromServer();
        LoadStateFromServer(1);
        LoadCitiesFromServer(1,1);



        employee_CountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if(countryName.size()>0)
                {
                    //country_val = countryName.get(position);
                    country_val=position+"";
                    //Toast.makeText(getApplicationContext(),countryClassArrayList.get(position).getId()+" ",Toast.LENGTH_LONG).show();
                    LoadStateFromServer(countryClassArrayList.get(position).getId());
                    countryId=countryClassArrayList.get(position).getId();
                }


            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        employee_StateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if(statesName.size()>0)
                {
                    state_val=position+"";
                    // state_val = statesName.get(position);
                    //Toast.makeText(getApplicationContext(),statesClassArrayList.get(position).getId()+" "+countryId,Toast.LENGTH_LONG).show();
                    LoadCitiesFromServer(statesClassArrayList.get(position).getId(),countryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        employee_CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int  position, long id) {
                if(citiesName.size()>0)
                {
                    //city_val = citiesName.get(position);
                    city_val=position+"";
                    //Toast.makeText(getApplicationContext(),city_val,Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

    }

    public void SendDataToServer()
    {

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/add/member-detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(EmployeeMemberDetails.this,"response"+s,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            firstName.setText("");
                            lastName.setText("");
                            role.setText("");
                            phoneNumber.setText("");
                            aadhaarId.setText("");
                            email.setText("");


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

                if(!TextUtils.isEmpty(firstName.getText().toString()))
                {
                    params.put("first_name",firstName.getText().toString());
                }
                else {
                    params.put("first_name",null+"");
                }
                if(!TextUtils.isEmpty(lastName.getText().toString()))
                {
                    params.put("last_name",lastName.getText().toString());
                }
                else {
                    params.put("last_name",null+"");
                }
                if(!TextUtils.isEmpty(role.getText().toString()))
                {
                    params.put("role",role.getText().toString());
                }
                else {
                    params.put("role",null+"");
                }
                if(!TextUtils.isEmpty(phoneNumber.getText().toString()))
                {
                    params.put("phone_number",phoneNumber.getText().toString());
                }
                else {
                    params.put("phone_number",null+"");
                }
                if(!TextUtils.isEmpty(aadhaarId.getText().toString()))
                {
                    params.put("aadhar_id",aadhaarId.getText().toString());
                }
                else {
                    params.put("aadhar_id",null+"");
                }
                if(!TextUtils.isEmpty(email.getText().toString()))
                {
                    params.put("email",email.getText().toString());
                }
                else {
                    params.put("email",null+"");
                }



                params.put("country",country_val);
                params.put("profile",null+"");
                params.put("state",state_val);
                params.put("city",city_val);
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);


    }

    private void BindDataTOViews()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserData","");
        UserData userData = gson.fromJson(json,UserData.class);
//
//            if(!userData.getCountry_id().equals("null"))
//                employee_CountrySpinner.setSelection(Integer.parseInt(userData.getCountry_id()));
//
//            if(!userData.getState_id().equals("null"))
//                employee_StateSpinner.setSelection(Integer.parseInt(userData.getState_id()));
//
//            if(!userData.getCity_id().equals("null"))
//                employee_CitySpinner.setSelection(Integer.parseInt(userData.getCity_id()));




    }
    public void LoadCountryFromServer()
    {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://archsqr.in/api/event/country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        // Toast.makeText(BasicDetails.this,"res"+s,Toast.LENGTH_LONG).show();
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



                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(EmployeeMemberDetails.this, android.R.layout.simple_list_item_1,countryName);
                            employee_CountrySpinner.setAdapter(spin_adapter1);

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
                        //Toast.makeText(.this,"res"+s,Toast.LENGTH_LONG).show();

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
                            ArrayAdapter<String> spin_adapter2 = new ArrayAdapter<String>(EmployeeMemberDetails.this, android.R.layout.simple_list_item_1,statesName);
                            employee_StateSpinner.setAdapter(spin_adapter2);
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
                            ArrayAdapter<String> spin_adapter3 = new ArrayAdapter<String>(EmployeeMemberDetails.this, android.R.layout.simple_list_item_1,citiesName);
                            employee_CitySpinner.setAdapter(spin_adapter3);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }



    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);



                    attachedImage.setImageBitmap(bitmap);



                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    employeeAttachment.setText(String.valueOf(System.currentTimeMillis()) + ".jpg");


                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        outFile.flush();

                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {



                Uri selectedImage = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                String[] fileName = picturePath.split("/");
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                // Log.w("path of image from gallery......******************.........", fileName[fileName.length-1]+"");

                attachedImage.setImageBitmap(thumbnail);
                employeeAttachment.setText(fileName[fileName.length-1]+"");

            }

        }

    }
}