package com.example.tkw.newstwo.News;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tkw.newstwo.CallBack.ImageCallBack;
import com.example.tkw.newstwo.R;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {

    private List<Stories> storiesList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView storiesImage;
        TextView storiesTitle;

        public ViewHolder(View view){
            super(view);
            storiesImage = (ImageView) view.findViewById(R.id.title_image_view);
            storiesTitle = (TextView) view.findViewById(R.id.title_text_view);
        }
    }

    public void setStoriesList(List<Stories> storiesList) {
        this.storiesList = storiesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stories_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Stories stories = storiesList.get(position);
        viewHolder.storiesTitle.setText(stories.getTitle());
        GetImage.ReturnImage().sendRequestWithHttpURLConnection(stories.getImages(), new ImageCallBack() {
            @Override
            public void imageCallBack(final Bitmap bitmap) {
                viewHolder.storiesImage.post(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.storiesImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }
}
