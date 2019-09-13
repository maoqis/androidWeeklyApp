package com.example.javadatasource.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by maoqis on 17/12/28.
 */

public class FileUtils {
    public static File makeSureDir() {
        File file = new File("JavaDataSource/files");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static void outFile(String name,String content) {
        if (content == null || content.length() ==0){
            return;
        }
        File dir = makeSureDir();
        File file = new File(dir,name);
        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

    }
}
