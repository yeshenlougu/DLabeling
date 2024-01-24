package com.dlabeling.common.utils;

import com.dlabeling.common.exception.file.FileException;
import com.dlabeling.common.exception.file.FileExistsException;
import com.dlabeling.common.exception.file.FileNotDirException;
import com.dlabeling.common.exception.file.FileNotFileException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
            throw new FileExistsException();
        }
        if (!isDirectory(dirPath)){
            throw new FileNotDirException();
        }
        Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new FileException("创建目录失败： "+dirPath, null);
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


}
