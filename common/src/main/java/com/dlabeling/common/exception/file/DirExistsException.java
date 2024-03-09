package com.dlabeling.common.exception.file;

import com.dlabeling.common.enums.ResponseCode;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/7
 */
public class DirExistsException extends FileException{

    public DirExistsException(){
        super("dir.exists.true", null, ResponseCode.DIR_CREATE_ERROR.getMessage());
    }
}
