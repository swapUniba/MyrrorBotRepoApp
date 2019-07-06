package com.uiresource.messenger.recylcerchat;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import android.os.Handler;
import android.os.Message;


import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Result;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.uiresource.messenger.Chat;
import com.uiresource.messenger.R;


import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class HolderSpotify extends RecyclerView.ViewHolder {

    TextView statoPlayer;
    ImageView imgSpotify;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    Button playBtn;
    Button playSkip;

    Track track;
    SpotifyService spotify;


    private SpotifyAppRemote mSpotifyAppRemote;

    public HolderSpotify(View v) {
        super(v);

        imgSpotify = (ImageView) v.findViewById(R.id.imgSpotify);

        playBtn = (Button) v.findViewById(R.id.playBtn);
        //elapsedTimeLabel = (TextView) v.findViewById(R.id.elapsedTimeLabel);
        //remainingTimeLabel = (TextView) v.findViewById(R.id.remainingTimeLabel);

        playSkip = (Button) v.findViewById(R.id.playSkip);

        statoPlayer = (TextView) v.findViewById(R.id.statoPlayer);
        statoPlayer.setText("1"); //Stato del player = IN RIPRODUZIONE
        playBtn.setBackgroundResource(R.drawable.stop); //Mostro il button per la pausa


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playBtnClick(v);
            }
        });

        playSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipBtnClick(v);
            }
        });

        //SPOTIFY WEB API PER ANDROID ----- USI FUTURI
        /*SpotifyApi api = new SpotifyApi();
        api.setAccessToken(Chat.accessToken);

        spotify = api.getService();

        setSpotify(spotify);

        spotify.getTrack("2J9TGb5CRT4omfAgnKmXn5", new Callback<kaaes.spotify.webapi.android.models.Track>() {
            @Override
            public void success(kaaes.spotify.webapi.android.models.Track track, Response response) {
                Log.d("Id traccia", track.id);
                Log.d("Nome traccia", track.name);

                // long minutes = (milliseconds / 1000) / 60;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(track.duration_ms);
                // long seconds = (milliseconds / 1000);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(track.duration_ms);

                Log.d("Durata traccia", String.valueOf(minutes) + " minuti e " + String.valueOf(minutes) + " secondi");
                Log.d("Uri", track.uri);

                setTrack(track);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Track failure", error.toString());
            }
        });

        //spotify.getPlaylistTracks("37i9dQZF1DWTMYgB8TqtmR",new C);
        spotify.getPlaylistTracks("GianGae", "37i9dQZF1DWTMYgB8TqtmR", new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                Log.i("dsfs",playlistTrackPager.items.get(0).track.uri);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/




        // Media Player

        /*mp = MediaPlayer.create(this,); //Lo prende dalla cartella raw dove sta il file .mp3
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();*/


                // Position Bar
        /*positionBar = (SeekBar) v.findViewById(R.id.positionBar);

        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mSpotifyAppRemote.getPlayerApi().seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );*/


                // Volume Bar
        /*volumeBar = (SeekBar) v.findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );*/

                // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }




    public void playBtnClick(View view) {

        mSpotifyAppRemote = getmSpotifyAppRemote();//Prendo il collegamento del player di Spotify



        if (statoPlayer.getText().toString().equalsIgnoreCase("0")){//Faccio partire la canzone
            Log.i("Player", "IN RIPRODUZIONE");
            mSpotifyAppRemote.getPlayerApi().resume();
            playBtn.setBackgroundResource(R.drawable.stop); //Mostro il button per la pausa
            statoPlayer.setText("1");
        }else {//Metto pausa
            mSpotifyAppRemote.getPlayerApi().pause();
            playBtn.setBackgroundResource(R.drawable.play); //Mostro il button per la pausa
            Log.i("Player", "IN PAUSA");
            statoPlayer.setText("0");
        }



    }

    public void skipBtnClick(View view) {

        mSpotifyAppRemote = getmSpotifyAppRemote();//Prendo il collegamento del player di Spotify
        mSpotifyAppRemote.getPlayerApi().skipNext();

    }

    public SpotifyAppRemote getmSpotifyAppRemote() {
        return mSpotifyAppRemote;
    }

    public void setmSpotifyAppRemote(SpotifyAppRemote mSpotifyAppRemote) {
        this.mSpotifyAppRemote = mSpotifyAppRemote;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public SpotifyService getSpotify() {
        return spotify;
    }

    public void setSpotify(SpotifyService spotify) {
        this.spotify = spotify;
    }
}
