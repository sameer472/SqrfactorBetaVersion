package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.MyViewHolder> {
    private ArrayList<PortfolioClass> portfolioClassArrayList;
    private Context context;

    public PortfolioAdapter(ArrayList<PortfolioClass> portfolioClassArrayList, Context context) {
        this.portfolioClassArrayList = portfolioClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.portfolio_adapter_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final PortfolioClass portfolioClass=portfolioClassArrayList.get(position);
        holder.articleName.setText(portfolioClass.getTitle());
        holder.shortDescrip.setText(portfolioClass.getShort_description());
        Glide.with(context).load("https://archsqr.in/"+portfolioClass.getArticleImageUrl())
                .into(holder.articleImage);
        holder.articleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FullPostActivity.class);
                intent.putExtra("Post_Slug_ID",portfolioClass.getSlug());
                context.startActivity(intent);
            }
        });

        holder.likes.setText(portfolioClass.getLikeCount());
        holder.comment.setText(portfolioClass.getCommentcount());
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.menuButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_portfolio);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.RemoveFromFeatures:
                                //handle case for RemoveFromFeatures here
                                break;
                            case R.id.EditPost:
                                //handle case for EditPost here
                                break;

                        }
                        return false;
                    }
                });

            }
        });
        //set profile image using glide libaray fromserver
    }

    @Override
    public int getItemCount() {
        return portfolioClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView articleName,shortDescrip,likes,comment;
        ImageView articleImage;
        ImageView menuButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            articleName=(TextView)itemView.findViewById(R.id.articleTitle_portfolio);
            shortDescrip=(TextView)itemView.findViewById(R.id.short_descrip_portfolio);
            likes=(TextView)itemView.findViewById(R.id.like_portfolio);
            comment=(TextView)itemView.findViewById(R.id.Comments_portfolio);
            articleImage=(ImageView)itemView.findViewById(R.id.imageProfile);
            menuButton=(ImageView)itemView.findViewById(R.id.more_portfolio);
        }
    }
}