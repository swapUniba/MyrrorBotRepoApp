package com.uiresource.messenger.recylcerchat;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uiresource.messenger.R;

import java.util.ArrayList;

public class holderMeteo  extends RecyclerView.ViewHolder {
   TextView ora1,temp1;
   ImageView icon1;
    TextView ora2,temp2;
    ImageView icon2;
    TextView ora3,temp3;
    ImageView icon3;
    TextView ora4,temp4;
    ImageView icon4;
    TextView ora5,temp5;
    ImageView icon5;
    CardView card1,card2,card3,card4,card5;


   public ArrayList<Meteo> arr;


    public holderMeteo(View itemView) {
        super(itemView);

        icon1 = (ImageView) itemView.findViewById(R.id.imageViewWheater1);
        ora1 = (TextView) itemView.findViewById(R.id.textViewdate1);
        temp1 = (TextView) itemView.findViewById(R.id.textViewtemp1);

        icon2 = (ImageView) itemView.findViewById(R.id.imageViewWheater2);
        ora2 = (TextView) itemView.findViewById(R.id.textViewdate2);
        temp2 = (TextView) itemView.findViewById(R.id.textViewtemp2);

        icon3 = (ImageView) itemView.findViewById(R.id.imageViewWheater3);
        ora3 = (TextView) itemView.findViewById(R.id.textViewdate3);
        temp3 = (TextView) itemView.findViewById(R.id.textViewtemp3);

        icon4 = (ImageView) itemView.findViewById(R.id.imageViewWheater4);
        ora4 = (TextView) itemView.findViewById(R.id.textViewdate4);
        temp4 = (TextView) itemView.findViewById(R.id.textViewtemp4);

        icon5 = (ImageView) itemView.findViewById(R.id.imageViewWheater5);
        ora5 = (TextView) itemView.findViewById(R.id.textViewdate5);
        temp5 = (TextView) itemView.findViewById(R.id.textViewtemp5);

        card1 = (CardView)itemView.findViewById(R.id.card1);
        card2 = (CardView)itemView.findViewById(R.id.card2);
        card3 = (CardView)itemView.findViewById(R.id.card3);
        card4 = (CardView)itemView.findViewById(R.id.card4);
        card5 = (CardView)itemView.findViewById(R.id.card5);


    }

    public void init(ArrayList<Meteo> list){


        int size = list.size();

        if (list.get(0) != null){
            Meteo m = list.get(0);
            icon1.setBackground(m.imgURL);
            ora1.setText(m.ora);
            temp1.setText(String.valueOf(m.temp)+"°");
        }

        if (size > 1){
            Meteo m = list.get(1);
            icon2.setBackground(m.imgURL);
            ora2.setText(m.ora);
            temp2.setText(String.valueOf(m.temp)+"°");
        }else {
            card2.setVisibility(View.GONE);
        }

        if (size > 2){
            Meteo m = list.get(2);
            icon3.setBackground(m.imgURL);
            ora3.setText(m.ora);
            temp3.setText(String.valueOf(m.temp)+"°");
        }else {
            card3.setVisibility(View.GONE);
        }


        if (size > 3){
            Meteo m = list.get(3);
            icon4.setBackground(m.imgURL);
            ora4.setText(m.ora);
            temp4.setText(String.valueOf(m.temp)+"°");
        }else {
            card4.setVisibility(View.GONE);
        }

        if (size > 4){
            Meteo m = list.get(4);
            icon5.setBackground(m.imgURL);
            ora5.setText(m.ora);
            temp5.setText(String.valueOf(m.temp)+"°");
        }else {
            card5.setVisibility(View.GONE);
        }

    }





}
