package com.mp.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mp.demo.entity.User;
import com.mp.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Retrieve {

    @Autowired
    private UserMapper userMapper;
    // 单个id
    @Test
    public void selectById() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }
    // 多个id
    @Test
    public void selectByIds() {
        List<Long> idList = (List<Long>) Arrays.asList(1L, 2L, 1256869506412351492L);
        List<User> userList = userMapper.selectBatchIds(idList);
        userList.forEach(System.out::println);
    }
    // 多个列值
    @Test
    public void selectByMap() {
        //map.put("name", "李子实") key 位列名, 不是属性名
        //map.put("age", 12)
        // where name = "李子实" and age = 12
        Map<String, Object> columnMap = new HashMap<>();
//        columnMap.put("name", "lzs");
        columnMap.put("age", 15);
        List<User> userList = userMapper.selectByMap(columnMap);
        userList.forEach(System.out::println);
    }

    /**
     * name like "%刚%" and age < 40
     */
    @Test
    public void selectByWrapper1() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        QueryWrapper<User> query = Wrappers.<User>query();
        queryWrapper.like("name", "刚").lt("age", 40);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * name like "%刚%" and age between 20 and 40 and email is not null
     */
    @Test
    public void selectByWrapper2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name", "刚").between("age", 20, 40).isNotNull("email");

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * name like "王%" or age >= 40 order by age desc,id asc
     */
    @Test
    public void selectByWrapper3() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王").or().ge("age", 40).orderByDesc("age")
                .orderByAsc("user_id");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 创建日期wei2020年5月4日, 并且直属上级名字首字母为T
     * date_format(create, '%Y-%m-%d') and manager_id in (select user_id from user where name like 'T%')
     */
    @Test
    public void selectByWrapper4() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        queryWrapper.apply("date_format(create_time, '%Y-%m-%d')={0}", "2020-05-04")
        queryWrapper.apply("date_format(create_time, '%Y-%m-%d')='2020-05-04'u or true or tre") // 容易导致sql注入
                .inSql("manager_id", "select user_id from user where name like 'T%'");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓, 并且(年龄小于四十或邮箱不为空)
     * name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectByWrapper5() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王").
                and(wq->wq.lt("age",40).or().isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓或者(年龄小于40岁并且年龄大于二十且邮箱不为空
     * name like '王%' or (age < 40 and age > 20 and email is not null)
     */
    @Test
    public void selectByWrapper6() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王").or(wq->wq.lt("age", 40)
            .gt("age", 20).isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * (年龄小于40或者邮箱不为空)并且名字为王姓
     * (age < 40 or email is not null) and name like '王%'
     */
    @Test
    public void selectByWrapper7() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.nested(wq->wq.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 年龄为21,31,35,34
     * age in (21,31,35,34)
     */
    @Test
    public void selectByWrapper8() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("age", Arrays.asList(21,31,35,34));

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 返回只满足条件的一条
     */
    @Test
    public void selectByWrapper9() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//  小心sql注入
        queryWrapper.in("age", Arrays.asList(21,31,35,34)).last("limit 1");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
//选择指定列
    /**
     * select user_id, name from user where name like "%刚%" and age < 40
     */
    @Test
    public void selectByWrapperSupper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        QueryWrapper<User> query = Wrappers.<User>query();
        queryWrapper.select("user_id","name").like("name", "刚").lt("age", 40);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
//    排除指定列
    @Test
    public void selectByWrapperSupper1() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        QueryWrapper<User> query = Wrappers.<User>query();
        queryWrapper.select("user_id","name").like("name", "刚").lt("age", 40)
                .select(User.class, info->!info.getColumn().equals("create_time")&&
                        !info.getColumn().equals("manager_id"));

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

//    添加执行条件的like
    @Test
    public void testCondition() {
        String name = "王";
        String email = "";
        condition(name, email);
    }
    private void condition(String name, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        if(StringUtils.isNotBlank(name)) {
//            queryWrapper.like("name", name);
//        }
//        if(StringUtils.isNotBlank(email)) {
//            queryWrapper.like("email", email);
//        }
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(email), "email", email);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

//    利用实体做为条件
    @Test
    public void selectByWrapperEntity() {
        User whereUser = new User();
        whereUser.setName("李子实");
        whereUser.setAge(40);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(whereUser);
        // 和下面的两个互不干扰, 都会添加到最后的查询语句中
//        queryWrapper.like("name", "子").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
// allEq
    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "李子实");
        params.put("age", null);
//        queryWrapper.allEq(params, false); // 配置false, 表示为null不加入查询
        queryWrapper.allEq(((k,v)->!k.equals("name")), params); // 指定某些key/value不加入查询
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
    // 返回map类型 Maps
    @Test
    public void selectByWrapperMaps() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        // 应用场景1, 当选则的列数比较少使,致 返回实体会导,实体中很多空值
        queryWrapper.select("user_id", "name").like("name", "刚").lt("age", 40);
        // 此时利用返回map, 比较简洁
        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }
// 应用二, 字段统计结果
    /**
     * 按照直属上级分组, 查询每组的平均年龄, 最大年龄, 最小年龄 并且只取年龄总和小于500的组
     * select avg(age) avg_age, min(age) min_age, max(age) max_age
     * from user
     * group by manager_id
     * having sum(age) < 500
     */
    @Test
    public void selectByWrapperMaps2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
            .groupBy("manager_id").having("sum(age)<{0}", 500);
        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

// selectObjs 只返回第一个字段(即第一列)
    @Test
    public void selectByWrapperObjs() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.select("user_id", "name").like("name", "刚").lt("age", 40);

        List<Object> userList = userMapper.selectObjs(queryWrapper);
        userList.forEach(System.out::println);
    }

    // 返回记录数 selectCount
    @Test
    public void selectByWrapperCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name", "刚").lt("age", 40);

        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println("总记录数 " + count);
    }

//      要求查询结果只用一条, 而不是取第一条
    @Test
    public void selectByWrapperOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name", "子").lt("age", 40);

        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }
//    lambda 条件构造器
    @Test
    public void selectLambda() {
        // 三种皆可创建lambda条件构造器
//        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.likeRight(User::getName, "王")
                .and(lqw->lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        // where name like "%刚%" and age < 40
        List<User> userList = userMapper.selectList(lambdaQuery);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectLambda3() {
        List<User> userList = new LambdaQueryChainWrapper<User>(userMapper)
                .like(User::getName, "刚").ge(User::getAge, 20).list();
        userList.forEach(System.out::println);
    }

    @Test
    public void selectMy() {
        // 三种皆可创建lambda条件构造器
//        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.likeRight(User::getName, "王")
                .and(lqw->lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        // where name like "%刚%" and age < 40
        List<User> userList = userMapper.selectAll(lambdaQuery);
        userList.forEach(System.out::println);
    }

//    分页
    @Test
    public void selectPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.ge("age", 15);

//        Page<User> page = new Page<User>(1,2);
//        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
//        System.out.println("总页数" + iPage.getPages());
//        System.out.println("总记录数" + iPage.getTotal());
//        List<User> userList = iPage.getRecords();

//      添加false 表示不查询总记录数
        IPage<Map<String, Object>> page = new Page<>(1, 2, false);
        IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(page, queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<Map<String, Object>> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }

//    自定义分页查询
    @Test
    public void selectMyPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.ge("age", 15);

        Page<User> page = new Page<User>(1,2);
        IPage<User> iPage = userMapper.selectUserPage(page, queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<User> userList = iPage.getRecords();
    }
}