package com.kidd.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * api接口信息
 */
@TableName(value = "interface_info")
@Data
public class InterfaceInfo implements Serializable {

    /*
     * 用于标记类的实例可以被序列化为字节流，以便将对象保存到文件、传输到网络或在进程之间进行通信。
     * 实现 Serializable 接口的类可以被 Java 序列化机制处理，从而可以将对象转换为字节流或从字节流恢复对象。
     * 为了确保反序列化时类的版本兼容性，可以使用 serialVersionUID 字段来控制类的版本。
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     * [
     * {"name": "username", "type": "string"}
     * ]
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     * sql自动处理更新
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDelete;

}