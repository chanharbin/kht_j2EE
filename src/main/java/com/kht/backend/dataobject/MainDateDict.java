package com.kht.backend.dataobject;

public class MainDateDict {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column main_data_dict.MAIN_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private Integer mainCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column main_data_dict.COL_NAME
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String colName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column main_data_dict.COL_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String colCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column main_data_dict.TAB_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    private String tabCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column main_data_dict.MAIN_CODE
     *
     * @return the value of main_data_dict.MAIN_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public Integer getMainCode() {
        return mainCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column main_data_dict.MAIN_CODE
     *
     * @param mainCode the value for main_data_dict.MAIN_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setMainCode(Integer mainCode) {
        this.mainCode = mainCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column main_data_dict.COL_NAME
     *
     * @return the value of main_data_dict.COL_NAME
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getColName() {
        return colName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column main_data_dict.COL_NAME
     *
     * @param colName the value for main_data_dict.COL_NAME
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setColName(String colName) {
        this.colName = colName == null ? null : colName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column main_data_dict.COL_CODE
     *
     * @return the value of main_data_dict.COL_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getColCode() {
        return colCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column main_data_dict.COL_CODE
     *
     * @param colCode the value for main_data_dict.COL_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setColCode(String colCode) {
        this.colCode = colCode == null ? null : colCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column main_data_dict.TAB_CODE
     *
     * @return the value of main_data_dict.TAB_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public String getTabCode() {
        return tabCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column main_data_dict.TAB_CODE
     *
     * @param tabCode the value for main_data_dict.TAB_CODE
     *
     * @mbg.generated Sun Jun 16 10:25:53 CST 2019
     */
    public void setTabCode(String tabCode) {
        this.tabCode = tabCode == null ? null : tabCode.trim();
    }
}