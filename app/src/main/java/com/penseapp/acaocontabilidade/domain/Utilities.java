package com.penseapp.acaocontabilidade.domain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by unity on 22/11/16.
 */

public class Utilities {
    private static final String SITE_ACAO = "http://acaocontabilidade.com";
    private static final String SITE_ACAO_FACEBOOK = "https://www.facebook.com/acaocontabilidade/";

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void goToAcaoWebsite(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(SITE_ACAO));
        context.startActivity(intent);
    }

    public static void goToAcaoFacebookWebsite(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(SITE_ACAO_FACEBOOK));
        context.startActivity(intent);
    }

    public static void hideKeyboard(AppCompatActivity appCompatActivity) {
        View view = appCompatActivity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
