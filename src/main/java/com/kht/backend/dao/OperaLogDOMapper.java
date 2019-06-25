package com.kht.backend.dao;



import com.kht.backend.dataobject.OperaLogDO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OperaLogDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table opera_log
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int deleteByPrimaryKey(Integer logCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table opera_log
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insert(OperaLogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table opera_log
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int insertSelective(OperaLogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table opera_log
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    OperaLogDO selectByPrimaryKey(Integer logCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table opera_log
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKeySelective(OperaLogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table opera_log
     *
     * @mbg.generated Wed Jun 19 15:57:39 CST 2019
     */
    int updateByPrimaryKey(OperaLogDO record);

    List<OperaLogDO> listAll();

    List<OperaLogDO> selectByOperator(String operator);

    List<OperaLogDO> selectByTime(long startTime, long endTime);
}