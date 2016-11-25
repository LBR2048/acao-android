package com.penseapp.acaocontabilidade.login;

import java.util.regex.Pattern;

/**
 * Created by Cleusa on 24/09/2016.
 */

public class Utilities {

    public static boolean validateEmail(String email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return password.length() > 5;
    }
}