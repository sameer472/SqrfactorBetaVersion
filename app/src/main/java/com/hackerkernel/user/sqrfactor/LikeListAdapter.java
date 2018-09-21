package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;



public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.MyViewAdapter> {
    private ArrayList<LikeClass> likeClassArrayList;
    private Context context;
    boolean flag = false;


    public LikeListAdapter(ArrayList<LikeClass> likeClassArrayList, Context context) {
        this.likeClassArrayList = likeClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public LikeListAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_list_adapter, parent, false);
        return new MyViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter holder, int position) {
        LikeClass likeClass = likeClassArrayList.get(position);
        SharedPreferences mPrefs = context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        if(likeClass.getId()==userClass.getUserId())
            holder.followbtn.setVisibility(View.GONE);

       // FollowMethod(likeClass.getId(),holder.followbtn);


        if(likeClass.getName().equals("null"))
        {
            holder.profileName.setText(likeClass.getFirst_name()+" "+likeClass.getLast_name());
        }

        else {
            holder.profileName.setText(likeClass.getName());
        }
        Glide.with(context).load("https://archsqr.in/"+likeClass.getProfile_url())
                .into(holder.profileImage);
        holder.followbtn.setText(likeClass.getIsFollowing());
        holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/follow_user",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                Log.v("ResponseLike", s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    UserFollowClass userFollowClass = new UserFollowClass(jsonObject);
                                    flag = userFollowClass.isReturnType();
                                    if (flag == false) {
                                        Log.v("follow", flag + "");
                                        Toast.makeText(context, "Follow", Toast.LENGTH_LONG).show();
                                        holder.followbtn.setText("Follow");
                                        flag = true;
                                    } else {
                                        Log.v("following", flag + "");
                                        Toast.makeText(context, "Following", Toast.LENGTH_LONG).show();
                                       holder.followbtn.setText("Following");
                                        flag = false;
                                    }
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
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("to_user",userClass.getUserId() + "");
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue1.add(stringRequest);

            }
        });
    }

    @Override
    public int getItemCount() {
        return likeClassArrayList.size();
    }



    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView profileName;
        Button followbtn;
        ImageView profileImage;


        public MyViewAdapter(View itemView) {
            super(itemView);


            profileName=(TextView)itemView.findViewById(R.id.like_auth_name);
            profileImage=(ImageView)itemView.findViewById(R.id.like_profileImage);
            followbtn = (Button)itemView.findViewById(R.id.like_list_followbtn);



        }

    }

    public void FollowMethod(final int user_id, final Button followbtn ) {


        RequestQueue requestQueue1 = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/follow_user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                        Log.v("ResponseLike", s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            UserFollowClass userFollowClass = new UserFollowClass(jsonObject);
                            if (!userFollowClass.isReturnType()) {
                                followbtn.setText("Follow");
                                userFollowClass.setReturnType(true);
                            } else {

                                followbtn.setText("Following");
                                userFollowClass.setReturnType(false);
                            }

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("to_user", user_id + "");
                return params;
            }
        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }


}