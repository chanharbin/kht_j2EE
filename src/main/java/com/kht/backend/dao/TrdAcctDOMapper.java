package com.kht.backend.dao;

import com.kht.backend.dataobject.TrdAcctDO;

public interface TrdAcctDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trd_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int deleteByPrimaryKey(String trdCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trd_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insert(TrdAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trd_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insertSelective(TrdAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trd_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    TrdAcctDO selectByPrimaryKey(String trdCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trd_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKeySelective(TrdAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table trd_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKey(TrdAcctDO record);
}