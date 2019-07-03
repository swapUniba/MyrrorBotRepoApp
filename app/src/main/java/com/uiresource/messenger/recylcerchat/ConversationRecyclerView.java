package com.uiresource.messenger.recylcerchat;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.uiresource.messenger.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;


public class ConversationRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<ChatData> items;
    private Context mContext;

    private final int DATE = 0, YOU = 1, ME = 2, NEWS = 3, METEO = 4 ,SPOTIFY = 5, YOUTUBE = 6;

    DisplayMetrics displayMetrics = new DisplayMetrics();


    // Provide a suitable constructor (depends on the kind of dataset)
    public ConversationRecyclerView(Context context, List<ChatData> items) {
        this.mContext = context;
        this.items = items;

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if (items.get(position).getType().equals("0")) {
            return DATE;
        } else if (items.get(position).getType().equals("1")) {
            return YOU;
        }else if (items.get(position).getType().equals("2")) {
            return ME;
        }else if (items.get(position).getType().equals("3")) {
            return NEWS;
        }else if (items.get(position).getType().equals("4")) {
            return METEO;
        }else if (items.get(position).getType().equals("5")) {
            return SPOTIFY;
        }else if (items.get(position).getType().equals("6")) {
            return YOUTUBE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case DATE:
                View v1 = inflater.inflate(R.layout.layout_holder_date, viewGroup, false);
                viewHolder = new HolderDate(v1);
                break;
            case YOU:
                View v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false);
                viewHolder = new HolderYou(v2);
                break;
            case SPOTIFY:
                View v5 = inflater.inflate(R.layout.layout_spotify_you, viewGroup, false);
                viewHolder = new HolderSpotify(v5);
                break;
            case YOUTUBE:
                View v6 = inflater.inflate(R.layout.layout_youtube_you, viewGroup, false);
                viewHolder = new HolderYoutube(v6);

                //return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_youtube_you, parent, false));

                break;
            default:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderMe(v);
                break;
        }
        return viewHolder;
    }
    public void addItem(List<ChatData> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case DATE:
                HolderDate vh1 = (HolderDate) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case YOU:
                HolderYou vh2 = (HolderYou) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case SPOTIFY:
                HolderSpotify vh5 = (HolderSpotify) viewHolder;
                configureViewHolder5(vh5, position);
                break;
            case YOUTUBE:
                HolderYoutube vh6 = (HolderYoutube) viewHolder;
                configureViewHolder6(vh6, position);
                break;
            default:
                HolderMe vh = (HolderMe) viewHolder;
                configureViewHolder3(vh, position);
                break;
        }
    }

    private void configureViewHolder5(HolderSpotify vh1, int position) {
        vh1.getTime().setText(items.get(position).getTime());
        vh1.getWebView().loadUrl(items.get(position).getText());


    }

    private void configureViewHolder6(HolderYoutube vh1, int position) {

        onBindYoutube(vh1,position);

    }

    private void configureViewHolder3(HolderMe vh1, int position) {
            vh1.getTime().setText(items.get(position).getTime());
            vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolder2(HolderYou vh1, int position) {
            vh1.getTime().setText(items.get(position).getTime());
            vh1.getChatText().setText(items.get(position).getText());
    }
    private void configureViewHolder1(HolderDate vh1, int position) {
            vh1.getDate().setText(items.get(position).getText());
    }

    public void onBindYoutube(HolderYoutube vh1, int position) {

        String url = items.get(position).getText();//Url del video
        String idUrl = url.substring(29);//Id del video

        Log.i("url", idUrl);

        final ChatData mYoutubeVideo = new ChatData();
        mYoutubeVideo.setId(1l);
        mYoutubeVideo.setImageUrl("https://img.youtube.com/vi/" + idUrl + "/mqdefault.jpg");//Prendo l'immagine(thumbnail) del video
        mYoutubeVideo.setTitle("Ecco qui il video richiesto 😀");
        mYoutubeVideo.setVideoId(idUrl);

        ((Activity) vh1.itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        if (mYoutubeVideo.getTitle() != null)
            vh1.textWaveTitle.setText(mYoutubeVideo.getTitle());

        if (mYoutubeVideo.getImageUrl() != null) {
            Glide.with(vh1.itemView.getContext())
                    .load(mYoutubeVideo.getImageUrl()).
                    apply(new RequestOptions().override(width - 36, 200))
                    .into(vh1.imageViewItems);
        }

        vh1.imageViewItems.setVisibility(View.VISIBLE);
        vh1.playButton.setVisibility(View.VISIBLE);
        vh1.youTubePlayerView.setVisibility(View.GONE);

        vh1.youTubePlayerView.enterFullScreen();
        vh1.youTubePlayerView.exitFullScreen();

        vh1.youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {

            }
        });


        vh1.playButton.setOnClickListener(view -> {
            vh1.imageViewItems.setVisibility(View.GONE);
            vh1.youTubePlayerView.setVisibility(View.VISIBLE);
            vh1.playButton.setVisibility(View.GONE);


            vh1.youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    initializedYouTubePlayer.loadVideo(mYoutubeVideo.getVideoId(), 0);
                }
            }), true);
        });
    }



}
