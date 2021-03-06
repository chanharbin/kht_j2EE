package com.kht.backend.dao;


import com.kht.backend.dataobject.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(Integer userCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    UserDO selectByPrimaryKey(Integer userCode);

    /**
     *
     * @param telephone
     * @return UserDO
     */
    UserDO selectByTelephone(Long telephone);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(UserDO record);

    int selectUserCount();

}