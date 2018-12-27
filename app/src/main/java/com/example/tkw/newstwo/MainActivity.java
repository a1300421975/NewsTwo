package com.example.tkw.newstwo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tkw.newstwo.CallBack.DateCallBack;
import com.example.tkw.newstwo.News.DateNews;
import com.example.tkw.newstwo.News.GetStories;
import com.example.tkw.newstwo.News.Stories;
import com.example.tkw.newstwo.News.StoriesAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView storiesRecyclerView = null;

    StoriesAdapter storiesAdapter = null;

    final String Url = "https://news-at.zhihu.com/api/4/news/";

    LinearLayoutManager layoutManager = null;

    android.text.format.Time time = new android.text.format.Time("GTM+8");

    private static int position = 0;

    List<Stories> storiesList = null;

    int lastVisibleItem = 0;

    boolean canChange = true;

    int times = 0;

    int dates = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storiesRecyclerView = (RecyclerView) findViewById(R.id.stories_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        storiesRecyclerView.setLayoutManager(layoutManager);
        storiesAdapter = new StoriesAdapter();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        dates = Integer.parseInt(simpleDateFormat.format(date));
        SetStories(Url + "latest");

        storiesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (!storiesRecyclerView.canScrollVertically(1)) {
                        ++times;
                        SetStories(Url + "before/" + (--dates));
                    }
                }
            }
        });
    }

    private void SetStories (String Url){
        GetStories.ReturnStories().sendRequestWithHttpURLConnection(Url, new DateCallBack() {
            @Override
            public void dateListCallBack(DateNews dateNews) {
                storiesList = dateNews.getStories();
                if (canChange) {
                    canChange = false;
                    storiesAdapter.setStoriesList(storiesList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storiesRecyclerView.setAdapter(storiesAdapter);
                        }
                    });
                }else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storiesAdapter.notifyItemChanged(storiesList.size() - 1);
                        }
                    });
                }
            }
        });
    }

}
