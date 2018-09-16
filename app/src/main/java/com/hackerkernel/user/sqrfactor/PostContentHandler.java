package com.hackerkernel.user.sqrfactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostContentHandler {
    Context context;
    String  content;
    LinearLayout linearLayout;
    TextView fullPostDescription;
    public PostContentHandler(Context context, String content , LinearLayout linearLayout,TextView fullPostDescription){
        this.context=context;
        this.content=content;
        this.linearLayout=linearLayout;
        this.fullPostDescription=fullPostDescription;

    }

    @SuppressLint("ResourceAsColor")
    public void setContentToView(){
        List<String> p = new ArrayList<>();
        List<String> src = new ArrayList<>();
        Document doc = Jsoup.parse(content);

        Elements elements = doc.getAllElements();

        for(Element element :elements){
            Tag tag = element.tag();

            if(tag.getName().equalsIgnoreCase("a")){
                String name  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des1",name);
                if(name.contains("span")||name.contains("<i>")||name.contains("<b>"))
                {
                    continue;
                }
                else {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(20);
                    textView.setTextColor(Color.RED);
                    textView.setText(name);
                    textView.setPadding(5, 0, 5, 0);
                    linearLayout.addView(textView);
                }

            }

            else if(tag.getName().equalsIgnoreCase("b")){
                String title  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des2",title);
                if(title.contains("&nbsp")||title.contains("href")||title.equals("<br>"))
                {
                    continue;
                }

                else {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(20);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(title);
                    textView.setPadding(8, 5, 8, 5);
                    linearLayout.addView(textView);
                    continue;
                }

            }

           else if(tag.getName().equalsIgnoreCase("p")){

                element.select("img").remove();
                String body= element.html();

                String[] parsedBody=body.split("\\.");
                StringBuilder builder = new StringBuilder();
                for(String s : parsedBody) {
                    Log.v("des3",s);
                    if(s.contains("&nbsp")||s.contains("<span")||s.contains("</span>")||s.contains("<br>"))
                    {
                        continue;
                    }
                    else
                    builder.append(s+".");
                }
                String str = builder.toString();
                if(body.contains("href")||body.equals("<br>")||body.contains("<b>"))
                {
                    continue;
                }
                else {
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tv=new TextView(context);
                    tv.setLayoutParams(lparams);
                    tv.setTextSize(16);
                    tv.setPadding(8, 0, 5, 0);
                    tv.setText(str);
                    linearLayout.addView(tv);
                    continue;
                }


            }
            else if (tag.getName().equalsIgnoreCase("img")){
                String url  = element.select("img").attr("src");
                Log.v("des4",url);

                final ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.getLayoutParams().height=getScreenWidth();
                        imageView.setAdjustViewBounds(true);
                        imageView.requestLayout();
                Glide.with(context).load(url)
                        .into(imageView);
                linearLayout.addView(imageView);
                continue;
            }
           else if (tag.getName().equalsIgnoreCase("iframe")){
                String url  = element.select("iframe").attr("src");
                Log.v("des5",url);

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = (FrameLayout) inflater.inflate(R.layout.fullpostwebview, null);
                WebView wv = (WebView)v.findViewById(R.id.webView1);
                WebSettings settings = wv.getSettings();
                settings.setJavaScriptEnabled(true);
                String[] stringId=url.split("/");
                String id=stringId[stringId.length-1];
                String src1="src="+'"'+"https://www.youtube.com/embed/"+id+'"';
                String html="<iframe width=\"100%\" height=\"200\" "+src1+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
                //String src1="src="+'"'+url+'"';
                // String html="<iframe width=\"100%\" height=\"200\" "+src1+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
                 wv.loadDataWithBaseURL(url,html, "text/html", "UTF-8", "");
                 v.getRootView();
                linearLayout.addView(v);
                v.setVisibility(View.VISIBLE);
                continue;
            }



        }
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}


