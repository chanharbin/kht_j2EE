package com.kht.backend.dao;

import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.service.model.UserFromOrg;
import org.springframework.stereotype.Component;


import java.util.List;
@Component
public interface AcctOpenInfoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(Integer infoCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(AcctOpenInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(AcctOpenInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    AcctOpenInfoDO selectByPrimaryKey(Integer infoCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(AcctOpenInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table acct_open_info
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(AcctOpenInfoDO record);

    List<UserFromOrg> selectByOrgCode(String orgCode);
}