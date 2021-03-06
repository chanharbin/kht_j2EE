package com.kht.backend.dataobject;

public class CustAcctDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.CUST_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String custCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.USER_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Integer userCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.IMG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Integer imgCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.ORG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String orgCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.NAME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.GENDER
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String gender;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.ID_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String idType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.ID_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String idCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.ID_EFF_DATE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long idEffDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.ID_EXP_DATE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long idExpDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.TELEPHONE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long telephone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.EMAIL
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.ADDRESS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.OCCUPATION
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String occupation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.COMPANY
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String company;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.EDUCATION
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String education;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.INVESTOR_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String investorType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.OPEN_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long openTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.CLOSE_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Long closeTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cust_acct.CUST_STATUS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String custStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.CUST_CODE
     *
     * @return the value of cust_acct.CUST_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getCustCode() {
        return custCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.CUST_CODE
     *
     * @param custCode the value for cust_acct.CUST_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setCustCode(String custCode) {
        this.custCode = custCode == null ? null : custCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.USER_CODE
     *
     * @return the value of cust_acct.USER_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Integer getUserCode() {
        return userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.USER_CODE
     *
     * @param userCode the value for cust_acct.USER_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.IMG_CODE
     *
     * @return the value of cust_acct.IMG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Integer getImgCode() {
        return imgCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.IMG_CODE
     *
     * @param imgCode the value for cust_acct.IMG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setImgCode(Integer imgCode) {
        this.imgCode = imgCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.ORG_CODE
     *
     * @return the value of cust_acct.ORG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.ORG_CODE
     *
     * @param orgCode the value for cust_acct.ORG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.NAME
     *
     * @return the value of cust_acct.NAME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.NAME
     *
     * @param name the value for cust_acct.NAME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.GENDER
     *
     * @return the value of cust_acct.GENDER
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getGender() {
        return gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.GENDER
     *
     * @param gender the value for cust_acct.GENDER
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.ID_TYPE
     *
     * @return the value of cust_acct.ID_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getIdType() {
        return idType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.ID_TYPE
     *
     * @param idType the value for cust_acct.ID_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setIdType(String idType) {
        this.idType = idType == null ? null : idType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.ID_CODE
     *
     * @return the value of cust_acct.ID_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getIdCode() {
        return idCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.ID_CODE
     *
     * @param idCode the value for cust_acct.ID_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setIdCode(String idCode) {
        this.idCode = idCode == null ? null : idCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.ID_EFF_DATE
     *
     * @return the value of cust_acct.ID_EFF_DATE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getIdEffDate() {
        return idEffDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.ID_EFF_DATE
     *
     * @param idEffDate the value for cust_acct.ID_EFF_DATE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setIdEffDate(Long idEffDate) {
        this.idEffDate = idEffDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.ID_EXP_DATE
     *
     * @return the value of cust_acct.ID_EXP_DATE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getIdExpDate() {
        return idExpDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.ID_EXP_DATE
     *
     * @param idExpDate the value for cust_acct.ID_EXP_DATE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setIdExpDate(Long idExpDate) {
        this.idExpDate = idExpDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.TELEPHONE
     *
     * @return the value of cust_acct.TELEPHONE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getTelephone() {
        return telephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.TELEPHONE
     *
     * @param telephone the value for cust_acct.TELEPHONE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.EMAIL
     *
     * @return the value of cust_acct.EMAIL
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.EMAIL
     *
     * @param email the value for cust_acct.EMAIL
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.ADDRESS
     *
     * @return the value of cust_acct.ADDRESS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.ADDRESS
     *
     * @param address the value for cust_acct.ADDRESS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.OCCUPATION
     *
     * @return the value of cust_acct.OCCUPATION
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.OCCUPATION
     *
     * @param occupation the value for cust_acct.OCCUPATION
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation == null ? null : occupation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.COMPANY
     *
     * @return the value of cust_acct.COMPANY
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getCompany() {
        return company;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.COMPANY
     *
     * @param company the value for cust_acct.COMPANY
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.EDUCATION
     *
     * @return the value of cust_acct.EDUCATION
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getEducation() {
        return education;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.EDUCATION
     *
     * @param education the value for cust_acct.EDUCATION
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.INVESTOR_TYPE
     *
     * @return the value of cust_acct.INVESTOR_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getInvestorType() {
        return investorType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.INVESTOR_TYPE
     *
     * @param investorType the value for cust_acct.INVESTOR_TYPE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setInvestorType(String investorType) {
        this.investorType = investorType == null ? null : investorType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.OPEN_TIME
     *
     * @return the value of cust_acct.OPEN_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getOpenTime() {
        return openTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.OPEN_TIME
     *
     * @param openTime the value for cust_acct.OPEN_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.CLOSE_TIME
     *
     * @return the value of cust_acct.CLOSE_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Long getCloseTime() {
        return closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.CLOSE_TIME
     *
     * @param closeTime the value for cust_acct.CLOSE_TIME
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cust_acct.CUST_STATUS
     *
     * @return the value of cust_acct.CUST_STATUS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getCustStatus() {
        return custStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cust_acct.CUST_STATUS
     *
     * @param custStatus the value for cust_acct.CUST_STATUS
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus == null ? null : custStatus.trim();
    }
}