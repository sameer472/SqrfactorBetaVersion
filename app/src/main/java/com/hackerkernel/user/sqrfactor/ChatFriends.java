package com.hackerkernel.user.sqrfactor;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChatFriends implements Serializable {
    public String userName,userProfile;
    public int userID;
    public transient JSONObject jsonObject=null;
    public String isOnline;
    public String lastSeen;
    public String name;

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public ChatFriends() {

    }
    public ChatFriends(String userName, String userProfile, int userID) {
        this.userProfile = userProfile;
        this.userName = userName;
        this.userID = userID;

    }

    public ChatFriends(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {

            this.userID = jsonObject.getInt("id");
            this.userName = jsonObject.getString("first_name") +" "+jsonObject.getString("last_name");
            this.name=jsonObject.getString("name");
            // Log.v("name",firstName);
            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
//           if(firstName==null) {
//               this.userName = jsonObject.getString("name");
//               Log.v("name2", jsonObject.getString("name"));
//           }
            this.userProfile = jsonObject.getString("profile");



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}