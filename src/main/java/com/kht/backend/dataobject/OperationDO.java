package com.kht.backend.dataobject;

public class OperationDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation.OPERA_CODE
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    private Integer operaCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation.OPERA_NAME
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    private String operaName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column operation.MENU_CODE
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    private String menuCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation.OPERA_CODE
     *
     * @return the value of operation.OPERA_CODE
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    public Integer getOperaCode() {
        return operaCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation.OPERA_CODE
     *
     * @param operaCode the value for operation.OPERA_CODE
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    public void setOperaCode(Integer operaCode) {
        this.operaCode = operaCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation.OPERA_NAME
     *
     * @return the value of operation.OPERA_NAME
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    public String getOperaName() {
        return operaName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation.OPERA_NAME
     *
     * @param operaName the value for operation.OPERA_NAME
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    public void setOperaName(String operaName) {
        this.operaName = operaName == null ? null : operaName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column operation.MENU_CODE
     *
     * @return the value of operation.MENU_CODE
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    public String getMenuCode() {
        return menuCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column operation.MENU_CODE
     *
     * @param menuCode the value for operation.MENU_CODE
     *
     * @mbg.generated Thu Jun 20 16:40:33 CST 2019
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }
}