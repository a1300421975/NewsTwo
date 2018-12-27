package com.example.tkw.newstwo.News;

import java.util.ArrayList;
import java.util.List;

public class DateNews {

    private static volatile DateNews dateNews = null;

    private DateNews(){}

    public static DateNews ReturnDateNews(){
        if(dateNews == null){
            synchronized (DateNews.class){
                if(dateNews == null){
                    dateNews = new DateNews();
                }
            }
        }
        return dateNews;
    }

    private List<Stories> stories = new ArrayList<>();

    private List<Stories> top_stories = new ArrayList<>();

    public List<Stories> getStories() {
        return stories;
    }

    public void addStories(List<Stories> stories) {
        if(this.stories.isEmpty()){
            this.stories = stories;
        }else {
            this.stories.addAll(stories);
        }
    }

    public List<Stories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Stories> top_stories) {
        if(this.top_stories.isEmpty()){
            this.top_stories = top_stories;
            return;
        }
        if(top_stories.equals(this.top_stories)){
            return;
        }else {
            this.top_stories.clear();
            this.top_stories = top_stories;
        }

    }
}
