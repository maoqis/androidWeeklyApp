package com.example.javadatasource.weekly;

import com.example.javadatasource.utils.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * Created by maoqis on 17/12/28.
 */

public class JsoupComponent {
    public static void main(String [] args){
        File dir = FileUtils.makeSureDir();
        File file = new File(dir,160+"");
        try {
            if (file.exists()){
                Document document = Jsoup.parse(file, "UTF-8");
                Elements date = document.getAllElements();
                for (Element element : date) {
                    System.out.println(element.text());
                    
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
