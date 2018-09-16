package com.hackerkernel.user.sqrfactor;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    String names[] = {"Henry Cavill", "Keanu Reeves"};
    Drawable images[];

    public SearchAdapter(Drawable[] images) {
        this.images = images;
    }

    @NonNull

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout, parent, false);
        ViewHolder v = new ViewHolder(ll);

        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.tv.setText(names[position]);
        holder.iv.setImageDrawable(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView)itemView.findViewById(R.id.iv);
            tv = (TextView)itemView.findViewById(R.id.tv);
        }
    }

}
