package com.kht.backend.dao;

import com.remake.dataobject.AcctOpenInfoDO;

public interface AcctOpenInfoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Tue Jul 02 10:10:49 CST 2019
     */
    int deleteByPrimaryKey(Integer infoCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Tue Jul 02 10:10:49 CST 2019
     */
    int insert(AcctOpenInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Tue Jul 02 10:10:49 CST 2019
     */
    int insertSelective(AcctOpenInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Tue Jul 02 10:10:49 CST 2019
     */
    AcctOpenInfoDO selectByPrimaryKey(Integer infoCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Tue Jul 02 10:10:49 CST 2019
     */
    int updateByPrimaryKeySelective(AcctOpenInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Tue Jul 02 10:10:49 CST 2019
     */
    int updateByPrimaryKey(AcctOpenInfoDO record);
}