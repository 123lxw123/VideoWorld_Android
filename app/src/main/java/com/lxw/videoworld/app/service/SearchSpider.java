package com.lxw.videoworld.app.service;

import com.lxw.videoworld.app.model.SearchModel;
import com.lxw.videoworld.app.util.StringUtil;

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
    public static List<SearchModel> getDiaosiSearchResult(String url){
        try{
            Document htmlString = Jsoup.connect(url).timeout(20000).get();
            Elements datas = htmlString.select("ul.mlist > li");
            List<SearchModel> results = new ArrayList<>();
            for (int i = 0; datas != null && i < datas.size(); i++){
                SearchModel result = new SearchModel();
                Elements spans = datas.get(i).select("dl.BotInfo").select("dt > span");
                result.setTitle(StringUtil.disposeField(datas.get(i).select("div.T1").text()));
                result.setSize(StringUtil.disposeField(spans.get(0).text()));
                result.setAmounts(StringUtil.disposeField(spans.get(1).text()));
                result.setDate(StringUtil.disposeField(spans.get(2).text()));
                result.setHot(StringUtil.disposeField(spans.get(3).text()));
                result.setCiliLink(datas.get(i).select("div.dInfo > a").first().attr("href").trim());
                result.setThunderLink(datas.get(i).select("div.dInfo > a").last().attr("href").trim());
                results.add(result);
            }
            return results;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<SearchModel> getZhongziSearchResult(String url){
        try{
            Document htmlString = Jsoup.connect(url).timeout(20000).get();
            Elements datas = htmlString.select("tbody");
            List<SearchModel> results = new ArrayList<>();
            for (int i = 0; datas != null && i < datas.size(); i++){
                SearchModel result = new SearchModel();
                Elements tds = datas.get(i).select("tr").get(1).select("td");
                result.setTitle(datas.get(i).select("h4").text());
                result.setDate(tds.get(0).select("strong").text());
                result.setSize(tds.get(1).select("strong").text());
                result.setHot(tds.get(2).select("strong").text());
                result.setCiliLink(tds.get(3).select("a").attr("href").trim());
                results.add(result);
            }
            return results;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
