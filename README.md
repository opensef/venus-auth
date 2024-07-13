# VenusAuth

## 简介

VenusAuth是一个轻量级的权限框架，实现执行身份认证、授权和会话管理功能，只需要少量代码便可轻松集成到项目中。

## 特性

- 身份认证
- 角色鉴权
- 权限鉴权
- 少量配置

## 快速开始

**1.添加maven依赖**

```xml

<dependency>
    <groupId>com.opensef</groupId>
    <artifactId>venus-auth-spring-boot3-starter</artifactId>
    <version>${venus-auth.version}</version>
</dependency>
```

**2.整合到spring项目**

```java
@Configuration
public class ProjectConfiguration {

    @Bean
    public FilterRegistrationBean<AuthFilter> registerAuthFilter() {
        AuthFilter authFilter = new AuthFilter()
                // 排除的路径
                .excludePath("/login", "/guest", "/favicon.ico");

        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authFilter);
        // 拦截的路径
        registration.addUrlPatterns("/*");
        registration.setName("authFilter");
        // 值越小，Filter越靠前
        registration.setOrder(-999999999);
        return registration;
    }

}
```

**3.实现登录**

```java
AuthToken authToken = AuthUtil.login("test");
System.out.println(authToken.getToken());
```



## 开发文档

详见[开发文档](./Document.md)

## License

[MIT © VenusAuth](./LICENSE)
