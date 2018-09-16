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
public class FollowingFragment extends Fragment {

    private RecyclerView recyclerView1;
    private ArrayList<FollowerClass> followerClassArrayList=new ArrayList<>();

    public FollowingFragment() {
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
        View view= inflater.inflate(R.layout.fragment_following, container, false);
        recyclerView1 =view.findViewById(R.id.recyclerView_following);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        //adding dummy data for testing
//        FollowerClass followerClass=new FollowerClass("Agnim Gupta","Gwalior, MP","8","10","url");
//        followerClassArrayList.add(followerClass);
//        FollowerClass followerClass1=new FollowerClass("Amit Maheshwari","Bangalore,  KA","6","5","url");
//        followerClassArrayList.add(followerClass1);
//        FollowersAdapter followersAdapter=new FollowersAdapter(followerClassArrayList,getContext());
//        recyclerView1.setAdapter(followersAdapter);
        return view;
    }
}
