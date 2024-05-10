package com.example.appstart;

 import android.content.Context;
 import android.widget.Toast;

public class Utils {

    // Show a short toast message
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Show a long toast message
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}