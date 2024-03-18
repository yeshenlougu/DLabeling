package com.dlabeling.common.utils;

import com.dlabeling.common.exception.file.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public class FileUtils {

    public static void makeDir(String dirPath){
        if(exists(dirPath)){
            if (!isDirectory(dirPath)){
                throw new FileNotDirException();
            }
            throw new DirExistsException();
        }

        Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new FileException("创建目录失败： "+dirPath, null);
        }

    }

    public static void deleteDir(String dirPath){
        File file = new File(dirPath);
        deleteFolder(file);
    }

    private static void deleteFolder(File folder){
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归删除子文件夹
                        deleteFolder(file);
                    } else {
                        // 删除文件
                        file.delete();
                    }
                }
            }
            // 删除文件夹本身
            folder.delete();
        }
    }

    public static String resolvePath(String root, String sub){
        Path rootPath = Paths.get(root);
        Path resolvePath = rootPath.resolve(sub);
        return resolvePath.toString();
    }

    public static void makeFile(String filePath){
        if (exists(filePath)){
            throw new FileExistsException();
        }
        if (isDirectory(filePath)){
            throw new FileNotFileException();
        }
        Path path = Paths.get(filePath);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new FileException("创建文件失败： "+filePath, null);
        }
    }

    public static void writeFile(File file, String text){}

    public static void writeString(String filePath, String content){
        Path path = Paths.get(filePath);
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new FileException("写入文件错误", null);
        }
    }

    public static Boolean exists(String filePath){
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    public static Boolean isDirectory(String dirPath){
        Path path = Paths.get(dirPath);
        return Files.isDirectory(path);
    }


    public static String getFileName(String path){
        if (isDirectory(path)){
            throw new FileNotFileException();
        }
        char separatorChar = File.separatorChar;
        int index = path.lastIndexOf(separatorChar);
        String fileName = path.substring(index + 1);

        return fileName;
    }

    public static String removeFileExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        return fileName.substring(0, i);
    }

    public static String getBase64(String filePath) throws IOException {
        File file = new File(filePath);
        if (isDirectory(filePath)){
            throw new FileNotFileException();
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputStream.read(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String changeSuffix(String fileName, String suffix){
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex>0 && dotIndex < fileName.length()-1);
        String newName = fileName.substring(0, dotIndex) + suffix;
        return newName;
    }

    public static void replace(File file, String pathDir){
        try {
            Path filePath = file.toPath();
            String fileName = file.getName();
            Path targetPath = Paths.get(FileUtils.resolvePath(pathDir, fileName));
            Files.move(filePath, targetPath);
        }catch (IOException e){
            throw new FileMoveException(file.getAbsolutePath()+" 移动到目标目录" + pathDir + "失败");
        }

    }

    public static void copyTo(String basePath, String newPath) {
        try {
            Files.copy(Paths.get(basePath), new File(newPath).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
