package com.kht.backend.dao;

import com.kht.backend.dataobject.AcctOpenInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
    AcctOpenInfoDO selectByInfoCode(int infoCode);

    AcctOpenInfoDO selectByUserCode(Integer userCode);

    List<AcctOpenInfoDO> listAll();
    List<AcctOpenInfoDO> listUnauditedUser();
    List<AcctOpenInfoDO> selectByEmployeeCodeAndStartTimeAndEndTime(@Param("employeeCode") String employeeCode,
                                                                    @Param("startTime") Long startTime,
                                                                    @Param("endTime")Long endTime);

    List<AcctOpenInfoDO> listAllByOrg(String orgCode);
    List<AcctOpenInfoDO> listUnauditedUserByOrg(String orgCode);
}