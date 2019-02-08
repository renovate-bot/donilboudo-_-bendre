package com.admedia.bendre.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    private Context context;

    MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals("http://www.admedia-technologies.com/"))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
