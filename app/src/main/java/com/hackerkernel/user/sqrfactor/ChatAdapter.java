package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private ArrayList<ChatFriends>chatFriends;
    private Context context;


    public ChatAdapter(ArrayList<ChatFriends> chatFriends, Context context) {
        this.chatFriends = chatFriends;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_layout, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChatFriends chatFriend = chatFriends.get(position);

        String name=chatFriend.getName();
        if(name.equals("null"))
        {
            holder.frndName.setText(chatFriend.getUserName());
        }
        else
        {
            holder.frndName.setText(chatFriend.getName());
        }
        Glide.with(context).load("https://archsqr.in/"+chatFriend.getUserProfile())
                .into(holder.frndProfile);
        holder.chatMessage.setText(chatFriend.getChat());


        if(chatFriend.getUnread_count()!=0)
        {
            Log.v("count",chatFriend.getUnread_count()+"");
            holder.unreadCount.setText(chatFriend.getUnread_count()+"");
            holder.unreadCount.setVisibility(View.VISIBLE);
        }



        if(chatFriend.getIsOnline().equals("True"))
        {
            holder.online.setVisibility(View.VISIBLE);
            //Toast.makeText(context,chatFriend.getIsOnline()+"",Toast.LENGTH_LONG).show();
        }
        else
        {
            holder.online.setVisibility(View.GONE);
            //Toast.makeText(context,chatFriend.getIsOnline()+"",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public int getItemCount() {
        return chatFriends.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView frndProfile,online;
        TextView frndName,chatMessage,unreadCount;



        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ChatWithAFriendActivity.class);
                    intent.putExtra("FriendId",chatFriends.get(getAdapterPosition()).getUserID());
                    if(chatFriends.get(getAdapterPosition()).getName().equals("null"))
                    {
                        intent.putExtra("FriendName",chatFriends.get(getAdapterPosition()).getUserName());
                    }
                    else
                    {
                        intent.putExtra("FriendName",chatFriends.get(getAdapterPosition()).getName());
                    }
                    intent.putExtra("FriendProfileUrl",chatFriends.get(getAdapterPosition()).getUserProfile());
                    intent.putExtra("isOnline",chatFriends.get(getAdapterPosition()).getIsOnline());
                    context.startActivity(intent);
                }
            });
            frndProfile =(ImageView)itemView.findViewById(R.id.chat_frnd_profile);
            unreadCount=(TextView)itemView.findViewById(R.id.unreadCount);
            frndName =(TextView) itemView.findViewById(R.id.chat_frnd_name);
            chatMessage =(TextView)itemView.findViewById(R.id.chat_last_message);
            online=(ImageView) itemView.findViewById(R.id.onlineicon);
        }
    }
}