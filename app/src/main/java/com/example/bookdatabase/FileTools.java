package com.example.bookdatabase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileTools {
    public static List<String> GetLinesFromFile(String filename, Context context) {
        List<String> lines = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(context.getFilesDir(), filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            fileInputStream.close();
            bufferedReader.close();
        } catch(Exception e) {
            Log.d(context.getResources().getString(R.string.app_name), e.getMessage());
        }
        return lines;
    }

    public static void RemoveFile(String filename, Context context) {
        try {
            File file = new File(context.getFilesDir(), filename);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void WriteTestDataToFile(String filename, Context context) {

        FileOutputStream outputStream;
        String[] data = new String[] {
                "Book Name 1\tAuthor Name 1\t2.5\ttrue\tno-pic\tThis is some comment 1.\r\n",
                "Book Name 2\tAuthor Name 2\t3.5\ttrue\tno-pic\tThis is some comment 2.\r\n",
                "Book Name 3\tAuthor Name 3\t4.5\ttrue\tno-pic\tThis is some comment 3.\r\n",
                "Book Name 4\tAuthor Name 4\t1.5\tfalse\tno-pic\tThis is some comment 4.\r\n",
                "Book Name 5\tAuthor Name 5\t2.5\tfalse\tno-pic\tThis is some comment 5.\r\n"
        };
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

    public static void RemoveNthLineFromFile(String filename, int lineIndex, Context context) {
        try {
            List<String> lines = GetLinesFromFile(filename, context);
            lines.remove(lineIndex);

            RemoveFile(filename, context);
            AppendLinesToFile(filename, lines, context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean AppendLinesToFile(String filename, List<String> lines, Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            for (String s : lines) {
                outputStream.write(s.concat("\r\n").getBytes());
            }
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void AppendLineToFile(String filename, String line, Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(line.concat("\r\n").getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetNthLineFromFile(String filename, int lineIndex, Context context) {
        try {
            List<String> lines = GetLinesFromFile(filename, context);
            return lines.get(lineIndex);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String saveImage(Bitmap myBitmap, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        try {
            File f = new File(context.getFilesDir(), Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getName();
        } catch (IOException e1) {
            Log.d("BookDatabase", e1.getMessage());
            Log.d("BookDatabase", e1.getStackTrace().toString());
            return "";
        }
    }

    public static String getAbsolutePath(String filename, Context context) {
        try {
            File f = new File(context.getFilesDir(), filename);
            return f.getAbsolutePath();
        } catch (Exception e) {
            Log.d("BookDatabase", e.toString());
            return "";
        }
    }
}
