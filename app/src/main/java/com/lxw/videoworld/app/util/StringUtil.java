package com.lxw.videoworld.app.util;


import android.text.TextUtils;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.framework.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Zion on 2017/5/20.
 */
public class StringUtil {

    // 处理 HTML 提取的文本内容，去掉首位空白，替换 HTML 特殊字符
    public static String disposeField(String oldStr){
        if(!TextUtils.isEmpty(oldStr)){
            String temp1 = HTMLUtil.trimHtml2Txt(oldStr, null);
            String temp2 = removeHtmlTag(temp1);
            String temp3 = replaceAll12288(temp2);
            String temp4 = removeExtraCharacters(temp3);
            return temp4;
        }else {
            return "";
        }
    }

    // 替换 12288 全角空格
    public static String replaceAll12288(String fullStr){
        if(TextUtils.isEmpty(fullStr)){
            return fullStr;
        }
        char[] c = fullStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
           if (c[i] == 12288) { // 空格
                c[i] = (char) 32;
            }
        }
        return new String(c);
    }


    /**
     * 删除Html标签
     * @param inputString
     * @return
     */
    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        java.util.regex.Matcher m_script;
        Pattern p_style;
        java.util.regex.Matcher m_style;
        Pattern p_html;
        java.util.regex.Matcher m_html;
        Pattern p_special;
        java.util.regex.Matcher m_special;
        try {
//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
// 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
// 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    public static String removeExtraCharacters(String oldStr){
        boolean frag_change = false;
        if(!TextUtils.isEmpty(oldStr)){
            String content = oldStr.trim();
            try{
                if(content.startsWith(",")){
                    content = content.substring(1, content.length());
                    frag_change = true;
                }
                if(content.endsWith(",")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.startsWith("，")){
                    content = content.substring(1, content.length());
                    frag_change = true;
                }
                if(content.endsWith("，")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.startsWith("】")){
                    content = content.substring(1, content.length());
                    frag_change = true;
                }
                if(content.endsWith("】")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.startsWith("【")){
                    content = content.substring(1, content.length());
                    frag_change = true;
                }
                if(content.endsWith("【")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.startsWith(":")){
                    content = content.substring(1, content.length());
                    frag_change = true;
                }
                if(content.endsWith(":")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.startsWith("：")){
                    content = content.substring(1, content.length());
                    frag_change = true;
                }
                if(content.endsWith("：")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.endsWith("]") && !content.contains("[")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(content.endsWith(")") && !content.contains("(")){
                    content = content.substring(0, content.length() - 1);
                    frag_change = true;
                }
                if(frag_change){
                    return removeExtraCharacters(content);
                }else{
                    return content;
                }
            }catch (Exception e){
                e.printStackTrace();
                return oldStr;
            }
        }else{
            return oldStr;
        }
    }

    public static ArrayList<String> getSourceLinks(SourceDetailModel sourceDetailModel) {
        final List<String> tempList = ValueUtil.string2list(sourceDetailModel.getLinks());
        final ArrayList<String> links = new ArrayList<>();
        if (tempList != null) {
            for (int i = 0; i < tempList.size(); i++) {
                if (!TextUtils.isEmpty(tempList.get(i)) && !TextUtils.isEmpty(tempList.get(i).trim()))
                    if (Constant.SOURCE_TYPE.equals(Constant.SOURCE_TYPE_4)) {
                        if (tempList.get(i).contains("m3u8")) links.add(tempList.get(i).trim());
                    } else links.add(tempList.get(i).trim());
            }
        }
        ArrayList<String> playList = new ArrayList<>();
        ArrayList<String> otherList = new ArrayList<>();
        ArrayList<String> resultList = new ArrayList<>();
        for (int  i = 0; i < links.size(); i++) {
            if (StringUtil.isAutoPlay(links.get(i))) {
                playList.add(links.get(i));
            } else {
                otherList.add(links.get(i));
            }
        }
        resultList.addAll(playList);
        resultList.addAll(otherList);
        return resultList;
    }

    public static boolean isAutoPlay(String link) {
        if (TextUtils.isEmpty(link)) return false;
        try {
            String[] suffixs = link.split("\\.");
            String suffix = suffixs[suffixs.length - 1];
            for (int i = 0; i < Constant.videos.length; i++) {
                if (Constant.videos[i].equals(suffix)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
