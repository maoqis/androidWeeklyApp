package com.maoqis.test.androidnew.room.db;

import android.support.annotation.Nullable;

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
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bjmaoqisheng on 2017/11/30.
 */

public class DataGenerator {


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
            database.weekDao().insertWeekItemList(week.getWeekItems());
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
        System.out.println(date1.getYear() + "-" + date1.getMonth() + "  +1  -" + date1.getDay());
        date1 = DateTimeUtils.formatDate(date1.getYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDay());
        System.out.println(date1.toString());

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
                System.out.println(date1.getYear() + "-" + date1.getMonth() + "  +1  -" + date1.getDay());
                date1 = DateTimeUtils.formatDate(date1.getYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDay());
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
                        String link = a.attr("href");
                        String title = a.text();
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
    private static Date findDate(Elements date) {
        Date date1 = null;

        for (Element element : date) {
            int year = 1970, month = 0, day = 0;
            String text = element.text();
            String[] split = text.split(" ");
            for (int i = split.length - 1; i >= 0; i--) {
                String s = split[i];
                if (i == 2) {
                    year = Integer.valueOf(s);
                } else if (i == 1) {

                    s = s.replace(",", "");
                    day = Integer.valueOf(s);
                } else if (i == 0) {
                    if (s.contains("十二月")) {
                        month = 11;
                    } else if (s.contains("十一月")) {
                        month = 10;
                    } else if (s.contains("十月")) {
                        month = 9;
                    } else if (s.contains("九月")) {
                        month = 8;
                    } else if (s.contains("八月")) {
                        month = 7;
                    } else if (s.contains("七月")) {
                        month = 6;
                    } else if (s.contains("六月")) {
                        month = 5;
                    } else if (s.contains("五月")) {
                        month = 4;
                    } else if (s.contains("四月")) {
                        month = 3;
                    } else if (s.contains("三月")) {
                        month = 2;
                    } else if (s.contains("二月")) {
                        month = 1;
                    } else if (s.contains("一月")) {
                        month = 0;
                    }


                }
            }

            date1 = new Date(year, month, day);
            break;

        }
        return date1;
    }
}
