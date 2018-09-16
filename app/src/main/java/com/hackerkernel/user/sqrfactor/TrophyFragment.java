package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class TrophyFragment extends Fragment {

    //Button news, red;
    TabLayout tabLayout1;
    ImageView btn, camera;
    Button like, comment, share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View) inflater.inflate(R.layout.fragment_trophy, container, false);

        //getChildFragmentManager().beginTransaction().replace(R.id.fragment, new StatusFragment()).addToBackStack(null).commit();

        btn = (ImageView)view.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getContext(), v);
                pop.getMenuInflater().inflate(R.menu.home_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.comp:
                                Intent i = new Intent(getContext(), LaunchCompetition.class);
                                startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        like = (Button)view.findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LikeDialog().show(getFragmentManager(), "");
            }
        });

        comment = (Button)view.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), CommentsPage.class);
                startActivity(i);

            }
        });

        share = (Button)view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog().show(getFragmentManager(),"");
            }
        });

        tabLayout1 = (TabLayout) view.findViewById(R.id.tabs2);

        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.status)
                .setText("Status"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.design)
                .setText("Design"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.article)
                .setText("Article"));

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 1:
                        Intent i2 = new Intent(getContext(), DesignActivity.class);
                        startActivity(i2);
                        break;

                    case 2:
                        Intent i3 = new Intent(getContext(), ArticleActivity.class);
                        startActivity(i3);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*news = (Button)view.findViewById(R.id.feed);
        red = (Button)view.findViewById(R.id.red);

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getChildFragmentManager().beginTransaction().replace(R.id.frag1, new NewsFeedFragment()).addToBackStack(null).commit();

            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getChildFragmentManager().beginTransaction().replace(R.id.frag1, new RedFragment()).addToBackStack(null).commit();

            }
        });*/

        return view;

    }

}
