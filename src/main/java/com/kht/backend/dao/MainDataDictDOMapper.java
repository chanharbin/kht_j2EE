package com.kht.backend.dao;


import com.kht.backend.dataobject.MainDataDictDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MainDataDictDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(Integer mainCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(MainDataDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(MainDataDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    MainDataDictDO selectByPrimaryKey(Integer mainCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(MainDataDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(MainDataDictDO record);

    List<MainDataDictDO> listAll();

    MainDataDictDO selectByColCodeAndTabCode(@Param("colCode") String colCode,@Param("tabCode") String tabCode);
}