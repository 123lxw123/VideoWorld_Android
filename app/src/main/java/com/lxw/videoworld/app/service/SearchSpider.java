package com.lxw.videoworld.app.service;

import com.lxw.videoworld.app.model.SearchModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * Created by Zion on 2017/10/13.
 */

public class SearchSpider {
    public static List<SearchModel> getZhongziSearchResult(String url){
        try{
            Document htmlString = Jsoup.connect(url).timeout(16000).get();
            Elements datas = htmlString.select("ul.mlist > li");
            List<SearchModel> results = new ArrayList<>();
            for (int i = 0; datas != null && i < datas.size(); i++){
                SearchModel result = new SearchModel();
                result.setTitle(datas.get(i).select("h4").text());
                result.setDate(getMatchContent(datas.get(i).text(), "创建日期：(.*?)</strong>"));
                result.setSize(getMatchContent(datas.get(i).text(), "大小：(.*?)</strong>"));
                result.setHot(getMatchContent(datas.get(i).text(), "热度：(.*?)</strong>"));
                result.setCiliLink(getMatchContent(datas.get(i).text(), "class=\"ls-magnet\">.*?href=\"(.*?)\""));
                results.add(result);
            }
            return results;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getMatchContent(String originText, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originText); // 获取 matcher 对象
        if (m.find()) return m.group(0);
        else return "";
    }
}
