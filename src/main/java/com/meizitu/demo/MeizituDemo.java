package com.meizitu.demo;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.scheduler.SchedulerContext;
import org.apache.commons.lang3.StringUtils;
import util.PicUtil;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by enum on 2017/12/3.
 */
@PipelineName("meizituDemo")
public class MeizituDemo implements Pipeline<PicDetail> {

    public void process(PicDetail picDetail) {
        String code = picDetail.getCode();
        String title = picDetail.getTitle();
        List<String> pics = picDetail.getPics();
        List<String> picNames = picDetail.getPicName();
        System.out.println("code: " + code);
        System.out.println("标题：" + title);
        for (int i = 0; i < pics.size(); i++) {
            String picUrl = pics.get(i);
            String picName = i + ".jpg";
            System.out.println("图片地址：" + picUrl);
            if (PicUtil.savePic(picUrl, picName, "F:" + File.separator + "meizitu" + File.separator + title)){
                System.out.println("保存图片" + picName + "成功!");
            }
        }
        //下一页继续抓取
        HttpRequest request = picDetail.getRequest();
        String currentUrl = request.getUrl();//http://www.meizitu.com/a/19.html
        if (StringUtils.isNotBlank(currentUrl)){
            String intPage = Pattern.compile("[^0-9]").matcher(currentUrl).replaceAll("").trim();
            int currentInt = Integer.valueOf(intPage);
            if (currentInt <= 788){
                currentInt++;
            }
            String nextUrl = "http://www.meizitu.com/a/" + currentInt +".html";
            SchedulerContext.into(request.subRequest(nextUrl));
        }
    }
}
