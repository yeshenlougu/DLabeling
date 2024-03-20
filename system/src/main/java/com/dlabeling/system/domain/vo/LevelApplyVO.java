package com.dlabeling.system.domain.vo;

import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.enums.LevelApplyStatus;
import com.dlabeling.system.enums.LevelApplyType;
import lombok.Data;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/12
 */
@Data
public class LevelApplyVO {

    private int id;

    private int applyer;
    private String applyerName;

    private String type;

    private String privilege;
    private String privilegeName;

    private String status;

    private int judger;
    private String jugerName;

    private Boolean approval;

    public static LevelApply convertToLevelApply(LevelApplyVO levelApplyVO){
        LevelApply levelApply = new LevelApply();
        levelApply.setId(levelApplyVO.getId());

        LevelApplyStatus levelApplyStatus = LevelApplyStatus.getLevelApplyStatusByMsg(levelApplyVO.getStatus());
        levelApply.setStatus( levelApplyStatus == null?LevelApplyStatus.APPLYING.getCode():levelApplyStatus.getCode());
        levelApply.setType(LevelApplyType.getLevelApplyTypeByType(levelApplyVO.getType()) !=null ? LevelApplyType.getLevelApplyTypeByType(levelApplyVO.getType()).getCode() : LevelApplyType.USER_APPLY.getCode());

        levelApply.setJudger(levelApplyVO.getJudger());
        levelApply.setApplyer(levelApplyVO.getApplyer());
        levelApply.setPrivilege(Integer.parseInt(levelApplyVO.getPrivilege()));

        return levelApply;
    }

    public static LevelApplyVO converToLevelApplyVO(LevelApply levelApply){
        LevelApplyVO levelApplyVO = new LevelApplyVO();
        levelApplyVO.setId(levelApply.getId());
        levelApplyVO.setApplyer(levelApply.getApplyer());
        levelApplyVO.setType(LevelApplyType.getLevelApplyTypeByCode(levelApply.getType()).getType());
        levelApplyVO.setPrivilege(String.valueOf(levelApply.getPrivilege()));
        levelApplyVO.setStatus(LevelApplyStatus.getLevelApplyStatusByCode(levelApply.getStatus()).getMsg());

        return levelApplyVO;
    }
}
