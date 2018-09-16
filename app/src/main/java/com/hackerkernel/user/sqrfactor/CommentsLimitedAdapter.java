package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class CommentsLimitedAdapter extends RecyclerView.Adapter<CommentsLimitedAdapter.MyViewHolder> {

    private ArrayList<comments_list> comments_limitedArrayList=new ArrayList<>();
    private Context context;
    private int flag=0,user_id,commentable_id,postId;
    private String isShared;

//    public CommentsLimitedAdapter(ArrayList<comments_limited> comments_limitedArrayList, Context context,int user_id,int commentable_id,int postId,String isShared) {
//
//        this.comments_limitedArrayList = comments_limitedArrayList;
//        this.context = context;
//        this.isShared=isShared;
//        this.commentable_id=commentable_id;
//        this.postId=postId;
//        this.user_id=user_id;
//    }

    public CommentsLimitedAdapter(ArrayList<comments_list> comments_limitedArrayList, Context context) {

        this.comments_limitedArrayList = comments_limitedArrayList;
        this.context = context;
//        this.isShared=isShared;
//        this.commentable_id=commentable_id;
//        this.postId=postId;
//        this.user_id=user_id;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_limited_adapter,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final comments_list commentsList=comments_limitedArrayList.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        if(userClass.getUserId()==user_id)
        {
            holder.comment_menu.setVisibility(View.VISIBLE);
        }


        holder.commentBody.setText(comments_limitedArrayList.get(position).getBody());
//        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                if (flag == 0) {
//                    holder.buttonLike.setTextColor(context.getColor(R.color.sqr));
//                    int result = comments_limitedArrayList.get(position).getCommentLikeCount()+1;
//                    holder.numberOfLikes.setText(result+" Like");
//                    flag = 1;
//                }
//                else {
//                    holder.buttonLike.setTextColor(context.getColor(R.color.gray));
//                    int result = comments_limitedArrayList.get(position).getCommentLikeCount();
//                    holder.numberOfLikes.setText(result+" Like");
//                    flag = 0;
//                }
//
//            }
//        });
        //holder.commenterUser.setText(comments_limitedArrayList.get(position).getUserClass().getUser_name());

        Glide.with(context).
                load("https://archsqr.in/"+commentsList.getFrom_user_profile())
                .centerCrop().into(holder.commenterProfile);

        holder.commenterUserName.setText(commentsList.getFrom_user_user_name());
        holder.commenterUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                //Log.v("Data",newsFeedStatus.getUserId()+" "+userName+" "+newsFeedStatus.getPostId());
                intent.putExtra("User_id",commentsList.getFrom_user_id());
                intent.putExtra("ProfileUserName",commentsList.getFrom_user_user_name());
                context.startActivity(intent);
            }
        });
       holder.numberOfLikes.setText(commentsList.getCommentLikeCount()+"");
        String dtc = commentsList.getComment_date();
        Log.v("dtc",dtc);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM",Locale.ENGLISH);
        Log.v("sdf1",sdf1.toString());
        Log.v("sdf2",sdf2.toLocalizedPattern());
        Date date = null;
        try{
            date = sdf1.parse(dtc);
            String newDate = sdf2.format(date);
            Log.v("date",date+"");
            System.out.println(newDate);
            Log.e("Date",newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff/(24*60*60*1000);
        holder.timeAgo.setText(days+" Days ago");

    }
    public void DeletePost(final int id, int comment_id)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/delete_post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(context, s , Toast.LENGTH_LONG).show();
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
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params.put("id",id);
//                params.put("is_shared",is_shared+"");
//
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {

        return comments_limitedArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView commentBody,commenterUserName,timeAgo,numberOfLikes;
        ImageView commenterProfile,comment_menu;
        Button buttonLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentBody=(TextView)itemView.findViewById(R.id.commentBody);
            commenterUserName=(TextView)itemView.findViewById(R.id.commenterUserName);
            timeAgo=(TextView)itemView.findViewById(R.id.timeAgo);
            comment_menu=(ImageView)itemView.findViewById(R.id.comment_menu);
            commenterProfile=(ImageView)itemView.findViewById(R.id.commenterProfileImage);
            buttonLike=(Button)itemView.findViewById(R.id.commentLike);
            numberOfLikes=(TextView)itemView.findViewById(R.id.numberOfLike);
            comment_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu pop = new PopupMenu(context, v);
                    pop.getMenuInflater().inflate(R.menu.comment_delete, pop.getMenu());
                    pop.show();

                    pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()){

                                case R.id.deleteComment:
                                    comments_limitedArrayList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    // DeletePost(newsFeedStatus.getPostId()+"",newsFeedStatus.getSharedId()+"",newsFeedStatus.getIs_Shared());
                                    return true;

                            }
                            return true;
                        }
                    });
                }
            });
        }
    }
}