package com.admedia.bendre.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.admedia.bendre.R;

/**
 * Created by fabrice on 2017-05-28.
 */

public class MessageUtil {
    private static MessageUtil instance = null;

    private MessageUtil() {
    }

    public static MessageUtil getInstance() {
        if (instance == null)
        {
            instance = new MessageUtil();
        }

        return instance;
    }

    public void ToastMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0,0);
        View view = toast.getView();
        TextView textView = view.findViewById(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        view.setBackgroundResource(R.color.colorToast);
        toast.show();
    }

    public void SnackMessage(Activity activity, String message, int viewId) {
        if (activity != null && message != null)
        {
            Snackbar s = Snackbar.make(activity.findViewById(android.R.id.message), message, Snackbar.LENGTH_LONG);
            View snackBarView = s.getView();
            snackBarView.setBackgroundColor(activity.getResources().getColor(R.color.primary));
            s.show();
        }
    }
}
