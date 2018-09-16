package com.hackerkernel.user.sqrfactor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class PostJob extends AppCompatActivity {

    Toolbar toolbar;
    EditText expirydate, skill1, skill2, skill3, qual1, qual2, qual3;
    TextView add_more, add_more2;
    Spinner country_spinner;
    ArrayList<String> countries = new ArrayList<>();

    private int cyear, cmonth, cdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

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

        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cdate = calendar.get(Calendar.DAY_OF_MONTH);

        add_more = (TextView)findViewById(R.id.add_more);
        skill1 = (EditText)findViewById(R.id.skill1);
        skill2 = (EditText)findViewById(R.id.skill2);
        skill3 = (EditText)findViewById(R.id.skill3);

        add_more2 = (TextView) findViewById(R.id.add_more2);
        qual1 = (EditText)findViewById(R.id.qual1);
        qual2 = (EditText)findViewById(R.id.qual2);
        qual3 = (EditText)findViewById(R.id.qual3);

        skill1.setVisibility(View.GONE);
        skill2.setVisibility(View.GONE);
        skill3.setVisibility(View.GONE);

        qual1.setVisibility(View.GONE);
        qual2.setVisibility(View.GONE);
        qual3.setVisibility(View.GONE);

        add_more.setOnClickListener(new View.OnClickListener() {
            int count=1;
            @Override
            public void onClick(View v) {
                if (count==1)
                    skill1.setVisibility(View.VISIBLE);
                else if (count==2) {

                    skill2.setVisibility(View.VISIBLE);
                }
                else if (count==3){

                    skill3.setVisibility(View.VISIBLE);
                }
                count++;
            }
        });

        add_more2.setOnClickListener(new View.OnClickListener() {
            int count=1;
            @Override
            public void onClick(View v) {
                if (count==1)
                    qual1.setVisibility(View.VISIBLE);
                else if (count==2) {

                    qual2.setVisibility(View.VISIBLE);
                }
                else if (count==3){

                    qual3.setVisibility(View.VISIBLE);
                }
                count++;
            }
        });

        expirydate = (EditText)findViewById(R.id.expirydate);
        expirydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(PostJob.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        expirydate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, cyear, cmonth, cdate).show();

            }
        });

        country_spinner = (Spinner)findViewById(R.id.country_spinner);
        Locale[] locale = Locale.getAvailableLocales();

        String country;
        for (Locale loc : locale){
            country = loc.getDisplayCountry();
            if (country.length()>0 && !countries.contains(country)){
                countries.add(country);
            }
        }

        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> country_adapter = new ArrayAdapter<>(PostJob.this, android.R.layout.simple_list_item_1, countries);
        country_spinner.setAdapter(country_adapter);

    }
}
