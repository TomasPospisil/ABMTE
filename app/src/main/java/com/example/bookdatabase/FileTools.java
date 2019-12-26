package com.example.bookdatabase;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileTools {
    public static List<String> GetLinesFromFile(String filename, Context context) {
        List<String> lines = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(context.getFilesDir(), filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ( (line = bufferedReader.readLine()) != null )
            {
                lines.add(line);
            }
            fileInputStream.close();
            bufferedReader.close();
        }
        catch(Exception e) {
            Log.d(context.getResources().getString(R.string.app_name), e.getMessage());
        }
        return lines;
    }

    public static void WriteTestDataToFile(String filename, Context context) {

        FileOutputStream outputStream;
        String[] data = new String[] {
                "Book Name 1 \tAuthor Name 1\r\n",
                "Book Name 2 \tAuthor Name 2\r\n",
                "Book Name 3 \tAuthor Name 3\r\n",
                "Book Name 4 \tAuthor Name 4\r\n",
                "Book Name 5 \tAuthor Name 5\r\n"};
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            for (String s : data) {
                outputStream.write(s.getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CreateFile(String filename, Context context) {
        try {
            File file = new File(context.getFilesDir(), filename);
            file.createNewFile();
        } catch (Exception e) {
            Log.d(context.getResources().getString(R.string.app_name), e.getMessage());
        }
    }

    public static boolean Exists(String filename, Context context) {
        return new File(context.getFilesDir(), filename).exists();
    }
}
