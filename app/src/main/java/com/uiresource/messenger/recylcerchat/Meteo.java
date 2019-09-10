package com.uiresource.messenger.recylcerchat;

import android.graphics.drawable.Drawable;

public class Meteo {

public int temp;
public String ora;
public Drawable imgURL;

public Meteo(int temp, String ora, Drawable img){
    this.imgURL = img;
    this.ora = ora;
    this.temp = temp;
}


}
