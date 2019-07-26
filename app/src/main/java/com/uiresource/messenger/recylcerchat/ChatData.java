package com.uiresource.messenger.recylcerchat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubePlayer;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.uiresource.messenger.Chat;
import com.uiresource.messenger.InstallaSpotify;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChatData {
    String type, text, time, spiegazione, spiegazioneNews;

    int idItem; //id dell'item

    public void setSpiegazione(String spiegazione) {
        this.spiegazione = spiegazione;
    }

    public String getSpiegazione() {
        return spiegazione;
    }

    public void setSpiegazioneNews(String spiegazioneNews) {
        this.spiegazioneNews = spiegazioneNews;

    }

    public String getSpiegazioneNews() {
        return spiegazioneNews;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdItem() {
        return idItem;
    }

    //METEO
    public ArrayList<Meteo> list ;

    //NEWS
    public String img,url;

    public void setImg(String s){
        this.img = s;
    }

    public String getImg(){
        return img;
    }

    public String answerLog;

    public String getAnswerLog() {
        return answerLog;
    }

    //YOUTUBE
    private String title;
    private Long id;
    private String videoId;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    //SPOTIFY
    private static final String CLIENT_ID = "7a17bf0bdb234ef2870a58f22f26e8bd";
    private static final String REDIRECT_URI = "com.uiresource.messenger://callback"; //Application ID della app
    private SpotifyAppRemote mSpotifyAppRemote;
    int flag; //0 per le canzoni, 1 per le playlist

    // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
    private static final int REQUEST_CODE = 1337;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getRedirectUri() {
        return REDIRECT_URI;
    }

    public SpotifyAppRemote getmSpotifyAppRemote() {
        return mSpotifyAppRemote;
    }

    public void setmSpotifyAppRemote(SpotifyAppRemote mSpotifyAppRemote) {
        this.mSpotifyAppRemote = mSpotifyAppRemote;
    }

    public void avvio(Context context, String urlSpotify, HolderSpotify holderSpotify){

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();


        if (SpotifyAppRemote.isSpotifyInstalled(context) == true){
            SpotifyAppRemote.connect(context, connectionParams,
                    new Connector.ConnectionListener() {

                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("MainActivity", "Connected! Yay!");

                            // Now you can start interacting with App Remote
                            connected(urlSpotify,holderSpotify);


                        }

                        public void onFailure(Throwable throwable) {
                            Log.e("MyActivity", throwable.getMessage(), throwable);

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });
        }else {

            //Nascondo le componenti
            holderSpotify.playSkip.setVisibility(View.INVISIBLE);
            holderSpotify.playBtn.setVisibility(View.INVISIBLE);
            holderSpotify.imgSpotify.setVisibility(View.INVISIBLE);

            //Le disattivo
            holderSpotify.playSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Niente
                }
            });

            holderSpotify.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Niente
                }
            });

            //Avvio l'intent per scaricare l'app di Spotify
            Intent i = new Intent(context, InstallaSpotify.class);
            context.startActivity(i);
        }


    }



    private void connected(String urlSpotify,HolderSpotify holderSpotify) {

        /*holderSpotify.getSpotify().getTrack("2J9TGb5CRT4omfAgnKmXn5", new Callback<kaaes.spotify.webapi.android.models.Track>() {
            @Override
            public void success(kaaes.spotify.webapi.android.models.Track track, Response response) {
                Log.d("Id traccia", track.id);
                Log.d("Nome traccia", track.name);
                Log.d("Durata traccia", String.valueOf(track.duration_ms));
                Log.d("Uri", track.uri);

                holderSpotify.setTrack(track);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Track failure", error.toString());
            }

        });*/

        kaaes.spotify.webapi.android.models.Track trackSpotify = holderSpotify.getTrack();

        if (this.getFlag() == 0){//Canzoni

            /*String url = urlSpotify.substring(31);//Id canzone, es. 37i9dQZF1DWTh5RC6ek3nb
            Log.i("url",url);

            // Play a playlist
            mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + url);
            Log.i("uri track",trackSpotify.preview_url);

            // Subscribe to PlayerState
            mSpotifyAppRemote.getPlayerApi()
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                        final Track track = playerState.track;
                        if (track != null) {
                            Log.d("MainActivity", track.name + " by " + track.artist.name);
                        }
                    });

            holderSpotify.setmSpotifyAppRemote(mSpotifyAppRemote);//Imposto il player di spotify*/



        }else if(this.getFlag() == 1){//Playlist

            String url = "";
            if (urlSpotify.contains("playlist")){
                url = urlSpotify.substring(40);//Id playlist, es. 37i9dQZF1DWTh5RC6ek3nb

                Log.i("url",url);


                // Play a playlist
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + url);

                // Subscribe to PlayerState
                mSpotifyAppRemote.getPlayerApi()
                        .subscribeToPlayerState()
                        .setEventCallback(playerState -> {
                            final Track track = playerState.track;
                            if (track != null) {
                                Log.d("MainActivity", track.name + " by " + track.artist.name);
                                holderSpotify.txtTitolo.setText(track.name);
                                holderSpotify.txtNomeArtista.setText(track.artist.name);
                            }
                        });

                holderSpotify.setmSpotifyAppRemote(mSpotifyAppRemote);//Imposto il player di spotify
            }else {
                url = urlSpotify.substring(37);//Id album, es. 37i9dQZF1DWTh5RC6ek3nb

                Log.i("url",url);


                // Play a playlist
                mSpotifyAppRemote.getPlayerApi().play("spotify:album:" + url);

                // Subscribe to PlayerState
                mSpotifyAppRemote.getPlayerApi()
                        .subscribeToPlayerState()
                        .setEventCallback(playerState -> {
                            final Track track = playerState.track;
                            if (track != null) {
                                Log.d("MainActivity", track.name + " by " + track.artist.name);
                                holderSpotify.txtTitolo.setText(track.name);
                                holderSpotify.txtNomeArtista.setText(track.artist.name);
                            }
                        });

                holderSpotify.setmSpotifyAppRemote(mSpotifyAppRemote);//Imposto il player di spotify
            }



        }



    }

    @Override
    public String toString() {
        return answerLog;
    }
}
