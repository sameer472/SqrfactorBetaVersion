package com.hackerkernel.user.sqrfactor;

import java.io.Serializable;

public class PushNotificationClass implements Serializable {
   private String body;
   private long created_at;
   private String type;
   private from_user from_user;
   private post post;

    public PushNotificationClass(String body, long created_at,from_user from_user , post post,String type) {
        this.body = body;
        this.created_at = created_at;
        this.from_user=from_user;
        this.post= post;
        this.type = type;

    }

    public com.hackerkernel.user.sqrfactor.post getPost() {
        return post;
    }

    public void setPost(com.hackerkernel.user.sqrfactor.post post) {
        this.post = post;
    }

    public com.hackerkernel.user.sqrfactor.from_user getFrom_user() {
        return from_user;
    }

    public void setFrom_user(com.hackerkernel.user.sqrfactor.from_user from_user) {
        this.from_user = from_user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



