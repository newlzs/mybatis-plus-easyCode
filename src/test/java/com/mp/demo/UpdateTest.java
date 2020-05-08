package com.mp.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.mp.demo.entity.User;
import com.mp.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateById() {
        User user = new User();
        user.setUserId(1L);
        user.setAge(60);
        user.setEmail("fuck@you.com");
        int rows = userMapper.updateById(user);
        System.out.println(rows);
    }
// updateWrapper 更新数据库的条件构造器
    @Test
    public void updateByWrapper() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>();
        updateWrapper.eq("name", "李子实").eq("age", 15);
        User user = new User();
        user.setAge(26);
        user.setEmail("lzs@qq.com");
        int rows = userMapper.update(user, updateWrapper);
        System.out.println(rows);
    }

//    实体条件
    @Test
    public void updateByWrapper1() {
        User whereUser = new User();
        whereUser.setName("李子实");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>(whereUser);
        updateWrapper.eq("name", "李子实").eq("age", 15);
        User user = new User();
        user.setAge(26);
        user.setEmail("lzs@qq.com");
        int rows = userMapper.update(user, updateWrapper);
        System.out.println(rows);
    }
//  直接使用条件构造器的set方法, 调整属性值
    @Test
    public void updateByWrapper2() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>();
        updateWrapper.eq("name", "李子实").eq("age", 26)
                .set("age", 27);

        int rows = userMapper.update(null, updateWrapper);
        System.out.println(rows);
    }
//  基于函数的条件构造器
    @Test
    public void updateByWrapperLambda() {
        LambdaUpdateWrapper<User> lambdaUpdate = Wrappers.<User>lambdaUpdate();
        lambdaUpdate.eq(User::getName, "李子实").eq(User::getAge,27).set(User::getAge, 25);

        int rows = userMapper.update(null, lambdaUpdate);
        System.out.println(rows);
    }
//  chainWrapper
    @Test
    public void updateByWrapperLambdaChain() {
        boolean update = new LambdaUpdateChainWrapper<User>(userMapper)
                .eq(User::getName, "李子实").eq(User::getAge,25).set(User::getAge, 26).update();

        System.out.println(update);
    }
}