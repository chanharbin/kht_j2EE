package com.kht.backend.dao;

import com.kht.backend.dataobject.MainDateDictDO;

public interface MainDateDictDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int deleteByPrimaryKey(Integer mainCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insert(MainDateDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insertSelective(MainDateDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    MainDateDictDO selectByPrimaryKey(Integer mainCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKeySelective(MainDateDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table main_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKey(MainDateDictDO record);
}