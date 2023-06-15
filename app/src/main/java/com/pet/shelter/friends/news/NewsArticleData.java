package com.pet.shelter.friends.news;

import androidx.annotation.NonNull;

public class NewsArticleData {
    private String authorName;
    private String category;
    private String description;
    private String mediaImageDownloadLink;
    private String postedDate;
    private String subcategory;
    private String title;
    private String newsArticleAuthorProfileImage;

    public NewsArticleData(String authorName,
                           String category,
                           String description,
                           String mediaImageDownloadLink,
                           String postedDate,
                           String subcategory,
                           String title,
                           String newsArticleAuthorProfileImage) {
        this.authorName = authorName;
        this.category = category;
        this.description = description;
        this.mediaImageDownloadLink = mediaImageDownloadLink;
        this.postedDate = postedDate;
        this.subcategory = subcategory;
        this.title = title;
        this.newsArticleAuthorProfileImage = newsArticleAuthorProfileImage;
    }

    public NewsArticleData() {

    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorUserId) {
        this.authorName = authorUserId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaImageDownloadLink() {
        return mediaImageDownloadLink;
    }

    public void setMediaImageDownloadLink(String mediaImageDownloadLink) {
        this.mediaImageDownloadLink = mediaImageDownloadLink;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsArticleAuthorProfileImage() {
        return newsArticleAuthorProfileImage;
    }

    public void setNewsArticleAuthorProfileImage(String newsArticleAuthorProfileImage) {
        this.newsArticleAuthorProfileImage = newsArticleAuthorProfileImage;
    }
}
