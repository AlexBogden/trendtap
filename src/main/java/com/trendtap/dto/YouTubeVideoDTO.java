package com.trendtap.dto;

public class YouTubeVideoDTO {
    private String videoId;
    private String title;
    private String thumbnailUrl;
    private String channelTitle;
    private long viewCount;

    public YouTubeVideoDTO() {
    }

    public YouTubeVideoDTO(String videoId, String title, String thumbnailUrl, String channelTitle, long viewCount) {
        this.videoId = videoId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
