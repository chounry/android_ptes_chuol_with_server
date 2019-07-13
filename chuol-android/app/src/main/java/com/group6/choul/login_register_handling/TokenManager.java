package com.group6.choul.login_register_handling;

import android.content.SharedPreferences;

public class TokenManager {
    public SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public boolean isLogin(TokenManager tokenManager){
        if(tokenManager.getToken().getAccessToken() != null)return true;
        return false;
    }

   public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null ){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token){
        editor.putString("ACCESS_TOKEN", token.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN", token.getRefreshToken()).commit();
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN");
        editor.remove("REFRESH_TOKEN");
        editor.remove("USER_ID");
        editor.remove("USERNAME");
        editor.remove("EMAIL");
        editor.apply();
    }

    public void saveUserInfo(int user_id, String username, String email){
        editor.putInt("USER_ID",user_id);
        editor.putString("USERNAME",username);
        editor.putString("EMAIL",email);
        editor.apply();
    }


    public int getUserId(){
        return prefs.getInt("USER_ID",0);
    }
    public String getEmail(){
        return prefs.getString("EMAIL",null);
    }

    public String getUserName(){
        return prefs.getString("USERNAME",null);
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN",null));
        token.setRefreshToken(prefs.getString("REFRESH_TOKEN",null));
        return token;
    }
}
