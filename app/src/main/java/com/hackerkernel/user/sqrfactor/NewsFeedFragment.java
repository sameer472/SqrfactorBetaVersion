package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class NewsFeedFragment extends Fragment {
    FloatingActionButton fabView, fabStatus, fabDesign, fabArticle;
    private boolean fabExpanded = false;
    private LinearLayout layoutFabStatus;
    private LinearLayout layoutFabDesign;
    private LinearLayout layoutFabArticle;

    private EditText writePost;
    private ImageView profileImage;

    Animation rotate_forward, rotate_Backward, fab_open, fab_close;
    Button button1,button2;
    public int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View)inflater.inflate(R.layout.fragment_news_feed, container, false);


//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("PREF_NAME",getActivity().MODE_PRIVATE);
//        String token = sharedPreferences.getString("TOKEN","sqr");
//        Log.v("Token2",token);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        profileImage =view.findViewById(R.id.newsProfileImage);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);
        writePost = view.findViewById(R.id.news_editPost);
        writePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

        fabView = view.findViewById(R.id.fab_view);
        fabStatus = view.findViewById(R.id.fab_status);
        fabDesign = view.findViewById(R.id.fab_design);
        fabArticle = view.findViewById(R.id.fab_article);

        layoutFabStatus = (LinearLayout) view.findViewById(R.id.layoutFabStatus);
        layoutFabDesign = (LinearLayout) view.findViewById(R.id.layoutFabDesign);
        layoutFabArticle = (LinearLayout) view.findViewById(R.id.layoutFabArticle);

        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_Backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        closeSubMenusFab();

        fabStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StatusPostActivity.class);
                getActivity().startActivity(intent);
            }
        });
        fabDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DesignActivity.class);
                getActivity().startActivity(intent);
            }
        });
        fabArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);
                getActivity().startActivity(intent);
            }
        });
        button1 = view.findViewById(R.id.newsFeedbtn);
        button2 = view.findViewById(R.id.whatsRedbtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();
            }


        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RedActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
    private void openSubMenusFab(){
        layoutFabStatus.setVisibility(View.VISIBLE);
        layoutFabDesign.setVisibility(View.VISIBLE);
        layoutFabArticle.setVisibility(View.VISIBLE);
        fabStatus.startAnimation(fab_open);
        fabDesign.setAnimation(fab_open);
        fabArticle.setAnimation(fab_open);
        fabView.startAnimation(rotate_forward);
        fabView.setImageResource(R.drawable.ic_add_black_24dp);
        fabExpanded = true;
    }
    private void closeSubMenusFab(){
        layoutFabStatus.setVisibility(View.GONE);
        layoutFabDesign.setVisibility(View.GONE);
        layoutFabArticle.setVisibility(View.GONE);
        fabStatus.startAnimation(fab_close);
        fabDesign.setAnimation(fab_close);
        fabArticle.setAnimation(fab_close);
        fabView.startAnimation(rotate_Backward);
        fabExpanded = false;
    }

}
