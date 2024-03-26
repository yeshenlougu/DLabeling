package com.dlabeling.labeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.labeling.domain.po.InterfaceAddress;
import com.dlabeling.labeling.domain.vo.InterfaceAddressVO;
import com.dlabeling.labeling.domain.vo.InterfaceVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/23
 */
@Mapper
public interface InterfaceAddressMapper extends BaseMapper<InterfaceAddress> {

    void addInterfaceAddress(InterfaceAddress interfaceAddress);

    void updateInterfaceAddress(InterfaceAddress interfaceAddress);

    void deleteInterfaceAddressByID(Integer id);
    InterfaceAddress selectInterfaceAddressByID(Integer id);
    List<InterfaceAddressVO> selectInterfaceByObj(InterfaceAddress interfaceAddress);

    List<InterfaceVO> selectAllInterfaceVO();
}
