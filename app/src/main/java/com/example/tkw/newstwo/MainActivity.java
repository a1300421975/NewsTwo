package com.example.tkw.newstwo;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tkw.newstwo.Adapter.ViewPagerAdapter;
import com.example.tkw.newstwo.CallBack.DateCallBack;
import com.example.tkw.newstwo.CallBack.ImageCallBack;
import com.example.tkw.newstwo.GetData.GetImage;
import com.example.tkw.newstwo.News.DateNews;
import com.example.tkw.newstwo.GetData.GetStories;
import com.example.tkw.newstwo.News.Stories;
import com.example.tkw.newstwo.Adapter.StoriesAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * RecyclerView部分
     */
    RecyclerView storiesRecyclerView = null;

    StoriesAdapter storiesAdapter = null;

    final String Url = "https://news-at.zhihu.com/api/4/news/";

    LinearLayoutManager layoutManager = null;

    android.text.format.Time time = new android.text.format.Time("GTM+8");

    List<Stories> storiesList = null;

    boolean canChange = true;

    private static int dates;

    /**
     * ViewPager部分
     * @param savedInstanceState
     */
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SetStories(Url + "latest");

        ScrollBottom();
        

    }

    /**
     * 初始化数据
     */
    public void init(){
        storiesRecyclerView = (RecyclerView) findViewById(R.id.stories_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        storiesRecyclerView.setLayoutManager(layoutManager);
        storiesAdapter = new StoriesAdapter();
        dates = GetDate();//获取时间
        viewPager = (ViewPager) findViewById(R.id.view_pager);

    }

    /**
     * 获取日期的int型
     * @return 只包含年月日的int型数据
     */
    public int GetDate(){
        int dates = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        dates = Integer.parseInt(simpleDateFormat.format(date));
        return dates;
    }

    /**
     * 获取stories,如果为首次加载，则直接显示，否则局部刷新加载
     * @param Url 请求stories的地址
     */
    private void SetStories (String Url){
        GetStories.ReturnStories().sendRequestWithHttpURLConnection(Url, new DateCallBack() {
            @Override
            public void dateListCallBack(DateNews dateNews) {
                storiesList = dateNews.getStories();
                initViewPager();
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

    /**
     * 滑动到底部，加载数据
     */
    private void ScrollBottom(){
        storiesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (!storiesRecyclerView.canScrollVertically(1)) {
                        SetStories(Url + "before/" + (--dates));
                    }
                }
            }
        });

    }

    private void initViewPager(){
        final List<View> rotationViewList = new ArrayList<>();
        for(int i = 0;i < DateNews.ReturnDateNews().getTop_stories().size();i++){
            View view = LayoutInflater.from(this).inflate(R.layout.rotation_image,null);
            final ImageView imageView = (ImageView) view.findViewById(R.id.rotation_image_view);
            TextView textView = (TextView) view.findViewById(R.id.rotation_text_view);
            textView.setText(DateNews.ReturnDateNews().getTop_stories().get(i).getTitle());
            GetImage.ReturnImage().sendRequestWithHttpURLConnection(DateNews.ReturnDateNews().getTop_stories().get(i).getImages(), new ImageCallBack() {
                @Override
                public void imageCallBack(final Bitmap bitmap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                }
            });
            rotationViewList.add(view);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(rotationViewList);
                viewPager.setAdapter(viewPagerAdapter);
            }
        });
    }

}
