package com.example.houserentproject;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private final SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public Session(Context context) {
        String SHARED_PREF_NAME = "POST_SESSION";
        prefs = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public void addToFavourite(String id){
        editor = prefs.edit();
        editor.putString(id,id);
        editor.apply();
    }

    public void removeFromFavourite(String id){
        editor = prefs.edit();
        editor.remove(id);
        editor.apply();
    }

    public boolean isFavourite(String id){
        String postId = prefs.getString(id,"");
        return !postId.equals("");
    }
}
