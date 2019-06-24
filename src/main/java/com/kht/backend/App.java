package com.kht.backend;

import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dataobject.EmployeeDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.kht.backend"})
@RestController
@MapperScan("com.kht.backend.dao")
public class App
{
    @Autowired
    private EmployeeDOMapper employeeDOMapper;
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class,args);
    }

}