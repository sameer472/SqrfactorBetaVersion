package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikeListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private LikeListAdapter likeListAdapter;
    private ArrayList<LikeClass> likeClassArrayList = new ArrayList<>();
    private static final String TAG = LikeListActivity.class.getSimpleName();
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);

        recyclerView = findViewById(R.id.likeList_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);



        likeListAdapter = new LikeListAdapter(likeClassArrayList,this);
        recyclerView.setAdapter(likeListAdapter);

        Intent intent = getIntent();
        postId = intent.getIntExtra("id",0);
        Toast.makeText(this, postId+"", Toast.LENGTH_SHORT).show();
        likelist();
        final DragToClose dragToClose = findViewById(R.id.drag_to_close);
        dragToClose.setDragListener(new DragListener() {
            @Override
            public void onStartDraggingView() {
                Log.d(TAG, "onStartDraggingView()");
            }

            @Override
            public void onViewCosed() {
                Log.d(TAG, "onViewCosed()");
            }
        });

    }

    public void likelist(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/fetchlikelist/"+postId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MorenewsFeedFromServer", response);
//                        Toast.makeText(FollowersActivity.this, response, Toast.LENGTH_LONG).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray userslist=jsonObject.getJSONArray("userslist");
                            for(int i=0;i<userslist.length();i++)
                            {
                                LikeClass likeClass=new LikeClass(userslist.getJSONObject(i));
                                likeClassArrayList.add(likeClass);
                            }

                            likeListAdapter.notifyDataSetChanged();



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
}