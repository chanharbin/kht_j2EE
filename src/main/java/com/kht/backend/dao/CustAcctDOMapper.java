package com.kht.backend.dao;

import com.kht.backend.dataobject.CustAcctDO;
import org.springframework.stereotype.Repository;

@Repository
public interface CustAcctDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int deleteByPrimaryKey(String custCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insert(CustAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int insertSelective(CustAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    CustAcctDO selectByPrimaryKey(String custCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKeySelective(CustAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    int updateByPrimaryKey(CustAcctDO record);
}