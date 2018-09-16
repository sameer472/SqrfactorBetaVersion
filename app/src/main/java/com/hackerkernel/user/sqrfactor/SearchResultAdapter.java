package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    Context context;
    ArrayList<SearchResultClass> searchResultClasses;
    public SearchResultAdapter(Context context, ArrayList<SearchResultClass> searchResultClasses) {
        this.context=context;
        this.searchResultClasses=searchResultClasses;
    }

    @NonNull
    @Override
    public SearchResultAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout_adapter, parent, false);
        return new SearchResultAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.MyViewHolder holder, int position) {

        final SearchResultClass searchResultClass=searchResultClasses.get(position);
        Glide.with(context).load("https://archsqr.in/"+searchResultClass.getProfile())
                .into(holder.profileImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserProfileActivity.class);
                intent.putExtra("User_id",searchResultClass.getId());
                intent.putExtra("ProfileUserName",searchResultClass.getUserName());
                context.startActivity(intent);
            }
        });
        if(!searchResultClass.getName().equals("null"))
        {
            holder.name.setText(searchResultClass.getName());
        }
        else {
            holder.name.setText(searchResultClass.getFirst_name()+" "+searchResultClass.getLast_name());
        }

    }

    @Override
    public int getItemCount() {
        return searchResultClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView name;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            profileImage=(ImageView)itemView.findViewById(R.id.profile);
            name=(TextView)itemView.findViewById(R.id.name);
        }
    }
}