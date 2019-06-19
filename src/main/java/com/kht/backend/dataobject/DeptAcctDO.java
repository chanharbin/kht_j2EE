package com.kht.backend.dataobject;

public class DeptAcctDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.DEP_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String depCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.CAP_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String capCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.BANK_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String bankType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.BANK_CARD_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String bankCardCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.OPEN_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long openTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.CLOSE_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long closeTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dep_acct.DEP_STATUS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String depStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.DEP_CODE
     *
     * @return the value of dep_acct.DEP_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getDepCode() {
        return depCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.DEP_CODE
     *
     * @param depCode the value for dep_acct.DEP_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setDepCode(String depCode) {
        this.depCode = depCode == null ? null : depCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.CAP_CODE
     *
     * @return the value of dep_acct.CAP_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getCapCode() {
        return capCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.CAP_CODE
     *
     * @param capCode the value for dep_acct.CAP_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setCapCode(String capCode) {
        this.capCode = capCode == null ? null : capCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.BANK_TYPE
     *
     * @return the value of dep_acct.BANK_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.BANK_TYPE
     *
     * @param bankType the value for dep_acct.BANK_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.BANK_CARD_CODE
     *
     * @return the value of dep_acct.BANK_CARD_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getBankCardCode() {
        return bankCardCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.BANK_CARD_CODE
     *
     * @param bankCardCode the value for dep_acct.BANK_CARD_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setBankCardCode(String bankCardCode) {
        this.bankCardCode = bankCardCode == null ? null : bankCardCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.OPEN_TIME
     *
     * @return the value of dep_acct.OPEN_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getOpenTime() {
        return openTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.OPEN_TIME
     *
     * @param openTime the value for dep_acct.OPEN_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.CLOSE_TIME
     *
     * @return the value of dep_acct.CLOSE_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getCloseTime() {
        return closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.CLOSE_TIME
     *
     * @param closeTime the value for dep_acct.CLOSE_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dep_acct.DEP_STATUS
     *
     * @return the value of dep_acct.DEP_STATUS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getDepStatus() {
        return depStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dep_acct.DEP_STATUS
     *
     * @param depStatus the value for dep_acct.DEP_STATUS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setDepStatus(String depStatus) {
        this.depStatus = depStatus == null ? null : depStatus.trim();
    }
}