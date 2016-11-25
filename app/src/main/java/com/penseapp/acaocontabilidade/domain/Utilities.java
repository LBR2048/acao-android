package com.penseapp.acaocontabilidade.domain;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by unity on 22/11/16.
 */

public class Utilities {
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

}
