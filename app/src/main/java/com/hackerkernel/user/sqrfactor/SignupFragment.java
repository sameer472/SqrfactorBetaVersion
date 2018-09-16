package com.hackerkernel.user.sqrfactor;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends Fragment {

    private Spinner userTypeSpinner,userCountrySpinner;
    private EditText firstName,lastName,userName,userEmail,userPassword,confirmPassword,userMobileNumber;
    private Button completeRegistration;
    private String countryName,userType;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_signup, container, false);


        userTypeSpinner= (Spinner) rootView.findViewById(R.id.userType);
        //Spinner spinner = (Spinner)rootView.findViewById(R.id.userType);
       // String text = spinner.getSelectedItem().toString();
        userType=userTypeSpinner.getSelectedItem().toString();
        Log.v("userType1",userType);
        if(userType.equals("Individual"))
        {
            userType = "work_individual";
            Log.v("userType2",userType);
         }
        else if(userType.equals("Firm/Companies (Design Service Providers)"))
        {
            userType = "work_architecture_firm_companies";
            Log.v("userType3",userType);
        }
        else if(userType.equals("Organisations, Companies, NGOs, Media"))
        {
            userType = "work_architecture_firm_organization";
            Log.v("userType4",userType);
        }
        else if(userType.equals("College/University"))
        {
            userType = "work_architecture_firm_college";
            Log.v("userType5",userType);
        }

        userCountrySpinner=(Spinner) rootView.findViewById(R.id.registerUserCountry);
        countryName=userCountrySpinner.getSelectedItem().toString();

        firstName=(EditText) rootView.findViewById(R.id.registerFristName);
        lastName=(EditText) rootView.findViewById(R.id.registerLastName);
        userName=(EditText) rootView.findViewById(R.id.registerUserName);
        userEmail=(EditText) rootView.findViewById(R.id.registerEmail);
        userPassword=(EditText) rootView.findViewById(R.id.registerUserPassword);
        userMobileNumber=(EditText) rootView.findViewById(R.id.registerUserMobileNumber);
        completeRegistration=(Button)rootView.findViewById(R.id.registerCompleteButton);
        confirmPassword=(EditText)rootView.findViewById(R.id.registerUserConfirmPassword);

        completeRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Log.v("data1","1234567");
//                Log.v("data",firstName.getText().toString()+lastName.getText().toString()+"1234567");

//                if(!TextUtils.isEmpty(firstName.getText())&&!TextUtils.isEmpty(lastName.getText())&&!TextUtils.isEmpty(userName.getText())&&
//                        !TextUtils.isEmpty(userEmail.getText())&&!TextUtils.isEmpty(userPassword.getText())&&isValidEmail(userEmail.getText().toString())
//                        &&!TextUtils.isEmpty(userMobileNumber.getText())&&!TextUtils.isEmpty(confirmPassword.getText()))
//                {

                    RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/register",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("Reponse", response);
                                    Toast.makeText(getActivity(),"Reponse"+response,Toast.LENGTH_LONG).show();
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
//                                         JSONObject TokenObject= (JSONObject) jsonObject.get("success");
//                                         String Token=(String)TokenObject.get("token");
//                                         Intent i = new Intent(getContext(), HomeScreen.class);
//                                         startActivity(i);
                                        Toast.makeText(getActivity(),"Check your mail for activation link",Toast.LENGTH_LONG).show();

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
                            return params;
                        }
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("user_type",userType);
                            params.put("first_name",firstName.getText().toString());
                            params.put("last_name",lastName.getText().toString());
                            params.put("user_name",userName.getText().toString());
                            params.put("email",userEmail.getText().toString());
                            params.put("country_code","91");
                            params.put("mobile_number",userMobileNumber.getText().toString());
                            params.put("password",userPassword.getText().toString());
                            params.put("password_confirmation",confirmPassword.getText().toString());
                            return params;
                        }


                    };

                    requestQueue.add(myReq);

                }


        });


/*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.user_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        return rootView;

    }


    private boolean isValidEmail(String email)
    {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

}


