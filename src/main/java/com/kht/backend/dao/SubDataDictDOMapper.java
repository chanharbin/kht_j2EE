package com.kht.backend.dao;

import com.kht.backend.dataobject.SubDataDictDO;

public interface SubDataDictDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sub_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int deleteByPrimaryKey(Integer subCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sub_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insert(SubDataDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sub_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insertSelective(SubDataDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sub_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    SubDataDictDO selectByPrimaryKey(Integer subCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sub_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKeySelective(SubDataDictDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sub_data_dict
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKey(SubDataDictDO record);
}