package com.maoqis.test.androidnew.room.db;

import android.util.Log;

import androidx.annotation.Nullable;

import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.maoqis.test.androidnew.net.NetworkComponent;
import com.maoqis.test.androidnew.room.db.entity.Week;
import com.maoqis.test.androidnew.room.db.entity.WeekItem;
import com.maoqis.test.androidnew.utils.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by bjmaoqisheng on 2017/11/30.
 */

public class DataGenerator {

    private static final String TAG = "DataGenerator";

    public static void generatorAllDate(AppDatabase database) {
        loadWeekData(database);
    }

    private static void loadWeekData(AppDatabase database) {
        File dir = FileUtils.makeSureDir();

        File fileWeekly = new File(dir, "weekly");
        if (!fileWeekly.exists() || !fileWeekly.isDirectory()) return;
        File[] files = fileWeekly.listFiles();
        for (File file : files) {

            Week week = loadWeekFileData(file, Integer.valueOf(file.getName()));
            database.weekDao().insertWeeks(week);
            List<WeekItem> weekItems = week.getWeekItems();
            if (weekItems == null) {
                Log.w(TAG, "loadWeekData: weekItems=" + weekItems);
            } else {
                database.weekDao().insertWeekItemList(weekItems);
            }

        }

    }

    public static Week loadWeekFileData(String docs, long weekId) {
        Week week = null;
        week = new Week();
        week.setId(weekId);
        week.setLink(NetworkComponent.getUrlById(weekId));

        Document document = Jsoup.parse(docs, "UTF-8");
        Elements date = document.getElementsByClass("date");
        Date date1 = findDate(date);
        SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd" );
        String str1 = sdf1.format(date1);
        Log.d(TAG, "loadWeekFileData: " + str1);

        week.setDate(date1);

        Elements content = document.getElementsByClass("post-entry");
        Element first = content.first();
        if (first.tagName().equals("div")) {
            String aClass = first.attr("class");
            if (aClass.equals("kg-card-markdown")) {
                first = first.children().first();
            }
        }
        addContent(weekId, week, first);

        return week;

    }

    private static Week loadWeekFileData(File file, long weekId) {
        Week week = null;

        try {
            if (file.exists()) {

                week = new Week();
                week.setId(weekId);
                week.setLink(NetworkComponent.getUrlById(weekId));

                Document document = Jsoup.parse(file, "UTF-8");
                Elements date = document.getElementsByClass("date");
                Date date1 = findDate(date);

                System.out.println(DateTimeUtils.formatTime(date1));
                System.out.println(date1.toString());

                week.setDate(date1);

                Elements content = document.getElementsByClass("post-entry");
                Element first = content.first();
                addContent(weekId, week, first);

//                first= first.getElementById("post-book-list");
//                addContent(weekId, week, first);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return week;
        }
    }

    private static void addContent(long weekId, Week week, Element first) {
        if (first == null) return;
        Elements allElements = first.children();
        String type = null;

        for (int i = 0; i < allElements.size(); i++) {
            Element element = allElements.get(i);
            String tagName = element.tagName();
            if (tagName.equals("div")) {
                String aClass = element.attr("class");
                if (aClass.equals("kg-card-markdown")) {
                    addContent(weekId, week, element);
                }

                return;
            }
            if (tagName.equals("h3")) {

                type = element.text();
                continue;
            } else {

                if (week != null) {
                    Elements li = element.getElementsByTag("li");
                    if (li.isEmpty()) {
                        continue;
                    }


                    for (Element element1 : li) {
                        String des = null;
                        Elements pList = element1.getElementsByTag("p");

                        if (pList.isEmpty()) break;

                        if (pList.size() > 1) {

                            Element last = pList.last();
                            des = last.text();

                        }
                        Elements as = element1.getElementsByTag("a");
                        Element a = as.first();
                        if (a == null) {
                            Log.w(TAG, "addContent: weekId:" + weekId + " a = null");
                        }
                        String link = a == null ? null : a.attr("href");
                        String title = a == null ? null : a.text();
                        WeekItem weekItem = new WeekItem(title, des, link, type, weekId);
                        System.out.println(weekItem);
                        if (week.getWeekItems() == null) {
                            week.setWeekItems(new ArrayList<>());
                        }

                        System.out.println(weekItem);
                        week.getWeekItems().add(weekItem);

                    }
                }
            }

        }

    }

    @Nullable
    private static Date findDate(Elements elements) {
        Date date1 = null;
        for (int j = 0; j < elements.size(); j++) {
            Element element = elements.get(j);
            int year = 1970, month = 0, day = 0;
            String text = element.text();
            Log.d(TAG, "findDate: text=" + text);
            String[] split = text.split(" ");
            for (int i = split.length - 1; i >= 0; i--) {
                String s = split[i];
                if (i == 2) {
                    year = Integer.valueOf(s);
                } else if (i == 0) {
                    month = enMonth(s);
                    if (month < 0) {
                        month = znMonth(s);
                    }
                    if (month < 0) {
                        month = 0;
                        Log.d(TAG, "findDate: not match month");
                    }

                } else if (i == 1) {
                    s = s.replace(",", "");
                    day = Integer.valueOf(s);
                }
            }

            String source = year + "-" + month + "-" + day;
            Log.v(TAG, "findDate: source=" + source);

            Calendar myCalendar = new GregorianCalendar(year, month, day);

            return myCalendar.getTime();

        }
        return null;
    }

    private static int znMonth(String m) {
        for (int i = 0; i < months_zh.length; i++) {
            String s = months_zh[i];
            if (m.contains(s)) {
                return i;
            }
        }
        return -1;
    }

    public static int enMonth(String m) {
        for (int i = 0; i < months_en.length; i++) {
            String s = months_en[i];
            if (m.contains(s)) {
                return i;
            }
        }
        return -1;
    }

    private static String[] months_zh = {"一月", "二月", "三月", "四月", "五月", "六月", "七月",
            "八月", "九月", "十月", "十一月", "十二月"};
    private static String[] months_en = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
}
