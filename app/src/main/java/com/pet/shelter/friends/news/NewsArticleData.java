package com.pet.shelter.friends.news;

import androidx.annotation.NonNull;

public class NewsArticleData {
    String authorUserId;
    String category;
    String description;
    String mediaImageDownloadLink;
    String postedDate;
    String subcategory;
    String title;
    String authorProfileImage;

    public NewsArticleData(String authorUserId,
                           String category,
                           String description,
                           String mediaImageDownloadLink,
                           String postedDate,
                           String subcategory,
                           String title) {
        this.authorUserId = authorUserId;
        this.category = category;
        this.description = description;
        this.mediaImageDownloadLink = mediaImageDownloadLink;
        this.postedDate = postedDate;
        this.subcategory = subcategory;
        this.title = title;
    }

    public NewsArticleData() {

    }

    @Override
    public String toString() {
        return "NewsArticleData{" +
                "authorUserId='" + authorUserId + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", mediaImageDownloadLink='" + mediaImageDownloadLink + '\'' +
                ", postedDate='" + postedDate + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", title='" + title + '\'' +
                ", authorProfileImage='" + authorProfileImage + '\'' +
                '}';
    }

    public String getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(String authorUserId) {
        this.authorUserId = authorUserId;
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

    public String getAuthorProfileImage() {
        return authorProfileImage;
    }

    public void setAuthorProfileImage(String authorProfileImage) {
        this.authorProfileImage = authorProfileImage;
    }
}
