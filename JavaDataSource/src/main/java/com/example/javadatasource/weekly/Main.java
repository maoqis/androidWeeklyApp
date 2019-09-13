package com.example.javadatasource.weekly;


import com.example.javadatasource.net.HttpRequest;
import com.example.javadatasource.utils.FileUtils;

/**
 * Created by maoqis on 17/12/26.
 */

public class Main {
    public static void main(String [] args) throws InterruptedException {
        //发送 GET 请求
        for (int i = 238; i >=160; i--) {
            downloadHtmlToFile(i);
            Thread.sleep(200+i);
        }







    }

    private static void downloadHtmlToFile(int i) {
        String s = getNetContent(i);
        FileUtils.outFile(i+"",s);
    }

    private static String getNetContent(int i) {
        String url = getUrlById(i);

        String s= HttpRequest.sendGet(url, null);
        if (s == null || s.equals("")){
            System.out.println(i);
        }
        return s;
    }

    public static String getUrlById(int i) {
        String url = "https://androidweekly.io/android-dev-weekly-issue-"+i;

        if (i == 210) {
            url = "https://www.androidweekly.cn/android-weekly-issue-"+i;
        }
        if (i==193){
            url = "https://www.androidweekly.cn/android-dev-wwekly-issue-"+i;
        }
        if (i == 154) {
            url = "https://www.androidweekly.cn/android-dev-wekly-issue-154/";
        }
        if (i <= 51 && i != 21) {
            url = "https://www.androidweekly.cn/android-dev-weekly-issue" + i;
        }
        return url;
    }


}
