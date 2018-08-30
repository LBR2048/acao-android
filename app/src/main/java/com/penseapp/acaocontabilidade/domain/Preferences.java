package com.penseapp.acaocontabilidade.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 10/03/17.
 */

public class Preferences {

    private static final String SETTINGS = "Settings";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_TYPE = "userType";
    private static final String USER_COMPANY = "userCompany";

    public static void saveUserPreferences(Context context, User user) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        settings.edit()
                .putString(USER_ID, user.getKey())
                .putString(USER_NAME, user.getName())
                .putString(USER_EMAIL, user.getEmail())
                .putString(USER_TYPE, user.getType())
                .putString(USER_COMPANY, user.getCompany())
                .apply();
    }

    public static User getUserFromPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String userId = settings.getString(USER_ID, "");
        String userName = settings.getString(USER_NAME, "");
        String userEmail = settings.getString(USER_EMAIL, "");
        String userType = settings.getString(USER_TYPE, "");
        String userCompany = settings.getString(USER_COMPANY, "");

       return new User(userId, userName, userEmail, userType, userCompany);
    }
}
