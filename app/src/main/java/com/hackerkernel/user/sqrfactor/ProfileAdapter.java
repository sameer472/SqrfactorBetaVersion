package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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
import com.google.firebase.database.DatabaseReference;
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

import static android.content.Context.MODE_PRIVATE;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private ArrayList<ProfileClass1> profileClassArrayList;
    private Context context;
    private int flag = 0;
    private String userName;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private int flag1 = 0;


    public ProfileAdapter(ArrayList<ProfileClass1> profileClasses, Context context) {
        this.profileClassArrayList = profileClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ProfileClass1 profileClass = profileClassArrayList.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        int isAlreadyLiked=0;

        if(profileClass.getType().equals("status"))
        {
            Log.v("status1",profileClass.getType());
            //holder.postTitle.setText(profileClass.getPostTitle());
            holder.userName.setText(profileClass.getUser_name_of_post());
            userName=profileClass.getUser_name_of_post();
            holder.postShortDescription.setText(profileClass.getFullDescription());
            Glide.with(context).load("https://archsqr.in/"+profileClass.getUserImageUrl())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+profileClass.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        else if(profileClass.getType().equals("design"))
        {
            Log.v("status2",profileClass.getType());
            //holder.postTitle.setText(profileClass.getPostTitle());
            holder.userName.setText(profileClass.getUser_name_of_post());
            userName=profileClass.getUser_name_of_post();
            holder.postTitle.setText(profileClass.getPostTitle());
            holder.postShortDescription.setText(profileClass.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+profileClass.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+profileClass.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        else if(profileClass.getType().equals("article"))
        {
            Log.v("status2",profileClass.getType());
            holder.userName.setText(profileClass.getUser_name_of_post());
            userName=profileClass.getUser_name_of_post();
            holder.postTitle.setText(profileClass.getPostTitle());
            holder.postShortDescription.setText(profileClass.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+profileClass.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+profileClass.getAuthImageUrl())
                    .into(holder.userProfile);
        }

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                Log.v("Data",profileClass.getUserId()+" "+userName+" "+profileClass.getPostId());
                intent.putExtra("User_id",profileClass.getUserId());
                intent.putExtra("ProfileUserName",userName);
                context.startActivity(intent);

            }
        });
        holder.postBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FullPostActivity.class);
                intent.putExtra("Post_Slug_ID",profileClass.getSlug());
                context.startActivity(intent);
            }
        });
        Glide.with(context).load("https://archsqr.in/"+userClass.getProfile())
                .into( holder.usercommentProfile);

        for(int i=0;i<profileClass.getAllLikesId().size();i++)
        {
            if(userClass.getUserId()==profileClass.AllLikesId.get(i))
            {
                holder.buttonLikeList.setTextColor(context.getResources().getColor(R.color.sqr));
                isAlreadyLiked=1;
                holder.buttonLike.setChecked(true);
                ///flag=1;
            }
        }
        final int isAlreadyLikedFinal=isAlreadyLiked;

        for(int i=0;i<profileClass.AllCommentId.size();i++)
        {
            if(userClass.getUserId()==profileClass.AllCommentId.get(i))
            {
//                holder.c.setTextColor(context.getResources().getColor(R.color.sqr));
//                holder.commentCheckBox.setChecked(true);
                //holder.co.setChecked(true);
            }
        }


        String dtc = profileClass.getTime();
        // Log.v("dtc",dtc);
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

        holder.postTime.setText(days+ " Days ago");
        //holder.fullDescription.setText(profileClass.);
        holder.buttonLikeList.setText(profileClass.getLike()+" Like");
        holder.buttonLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,LikeListActivity.class);
                intent.putExtra("id",profileClass.getPostId());
                context.startActivity(intent);
            }

        });

        holder.buttonComment.setText(profileClass.getComments()+" Comment");
        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(flag1 == 0) {
                    holder.buttonComment.setTextColor(context.getColor(R.color.sqr));
                    flag1 = 1;

                }
                else {
                    holder.buttonComment.setTextColor(context.getColor(R.color.gray));
                    flag1 = 0;
                }


                Intent intent = new Intent(context, CommentsPage.class);
                intent.putExtra("PostSharedId",profileClass.getSharedId()); //second param is Serializable
                context.startActivity(intent);

            }
        });
        if(userClass.getUserId()==profileClass.getUserId())
        {
            holder.user_post_menu.setVisibility(View.VISIBLE);
        }

        holder.buttonLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    int likeCount=Integer.parseInt(profileClass.getLike());
//                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
                    holder.buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                    if(isAlreadyLikedFinal==1)
                        holder.buttonLikeList.setText(likeCount+" Like");
                    else
                    {
                        likeCount=likeCount+1;
                        holder.buttonLikeList.setText(likeCount+" Like");
                    }

                    database= FirebaseDatabase.getInstance();
                    ref = database.getReference();

                    Log.v("daattataatat",userClass.getUserId()+" "+userClass.getProfile()+" ");
                }
                else {

                    if(isAlreadyLikedFinal==1)
                    {
                        Log.v("isAlreadyLiked1",isAlreadyLikedFinal+" ");
                        holder.buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        int likeCount1=Integer.parseInt(profileClass.getLike());
                        Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                        likeCount1=likeCount1-1;
                        holder.buttonLikeList.setText(likeCount1+" Like");
                    }
                    else
                    {
                        Log.v("isAlreadyLiked2",isAlreadyLikedFinal+" ");
                        Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                        holder.buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        holder.buttonLikeList.setText(profileClass.getLike()+" Like");
                    }


                }
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);


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

                        params.put("likeable_id",profileClass.getSharedId()+"");
                        params.put("likeable_type","users_post_share");
//
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }


        });
        holder.user_post_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(context, v);
                pop.getMenuInflater().inflate(R.menu.delete_news_post_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.fullView:
                                Intent intent=new Intent(context,FullPostActivity.class);
                                intent.putExtra("Post_Slug_ID",profileClass.getSlug());
                                context.startActivity(intent);
                                break;
                            case R.id.editPost:
                                if(profileClass.getType().equals("design"))
                                {
                                    context.startActivity(new Intent(context,DesignActivity.class));
                                }
                                else if(profileClass.getType().equals("article"))
                                {
                                    context.startActivity(new Intent(context,ArticleActivity.class));
                                }
                                else if(profileClass.getType().equals("status"))
                                {
                                    //context.startActivity(new Intent(context,ArticleActivity.class));
                                }
                                break;
                            case R.id.deletePost:
                                profileClassArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, 1);
                                //DeletePost(newsFeedStatus.getPostId()+"",newsFeedStatus.getSharedId()+"",newsFeedStatus.getIs_Shared());
                                break;
                            case R.id.selectAsFeaturedPost:
                                return true;

                        }
                        return true;
                    }
                });
            }
        });


        holder.commentpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);

                                holder.user_comment_time.setText("0 minutes ago");
                                holder.user_post_likeListcomment.setText("0 Likes");
                                holder.user_comment.setText(holder.user_write_comment.getText().toString());
                                Glide.with(context).load("https://archsqr.in/"+userClass.getProfile())
                                        .into(holder.user_comment_image);
                                holder.user_comment_name.setText(userClass.getUser_name());
                                holder.commentCardView.setVisibility(View.VISIBLE);
                                holder.user_write_comment.setText("");
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

                        params.put("commentable_id",profileClass.getSharedId()+"");
//
                        params.put("comment_text",holder.user_write_comment.getText().toString());

                        //returning parameters
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, postTime, postShortDescription, postDescription,postTitle,buttonLikeList,commentpost,user_comment_name,user_comment_time,user_comment;
        EditText userComment,user_write_comment;
        ImageView usercommentProfile, userProfile, postBannerImage,user_comment_image,user_post_menu;
        Button  buttonComment,buttonShare,user_post_likeListcomment;
        CheckBox buttonLike;
        CardView commentCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            postTitle=(TextView)itemView.findViewById(R.id.user_post_title);
            postBannerImage = (ImageView) itemView.findViewById(R.id.user_post_image);
            userName = (TextView) itemView.findViewById(R.id.userprofle_name);
            postTime = (TextView) itemView.findViewById(R.id.user_post_time);
            postShortDescription = (TextView) itemView.findViewById(R.id.user_post_short_descriptions);
            usercommentProfile = (ImageView) itemView.findViewById(R.id.user_profileImage);
            userProfile = (ImageView) itemView.findViewById(R.id.userprofile_image);
            buttonLike = (CheckBox) itemView.findViewById(R.id.user_post_like);
            buttonLikeList = (TextView) itemView.findViewById(R.id.user_post_likeList);
            buttonComment = (Button) itemView.findViewById(R.id.user_post_comment);
            buttonShare = (Button) itemView.findViewById(R.id.user_post_share);

            commentCardView=(CardView)itemView.findViewById(R.id.userProfile_commentCardView);
            user_post_likeListcomment=(Button)itemView.findViewById(R.id.user_post_likeListcomment);
            user_write_comment=(EditText)itemView.findViewById(R.id.user_write_comment);
            user_comment_image=(ImageView)itemView.findViewById(R.id.user_comment_image);

            user_post_menu=(ImageView)itemView.findViewById(R.id.user_post_menu);
            user_comment_name=(TextView)itemView.findViewById(R.id.user_comment_name);


            user_comment_time=(TextView)itemView.findViewById(R.id.user_comment_time);
            user_comment=(TextView)itemView.findViewById(R.id.user_comment);
            userComment = (EditText) itemView.findViewById(R.id.user_write_comment);
            commentpost = (TextView) itemView.findViewById(R.id.user_post_button);

        }
    }
}
