# api-platform-backend 后端
# api-platform-frontend 前端
## 1.1 需求分析

背景：

1. 前端开发需要用到后端接口
2. 使用现成的系统的功能（http://api.btstu.cn/)

做一个 API 接口平台：

1. 管理员可以对接口信息进行增删改查
2. 用户可以访问前台，查看接口信息

其他要求：

1. 防止攻击（安全性）
2. 不能随便调用（限制、开通）
3. 统计调用次数
4. 计费
5. 流量保护
6. API 接入
7. 预警

- 业务流程

![Screenshot 2024-03-17 at 5.00.51 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/21b776f6-7a05-4e4b-9889-a08296bad789/90a0f6de-c40e-42ee-b4c1-52579eca3a26/Screenshot_2024-03-17_at_5.00.51_PM.png)

## 1.2 技术选型

前端

- Ant Design Pro
- React
- Ant Design Procomponents
- Umi
- Umi Request （Axios 的封装）

后端

- Java Spring Boot
- Spring Boot Starter (SDK F*)
- Dubbo (RPC)
- Nacos
- Spring Cloud Gateway（网关、限流、日志实现）

## 1.3 项目搭建

1. 项目脚手架搭建（初始化）前端+后端
    - 前端：ant design pro 脚手架（[https://pro.ant.design/zh-CN/）](https://pro.ant.design/zh-CN/%EF%BC%89)
        - 管理员可以对接口增删改查
        - 用户可以访问前台，查看接口信息
    - 后端: 直接下载星球后端代码模板（见星球代码库：https://t.zsxq.com/08DElcxT7 => springboot-init 项目）
        - 建立数据库表：**SQL之父** 方便建表/生成模拟数据
2. 基础功能开发
    - 增删改查,登录功能(通过复制粘贴完成)
    - **前端接口调用**：后端使用遵循 openapi 的规范的**swagger** 文档，使用前端 Ant Design Pro 框架集成的 **openapi**插件自动生成。
