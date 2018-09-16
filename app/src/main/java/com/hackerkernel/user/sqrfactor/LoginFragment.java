
package com.hackerkernel.user.sqrfactor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.hackerkernel.user.sqrfactor.HomeScreen;
import com.hackerkernel.user.sqrfactor.UserClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String username, password;
    private Button login;
    private TextView forgot;
    private EditText loginEmail, loginPassword;
    private CheckBox loginRemberMe;
    private SharedPreferences.Editor editor;
    private SharedPreferences mPrefs;
    private   FirebaseDatabase database;
    private   DatabaseReference ref;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("PREF_NAME", getActivity().MODE_PRIVATE);
        editor = sharedPref.edit();

        mPrefs = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        database= FirebaseDatabase.getInstance();

        ref = database.getReference();

        login = (Button) rootView.findViewById(R.id.login);
        forgot = (TextView) rootView.findViewById(R.id.forgot);
        loginEmail = (EditText) rootView.findViewById(R.id.loginEmail);
        loginPassword = (EditText) rootView.findViewById(R.id.loginPassword);
        loginRemberMe = (CheckBox) rootView.findViewById(R.id.rememberMeLoginCheckBox);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            loginEmail.setText(loginPreferences.getString("username", ""));
            loginPassword.setText(loginPreferences.getString("password", ""));
            loginRemberMe.setChecked(true);
        }


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Token" + loginEmail.getText() + loginPassword.getText(), Toast.LENGTH_SHORT).show();
                // if(!TextUtils.isEmpty(loginEmail.getText())&&!TextUtils.isEmpty(loginPassword.getText())) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(loginEmail.getWindowToken(), 0);

                username = loginEmail.getText().toString();
                password = loginPassword.getText().toString();

                if (loginRemberMe.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                    doSomethingElse();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                    doSomethingElse();

                }




            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext().getApplicationContext(), ResetPassword.class);
                startActivity(i);

            }
        });

        return rootView;

    }

    public void doSomethingElse() {


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Reponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            UserClass userClass = new UserClass(jsonObject);
                            // notification listner for like and comment
                            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                            FirebaseMessaging.getInstance().subscribeToTopic("chats"+userClass.getUserId());
                            //code for user status
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();

                            IsOnline isOnline=new IsOnline("True",formatter.format(date));
//
                            ref.child("Status").child(userClass.getUserId()+"").child("android").setValue(isOnline);
                            IsOnline isOnline1=new IsOnline("False",formatter.format(date));
//
                            ref.child("Status").child(userClass.getUserId()+"").child("web").setValue(isOnline1);


//                            DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference().child("Status").child(userClass.getUserId()+"").child("android");
//
//                            IsOnline isOnline=new IsOnline("False",ServerValue.TIMESTAMP.toString());
//                            presenceRef.onDisconnect().setValue(isOnline);
                            JSONObject TokenObject = jsonObject.getJSONObject("success");
                            String Token = TokenObject.getString("token");

                            editor.putString("TOKEN", Token);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(userClass);
                            prefsEditor.putString("MyObject", json);
                            prefsEditor.commit();
                            editor.commit();
                            Intent i = new Intent(getActivity(), HomeScreen.class);
                            getActivity().startActivity(i);
                            getActivity().finish();

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", loginEmail.getText().toString());
                params.put("password", loginPassword.getText().toString());
                return params;
            }
        };
        requestQueue.add(myReq);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.v("OnStart","OnstartCllaed");
//        SharedPreferences mPrefs =getActivity().getSharedPreferences("User",MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPrefs.getString("MyObject", "");
//        UserClass userClass = gson.fromJson(json, UserClass.class);
//        HashMap<String,String> status=new HashMap<>();
//        status.put("isOnline","True");
//        status.put("time", ServerValue.TIMESTAMP.toString());
//        ref.child("Status").child(userClass.getUserId()+"").setValue(status);
//    }



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        SharedPreferences mPrefs =getActivity().getSharedPreferences("User",MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPrefs.getString("MyObject", "");
//        UserClass userClass = gson.fromJson(json, UserClass.class);
//        HashMap<String,String> status=new HashMap<>();
//        status.put("isOnline","False");
//        status.put("time", ServerValue.TIMESTAMP.toString());
//        ref.child("Status").child(userClass.getUserId()+"").setValue(status);
//    }

}