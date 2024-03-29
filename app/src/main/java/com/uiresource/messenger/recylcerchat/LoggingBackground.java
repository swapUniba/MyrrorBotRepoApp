package com.uiresource.messenger.recylcerchat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LoggingBackground extends AsyncTask<String, Void, ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String mess = strings[0];//Domanda dell'utente
        String risposta = strings[1];//Risposta
        String email = strings[2];//Email
        String rate = strings[3];//Rate

        String timestampStart = strings[4];
        String timestampEnd = strings[5];


        String flag = "" ;


        String result = "";
        String urlString = "http://90.147.102.243:8080/php/log.php";

        try {

            //Imposto parametri per la connessione
            URL url = new URL(urlString);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);


            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));

            String data = "";

            try{
                flag = strings[6];


                //Stringa di output
                data = URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(risposta), "UTF-8")
                        +"&&rating="+URLEncoder.encode(rate,"UTF-8")+"&&quest="+URLEncoder.encode(mess,"UTF-8")
                        +"&&email="+URLEncoder.encode(email,"UTF-8")
                        +"&&timestampStart="+URLEncoder.encode(timestampStart,"UTF-8")
                        +"&&timestampEnd="+URLEncoder.encode(timestampEnd,"UTF-8")
                        +"&&flag="+URLEncoder.encode(flag,"UTF-8");

                Log.i("flag",flag);


            }catch (ArrayIndexOutOfBoundsException e){

                //Stringa di output
                data = URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(risposta), "UTF-8")
                        +"&&rating="+URLEncoder.encode(rate,"UTF-8")+"&&quest="+URLEncoder.encode(mess,"UTF-8")
                        +"&&email="+URLEncoder.encode(email,"UTF-8")
                        +"&&timestampStart="+URLEncoder.encode(timestampStart,"UTF-8")
                        +"&&timestampEnd="+URLEncoder.encode(timestampEnd,"UTF-8");

            }




            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));

            String line = "";

            while ((line = reader.readLine()) != null) {
                result += line;


                reader.close();
                ips.close();
                http.disconnect();

            }

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(result);
            arrayList.add(mess);

            return arrayList;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(result);
        arrayList.add(mess);

        return arrayList;
    }
}
