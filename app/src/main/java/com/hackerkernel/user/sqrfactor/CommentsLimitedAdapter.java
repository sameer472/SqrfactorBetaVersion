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
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class CommentsLimitedAdapter extends RecyclerView.Adapter<CommentsLimitedAdapter.MyViewHolder> {

    private ArrayList<comments_list> comments_limitedArrayList=new ArrayList<>();
    private Context context;
    private int flag=0,user_id,commentable_id,postId;
    private String isShared;


    public CommentsLimitedAdapter(ArrayList<comments_list> comments_limitedArrayList, Context context) {

        this.comments_limitedArrayList = comments_limitedArrayList;
        this.context = context;

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


        Glide.with(context).
                load("https://archsqr.in/"+commentsList.getFrom_user_profile())
                .into(holder.commenterProfile);

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

        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date past = format.parse(dtc);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days1=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<60)
            {
                holder.timeAgo.setText(seconds+" sec ago");

            }
            else if(minutes<60)
            {
                holder.timeAgo.setText(minutes+" min ago");
            }
            else if(hours<24)
            {
                holder.timeAgo.setText(hours+" hours ago");
            }
            else
            {
                holder.timeAgo.setText(days1+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }

//        holder.commentLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
//                   // int likeCount=Integer.parseInt(commentsList.getLike());
////                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
//                    holder..setTextColor(context.getColor(R.color.sqr));
//                    if(isAlreadyLikedFinal==1)
//                        holder.likelist.setText(likeCount+" Like");
//                    else
//                    {
//                        likeCount=likeCount+1;
//                        holder.likelist.setText(likeCount+" Like");
//                    }
//
//                    database= FirebaseDatabase.getInstance();
//                    ref = database.getReference();
//                    SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
//                    Gson gson = new Gson();
//                    String json = mPrefs.getString("MyObject", "");
//                    UserClass userClass = gson.fromJson(json, UserClass.class);
//
//                    Log.v("daattataatat",userClass.getUserId()+" "+userClass.getProfile()+" ");
//                    if(newsFeedStatus.getType().equals("status")&& userClass.getUserId()!=newsFeedStatus.getUserId())
//                    {
//                        PushNotificationClass pushNotificationClass;
//                        from_user fromUser;
//                        post post1=new post(newsFeedStatus.getFullDescription(),newsFeedStatus.getSlug(),"post Title",newsFeedStatus.getType(),newsFeedStatus.getPostId());
//                        if(userClass.getName()!="null")
//                        {
//                            fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
//                            pushNotificationClass=new PushNotificationClass(userClass.getName()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
//                        }
//                        else
//                        {
//                            fromUser=new from_user(userClass.getEmail(),userClass.getFirst_name()+" "+userClass.getLast_name(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
//                            pushNotificationClass=new PushNotificationClass(userClass.getFirst_name()+" "+userClass.getLast_name()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
//                        }
//
//                        String key =ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").push().getKey();
//                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);
//                        Map<String,String> unred=new HashMap<>();
//                        unred.put("unread",key);
//                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("unread").child(key).setValue(unred);
//                    }
//                    else if(userClass.getUserId()!=newsFeedStatus.getUserId())
//                    {
//                        from_user fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
//                        post post1=new post(newsFeedStatus.getShortDescription(),newsFeedStatus.getSlug(),newsFeedStatus.getPostTitle(),newsFeedStatus.getType(),newsFeedStatus.getPostId());
//                        PushNotificationClass pushNotificationClass;
//                        if(userClass.getName().equals("null"))
//                        {
//                            pushNotificationClass=new PushNotificationClass(userClass.getUser_name()+" liked your article ",new Date().getTime(),fromUser,post1,"like_post");
//                        }
//                        else
//                        {
//                            pushNotificationClass=new PushNotificationClass(userClass.getName()+" liked your article ",new Date().getTime(),fromUser,post1,"like_post");
//                        }
//
//                        //=new PushNotificationClass(userClass.getUser_name()+" liked your article",new Date().getTime(),fromUser,post1,"like_post");
//                        String key =ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").push().getKey();
//                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);
//                        Map<String,String> unred=new HashMap<>();
//                        unred.put("unread",key);
//                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("unread").child(key).setValue(unred);
//
//                    }
//                }
//                else {
//
//                    if(isAlreadyLikedFinal==1)
//                    {
//                        Log.v("isAlreadyLiked1",isAlreadyLikedFinal+" ");
//                        holder.likelist.setTextColor(context.getColor(R.color.gray));
//                        int likeCount1=Integer.parseInt(newsFeedStatus.getLike());
//                        Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
//                        likeCount1=likeCount1-1;
//                        holder.likelist.setText(likeCount1+" Like");
//                    }
//                    else
//                    {
//                        Log.v("isAlreadyLiked2",isAlreadyLikedFinal+" ");
//                        Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
//                        holder.likelist.setTextColor(context.getColor(R.color.gray));
//                        holder.likelist.setText(newsFeedStatus.getLike()+" Like");
//                    }
//
//
//                }
//                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String s) {
//                                Log.v("ResponseLike",s);
//
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//
//                            }
//                        }){
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Accept", "application/json");
//                        params.put("Authorization", "Bearer " +TokenClass.Token);
//
//                        return params;
//                    }
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String,String> params = new HashMap<>();
//
//                        params.put("likeable_id",newsFeedStatus.getSharedId()+"");
//                        params.put("likeable_type","users_post_share");
////
//                        return params;
//                    }
//                };
//
//                requestQueue.add(stringRequest);
//            }
//
//
//        });



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
        CheckBox commentLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentBody=(TextView)itemView.findViewById(R.id.commentBody);
            commenterUserName=(TextView)itemView.findViewById(R.id.commenterUserName);
            timeAgo=(TextView)itemView.findViewById(R.id.timeAgo);
            comment_menu=(ImageView)itemView.findViewById(R.id.comment_menu);
            commenterProfile=(ImageView)itemView.findViewById(R.id.commenterProfileImage);
            commentLike=(CheckBox) itemView.findViewById(R.id.commentLike);
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