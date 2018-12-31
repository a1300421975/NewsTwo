package com.example.tkw.newstwo.GetData;

import com.example.tkw.newstwo.CallBack.DateCallBack;
import com.example.tkw.newstwo.News.DateNews;
import com.example.tkw.newstwo.News.Stories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetStories {

    private static volatile GetStories getStories;

    private GetStories(){}

    public static GetStories ReturnStories(){
        if(getStories == null){
            synchronized (GetStories.class){
                if(getStories == null){
                    getStories = new GetStories();
                }
            }
        }
        return getStories;
    }

    public void sendRequestWithHttpURLConnection(final String Url, final DateCallBack dateCallBack){
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
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    parseJSONWithJSONObject(response.toString(), dateCallBack);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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

    public void parseJSONWithJSONObject(String jsonData, DateCallBack dateCallBack){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date1 = new Date();
        int dates = Integer.parseInt(simpleDateFormat.format(date1));
        try {
            JSONObject object = new JSONObject(jsonData);
            int date = object.getInt("date");   //获取日期

            JSONArray storiesJSONArray = object.getJSONArray("stories");
            List<Stories>  storiesList = new ArrayList<>();   //stories集合
            //添加stories成员
            for(int i = 0; i < storiesJSONArray.length();i++){
                JSONObject storiesObject = storiesJSONArray.getJSONObject(i);
                Stories stories = new Stories();
                stories.setId(storiesObject.getInt("id"));
                JSONArray imagesArray = storiesObject.getJSONArray("images");
                for(int j = 0;j < imagesArray.length();j++){
                    String imagesAddress = imagesArray.getString(j);
                    stories.setImages(imagesAddress);
                }

                //String imageAddress = storiesObject.getString("images");

                stories.setTitle(storiesObject.getString("title"));
                storiesList.add(stories);
            }
            DateNews.ReturnDateNews().addStories(storiesList);

            if(dates == date) {
                JSONArray topStoriesJSONArray = object.getJSONArray("top_stories");
                List<Stories> topStoriesList = new ArrayList<>();  //top_stories集合
                //添加stories成员
                for (int i = 0; i < topStoriesJSONArray.length(); i++) {
                    JSONObject topStoriesObject = topStoriesJSONArray.getJSONObject(i);
                    Stories topStories = new Stories();
                    topStories.setId(topStoriesObject.getInt("id"));
                    String topImageAddress = topStoriesObject.getString("image");
                    topStories.setImages(topImageAddress);
                    topStories.setTitle(topStoriesObject.getString("title"));
                    topStoriesList.add(topStories);
                }
                DateNews.ReturnDateNews().setTop_stories(topStoriesList);
            }

            dateCallBack.dateListCallBack(DateNews.ReturnDateNews());
            //BuildConfig.BUILD_TIMESTAMP;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
