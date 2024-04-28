package com.gyzer.legendaryrealms.Utils;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FileOperateUtils {

    public static void deleteDirectory(File directory) {
        try (Stream<Path> walk = Files.walk(directory.toPath())) {
            walk.sorted(Comparator.reverseOrder()).forEachOrdered(path -> {
                try {
                    Files.delete(path);
                } catch (FileSystemException e){

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void copyDirectory(String sourcePath,String outputPath,List<String> arrayList){
        File source = new File(sourcePath);
        File out = new File(outputPath);
        if (!out.exists()){
            if (out.mkdir()){
            }
            else {
                return;
            }
        }
        for (File file : source.listFiles()){
            if (file.isDirectory()){
                copyDirectory(sourcePath + File.separator + file.getName(),outputPath + File.separator + file.getName(),arrayList);
            }
            else {
                if (arrayList.contains(file.getName())){
                    continue;
                }
                copyFile(new File(sourcePath + File.separator + file.getName()), new File(outputPath + File.separator + file.getName()));
            }
        }
    }

    private static void copyFile(File oldDir, File newDir) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        byte[] b = new byte[1024];

        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(oldDir));

            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newDir));

            int len;
            while ((len = bufferedInputStream.read(b)) > -1)
            {
                bufferedOutputStream.write(b, 0, len);
            }

            bufferedOutputStream.flush();
        } catch (IOException len) {
            IOException iOException;
        } finally {
            if (bufferedInputStream != null) {

                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
