package com.uiresource.messenger.recylcerchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uiresource.messenger.R;

import java.io.InputStream;

public class HolderNews extends RecyclerView.ViewHolder {
    private TextView time, chatText;
    private ImageView immagine;
    public View v;



    public HolderNews(View v) {
        super(v);
        immagine = (ImageView) v.findViewById(R.id.imageViewNews);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
        this.v = v;

    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getChatText() {
        return chatText;
    }

    public void setImgBack(String s){
        GetImageFromURL load = new GetImageFromURL();
        load.execute(s);

    }




    public ImageView getImg(){
        return immagine;
    }

    public void setTitle(TextView chatText) {
        this.chatText = chatText;

    }



    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap> {


        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            Bitmap bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            immagine.setImageBitmap(bitmap);
        }

    }



}
