package com.dlabeling.common.exception.file;

import com.dlabeling.common.enums.ResponseCode;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/17
 */
public class FileMoveException extends FileException{

    public FileMoveException(){
        super("file.move.fail", null, ResponseCode.FILE_MOVE_FAIL.getMessage());
    }

    public FileMoveException(String msg){
        super("file.move.fail", null, msg);
    }
}
