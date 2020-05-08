package com.mp.demo;

import com.mp.demo.entity.User;
import com.mp.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ARTest {
    @Test
    public void testSelect() {
       User user = new User();
       user.setName("张艹");
       user.setAge(21);
       user.setEmail("sb@a.xyz");
       user.setManagerId(1L);
       user.setCreateTime(LocalDateTime.now());
       boolean insert = user.insert();
       System.out.println(insert);
    }

    @Test
    public void selectById() {
        User user = new User();
        User userSelect = user.selectById(1);
        System.out.println(userSelect == user);
        System.out.println(userSelect);
    }

    @Test
    public void selectById2() {
        User user = new User();
        user.setUserId(1L);
        User userSelect = user.selectById();
        System.out.println(userSelect == user);
        System.out.println(userSelect);
    }

    @Test
    public void updateById() {
        User user = new User();
        user.setUserId(1L);
        user.setName("张三");
        boolean update = user.updateById();
        System.out.println(update);
    }

    @Test
    public void deleteById() {
        User user = new User();
        user.setUserId(1256869506412351505L);
        user.setName("张三");
        boolean del = user.updateById();
        System.out.println(del);
    }
    @Test
    public void insertOrUpdate() {
        User user = new User();
//        user.setUserId(1256869506412351505L); // 没写或者查不到会执行insert, 否则执行update
        user.setName("黑铁");
        user.setManagerId(1L);
        user.setCreateTime(LocalDateTime.now());
        boolean del = user.insertOrUpdate();
        System.out.println(del);
    }

}