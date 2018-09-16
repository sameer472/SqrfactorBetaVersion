package com.hackerkernel.user.sqrfactor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class following_user implements Serializable {


    private JSONObject jsonObject;
    private int id,from_user,to_user,active,portfolioPost_count;
    private String portfolioCount,postCount;
    private String created_at,updated_at,slug,user_name,name,first_name,last_name,profile,mobile_number,email,mobile_verify,email_verify
            ,type,user_type,city,state,country;


    public String getPortfolioCount() {
        return portfolioCount;
    }

    public void setPortfolioCount(String portfolioCount) {
        this.portfolioCount = portfolioCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getPostCount() {
        return postCount;
    }

    public following_user(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
        try {
            this.id=jsonObject.getInt("id");

            this.slug=jsonObject.getString("slug");
            this.name=jsonObject.getString("name");
            this.first_name=jsonObject.getString("first_name");
            this.last_name=jsonObject.getString("last_name");
            this.user_name=jsonObject.getString("user_name");
            this.profile=jsonObject.getString("profile");
            JSONArray portfolioPost=jsonObject.getJSONArray("portfolio_post");
            if(portfolioPost!=null)
            {
                this.portfolioCount=portfolioPost.length()+" ";
            }

            else {
                this.portfolioCount=0+" ";
            }
            JSONArray post=jsonObject.getJSONArray("posts");
            if(post!=null)
            {
                this.postCount=post.length()+" ";
            }
            else {
                this.postCount=0+" ";
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public int getPortfolioPost_count() {
        return portfolioPost_count;
    }

    public void setPortfolioPost_count(int portfolioPost_count) {
        this.portfolioPost_count = portfolioPost_count;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_user() {
        return from_user;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_verify() {
        return mobile_verify;
    }

    public void setMobile_verify(String mobile_verify) {
        this.mobile_verify = mobile_verify;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
