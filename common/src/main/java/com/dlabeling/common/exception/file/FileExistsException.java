package com.dlabeling.common.exception.file;

import com.dlabeling.common.enums.ResponseCode;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
public class FileExistsException extends FileException{

    public FileExistsException(){
        super("file.exists.true", null, ResponseCode.FILE_EXISTS.getMessage());
    }


}
