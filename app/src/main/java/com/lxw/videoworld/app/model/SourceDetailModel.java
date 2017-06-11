package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by lxw9047 on 2017/5/3.
 */
public class SourceDetailModel implements Serializable {

    private String url;
    private String id;
    private String category;// 一级分类
    private String type;// 二级分类
    private String title;
    private String images;
    private String content;// 详情信息
    private String name;// 名字
    private String translateName;// 译名
    private String year;// 年份
    private String area;// 国家、地区
    private String style;// 详情里的分类
    private String language;// 语言
    private String subtitles;// 字幕语言
    private String releaseDate;// 首映日期
    private String imdbScore;
    private String imdbIntro;
    private String imdbUrl;
    private String doubanScore;// 豆瓣评分
    private String doubanIntro;// 豆瓣评分说明
    private String doubanUrl;// 豆瓣评分地址
    private String fileFormat;// 文件格式
    private String fileSize;// 清晰度
    private String fileAmounts;// 张数 1 CD
    private String fileLength;// 时长
    private String author;// 编剧
    private String director;// 导演
    private String performer;// 主演
    private String intro;// 简介
    private String awards;// 获奖情况
    private String episodes;// 集数
    private String links;// 下载链接
    private String date;// 网站更新时间
    private String status;
    private long time;// 数据库时间

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslateName() {
        return translateName;
    }

    public void setTranslateName(String translateName) {
        this.translateName = translateName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImdbScore() {
        return imdbScore;
    }

    public void setImdbScore(String imdbScore) {
        this.imdbScore = imdbScore;
    }

    public String getDoubanScore() {
        return doubanScore;
    }

    public void setDoubanScore(String doubanScore) {
        this.doubanScore = doubanScore;
    }

    public String getImdbIntro() {
        return imdbIntro;
    }

    public void setImdbIntro(String imdbIntro) {
        this.imdbIntro = imdbIntro;
    }

    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    public String getDoubanIntro() {
        return doubanIntro;
    }

    public void setDoubanIntro(String doubanIntro) {
        this.doubanIntro = doubanIntro;
    }

    public String getDoubanUrl() {
        return doubanUrl;
    }

    public void setDoubanUrl(String doubanUrl) {
        this.doubanUrl = doubanUrl;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileAmounts() {
        return fileAmounts;
    }

    public void setFileAmounts(String fileAmounts) {
        this.fileAmounts = fileAmounts;
    }

    public String getFileLength() {
        return fileLength;
    }

    public void setFileLength(String fileLength) {
        this.fileLength = fileLength;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
