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

    // TODO if preferences are not available, the auth user info should be
    // downloaded, instead of using default values
    public static User getUserFromPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String userId = settings.getString(USER_ID, "default_id");
        String userName = settings.getString(USER_NAME, "default_name");
        String userEmail = settings.getString(USER_EMAIL, "default_email");
        String userType = settings.getString(USER_TYPE, "default_type");
        String userCompany = settings.getString(USER_COMPANY, "default_company");

       return new User(userId, userName, userEmail, userType, userCompany);
    }
}
