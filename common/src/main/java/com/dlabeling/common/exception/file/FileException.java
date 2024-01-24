package com.dlabeling.common.exception.file;

import com.dlabeling.common.exception.base.BaseException;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */

public class FileException extends BaseException {

    public FileException(String code, Object[] args){
        super("file", code, args, null);
    }
    

    public FileException(String code, Object[] args, String message){
        super("file", code, args, message);
    }
}
