package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    String text;
    private ArrayList<NotificationClass> notificationsClassArrayList;
    private Context context;

    public NotificationsAdapter(String s) {
        text = s;
    }
    public NotificationsAdapter(ArrayList<NotificationClass> notificationlist, Context context) {
        this.notificationsClassArrayList = notificationlist;
        this.context = context;

    }

    @NonNull

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_layout, parent, false);

        ViewHolder v = new ViewHolder(ll);
        return v;

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        final NotificationClass notificationsClass=notificationsClassArrayList.get(position);
        SharedPreferences mPrefs =context.getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FullPostActivity.class);
                intent.putExtra("Post_Slug_ID",notificationsClass.getSlug());
                context.startActivity(intent);
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                intent.putExtra("User_id",notificationsClass.getUserId());
                intent.putExtra("ProfileUserName",notificationsClass.getUser_name());
                context.startActivity(intent);
            }
        });

        if(notificationsClass.getType().equals("App\\Notifications\\CommentNoti_Post")){
            holder.notificationLine.setText("Commented on you post");
        }
        if(notificationsClass.getType().equals("App\\Notifications\\LikeNoti_Post")){
            holder.notificationLine.setText("Liked your post");
        }
        if (notificationsClass.getType().equals("App\\Notifications\\FollowNoti")){
            holder.notificationLine.setText("Started following you");
        }
        if (notificationsClass.getType().equals("App\\Notifications\\LikeNoti_Comment")){
            holder.notificationLine.setText("Liked your comment");
        }
        if(notificationsClass.getName().equals("null"))
        {
            holder.name.setText(notificationsClass.getFirst_name()+" "+notificationsClass.getLast_name());
        }
        else {
            holder.name.setText(notificationsClass.getName());
        }

//        if(notificationsClass.getType().equals("status"))
//        {
//            Log.v("status1",notificationsClass.getType());
//            holder.description.setText(notificationsClass.getDescription());
//
//        }
//
//        else if(notificationsClass.getType().equals("design"))
//        {
//            Log.v("status2",notificationsClass.getType());
//            //holder.postTitle.setText(newsFeedStatus.getPostTitle());
//            holder.description.setText(notificationsClass.getDescription());
//        }
//
//        else if(notificationsClass.getType().equals("article"))
//        {
//            Log.v("status2",notificationsClass.getType());
//            holder.description.setText(notificationsClass.getDescription());
//        }
        holder.description.setText(notificationsClass.getTitle());
        String dtc = notificationsClass.getTime();
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date past = format.parse(dtc);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<60)
            {
                holder.time.setText(seconds+" sec ago");

            }
            else if(minutes<60)
            {
                holder.time.setText(minutes+" min ago");
            }
            else if(hours<24)
            {
                holder.time.setText(hours+" hours ago");
            }
            else
            {
                holder.time.setText(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }


        Glide.with(context).load("https://archsqr.in/"+notificationsClass.getProfile())
                .into(holder.profile);
        Toast.makeText(context,notificationsClass.getProfile(), Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return notificationsClassArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name,notificationLine,time,postTitle,description;
        public ImageView profile;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.notification_transfer);
            notificationLine = (TextView)itemView.findViewById(R.id.notification_line);
            name = (TextView)itemView.findViewById(R.id.notification_name);
            time = (TextView)itemView.findViewById(R.id.notification_time);
            description= (TextView)itemView.findViewById(R.id.notification_title);
            profile =(ImageView)itemView.findViewById(R.id.notification_user_image);
        }
    }
    }


