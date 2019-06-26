package com.kht.backend.dataobject;

public class ImageDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.IMG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private Integer imgCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.ID_FRONT
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String idFront;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.ID_BACK
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String idBack;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column image.FACE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    private String face;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.IMG_CODE
     *
     * @return the value of image.IMG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public Integer getImgCode() {
        return imgCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.IMG_CODE
     *
     * @param imgCode the value for image.IMG_CODE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setImgCode(Integer imgCode) {
        this.imgCode = imgCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.ID_FRONT
     *
     * @return the value of image.ID_FRONT
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getIdFront() {
        return idFront;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.ID_FRONT
     *
     * @param idFront the value for image.ID_FRONT
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setIdFront(String idFront) {
        this.idFront = idFront == null ? null : idFront.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.ID_BACK
     *
     * @return the value of image.ID_BACK
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getIdBack() {
        return idBack;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.ID_BACK
     *
     * @param idBack the value for image.ID_BACK
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setIdBack(String idBack) {
        this.idBack = idBack == null ? null : idBack.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column image.FACE
     *
     * @return the value of image.FACE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public String getFace() {
        return face;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column image.FACE
     *
     * @param face the value for image.FACE
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    public void setFace(String face) {
        this.face = face == null ? null : face.trim();
    }

    @Override
    public String toString() {
        return "ImageDO{" +
                "imgCode=" + imgCode +
                ", idFront='" + idFront + '\'' +
                ", idBack='" + idBack + '\'' +
                ", face='" + face + '\'' +
                '}';
    }
}