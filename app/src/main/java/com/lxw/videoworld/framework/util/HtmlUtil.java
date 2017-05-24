package com.lxw.videoworld.framework.util;

import com.lxw.dailynews.framework.log.LoggerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.name;
import static android.R.attr.width;
import static android.R.string.no;

/**
 * Created by lxw9047 on 2016/11/22.
 */

public class HtmlUtil {
    /**
     * 给HTML body添加头部
     * 给图片添加点击事件
     * 替换图片地址
     * @param htmlBody HTML body
     * @return
     */
    public static String getHtmlData(String htmlBody) {
        String body = htmlBody;
        List<String> imgUrlList = getImgUrlList(htmlBody);
        for (int i = 0; i < imgUrlList.size(); i++){
            body = body.replaceAll(imgUrlList.get(i), imgUrlList.get(i) + "\" onclick = \"onImageClick(\'" + imgUrlList.get(i) + "\')");
        }

        String head = "<head>" +
                "<meta charset='utf-8'>" +
                "<meta name='viewport' content='width=device-width,user-scalable=no'>" +
                "<link href='file:///android_asset/news_qa.min.css' rel='stylesheet'>" +
                "<script src='file:///android_asset/zepto.min.js'></script>" +
                "<script src='file:///android_asset/img_replace.js'></script>" +
                "<script src='file:///android_asset/video.js'></script>" +
                "</head>";
        String script = "<script>" +
                "(function(){" +
                " var links = document.querySelectorAll('a[href^=\"http://daily.zhihu.com/story/\"]');" +
                "var length = links.length;" +
                "if (length) {" +
                "for (var i = 0; i < length; ++i) {" +
                "var link = links[i];" +
                "link.href = link.href.replace('http://daily.zhihu.com/story/', 'zhihudaily://story/');" +
                "}" +
                "}" +
                "})()" +
                "</script>";
        return "<html>" + head + "<body className=\"\" onload=\"onLoaded()\">" + body + script + "</body></html>";
    }



    /**
     * @param htmlBody
     * @return 获得图片地址
     */

    public static List<String> getImgUrlList(String htmlBody) {
        ArrayList<String> pics = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlBody);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    //提取imgUrl最后一段作为保存图片的文件名
    public static String getFileName(String imgUrl) {
        String[] strings = imgUrl.split("/");
        String fileName = strings[strings.length - 1];
        return fileName;
    }

}
