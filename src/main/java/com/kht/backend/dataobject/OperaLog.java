package com.kht.backend.dataobject;

public class OperaLog {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column opera_log.LOG_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private Integer logCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column opera_log.LOG_TIME
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private Long logTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column opera_log.EMPLOYEE_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String employeeCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column opera_log.OPERATED_OBJ
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String operatedObj;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column opera_log.OPERATION
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String operation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column opera_log.DESCRIPTION
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String description;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column opera_log.LOG_CODE
     *
     * @return the value of opera_log.LOG_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public Integer getLogCode() {
        return logCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column opera_log.LOG_CODE
     *
     * @param logCode the value for opera_log.LOG_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setLogCode(Integer logCode) {
        this.logCode = logCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column opera_log.LOG_TIME
     *
     * @return the value of opera_log.LOG_TIME
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public Long getLogTime() {
        return logTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column opera_log.LOG_TIME
     *
     * @param logTime the value for opera_log.LOG_TIME
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column opera_log.EMPLOYEE_CODE
     *
     * @return the value of opera_log.EMPLOYEE_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getEmployeeCode() {
        return employeeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column opera_log.EMPLOYEE_CODE
     *
     * @param employeeCode the value for opera_log.EMPLOYEE_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode == null ? null : employeeCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column opera_log.OPERATED_OBJ
     *
     * @return the value of opera_log.OPERATED_OBJ
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getOperatedObj() {
        return operatedObj;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column opera_log.OPERATED_OBJ
     *
     * @param operatedObj the value for opera_log.OPERATED_OBJ
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setOperatedObj(String operatedObj) {
        this.operatedObj = operatedObj == null ? null : operatedObj.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column opera_log.OPERATION
     *
     * @return the value of opera_log.OPERATION
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getOperation() {
        return operation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column opera_log.OPERATION
     *
     * @param operation the value for opera_log.OPERATION
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column opera_log.DESCRIPTION
     *
     * @return the value of opera_log.DESCRIPTION
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column opera_log.DESCRIPTION
     *
     * @param description the value for opera_log.DESCRIPTION
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}