package com.pet.shelter.friends.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsArticlesCustomAdapter extends ArrayAdapter<NewsArticleData> {

    private List<NewsArticleData> newsArticlesList;
    private final int custom_news_article_view_layout_id;

    public NewsArticlesCustomAdapter(@NonNull Context context, int resource, @NonNull List<NewsArticleData> objects) {
        super(context, resource, objects);
        newsArticlesList = objects;
        custom_news_article_view_layout_id = resource;
    }

    @Override
    public int getCount() {
        return newsArticlesList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(custom_news_article_view_layout_id, null);
        }

        // initializing the card elements and setting data
        ImageView authorProfileImageView = v.findViewById(R.id.newsArticleCardUserProfileImage_imageView);
        ImageView articleMediaImageView = v.findViewById(R.id.newsArticleCardMedia_imageView);
        MaterialTextView newsArticleSubcategoryMaterialTextView = v.findViewById(R.id.newsArticleCardSubcategory_materialTextView);
        MaterialTextView articleTitleMaterialTextView = v.findViewById(R.id.newsArticleCardTitle_materialTextView);
        MaterialTextView shortDescriptionMaterialTextView = v.findViewById(R.id.newsArticleCardShortDescription_materialTextView);
        MaterialTextView postedDateMaterialTextView = v.findViewById(R.id.newsArticleCardPostedDate_materialTextView);

        // get the item using the position param
        NewsArticleData newsArticleData = newsArticlesList.get(position);

        Picasso.get().load(newsArticleData.getMediaImageDownloadLink()).into(articleMediaImageView);

        newsArticleSubcategoryMaterialTextView.setText(newsArticleData.getSubcategory());
        articleTitleMaterialTextView.setText(newsArticleData.getTitle());
        shortDescriptionMaterialTextView.setText(newsArticleData.getDescription());
        postedDateMaterialTextView.setText(newsArticleData.getPostedDate());

        return v;
    }

    public void upToDate(List<NewsArticleData> newList){
        newsArticlesList = new ArrayList<>();
        newsArticlesList.addAll(newList);
        notifyDataSetChanged();
    }
}
