package com.example.tkw.newstwo.News;

import android.graphics.BitmapFactory;

import com.example.tkw.newstwo.CallBack.ImageCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetImage {

    private static volatile GetImage getImage;

    private GetImage(){};

    public static GetImage ReturnImage(){
        if(getImage == null){
            synchronized (GetImage.class) {
                if (getImage == null) {
                    getImage = new GetImage();
                }
            }
        }
        return getImage;
    }

    public void sendRequestWithHttpURLConnection(final String Url, final ImageCallBack imageCallBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL(Url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8 * 1000);
                    connection.setReadTimeout(8 * 1000);
                    InputStream in = connection.getInputStream();
                    imageCallBack.imageCallBack( BitmapFactory.decodeStream(in));//回调ImageCallBack
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
