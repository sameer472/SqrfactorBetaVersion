    package com.hackerkernel.user.sqrfactor;

    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.LinearLayout;
    import android.widget.Spinner;
    import android.widget.TextView;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Locale;

    public class PostEvent extends AppCompatActivity {

    Toolbar toolbar;
    Spinner country_spinner;
    Button image, img1, img2, img3;
    EditText attach;
    TextView add_more;
    LinearLayout more_img1, more_img2, more_img3;
    ArrayList<String> countries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

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

        image = (Button)findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivity(i);
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
        more_img1 = (LinearLayout)findViewById(R.id.more_img1);
        more_img2 = (LinearLayout)findViewById(R.id.more_img2);
        more_img3 = (LinearLayout)findViewById(R.id.more_img3);

        img1 = (Button)findViewById(R.id.img1);
        img2 = (Button)findViewById(R.id.img2);
        img3 = (Button)findViewById(R.id.img3);

        more_img1.setVisibility(View.GONE);
        more_img2.setVisibility(View.GONE);
        more_img3.setVisibility(View.GONE);

        add_more.setOnClickListener(new View.OnClickListener() {
            int count=1;
            @Override
            public void onClick(View v) {
                if (count==1)
                    more_img1.setVisibility(View.VISIBLE);
                if (count==2)
                    more_img2.setVisibility(View.VISIBLE);
                if (count==3)
                    more_img3.setVisibility(View.VISIBLE);
                count++;
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivity(i);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivity(i);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivity(i);
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

        ArrayAdapter<String> country_adapter = new ArrayAdapter<>(PostEvent.this, android.R.layout.simple_list_item_1, countries);
        country_spinner.setAdapter(country_adapter);

    }

    }
