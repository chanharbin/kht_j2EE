package com.kht.backend.dao;


import com.kht.backend.dataobject.CustAcctDO;

public interface CustAcctDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(String custCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(CustAcctDO record);

    //TODO
    /**
     *
     * @param customerCode
     * @return
     */


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(CustAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    CustAcctDO selectByPrimaryKey(String custCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(CustAcctDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cust_acct
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(CustAcctDO record);
}