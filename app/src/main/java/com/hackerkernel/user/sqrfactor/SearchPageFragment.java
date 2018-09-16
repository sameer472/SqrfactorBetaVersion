package com.hackerkernel.user.sqrfactor;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchPageFragment extends Fragment {

    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    Drawable dw1, dw2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_search_page, container, false);

        dw1 = getContext().getResources().getDrawable(R.drawable.henry);
        dw2 = getContext().getResources().getDrawable(R.drawable.profilepic);

        Drawable images[] = {dw1, dw2};

        recycler = (RecyclerView)view.findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recycler.addItemDecoration(decoration);

        adapter = new SearchAdapter(images);
        recycler.setAdapter(adapter);

        return view;

    }

}
