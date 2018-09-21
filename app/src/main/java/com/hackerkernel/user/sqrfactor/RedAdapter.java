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

public class RedAdapter extends RecyclerView.Adapter<RedAdapter.MyViewHolder> {
    private Context context;
    int flag = 0;
    int flag1 = 0;
    private PopupWindow popupWindow;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<NewsFeedStatus> whatsRed;
    private int result=0;
    public RedAdapter(Context context,ArrayList<NewsFeedStatus> whatsRed) {

        this.context = context;
        this.whatsRed=whatsRed;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.red_adapter,parent,false);

        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        int isAlreadyLiked=0;
        final NewsFeedStatus newsFeedStatus=whatsRed.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        result = Integer.parseInt(newsFeedStatus.getComments());

        if(userClass.getUserId()==newsFeedStatus.getUserId())
        {
            holder.red_menu.setVisibility(View.VISIBLE);
        }
        if(newsFeedStatus.getType().equals("status"))
        {

            holder.authName.setText(newsFeedStatus.getUser_name_of_post());
            holder.postDescription.setText(newsFeedStatus.getFullDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getUserImageUrl())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.authProfile);
        }

        else if(newsFeedStatus.getType().equals("design"))
        {

            holder.authName.setText(newsFeedStatus.getUser_name_of_post());
            holder.postTitle.setText(newsFeedStatus.getPostTitle());
            holder.postDescription.setText(newsFeedStatus.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.authProfile);
        }

        else if(newsFeedStatus.getType().equals("article"))
        {

            holder.authName.setText(newsFeedStatus.getUser_name_of_post());
            holder.postTitle.setText(newsFeedStatus.getPostTitle());
            holder.postDescription.setText(newsFeedStatus.getShortDescription());
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getPostImage())
                    .into(holder.postBannerImage);
            Glide.with(context).load("https://archsqr.in/"+newsFeedStatus.getAuthImageUrl())
                    .into(holder.authProfile);

        }
        holder.authName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                intent.putExtra("User_id",newsFeedStatus.getUserId());
                intent.putExtra("ProfileUserName",newsFeedStatus.getUser_name_of_post());
                context.startActivity(intent);

            }
        });

        holder.postBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FullPostActivity.class);
                intent.putExtra("Post_Slug_ID",newsFeedStatus.getSlug());
                context.startActivity(intent);
            }
        });
        holder.red_menu.setOnClickListener(new View.OnClickListener() {
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
                                intent.putExtra("Post_Slug_ID",newsFeedStatus.getSlug());
                                context.startActivity(intent);
                                break;
                            case R.id.editPost:
                                if(newsFeedStatus.getType().equals("design"))
                                {
                                    context.startActivity(new Intent(context,DesignActivity.class));
                                }
                                else if(newsFeedStatus.getType().equals("article"))
                                {
                                    context.startActivity(new Intent(context,ArticleActivity.class));
                                }
                                else if(newsFeedStatus.getType().equals("status"))
                                {
                                    context.startActivity(new Intent(context,ArticleActivity.class));
                                }
                                break;
                            case R.id.deletePost:
                                whatsRed.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, 1);
                                // DeletePost(newsFeedStatus.getPostId()+"",newsFeedStatus.getSharedId()+"",newsFeedStatus.getIs_Shared());
                                break;
                            case R.id.selectAsFeaturedPost:
                                return true;

                        }
                        return true;
                    }
                });
            }
        });



        String dtc = newsFeedStatus.getTime();
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

        holder.postTime.setText(days+ " Days ago");
        holder.postViews.setText(newsFeedStatus.getWeek_views());
        holder.buttonLikeList.setText(newsFeedStatus.getLike()+" Like");
        holder.buttonComment.setText(newsFeedStatus.getComments()+" Comment");

        holder.buttonLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LikeListActivity.class);
                intent.putExtra("id",newsFeedStatus.getPostId());
                context.startActivity(intent);

            }

        });

        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CommentsPage.class);
                intent.putExtra("PostSharedId",newsFeedStatus.getSharedId()); //second param is Serializable
                context.startActivity(intent);
                }
        });
        for(int i=0;i<newsFeedStatus.AllLikesId.size();i++)
        {
            if(userClass.getUserId()==newsFeedStatus.AllLikesId.get(i))
            {
                holder.buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                isAlreadyLiked=1;
                holder.buttonLike.setChecked(true);
            }
        }
        final int isAlreadyLikedFinal=isAlreadyLiked;

        for(int i=0;i<newsFeedStatus.AllCommentId.size();i++)
        {
            if(userClass.getUserId()==newsFeedStatus.AllCommentId.get(i))
            {
                holder.buttonComment.setTextColor(context.getColor(R.color.sqr));
            }
        }

        holder.commentpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.v("ResponseLike",s);
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date();
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("User",MODE_PRIVATE);
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("MyObject","");
                                    UserClass userClass = gson.fromJson(json,UserClass.class);
                                    PostCommentClass postCommentClass=new PostCommentClass(userClass.getProfile(),userClass.getUser_name()
                                            ,formatter.format(date),holder.userComment.getText().toString(),"4");
                                    holder.commentCardView.setVisibility(View.VISIBLE);
                                    holder.commentUserName.setText(postCommentClass.getProfileName());
                                    holder.commentMessage.setText(postCommentClass.getCommentMsg());
                                    holder.commentTime.setText(postCommentClass.getCommentTime());
                                    Glide.with(context).load("https://archsqr.in/"+postCommentClass.getProfileImage())
                                            .into(holder.commentProfile);
                                    database= FirebaseDatabase.getInstance();
                                    ref = database.getReference();
//                                    PushNotificationClass notificationClass=new PushNotificationClass(userClass.getUserId(),userClass.getProfile(),newsFeedStatus.getPostId(),newsFeedStatus.getPostTitle(),newsFeedStatus.getShortDescription(),"Commented",userClass.getUser_name()+" commented on your post");
//                                    ref.child("Notifications").child(newsFeedStatus.getUserId()+"").setValue(notificationClass);
                                    holder.userComment.setText("");
                                    result=result+1;
                                    holder.buttonComment.setText(result+" Comment");

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
                            params.put("Authorization", "Bearer " +TokenClass.Token);

                            return params;
                        }
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();

                            params.put("commentable_id",newsFeedStatus.getSharedId()+"");
//
                            params.put("comment_text",holder.userComment.getText().toString());

                            //returning parameters
                            return params;
                        }
                    };

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }
            });
        holder.buttonLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    holder.buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                    int likeCount=Integer.parseInt(newsFeedStatus.getLike());
//                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
                    holder.buttonLikeList.setTextColor(context.getColor(R.color.sqr));
                    if(isAlreadyLikedFinal==1)
                        holder.buttonLikeList.setText(likeCount+" Like");
                    else
                    {
                        likeCount=likeCount+1;
                        holder.buttonLikeList.setText(likeCount+" Like");
                    }

                  //  buttonLikeList.setText(result+" Like");
                    database= FirebaseDatabase.getInstance();
                    ref = database.getReference();
                    SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("MyObject", "");
                    UserClass userClass = gson.fromJson(json, UserClass.class);

                    Log.v("daattataatat",userClass.getUserId()+" "+userClass.getProfile()+" ");
                    if(newsFeedStatus.getType().equals("status")&& userClass.getUserId()!=newsFeedStatus.getUserId())
                    {
                        PushNotificationClass pushNotificationClass;
                        from_user fromUser;
                        post post1=new post(newsFeedStatus.getFullDescription(),newsFeedStatus.getSlug(),"post Title",newsFeedStatus.getType(),newsFeedStatus.getPostId());
                        if(userClass.getName()!="null")
                        {
                            fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                            pushNotificationClass=new PushNotificationClass(userClass.getName()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
                        }
                        else
                        {
                            fromUser=new from_user(userClass.getEmail(),userClass.getFirst_name()+" "+userClass.getLast_name(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                            pushNotificationClass=new PushNotificationClass(userClass.getFirst_name()+" "+userClass.getLast_name()+" liked your status ",new Date().getTime(),fromUser,post1,"like_post");
                        }

                        String key =ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").push().getKey();
                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);


                        Map<String,String> unred=new HashMap<>();
                        unred.put("unread",key);
                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("unread").child(key).setValue(unred);
                    }
                    else if(userClass.getUserId()!=newsFeedStatus.getUserId())
                    {
                        from_user fromUser=new from_user(userClass.getEmail(),userClass.getName(),userClass.getUserId(),userClass.getUser_name(),userClass.getProfile());
                        post post1=new post(newsFeedStatus.getShortDescription(),newsFeedStatus.getSlug(),newsFeedStatus.getPostTitle(),newsFeedStatus.getType(),newsFeedStatus.getPostId());
                        PushNotificationClass pushNotificationClass;
                        if(userClass.getName().equals("null"))
                        {
                            pushNotificationClass=new PushNotificationClass(userClass.getUser_name()+" liked your article ",new Date().getTime(),fromUser,post1,"like_post");
                        }
                        else
                        {
                            pushNotificationClass=new PushNotificationClass(userClass.getName()+" liked your article ",new Date().getTime(),fromUser,post1,"like_post");
                        }

                        String key =ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").push().getKey();
                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("all").child(key).setValue(pushNotificationClass);


                        Map<String,String> unred=new HashMap<>();
                        unred.put("unread",key);
                        ref.child("notification").child(newsFeedStatus.getUserId()+"").child("unread").child(key).setValue(unred);

                    }

                    flag = 1;
                }
                else {
                    if(isAlreadyLikedFinal==1)
                    {
                        Log.v("isAlreadyLiked1",isAlreadyLikedFinal+" ");
                        holder.buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        int likeCount1=Integer.parseInt(newsFeedStatus.getLike());
                        Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                        likeCount1=likeCount1-1;
                        holder.buttonLikeList.setText(likeCount1+" Like");
                    }
                    else
                    {
                        Log.v("isAlreadyLiked2",isAlreadyLikedFinal+" ");
                        Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                        holder.buttonLikeList.setTextColor(context.getColor(R.color.gray));
                        holder.buttonLikeList.setText(newsFeedStatus.getLike()+" Like");
                    }
                }
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);

//
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
                        params.put("Authorization", "Bearer " +TokenClass.Token);

                        return params;
                    }
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();

                        params.put("likeable_id",newsFeedStatus.getSharedId()+"");
                        params.put("likeable_type","users_post_share");
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }


        });

//        holder.share.setText(newsFeedStatus.getShare());
//        holder.commentUserName.setText(newsFeedStatus.getCommentUserName());
//        holder.commentTime.setText(newsFeedStatus.getCommentTime());
//        holder.commentDescription.setText(newsFeedStatus.getCommentDescription());
//        holder.commentLike.setText(newsFeedStatus.getCommentLike());
//        holder.postId.setText(newsFeedStatus.getPostId());
        //      holder.userId.setText(newsFeedStatus.getUserId());
    }

    @Override
    public int getItemCount() {
        return whatsRed.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView authName,postTime,postTitle,postDescription, postViews,commentpost,buttonLikeList;
        EditText userComment;
        ImageView authProfile,userProfile,postBannerImage;
        Button buttonComment,buttonShare,commentLike;
        CheckBox buttonLike;
        CardView commentCardView;
        TextView commentUserName,commentTime,commentMessage;
        ImageView commentProfile,red_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            postBannerImage=(ImageView) itemView.findViewById(R.id.red_postImage);
            authName=(TextView)itemView.findViewById(R.id.red_authName);
            postTime=(TextView)itemView.findViewById(R.id.red_postTime);
            postTitle =(TextView)itemView.findViewById(R.id.red_postTitle);
            postDescription=(TextView)itemView.findViewById(R.id.red_postDescription);
            authProfile=(ImageView)itemView.findViewById(R.id.red_authImage);
            postViews = (TextView) itemView.findViewById(R.id.red_postViews);
            buttonLike=(CheckBox) itemView.findViewById(R.id.red_like);
            commentCardView=(CardView)itemView.findViewById(R.id.red_commentListCard);
            buttonLikeList=(TextView) itemView.findViewById(R.id.red_likeList);
            buttonComment=(Button)itemView.findViewById(R.id.red_comment);
            buttonShare=(Button)itemView.findViewById(R.id.red_share);
            userComment = (EditText)itemView.findViewById(R.id.red_userComment);
            red_menu=(ImageView)itemView.findViewById(R.id.red_menu);
            commentProfile=(ImageView)itemView.findViewById(R.id.red_commenterProfile);
            commentMessage=itemView.findViewById(R.id.red_commentMsg);
            commentTime=itemView.findViewById(R.id.red_commentTime);
            commentUserName=itemView.findViewById(R.id.red_commentUserName);
            commentpost=(TextView)itemView.findViewById(R.id.red_commentPostbtn);
            buttonShare.setOnClickListener(new View.OnClickListener() {

                int flag = 0;
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    if (flag == 0) {
                        buttonShare.setTextColor(context.getColor(R.color.sqr));
                        flag = 1;
                    }
                    else {
                        buttonShare.setTextColor(context.getColor(R.color.gray));
                        flag = 0;
                    }
                    shareIt();
                }
            });
        }
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "SqrFactor");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "professional network for the architecture community visit https://sqrfactor.com");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
        public void showPopup() {
        LayoutInflater li = LayoutInflater.from(context);
//        final View promptsView = li.inflate(R.layout.post_likes_popup, null);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                context);
//
//        // set prompts.xml to alertdialog builder
////        alertDialogBuilder.setView(promptsView);
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        RecyclerView recyclerView = promptsView.findViewById(R.id.like_recycler);
        // show it
//        alertDialog.show();
    }
    }


