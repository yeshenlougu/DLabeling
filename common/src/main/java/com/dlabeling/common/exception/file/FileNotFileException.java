package com.dlabeling.common.exception.file;

import com.dlabeling.common.enums.ResponseCode;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public class FileNotFileException extends FileException{

    public FileNotFileException(){
        super("file.file.not", null, ResponseCode.FILE_NOT_FILE.getMessage());
    }
}
