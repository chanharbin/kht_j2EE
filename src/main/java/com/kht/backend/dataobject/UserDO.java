package com.kht.backend.dataobject;

public class UserDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.USER_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private Integer userCode;


    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.TELEPHONE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private Long telephone;


    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.PASSWORD
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String password;
    //userType 0 "用户" 1 "员工"
    private String userType;

    public UserDO() {
        this.userCode=0;
    }

    public UserDO(Integer userCode, Long telephone, String password,String userType) {
        this.userCode = userCode;
        this.telephone = telephone;
        this.password = password;
        this.userType=userType;
    }
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.USER_CODE
     *
     * @return the value of user.USER_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public Integer getUserCode() {
        return userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.USER_CODE
     *
     * @param userCode the value for user.USER_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.TELEPHONE
     *
     * @return the value of user.TELEPHONE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public Long getTelephone() {
        return telephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.TELEPHONE
     *
     * @param telephone the value for user.TELEPHONE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }


    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.PASSWORD
     *
     * @return the value of user.PASSWORD
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.PASSWORD
     *
     * @param password the value for user.PASSWORD
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}