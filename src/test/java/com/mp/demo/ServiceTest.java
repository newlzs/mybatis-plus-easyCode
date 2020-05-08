package com.mp.demo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mp.demo.Service.UserService;
import com.mp.demo.entity.User;
import com.mp.demo.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void updateById() {
        User user = new User();
        user.setUserId(110L);
        user.setAge(100);
        boolean update = userService.updateById(user);
        System.out.println(update);
    }
    @Test
    public void getOne() {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge,25), false);
        System.out.println(user);
    }

    @Test
    public void Batch() {
        User user1 = new User();
        user1.setName("徐丽");
        user1.setAge(28);

        User user2 = new User();
        user2.setUserId(1258619516367831042L);
        user2.setName("徐丽丽");
        user2.setAge(19);

        List<User> userList = Arrays.asList(user1, user2);
        boolean saveBatch = userService.saveOrUpdateBatch(userList);
        System.out.println(userService);
    }

    @Test
    public void chain() {
        List<User> userList = userService.lambdaQuery().gt(User::getAge, 25).like(User::getName, "子").list();
        userList.forEach(System.out::println);
    }
    @Test
    public void chain1() {
        boolean update = userService.lambdaUpdate().eq(User::getAge, 25).set(User::getAge, 26).update();
        System.out.println(update);
    }
    @Test
    public void chain2() {
        boolean remove = userService.lambdaUpdate().eq(User::getAge, 15).remove();
        System.out.println(remove);
    }

}