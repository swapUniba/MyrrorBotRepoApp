package com.uiresource.messenger.recylcerchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.uiresource.messenger.R;

public class HolderSpotify extends RecyclerView.ViewHolder {

    private TextView time;
    private WebView webView;

    public HolderSpotify(View v) {
        super(v);
        time = (TextView) v.findViewById(R.id.tv_time);
        webView = (WebView) v.findViewById(R.id.webView);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public WebView getWebView() {
        return webView;
    }
}
