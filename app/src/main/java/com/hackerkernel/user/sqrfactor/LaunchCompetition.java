package com.hackerkernel.user.sqrfactor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

public class LaunchCompetition extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button nextbtn;
    EditText startdate, closedate, closedate2, announcedate, attach;
    RadioButton rb1, rb2;
    TextView add_more;
    LinearLayout jury1, jury2, jury3;
    private int cyear, cmonth, cdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_competition);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        attach = (EditText)findViewById(R.id.attach);

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivity(i);
            }
        });

        add_more = (TextView)findViewById(R.id.add_more);
        jury1 = (LinearLayout)findViewById(R.id.jury1);
        jury2 = (LinearLayout)findViewById(R.id.jury2);
        jury3 = (LinearLayout)findViewById(R.id.jury3);

        jury1.setVisibility(View.GONE);
        jury2.setVisibility(View.GONE);
        jury3.setVisibility(View.GONE);

        add_more.setOnClickListener(new View.OnClickListener() {
            int count=1;
            @Override
            public void onClick(View v) {
                if (count==1)
                    jury1.setVisibility(View.VISIBLE);
                if (count==2)
                    jury2.setVisibility(View.VISIBLE);
                if (count==3)
                    jury3.setVisibility(View.VISIBLE);
                count++;
            }
        });

        nextbtn = (Button)findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Launch2.class);
                startActivity(i);
            }
        });

        startdate = (EditText)findViewById(R.id.startdate);
        closedate = (EditText)findViewById(R.id.closedate);
        closedate2 = (EditText)findViewById(R.id.closedate2);
        announcedate = (EditText)findViewById(R.id.announcedate);

        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cdate = calendar.get(Calendar.DAY_OF_MONTH);

        startdate.setOnClickListener(this);
        closedate.setOnClickListener(this);
        closedate2.setOnClickListener(this);
        announcedate.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(v==startdate)
                    startdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                else if (v==closedate)
                    closedate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                else if(v==closedate2)
                    closedate2.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                else
                    announcedate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        };

        new DatePickerDialog(LaunchCompetition.this, listener, cyear, cmonth, cdate).show();
    }

}
