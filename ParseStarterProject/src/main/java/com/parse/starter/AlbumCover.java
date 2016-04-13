package com.parse.starter;

import com.parse.ParseACL;
import com.parse.ParseUser;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prithvi Macherla on 12/12/2015.
 */
public class AlbumCover {
    String name="";
    String url;
ArrayList<String> usersnames=new ArrayList<>();
    boolean privacy;
    public AlbumCover(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public AlbumCover(String url) {
        this.url = url;
    }

    public ArrayList<String> getUsersnames() {
        return usersnames;
    }

    public void setUsersnames(ArrayList<String> usersnames) {
        this.usersnames = usersnames;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
