package com.uiresource.messenger.recylcerchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.uiresource.messenger.R;

public class HolderYoutube extends RecyclerView.ViewHolder {

    private TextView time;
    private WebView webView;

    TextView textWaveTitle;
    ImageView playButton;
    ImageView imageViewItems;
    com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView youTubePlayerView;//package diverso com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;


    public HolderYoutube(View v) {
        super(v);
        time = (TextView) v.findViewById(R.id.tv_time);
        webView = (WebView) v.findViewById(R.id.webView);

        textWaveTitle = (TextView) v.findViewById(R.id.textViewTitle);
        playButton = (ImageView) v.findViewById(R.id.btnPlay);
        imageViewItems = (ImageView) v.findViewById(R.id.imageViewItem);
        youTubePlayerView = (com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView) v.findViewById(R.id.youtube_view);

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

    public ImageView getImageViewItems() {
        return imageViewItems;
    }

    public void setImageViewItems(ImageView imageViewItems) {
        this.imageViewItems = imageViewItems;
    }

    public TextView getTextWaveTitle() {
        return textWaveTitle;
    }

    public void setTextWaveTitle(TextView textWaveTitle) {
        this.textWaveTitle = textWaveTitle;
    }

    public YouTubePlayerView getYouTubePlayerView() {
        return youTubePlayerView;
    }

    public void setYouTubePlayerView(YouTubePlayerView youTubePlayerView) {
        this.youTubePlayerView = youTubePlayerView;
    }
}
