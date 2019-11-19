package com.example.livedataandretrofit.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Blog extends RealmObject {

    @SerializedName("author")
    private String mAuthor;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("link")
    private String mLink;
    @SerializedName("pubDate")
    private String mPubDate;
    @SerializedName("thumbnail")
    private String mThumbnail;
    @SerializedName("title")
    private String mTitle;

    @PrimaryKey
    transient long mId;

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        mPubDate = pubDate;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public long getmId() {
        return mId;
    }

    public void setmId(long id) {
        this.mId = id;
    }
}