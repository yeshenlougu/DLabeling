package com.dlabeling.common.exception.file;

import com.dlabeling.common.enums.ResponseCode;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public class FileNotDirException extends FileException{

    public FileNotDirException(){
        super("file.dir.not", null, ResponseCode.FILE_NOT_DIR.getMessage());
    }
}
