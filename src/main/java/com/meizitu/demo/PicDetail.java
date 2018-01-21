package com.meizitu.demo;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.List;

/**
 * Created by enum on 2017/12/3.
 */
@Gecco(matchUrl = "http://www.meizitu.com/a/{code}.html", timeout = 90000, pipelines = {"consolePipeline", "meizituDemo"})
public class PicDetail implements HtmlBean {

    @Request
    private HttpRequest request;

    @RequestParameter
    private String code;

    @Text
    @HtmlField(cssPath = ".metaRight > h2 > a")
    private String title;

    @Image
    @HtmlField(cssPath=".postContent img")
    private List<String> pics;

    @Attr("alt")
    @HtmlField(cssPath=".postContent img")
    private List<String> picName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getPicName() {
        return picName;
    }

    public void setPicName(List<String> picName) {
        this.picName = picName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public static void main(String[] args) {
        //先获取分类列表
        HttpGetRequest start = new HttpGetRequest("http://www.meizitu.com/a/153.html");
        start.setCharset("GBK");
        GeccoEngine.create()
        .classpath("com.meizitu.demo")
        //开始抓取的页面地址
        .start(start)
        //开启几个爬虫线程
        .thread(30)
        //单个爬虫每次抓取完一个请求后的间隔时间
        .interval(2000)
        .run();
    }
}
