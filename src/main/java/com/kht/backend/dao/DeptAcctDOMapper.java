package com.kht.backend.dao;


import com.kht.backend.dataobject.DeptAcctDO;
import org.springframework.stereotype.Component;

@Component
public interface DeptAcctDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dep_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(String depCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dep_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(DeptAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dep_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(DeptAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dep_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    DeptAcctDO selectByPrimaryKey(String depCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dep_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(DeptAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dep_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(DeptAcctDO record);
}