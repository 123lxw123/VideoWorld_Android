package com.lxw.videoworld.app.service;

import android.text.Html;

import com.lxw.videoworld.app.model.SearchModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by Zion on 2017/10/13.
 */

public class SearchSpider {
    public static List<SearchModel> getZhongziSearchResult(String url){
//        List<String> titles = page.getHtml().css("table.table-striped").css("h4").all();
//        List<String> dates = page.getHtml().css("table.table-striped").regex("创建日期：(.*?)</strong>").all();
//        List<String> sizes = page.getHtml().css("table.table-striped").regex("大小：(.*?)</strong>").all();
//        List<String> hots = page.getHtml().css("table.table-striped").regex("热度：(.*?)</strong>").all();
//        Html html = new Html(page.getRawText());
//        List<String> links = html.css("table.table-striped").regex("class=\"ls-magnet\">.*?href=\"(.*?)\"").all();
//        List<SearchResult> results = new ArrayList<>();
//        if(links != null && links.size() > 0){
//            for(int i = 0 ; i < links.size(); i++){
//                SearchResult result = new SearchResult();
//                result.setTitle(StringUtil.disposeField(titles.get(i)));
//                result.setDate(StringUtil.disposeField(dates.get(i)));
//                result.setSize(StringUtil.disposeField(sizes.get(i)));
//                result.setHot(StringUtil.disposeField(hots.get(i)));
//                result.setCiliLink(StringUtil.disposeField(links.get(i)));
//                results.add(result);
//            }
//        }
        try{
            Document htmlString = Jsoup.connect(url).get();
            Elements datas = htmlString.select("table.table-striped");
//            Elements titles = htmlString.select("table.table-striped").select("h4");
//            Elements dates = htmlString.select("table.table-striped").select("h4").text().ma;
//            Elements sizes = htmlString.select("table.table-striped").select("h4");
//            Elements hots = htmlString.select("table.table-striped").select("h4");
            List<SearchModel> results = new ArrayList<>();
            for (int i = 0; datas != null && i < datas.size(); i++){
                SearchModel result = new SearchModel();
                result.setTitle(datas.get(i).select("h4").text());
                result.setDate(datas.get(i).text().matches("创建日期：(.*?)</strong>"));
                result.setTitle(datas.get(i).select("h4").text());
                result.setTitle(datas.get(i).select("h4").text());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
