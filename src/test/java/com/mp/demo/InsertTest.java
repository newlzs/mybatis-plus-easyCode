package com.mp.demo;


import com.mp.demo.entity.User;
import com.mp.demo.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        System.out.println(("----- Insert method test ------"));
        User user = new User();
        user.setUserId(111L);
        user.setName("李子实");
        user.setAge(21);
        user.setEmail("lzs.online@foxmail.com");
        user.setCreateTime(LocalDateTime.now());
        user.setRemark("我是一个莫的感情的备注");
        user.setManagerId(0L);
        int rows = userMapper.insert(user);
        System.out.println("影响记录数: " + rows+ " UserId: " + user.getUserId());
    }
}