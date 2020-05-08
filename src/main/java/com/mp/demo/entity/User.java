package com.mp.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

@Data   //利用lombok来简化代码, 省去getter,seeter,tostring等
@TableName("user")  // 可以指定表, 不指定的话的表名位类名的首字母小写?
public class User extends Model<User> { // 继承Model, 实现AR模式
    //主键
    @TableId(type = IdType.ASSIGN_ID) // 可以用来指定主键, 但如果数据库中已经将其设为主键, 则可以不加
    private Long userId;
    //直属上级id
    private Long managerId;
    //姓名
//    @TableField(value = "name", condition = SqlCondition.LIKE) // 用来指定对应于数据库的那一列, 不写默认就是变量名
    private String name;
    //年龄
    @TableField(condition = "%s&lt;#{%s}")
    private Integer age;
    //邮箱
    private String email;
    //创建时间
    private LocalDateTime createTime;
    //备注, 临时数据, 不保存到数据库
//    private transient String remark;      // 方法一, 让其不参与序列化
//    private static String remark;         // 方法二, 将其设为static
    @TableField(exist = false)              // 方法三, 将其设为在表中不存在, very good
    private String remark;
}