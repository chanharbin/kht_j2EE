package com.kht.backend.dao;


import com.kht.backend.dataobject.PositionDO;
import org.springframework.stereotype.Component;

@Component
public interface PositionDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table position
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(Integer posCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table position
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(PositionDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table position
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(PositionDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table position
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    PositionDO selectByPrimaryKey(Integer posCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table position
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(PositionDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table position
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(PositionDO record);
}