package com.kidd.api.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户视图（脱敏）
 * 在 Java 开发中，VO（Value Object）通常用于表示值对象，是一种用于封装数据的对象类型。VO
 * 通常用于表示领域模型中的数据结构，以便在不同的层之间传递数据。它与 DTO（Data Transfer
 * Object）类似，但通常更加重视领域模型的概念，因此可能包含一些领域逻辑或业务规则。
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}