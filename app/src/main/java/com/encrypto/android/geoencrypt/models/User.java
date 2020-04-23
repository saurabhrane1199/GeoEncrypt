package com.encrypto.android.geoencrypt.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class User implements Serializable {
    public String uid;
    public String name;
    public String email;
    public ArrayList<String> filesId;

    public User() {}

    public User(String uid, String name, String email){
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public User(String uid, String name, String email, ArrayList<String> files) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.filesId = files;
    }
}
