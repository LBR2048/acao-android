package com.penseapp.acaocontabilidade.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 10/03/17.
 */

public class Preferences {

    public static void saveUserPreferences(Context context, User user) {
        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        settings.edit()
                .putString("userId", user.getKey())
                .putString("userName", user.getName())
                .putString("userEmail", user.getEmail())
                .putString("userType", user.getType())
                .putString("userCompany", user.getCompany())
                .apply();
    }
}
