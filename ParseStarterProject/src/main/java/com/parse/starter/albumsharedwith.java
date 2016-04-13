package com.parse.starter;

import java.util.ArrayList;

/**
 * Created by Prithvi Macherla on 12/13/2015.
 */
public class albumsharedwith {
    String album;
    ArrayList<String> name;

    public albumsharedwith(String album, ArrayList<String> name) {
        this.album = album;
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }
}
