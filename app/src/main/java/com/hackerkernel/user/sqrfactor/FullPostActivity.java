package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.github.irshulx.Editor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FullPostActivity extends AppCompatActivity {
    private ArrayList<NewsFeedStatus> newsFeedStatuses;
    private TextView postTitle, postDescription, postTag, red_authName, red_postTime,red_postViews,red_likeList,likeList,post;
    private Button red_comment, red_share;
    private ImageView red_authImage,userImage,full_post_menu,commentProfileImageUrl;
    private Editor editor;
    private WebView webView;
    private CheckBox like;
    private LinearLayout web;
    private EditText written_comment_body;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String slug;
    private int flag = 0,sharedId;
    UserClass userClass;
    private CardView news_comment_card;
    private TextView commentTime,commentDescription,commentUserName,fullPostDescription;
    private int flag1 = 0,isAlreadyLiked=0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
         userClass = gson.fromJson(json, UserClass.class);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fullPostDescription=(TextView)findViewById(R.id.fullPostDescription);
        web=(LinearLayout)findViewById(R.id.webView);
        postTitle = findViewById(R.id.full_postTitle);
        postDescription = findViewById(R.id.full_postDescription);
        postTag = findViewById(R.id.full_postTag);
        red_authName = findViewById(R.id.full_post_authName);
        red_postTime = findViewById(R.id.full_postTime);
        red_postViews = findViewById(R.id.full_postViews);
        like = findViewById(R.id.full_like);
        likeList = findViewById(R.id.full_likeList);
        red_comment = findViewById(R.id.full_comment);
        red_share = findViewById(R.id.full_share);
        red_authImage = findViewById(R.id.full_authImage);
        userImage = findViewById(R.id.full_post_userProfile);
        red_share = findViewById(R.id.full_share);
        post = findViewById(R.id.full_post_commentPostbtn);
        full_post_menu = findViewById(R.id.full_post_menu);
        written_comment_body=findViewById(R.id.full_post_userComment);

        commentTime=findViewById(R.id.news_comment_time_fullpost);
        commentDescription=findViewById(R.id.news_comment_descrip_fullpost);
        commentProfileImageUrl=findViewById(R.id.news_comment_image_fullpost);
        commentUserName=findViewById(R.id.news_comment_name_fullpost);
        news_comment_card=findViewById(R.id.news_comment_card_fullPost);
        // htmlContent=(TextView)findViewById(R.id.htmlContent);
        //editor=findViewById(R.id.editor1);
        //webView=(WebView)findViewById(R.id.webview);
        //editor=(Editor)findViewById(R.id.full_post_editor);

        // htmlTextView = (HtmlTextView)findViewById(R.id.html_text);

// loads html from string and displays http://www.example.com/cat_pic.png from the Internet



        if(getIntent().getStringExtra("Post_Slug_ID")!=null)
            slug = getIntent().getStringExtra("Post_Slug_ID");
        Toast.makeText(this,slug,Toast.LENGTH_LONG).show();
        LoadFullPostDataFromServer();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    public void LoadFullPostDataFromServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(FullPostActivity.this);
        // "https://archsqr.in/api/profile/detail/
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/post-detail/"+slug,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("post");
                            final FullPost fullPost = new FullPost(jsonObjectFullPost);
                            sharedId=fullPost.getShared_id();
                            if(!fullPost.getTitle().equals("null"))
                            {
                                postTitle.setText(fullPost.getTitle());
                            }
                            if(!fullPost.getShort_description().equals("null"))
                            {
                                postDescription.setText(fullPost.getShort_description());
                            }

                            postTag.setText(fullPost.getType());
                            if(fullPost.getUser_name_of_post().equals("null"))
                            {
                                red_authName.setText(fullPost.getAuthName());
                            }
                            else {
                                red_authName.setText(fullPost.getUser_name_of_post());
                            }


                            red_postTime.setText(ConvertTime(fullPost.getUpdated_at()) + " days ago");
                            Glide.with(getApplicationContext()).load("https://archsqr.in/" + fullPost.getAuthImageUrl())
                                    .into(red_authImage);


                            final String finalHtml="   <html>\n" +
                                    "  <head>\n" +
                                    "    <title>Combined</title>\n" +
                                    "  </head>\n" +
                                    "  <body>\n" +
                                    "    <div id=\"page1\">\n" +
                                    fullPost.getDescription() +
                                    "    </div>\n" +
                                    "  </body>\n" +
                                    "</html>";

                            Log.v("des0",fullPost.getDescription());

                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try { Thread.sleep(300); }
                                    catch (InterruptedException e) {}

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            PostContentHandler postContentHandler=new PostContentHandler(getApplicationContext(),finalHtml,web,fullPostDescription);
                                            postContentHandler.setContentToView();
                                        }
                                    });
                                }
                            };
                            thread.start();


                            likeList.setText(fullPost.getLike()+" Likes");
                            red_comment.setText(fullPost.getComments_count()+" comments");
                            for(int i=0;i<fullPost.getAllLikesId().size();i++)
                            {
                                if(userClass.getUserId()==fullPost.AllLikesId.get(i))
                                {
                                    likeList.setTextColor(getResources().getColor(R.color.red));
                                    isAlreadyLiked=1;
                                    like.setChecked(true);
                                }
                            }

                            for(int i=0;i<fullPost.getAllCommentId().size();i++)
                            {
                                if(userClass.getUserId()==fullPost.AllCommentId.get(i))
                                {
                                    red_comment.setTextColor(getResources().getColor(R.color.sqr));
                                }
                            }


                            LikeAndCommentFunction(fullPost);
//                            red_postTime.setText(fullPost.getTitle());
//                            postTitle.setText(fullPost.getTitle());
//                            postTitle.setText(fullPost.getTitle());


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


    private void LikeAndCommentFunction(final FullPost fullPost) {
        SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(userImage);




        likeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LikeListActivity.class);
                intent.putExtra("id",fullPost.getId());
                startActivity(intent);
            }
        });
        red_comment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsPage.class);
                intent.putExtra("PostSharedId",fullPost.getShared_id()); //second param is Serializable
                startActivity(intent);


            }
        });

       like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
//                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    int likeCount=Integer.parseInt(fullPost.getLike());
//                        DrawableCompat.setTint(like.getDrawable(), ContextCompat.getColor(context,R.color.sqr));
                   likeList.setTextColor(getColor(R.color.sqr));
                    if(isAlreadyLiked==1)
                        likeList.setText(likeCount+" Like");
                    else
                    {
                        likeCount=likeCount+1;
                        likeList.setText(likeCount+" Like");
                    }

                    database= FirebaseDatabase.getInstance();
                    ref = database.getReference();
                    SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = mPrefs.getString("MyObject", "");
                    UserClass userClass = gson.fromJson(json, UserClass.class);

                    Log.v("daattataatat",userClass.getUserId()+" "+userClass.getProfile()+" ");
                    if(fullPost.getType().equals("status"))
                    {
//                        PushNotificationClass pushNotificationClass=new PushNotificationClass(userClass.getUserId(),userClass.getProfile(),Integer.parseInt(fullPost.getUser_post_id()),fullPost.getDescription(),fullPost.getDescription(),"Like",userClass.getUser_name()+" liked your post");
//                        ref.child("Notifications").child(fullPost.getUser_id()+"").setValue(pushNotificationClass);

                    }
                    else
                    {
//                        PushNotificationClass pushNotificationClass=new PushNotificationClass(userClass.getUserId(),userClass.getProfile(),Integer.parseInt(fullPost.getUser_post_id()),fullPost.getTitle(),fullPost.getDescription(),"Like",userClass.getUser_name()+" liked your post");
//                        ref.child("Notifications").child(fullPost.getUser_id()+"").setValue(pushNotificationClass);

                    }
                }
                else {

                    if(isAlreadyLiked==1)
                    {
                        Log.v("isAlreadyLiked1",isAlreadyLiked+" ");
                        likeList.setTextColor(getColor(R.color.gray));
                        int likeCount1=Integer.parseInt(fullPost.getLike());
                        //Toast.makeText(context, "Unchecked1", Toast.LENGTH_SHORT).show();
                        likeCount1=likeCount1-1;
                       likeList.setText(likeCount1+" Like");
                    }
                    else
                    {
                        Log.v("isAlreadyLiked2",isAlreadyLiked+" ");
                        //Toast.makeText(context, "Unchecked2", Toast.LENGTH_SHORT).show();
                       likeList.setTextColor(getColor(R.color.gray));
                        likeList.setText(fullPost.getLike()+" Like");
                    }


                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/like_post",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

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

                        params.put("likeable_id",fullPost.getShared_id()+"");
                        params.put("likeable_type","users_post_share");
//
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }


        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/comment",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike",s);
                                SharedPreferences mPrefs =getApplicationContext().getSharedPreferences("User",MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = mPrefs.getString("MyObject", "");
                                UserClass userClass = gson.fromJson(json, UserClass.class);
                                commentTime.setText("0 minutes ago");
                                commentDescription.setText( written_comment_body.getText().toString());
                                Glide.with(getApplicationContext()).load("https://archsqr.in/"+userClass.getProfile())
                                        .into(commentProfileImageUrl);
                                commentUserName.setText(userClass.getUser_name());
                                news_comment_card.setVisibility(View.VISIBLE);
                                database= FirebaseDatabase.getInstance();
                                ref = database.getReference();
//                                PushNotificationClass notificationClass=new PushNotificationClass(userClass.getUserId(),userClass.getProfile(),newsFeedStatuses.get(getAdapterPosition()).getPostId(),newsFeedStatuses.get(getAdapterPosition()).getPostTitle(),newsFeedStatuses.get(getAdapterPosition()).getShortDescription(),"Commented",userClass.getUser_name()+" commented on your post");
//                                ref.child("Notifications").child(newsFeedStatuses.get(getAdapterPosition()).getUserId()+"").setValue(notificationClass);
                                written_comment_body.setText("");

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

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

                        params.put("commentable_id",fullPost.getShared_id()+"");
                        params.put("comment_text",written_comment_body.getText().toString());

                        //returning parameters
                        return params;
                    }
                };

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        });
        full_post_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenuInflater().inflate(R.menu.delete_fullpost_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){


                            case R.id.editPost:
                                if(fullPost.getType().equals("design"))
                                {
                                    startActivity(new Intent(FullPostActivity.this,DesignActivity.class));
                                }
                                else if(fullPost.getType().equals("article"))
                                {
                                    startActivity(new Intent(FullPostActivity.this,ArticleActivity.class));
                                }
                                else if(fullPost.getType().equals("status"))
                                {

                                }
                                break;
//                                Intent i = new Intent(context, FullPostActivity.class);
//                                context.startActivity(i);
                            case R.id.deletePost:
                                DeletePost(fullPost.getUser_post_id()+"",fullPost.getShared_id()+"",fullPost.getIs_shared());
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }

    public void DeletePost(final String  user_post_id, final String  id, final String is_shared) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://archsqr.in/api/delete_post",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike",s);
                        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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
                params.put("users_post_id",user_post_id+"");
                params.put("id",id+"");
                params.put("is_shared",is_shared+"");
//
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void FetchFullDeatailsFromServer(String slug) {

        StringRequest myReq = new StringRequest(Request.Method.GET, "http://archsqr.in/api/post-detail/testing-article-7404",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("posts");
                            FullPost fullPost = new FullPost(jsonObjectFullPost);
                            postTitle.setText(fullPost.getTitle());
                            postDescription.setText(fullPost.getShort_description());
                            postTag.setText(fullPost.getType());
                            red_authName.setText(fullPost.getAuthName());
                            red_postTime.setText(ConvertTime(fullPost.getUpdated_at()) + "");
                            Glide.with(getApplicationContext()).load("https://archsqr.in/" + fullPost.getAuthImageUrl())
                                    .into(red_authImage);
                            editor.render(fullPost.getDescription());
//                            red_postTime.setText(fullPost.getTitle());
//                            postTitle.setText(fullPost.getTitle());
//                            postTitle.setText(fullPost.getTitle());


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


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(myReq);

    }
    public static String changedHeaderHtml(String htmlText) {

        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";

        String closedTag = "</body></html>";
        String changeFontHtml = head + htmlText + closedTag;
        return changeFontHtml;
    }

    public long ConvertTime(String dtc) {
        // String dtc = messageClassArrayList.get(position).getUpdatedAt();
        Log.v("dtc", dtc);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
        Log.v("sdf1", sdf1.toString());
        Log.v("sdf2", sdf2.toLocalizedPattern());
        Date date = null;
        try {
            date = sdf1.parse(dtc);
            String newDate = sdf2.format(date);
            Log.v("date", date + "");
            System.out.println(newDate);
            Log.e("Date", newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        long today = System.currentTimeMillis();

        long diff = today - thatDay.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);
        return days;

    }
}