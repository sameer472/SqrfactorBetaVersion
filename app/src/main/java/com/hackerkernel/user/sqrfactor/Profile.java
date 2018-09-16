package com.hackerkernel.user.sqrfactor;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    ImageButton menu;
    Context context;
    PopupMenu popupMenu;
    Toolbar toolbar;
    TabLayout tabLayout1, tablayout;
    TextView bluePrint,portfolio,followers,following;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PostFragment()).commit();

        /*Intent i = getIntent();
        String s = i.getStringExtra("Activity");
        if (s.equals("1")){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PostFragment()).addToBackStack(null).commit();
        }
        else if (s.equals("2"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SettingsFragment()).addToBackStack(null).commit();

        else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PostFragment()).addToBackStack(null).commit();
        */
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        tablayout = (TabLayout)findViewById(R.id.tablayout);

        tablayout.addTab(tablayout.newTab().setText("0\nBlueprint"));
        tablayout.addTab(tablayout.newTab().setText("0\nPortfolio"));
        tablayout.addTab(tablayout.newTab().setText("0\nFollowers"));
        tablayout.addTab(tablayout.newTab().setText("0\nFollowing"));

        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){

                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new BluePrintFragment()).commit();
                        break;

                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Portfolio()).commit();
                        break;

                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FollowersFragment()).commit();
                        break;

                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FollowingFragment()).commit();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getPosition()==0)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new BluePrintFragment()).commit();
            }
        });

        menu = (ImageButton)findViewById(R.id.prof_more);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Profile.this,v);
                popupMenu.inflate(R.menu.profie_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.show();
                }

            });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*tabLayout1 = (TabLayout)findViewById(R.id.tabs2);

        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.status)
                .setText("Status"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.design)
                .setText("Design"));
        tabLayout1.addTab(tabLayout1.newTab().setIcon(R.drawable.article)
                .setText("Article"));

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            switch (tab.getPosition()){

                case 1:
                    Intent i2 = new Intent(getApplicationContext(), DesignActivity.class);
                    startActivity(i2);
                    break;

                case 2:
                    Intent i3 = new Intent(getApplicationContext(), ArticleActivity.class);
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
    });*/

        /*bluePrint = (TextView)findViewById(R.id.prof_blueprint);
        bluePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Profile.this,BluePrintFragment.class);
//                startActivity(i);
                BluePrintFragment bluePrintFragment = new BluePrintFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, bluePrintFragment);
                //transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        portfolio = (TextView)findViewById(R.id.prof_portfolio);
        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent j = new Intent(Profile.this,Portfolio.class);
//                startActivity(j);
                Portfolio portfolioFragment = new Portfolio();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, portfolioFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        followers = (TextView)findViewById(R.id.prof_followers);
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent k = new Intent(Profile.this,Followers.class);
//                startActivity(k);
                FollowersFragment followers = new FollowersFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer,followers);
                //transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        following = (TextView)findViewById(R.id.prof_following);
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent l = new Intent(Profile.this,Following.class);
//                startActivity(l);
                FollowingFragment following = new FollowingFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer,following);
                //transaction.addToBackStack(null);
                transaction.commit();

            }
        });*/
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


}
