package com.hackerkernel.user.sqrfactor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    private RecyclerView recyclerView1;
    private ArrayList<FollowerClass> followerClassArrayList=new ArrayList<>();

    public FollowersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_followers, container, false);
        recyclerView1 =view.findViewById(R.id.recyclerView_followers);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

////        adding dummy data for testing
//        FollowerClass followerClass=new FollowerClass("Sameer Shekhar","Chappra,  saran,  Bihar","5","10","url");
//        followerClassArrayList.add(followerClass);
//        FollowerClass followerClass1=new FollowerClass("Amit Maheshwari","Bangalore,  KA","6","5","url");
//        followerClassArrayList.add(followerClass1);
//        FollowersAdapter followersAdapter=new FollowersAdapter(followerClassArrayList,getContext());
//        recyclerView1.setAdapter(followersAdapter);
        return view;
    }

}
