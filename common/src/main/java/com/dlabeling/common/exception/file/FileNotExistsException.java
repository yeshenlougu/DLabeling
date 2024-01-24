package com.dlabeling.common.exception.file;

import com.dlabeling.common.enums.ResponseCode;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public class FileNotExistsException extends FileException{

    public FileNotExistsException(){
        super("file.exists.false", null, ResponseCode.FILE_NOT_EXISTS.getMessage());
    }
}
