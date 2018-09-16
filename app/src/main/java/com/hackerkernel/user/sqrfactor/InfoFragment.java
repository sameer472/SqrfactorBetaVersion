package com.hackerkernel.user.sqrfactor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    TextView brief1, brief2, brief3, brief4, prizes1, prizes2, prizes3, prizes4, prizes5, prizes6, prizes7, prizes8, prizes9, prizes10, heading1, heading2, sponsor, note, eligibility;
    Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_info, container, false);

        String s1 = getContext().getResources().getString(R.string.comp_brief2);
        String s2 = getContext().getResources().getString(R.string.comp_brief3);
        String s3 = getContext().getResources().getString(R.string.comp_brief4);
        String s4 = getContext().getResources().getString(R.string.comp_brief5);
        String s5 = getContext().getResources().getString(R.string.prizes1);
        String s6 = getContext().getResources().getString(R.string.prizes2);
        String s7 = getContext().getResources().getString(R.string.prizes3);
        String s8 = getContext().getResources().getString(R.string.prizes4);
        String s9 = getContext().getResources().getString(R.string.prizes5);
        String s10 = getContext().getResources().getString(R.string.prizes6);
        String s11 = getContext().getResources().getString(R.string.prizes7);
        String s12 = getContext().getResources().getString(R.string.prizes8);
        String s13 = getContext().getResources().getString(R.string.prizes9);
        String s14 = getContext().getResources().getString(R.string.prizes10);
        String h1 = getContext().getResources().getString(R.string.heading1);
        String h2 = getContext().getResources().getString(R.string.heading2);
        String spon = getContext().getResources().getString(R.string.sponsor);
        String note_string = getContext().getResources().getString(R.string.note);
        String eligible = getContext().getResources().getString(R.string.eligible);

        brief1 = (TextView)view.findViewById(R.id.brief1);
        brief2 = (TextView)view.findViewById(R.id.brief2);
        brief3 = (TextView)view.findViewById(R.id.brief3);
        brief4 = (TextView)view.findViewById(R.id.brief4);
        prizes1 = (TextView)view.findViewById(R.id.prizes1);
        prizes2 = (TextView)view.findViewById(R.id.prizes2);
        prizes3 = (TextView)view.findViewById(R.id.prizes3);
        prizes4 = (TextView)view.findViewById(R.id.prizes4);
        prizes5 = (TextView)view.findViewById(R.id.prizes5);
        prizes6 = (TextView)view.findViewById(R.id.prizes6);
        prizes7 = (TextView)view.findViewById(R.id.prizes7);
        prizes8 = (TextView)view.findViewById(R.id.prizes8);
        prizes9 = (TextView)view.findViewById(R.id.prizes9);
        prizes10 = (TextView)view.findViewById(R.id.prizes10);
        heading1 = (TextView)view.findViewById(R.id.heading1);
        heading2 = (TextView)view.findViewById(R.id.heading2);
        sponsor = (TextView)view.findViewById(R.id.sponsor);
        note = (TextView)view.findViewById(R.id.note);
        eligibility = (TextView)view.findViewById(R.id.eligibility);

        brief1.setText(Html.fromHtml(s1));
        brief2.setText(Html.fromHtml(s2));
        brief3.setText(Html.fromHtml(s3));
        brief4.setText(Html.fromHtml(s4));
        prizes1.setText(Html.fromHtml(s5));
        prizes2.setText(Html.fromHtml(s6));
        prizes3.setText(Html.fromHtml(s7));
        prizes4.setText(Html.fromHtml(s8));
        prizes5.setText(Html.fromHtml(s9));
        prizes6.setText(Html.fromHtml(s10));
        prizes7.setText(Html.fromHtml(s11));
        prizes8.setText(Html.fromHtml(s12));
        prizes9.setText(Html.fromHtml(s13));
        prizes10.setText(Html.fromHtml(s14));
        heading1.setText(Html.fromHtml(h1));
        heading2.setText(Html.fromHtml(h2));
        sponsor.setText(Html.fromHtml(spon));
        note.setText(Html.fromHtml(note_string));
        eligibility.setText(Html.fromHtml(eligible));

        register = (Button)view.findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegistrationDialog().show(getFragmentManager(), "");
            }
        });

        return view;

    }

}
