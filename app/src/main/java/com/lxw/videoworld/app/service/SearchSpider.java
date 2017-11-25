package com.lxw.videoworld.app.service;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.MaoYanMovieModel;
import com.lxw.videoworld.app.model.SearchModel;
import com.lxw.videoworld.app.util.StringUtil;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.lxw.videoworld.app.api.HttpHelper.DEFAULT_TIMEOUT;

/**
 * Created by Zion on 2017/10/13.
 */

public class SearchSpider {
    public static List<SearchModel> getDiaosiSearchResult(String url) {
        try {
            Document htmlString = Jsoup.connect(url)
                    .timeout(DEFAULT_TIMEOUT * 1000)
                    .get();
            Elements datas = htmlString.select("ul.mlist > li");
            List<SearchModel> results = new ArrayList<>();
            for (int i = 0; datas != null && i < datas.size(); i++) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SearchModel> getZhongziSearchResult(String url) {
        try {
            Document htmlString = Jsoup.connect(url)
                    .timeout(DEFAULT_TIMEOUT * 1000)
                    .get();
            Elements datas = htmlString.select("tbody");
            List<SearchModel> results = new ArrayList<>();
            for (int i = 0; datas != null && i < datas.size(); i++) {
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getMaoYanMovies(){
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(Constant.BASE_MAOYAN_MOVIE).build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        MaoYanMovieModel maoYanMovieModel = GsonUtil.json2Bean(response.body().string(), MaoYanMovieModel.class);
                        List<MaoYanMovieModel.DataBean.ListBean> list = maoYanMovieModel.getData().getList();
                        if (maoYanMovieModel.isSuccess() && maoYanMovieModel.getData() != null && list != null ){
                            String[] hotwords = new String[list.size()];
                            for(int i = 0; i < list.size(); i++){
                                hotwords[i] = list.get(i).getMovieName();
                            }
                            if (hotwords.length >= 4){
                                SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_SEARCH_HOTWORDS, Arrays.toString(hotwords));
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
