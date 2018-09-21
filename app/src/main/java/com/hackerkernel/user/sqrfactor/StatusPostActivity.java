package com.hackerkernel.user.sqrfactor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class StatusPostActivity extends AppCompatActivity {

    private ImageView displayImage;
    private ImageView camera;// button
    public ImageButton mRemoveButton;
    RecyclerView recyclerView;
    private NewsFeedStatus newsFeedStatus;
    Button like, comment, share, like2;
    String token;
    SharedPreferences sharedPreferences;
    ImageView user_profile_photo;
    String message, encodedImage;
    private boolean isScrolling;
    int currentItems,totalItems,scrolledItems;
    private ProgressBar progressBar;
    TextView btnSubmit;
    private boolean isEdit=false;
    EditText writePost;
    FrameLayout frameLayout;
    private ProgressDialog pDialog;
    public static String UPLOAD_URL = "https://archsqr.in/api/post";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    LinearLayoutManager layoutManager;
    private NewsFeedAdapter newsFeedAdapter;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private String slug;
    private int postId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post Status");
        setSupportActionBar(toolbar);


        camera = findViewById(R.id.news_camera);
        displayImage = findViewById(R.id.news_upload_image);
        btnSubmit = findViewById(R.id.news_postButton);
        writePost = findViewById(R.id.write_status);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        frameLayout = findViewById(R.id.rl);
        frameLayout.setVisibility(View.GONE);
        jsonObject = new JSONObject();

        Intent intent=getIntent();
        if(intent!=null)
        {
            Toast.makeText(this,intent.getStringExtra("Post_Slug_ID"),Toast.LENGTH_LONG).show();
            isEdit=true;
            postId=intent.getIntExtra("Post_ID",0);
            slug=intent.getStringExtra("Post_Slug_ID");
            FetchDataFromServerAndBindToViews(intent.getStringExtra("Post_Slug_ID"));
        }


        mRemoveButton = findViewById(R.id.ib_remove);
        displayImage.setVisibility(View.GONE);
        mRemoveButton.setVisibility(View.GONE);

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
                displayImage.setImageBitmap(null);
                displayImage.setVisibility(View.GONE);
                mRemoveButton.setVisibility(View.GONE);


            }

        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                displayImage.setVisibility(View.VISIBLE);
                mRemoveButton.setVisibility(View.VISIBLE);
                showFileChooser();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit)
                {
                    uploadEditedStatusToServer();
                }
                else {
                    uploadImage(); 
                }
                
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        }

    }

    private void uploadEditedStatusToServer() {
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/status/edit-status",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Disimissing the progress dialog
                        loading.dismiss();
                        writePost.setText("");
                        displayImage.setVisibility(View.GONE);
                        mRemoveButton.setVisibility(View.GONE);

//                        //Showing toast message of the response
                        Log.v("response",s);
                        Toast.makeText(getApplicationContext(), "response"+s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.v("response",volleyError.toString());
                        Toast.makeText(getApplicationContext(), "response"+volleyError.toString() , Toast.LENGTH_LONG).show();
                        //Showing toast
                        // Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String image = getStringImage(bitmap);

                 Log.v("data",slug+" "+writePost.getText().toString()+" "+image);
                params.put("image","data:image/jpeg;base64,"+image);
                params.put("post_slug",slug);
                params.put("description",writePost.getText().toString().trim());

                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Disimissing the progress dialog
                        loading.dismiss();
//                        //Showing toast message of the response
                        Log.v("response",s);
                        Toast.makeText(getApplicationContext(), "response"+s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.v("response",volleyError.toString());
                        Toast.makeText(getApplicationContext(), "response"+volleyError.toString() , Toast.LENGTH_LONG).show();
                        //Showing toast
                        // Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " +TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String image = getStringImage(bitmap);

               // Log.v("ImageUrl","data:image/jpeg;base64,"+image);
                params.put("image_value","data:image/jpeg;base64,"+image);
                params.put("description",writePost.getText().toString().trim());

                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void cameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data !=null){
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),filepath);
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5),(int)(bitmap.getHeight()*0.5),false );
                displayImage.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);

    }

    private void FetchDataFromServerAndBindToViews(String post_slug_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // "https://archsqr.in/api/profile/detail/
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/status/edit/"+post_slug_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("StatusPost",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("statusPostEdit");
                            final ArticleEditClass statusPostEdit = new ArticleEditClass(jsonObjectFullPost);
                            writePost.setText(statusPostEdit.getDescription());
                            Log.v("url","https://archsqr.in/"+statusPostEdit.getImage());
                            displayImage.setVisibility(View.VISIBLE);
                            frameLayout.setVisibility(View.VISIBLE);
                            mRemoveButton.setVisibility(View.VISIBLE);
//                            Glide.with(getApplicationContext()).load("https://archsqr.in/"+statusPostEdit.getImage())
//                                    .into(displayImage);


                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load("https://archsqr.in/"+statusPostEdit.getImage())
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            Bitmap bitmapResized = Bitmap.createScaledBitmap(resource,
                                                    (int) (resource.getWidth() * 0.5), (int) (resource.getHeight() * 0.5), false);
                                           // imageString=getStringImage(resource);
                                            displayImage.setImageBitmap(bitmapResized);
                                            bitmap=bitmapResized;
                                        }
                                    });
//                            displayImage.invalidate();
//                            BitmapDrawable drawable = (BitmapDrawable) displayImage.getDrawable();
//                            bitmap= drawable.getBitmap();
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("Authorization", "Bearer " + TokenClass.Token);
                return params;
            }

        };
        requestQueue.add(myReq);
    }

}
