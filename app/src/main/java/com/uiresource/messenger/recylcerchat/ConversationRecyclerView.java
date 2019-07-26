package com.uiresource.messenger.recylcerchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.uiresource.messenger.Chat;
import com.uiresource.messenger.PreferenceData;
import com.uiresource.messenger.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.uiresource.messenger.WebActivity;

import org.json.JSONObject;


public class ConversationRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<ChatData> items;
    private Context mContext;

    private final int DATE = 0, YOU = 1, ME = 2, NEWS = 3, METEO = 4 ,SPOTIFY = 5, YOUTUBE = 6, SPOTIFY_SINGLE_TRACK = 7, LOGGING = 8;

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

    public List<ChatData> getItems() {
        return items;
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
        }else if (items.get(position).getType().equals("7")) {
            return SPOTIFY_SINGLE_TRACK;
        }else if (items.get(position).getType().equals("8")) {
            return LOGGING;
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
            case NEWS:
                View vn = inflater.inflate(R.layout.layout_holder_news, viewGroup, false);
                viewHolder = new HolderNews(vn);
                break;
            case METEO:
                View v4 = inflater.inflate(R.layout.layout_meteo, viewGroup, false);
                viewHolder = new holderMeteo(v4);
                break;
            case SPOTIFY:
                View v5 = inflater.inflate(R.layout.layout_spotify_you, viewGroup, false);
                viewHolder = new HolderSpotify(v5);
                break;
            case YOUTUBE:
                View v6 = inflater.inflate(R.layout.layout_youtube_you, viewGroup, false);
                viewHolder = new HolderYoutube(v6);
                break;
            case SPOTIFY_SINGLE_TRACK:
                View v7 = inflater.inflate(R.layout.layout_spotify_single_track, viewGroup, false);
                viewHolder = new HolderSpotifySingleTrack(v7);
                break;
            case LOGGING:
                View v8 = inflater.inflate(R.layout.layout_holder_logging, viewGroup, false);
                viewHolder = new HolderLogging(v8);
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
            case SPOTIFY_SINGLE_TRACK:
                HolderSpotifySingleTrack vh7 = (HolderSpotifySingleTrack) viewHolder;
                configureViewHolder7(vh7, position);
                break;
            case NEWS:
                HolderNews vhn = (HolderNews) viewHolder;
                configureViewHolderNews(vhn, position);
                break;
            case METEO:
                holderMeteo vh4 = (holderMeteo) viewHolder;
                configureViewHolder4(vh4,position);
                break;
            case LOGGING:
                HolderLogging vh8 = (HolderLogging) viewHolder;
                configureViewHolder8(vh8, position);
                break;
            default:
                HolderMe vh = (HolderMe) viewHolder;
                configureViewHolder3(vh, position);
                break;
        }
    }

    //Logging
    private void configureViewHolder8(HolderLogging vh1, int position) {

        //Se viene premuto il pulsante No
        vh1.getBtnNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = PreferenceData.getLoggedInEmailUser(view.getContext());
                String domanda = "";
                String risposta = "";
                int id = items.get(position).getIdItem();
                for (ChatData chatItem:items) {
                    if (chatItem.getIdItem() == id && !chatItem.getType().equalsIgnoreCase("8")){

                        if (chatItem.getType().equalsIgnoreCase("1")){
                            if (chatItem.getAnswerLog() != null){
                                risposta = chatItem.getAnswerLog();
                            }else {
                                risposta = chatItem.getText();
                            }

                        }else if (chatItem.getType().equalsIgnoreCase("2")){
                            domanda = chatItem.getText();
                        }else {
                            if (chatItem.getAnswerLog() != null){
                                risposta = chatItem.getAnswerLog();
                            }
                        }

                    }
                }
                //Log.i("STAMPA",id + " " + email + " " +domanda + " " + risposta);
                LoggingBackground loggingBackground = new LoggingBackground();
                loggingBackground.execute(domanda,risposta,email,"No");

                vh1.getBtnSi().setEnabled(false);
                vh1.getBtnNo().setEnabled(false);
            }
        });

        //Se viene premuto il pulsante Si
        vh1.getBtnSi().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = PreferenceData.getLoggedInEmailUser(view.getContext());
                String domanda = "";
                String risposta = "";
                int id = items.get(position).getIdItem();
                for (ChatData chatItem:items) {
                    if (chatItem.getIdItem() == id && !chatItem.getType().equalsIgnoreCase("8")){

                        if (chatItem.getType().equalsIgnoreCase("1")){
                            if (chatItem.getAnswerLog() != null){
                                risposta = chatItem.getAnswerLog();
                            }else {
                                risposta = chatItem.getText();
                            }

                        }else if (chatItem.getType().equalsIgnoreCase("2")){
                            domanda = chatItem.getText();
                        }else {
                            if (chatItem.getAnswerLog() != null){
                                risposta = chatItem.getAnswerLog();
                            }
                        }

                    }
                }
                //Log.i("STAMPA",id + " " + email + " " +domanda + " " + risposta);
                LoggingBackground loggingBackground = new LoggingBackground();
                loggingBackground.execute(domanda,risposta,email,"Si");

                vh1.getBtnSi().setEnabled(false);
                vh1.getBtnNo().setEnabled(false);

            }
        });


    }

    private void configureViewHolderNews(final HolderNews vh1, int position) {
        final String url = items.get(position).getText();
        //Log.w(TAG,url);
        vh1.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("url",url);
                view.getContext().startActivity(intent);
            }
        });
        vh1.setImgBack(items.get(position).getImg());
        vh1.getChatText().setText(items.get(position).getText());

    }

    private void configureViewHolder4(holderMeteo vh1, int position) {

        ArrayList<Meteo> listMeteo = items.get(position).list;
        vh1.init(listMeteo);

    }

    private void configureViewHolder7(HolderSpotifySingleTrack vh1, int position) {


        vh1.getChatText().setText(items.get(position).getText());

        vh1.getChatText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = items.get(position).getText();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            }
        });


    }


    private void configureViewHolder5(HolderSpotify vh1, int position) {
        onBindSpotify(vh1, position);

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

    public void onBindSpotify(HolderSpotify vh1, int position){


        String url = items.get(position).getText();//Url della canzone

        items.get(position).avvio(mContext,url,vh1);


    }

    public void onBindYoutube(HolderYoutube vh1, int position) {

        String url = items.get(position).getText();//Url del video
        String idUrl = url.substring(29);//Id del video

        Log.i("url", idUrl);

        final ChatData mYoutubeVideo = new ChatData();
        mYoutubeVideo.setId(1l);
        mYoutubeVideo.setImageUrl("https://img.youtube.com/vi/" + idUrl + "/mqdefault.jpg");//Prendo l'immagine(thumbnail) del video
        mYoutubeVideo.setTitle(mContext.getResources().getString(R.string.videoRichiestoStr));
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
