package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static com.hackerkernel.user.sqrfactor.ArticleActivity.RequestPermissionCode;
import static com.hackerkernel.user.sqrfactor.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class Design2Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private String cropedImageString;
    private Uri uri;
    private Intent CamIntent, GalIntent, CropIntent ;
    private RadioButton radioButtonCompetition,radioButtonCollegeProject,collegeYes,collegeNo;
    private Spinner buildingType,designType,statusType,INRType;
    private String spin_val=null;
    private String college_part_string="No";
    private boolean isEdit=false;
    private String project_part_string="No";
    private EditText startYear,endYear, budget,competitionLink,semester,tags;;
    private TextInputLayout competition,collegeProject;
    private Button newsPublish;
    private ArticleEditClass designEditClass;
    private SecondPageDesignData secondPageDesignData;
    private TextView bannerImage;
    private RadioGroup radioGroup1,radioGroup2;
    private boolean edDate=false;
    private boolean stDate=false;
    private TextView status_banner_image;
    private ImageView bannerImageView,bannerAttachedBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design2);
        ContinueAfterPermission();
    }

    public void ContinueAfterPermission(){



        Intent intent=getIntent();
        if(intent!=null && intent.hasExtra("ArticleEditClass")&& intent.hasExtra("SecondPageDesignData"))
        {
            isEdit=true;
            designEditClass= (ArticleEditClass) intent.getSerializableExtra("ArticleEditClass");
            secondPageDesignData=(SecondPageDesignData)intent.getSerializableExtra("SecondPageDesignData");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post Design");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        budget = findViewById(R.id.status_budget_text);
        if(secondPageDesignData.getTotal_budget()!=null)
        {
            budget.setText(secondPageDesignData.getTotal_budget());
        }


        competition=findViewById(R.id.competition_link);
        competitionLink = findViewById(R.id.competition_link_text);

        if(secondPageDesignData.getProject_part().equals("yes"))
        {
            competition.setVisibility(View.VISIBLE);
            project_part_string="yes";
            competitionLink.setText(secondPageDesignData.getCompetition_link());

        }
        collegeProject=findViewById(R.id.semester);
        semester = findViewById(R.id.semester_text);
        if(secondPageDesignData.getCollege_part().equals("yes"))
        {
            collegeProject.setVisibility(View.VISIBLE);
            semester.setText(secondPageDesignData.getCollege_link());
            college_part_string="yes";

        }
        status_banner_image=findViewById(R.id.status_banner_image1);



        tags = findViewById(R.id.status_tags_text);
        if(secondPageDesignData.getTags()!=null)
        {
            tags.setText(secondPageDesignData.getTags());
        }
        newsPublish = findViewById(R.id.publish_news);
        bannerAttachedBanner = findViewById(R.id.banner_attachment_icon1);

        radioGroup1=findViewById(R.id.RadioCompetetion);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.competition_yes:
                        competition.setVisibility(View.VISIBLE);
                        project_part_string="Yes";
                        break;
                    case R.id.competition_no:
                        competition.setVisibility(View.GONE);
                        project_part_string="No";
                        break;
                }
            }
        });


        radioGroup2=findViewById(R.id.RadioCollegeProject);

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.college_yes:
                        collegeProject.setVisibility(View.VISIBLE);
                        college_part_string="Yes";
                        break;
                    case R.id.college_no:
                        collegeProject.setVisibility(View.GONE);
                        college_part_string="No";
                        break;
                }
            }
        });

        buildingType =(Spinner)findViewById(R.id.select_building);
        if(secondPageDesignData.getBuilding_program()!=null)
        {
            buildingType.setSelection(getIndex(buildingType, secondPageDesignData.getBuilding_program()));
        }
        designType =(Spinner)findViewById(R.id.select_design_type);
        if(secondPageDesignData.getSelect_design_type()!=null)
        {
            designType.setSelection(getIndex(designType, secondPageDesignData.getSelect_design_type()));
        }
        statusType =(Spinner)findViewById(R.id.select_status);
        if(secondPageDesignData.getStatus()!=null)
        {
            statusType.setSelection(getIndex(statusType, secondPageDesignData.getStatus()));
        }
        INRType =(Spinner)findViewById(R.id.select_INR);
        if(secondPageDesignData.getCurrency()!=null)
        {
            INRType.setSelection(getIndex(INRType, secondPageDesignData.getCurrency()));
        }

        startYear = findViewById(R.id.status_Start_year_text);
        if(secondPageDesignData.getStart_year()!=null)
        {
            startYear.setText(secondPageDesignData.getStart_year());
        }
        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };
        startYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stDate=true;
                new DatePickerDialog(Design2Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endYear = findViewById(R.id.status_End_year_text);
        if(secondPageDesignData.getEnd_year()!=null)
        {
            endYear.setText(secondPageDesignData.getEnd_year());
        }

        endYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edDate=true;
                new DatePickerDialog(Design2Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        bannerAttachedBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Design2Activity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Design2Activity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(Design2Activity.this,
                        android.Manifest.permission.CAMERA))
                {

                    Toast.makeText(Design2Activity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(Design2Activity.this,new String[]{
                            CAMERA}, RequestPermissionCode);

                }
                selectImage();

            }
        });


        bannerImageView = findViewById(R.id.banner_attachment_image1);
        if(designEditClass.getBanner_image()!=null)
        {
            Glide.with(this).load("https://archsqr.in/"+designEditClass.getBanner_image())
                    .into(bannerImageView);
            bannerImageView.setVisibility(View.VISIBLE);
        }


        newsPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerImageView.invalidate();
                BitmapDrawable drawable = (BitmapDrawable)bannerImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String image=getStringImage(bitmap);
                if(isEdit)
                {
                      PostEditedDesignToServer(image);
                }
                else {
                    PostDesignToServer(image);
                }

            }
        });



    }

    private void PostEditedDesignToServer(final String image) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/design-parse-2-edit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tags.setText("");
                            endYear.setText("");
                            startYear.setText("");
                            budget.setText("");
                            bannerImageView.setVisibility(View.GONE);
                            status_banner_image.setText("");
                            if(project_part_string.equals("Yes"))
                            {
                                competitionLink.setText("");
                            }
                            if(college_part_string.equals("Yes"))
                            {
                                semester.setText("");
                            }

                            Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//



                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.v("result", getIntent().getStringExtra("slug")+secondPageDesignData.getLng()+secondPageDesignData.getLat()+buildingType.getSelectedItem().toString()+designType.getSelectedItem().toString()+statusType.getSelectedItem().toString()+INRType.getSelectedItem().toString()+budget.getText().toString()+endYear.getText().toString()+startYear.getText().toString()+tags.getText().toString()+getIntent().getStringExtra("Description")+getIntent().getStringExtra("ShortDescription")+getIntent().getStringExtra("Location")+getIntent().getStringExtra("Title")+image);
                params.put("banner_image","data:image/jpeg;base64,"+image);
                params.put("oldTitle",getIntent().getStringExtra("Title"));
                params.put("oldDescription",getIntent().getStringExtra("Description"));
                params.put("oldDescription_short",getIntent().getStringExtra("ShortDescription"));
                params.put("oldFormatted_address",getIntent().getStringExtra("Location"));
                params.put("slug",getIntent().getStringExtra("slug"));
                params.put("oldType","design");
                params.put("oldLat",secondPageDesignData.getLat());
                params.put("oldLng",secondPageDesignData.getLng());
                params.put("tags",tags.getText().toString());
                params.put("select_design_type",designType.getSelectedItem().toString());
                params.put("status",statusType.getSelectedItem().toString());
                params.put("building_program",buildingType.getSelectedItem().toString());
                params.put("start_year",startYear.getText().toString());
                params.put("end_year",endYear.getText().toString());
                params.put("total_budget",budget.getText().toString());
                params.put("inr",INRType.getSelectedItem().toString());
                if(project_part_string.equals("Yes"))
                {
                    params.put("project_part_val","Yes");
                    Log.v("competition_link",competitionLink.getText().toString());
                    params.put("competition_link",competitionLink.getText().toString());
                }
                else {
                    params.put("project_part","No");
                    params.put("competition_link","null");
                }
                if(college_part_string.equals("Yes"))
                {
                    params.put("college_part_val","Yes");
                    Log.v("college_link",semester.getText().toString());
                    params.put("college_link",semester.getText().toString());
                }
                else {
                    params.put("college_part","No");
                    params.put("college_link","null");
                }
                return params;
            }
        };

        requestQueue.add(myReq);


    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(stDate==true)
            startYear.setText(sdf.format(myCalendar.getTime()));
        if(edDate==true)
            endYear.setText(sdf.format(myCalendar.getTime()));
    }


    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 4);

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 5);
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 4) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);



                    bannerImageView.setImageBitmap(bitmap);
                    bannerImageView.setVisibility(View.VISIBLE);



                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    status_banner_image.setText(String.valueOf(System.currentTimeMillis()) + ".jpg");


                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        outFile.flush();

                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 5) {



                Uri selectedImage = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);
                String[] fileName = picturePath.split("/");
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                // Log.w("path of image from gallery......******************.........", fileName[fileName.length-1]+"");
                bannerImageView.setVisibility(View.VISIBLE);
                bannerImageView.setImageBitmap(thumbnail);

                status_banner_image.setText(fileName[fileName.length-1]+"");

            }

        }


        else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }


        else if (requestCode == 1) {
            if (data != null) {

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                //cropedImageString = getStringImage(bitmap);
                bannerImageView.setImageBitmap(bitmap);

            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 900);
            CropIntent.putExtra("outputY", 950);
            CropIntent.putExtra("aspectX", 0);
            CropIntent.putExtra("aspectY", 0);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }

    public void PostDesignToServer(final String image)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/design-parse-2",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tags.setText("");
                            endYear.setText("");
                            startYear.setText("");
                            budget.setText("");
                            bannerImageView.setVisibility(View.GONE);
                            status_banner_image.setText("");
                            if(project_part_string.equals("Yes"))
                            {
                                competitionLink.setText("");
                            }
                            if(college_part_string.equals("Yes"))
                            {
                                semester.setText("");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//



                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+TokenClass.Token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.v("result", buildingType.getSelectedItem().toString()+designType.getSelectedItem().toString()+statusType.getSelectedItem().toString()+INRType.getSelectedItem().toString()+budget.getText().toString()+endYear.getText().toString()+startYear.getText().toString()+designType.getSelectedItem().toString()+tags.getText().toString()+getIntent().getStringExtra("Description")+getIntent().getStringExtra("ShortDescription")+getIntent().getStringExtra("Location")+getIntent().getStringExtra("Title")+image);
                params.put("banner_image","data:image/jpeg;base64,"+image);
                params.put("oldTitle",getIntent().getStringExtra("Title"));
                params.put("oldDescription",getIntent().getStringExtra("Description"));
                params.put("oldDescription_short",getIntent().getStringExtra("ShortDescription"));
                params.put("oldFormatted_address",getIntent().getStringExtra("Location"));
                params.put("oldType","design");
                params.put("tags",tags.getText().toString());
                params.put("select_design_type",designType.getSelectedItem().toString());
                params.put("status",statusType.getSelectedItem().toString());
                params.put("building_program",buildingType.getSelectedItem().toString());
                params.put("start_year",startYear.getText().toString());
                params.put("end_year",endYear.getText().toString());
                params.put("total_budget",budget.getText().toString());
                params.put("inr",INRType.getSelectedItem().toString());
                if(project_part_string.equals("Yes"))
                {
                    params.put("project_part","Yes");
                    params.put("competition_link",competitionLink.getText().toString());
                }
                else {
                    params.put("project_part","No");
                    params.put("competition_link","null");
                }
                if(college_part_string.equals("Yes"))
                {
                    params.put("college_part","Yes");
                    params.put("college_link",semester.getText().toString());
                }
                else {
                    params.put("college_part","No");
                    params.put("college_link","null");
                }
                return params;
            }
        };

        requestQueue.add(myReq);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {



        if(requestCode== RequestPermissionCode)

        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(Design2Activity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

            }
            else {

                Toast.makeText(Design2Activity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

            }
        }

        else if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContinueAfterPermission();
            } else {
                // Permission Denied
                Toast.makeText(Design2Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}