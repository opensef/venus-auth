# 简介

VenusAuth是一个轻量级的权限框架，实现执行身份认证、授权和会话管理功能，只需要少量代码便可轻松集成到项目中。

下面是一个最基本的示例：

```java
AuthToken authToken = AuthUtil.login("test");
System.out.println(authToken.getToken());
```



<div style="font-size:18pt; font-weight:bold; ">特性</div>

- 身份认证
- 角色鉴权
- 权限鉴权
- 少量配置

# 快速开始

## 环境要求

- 本项目基于JDK11构建，请使用JDK11及以上版本
- 推荐使用springboot 2.7以上版本

## 安装及配置

添加maven依赖

```xml
<dependency>
    <groupId>com.opensef</groupId>
    <artifactId>venus-auth-spring-boot3-starter</artifactId>
    <version>${venus-auth.version}</version>
</dependency>
```

# Demo示例

接下来通过一个demo，来演示venus-auth的基本使用方式。

> 本示例使用springboot3版本

<div style="font-size:16pt; font-weight:bold; ">1、新建一个springboot工程，添加pom依赖</div>

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
    <groupId>com.opensef</groupId>
        <artifactId>venus-auth-spring-boot3-starter</artifactId>
        <version>${venus-auth.version}</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </dependency>
</dependencies>
```

> 同时引入lombok依赖，用于自动生成Getter、Setter

<div style="font-size:16pt; font-weight:bold; ">2、配置</div>

```yaml
auth:
  # token名称，用于从Header或URL中解析token
  token-name: Authorization
  # token过期时间（单位：秒）
  timeout: 1800
  # token格式，默认为UUID
  auth.token-style: uuid
```

<div style="font-size:16pt; font-weight:bold; ">3、配置过滤器</div>

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

<div style="font-size:16pt; font-weight:bold; ">3、实现权限接口，获取角色和权限信息</div>

```java
@Component
public class AuthPermissionHandler implements PermissionHandler {

    @Override
    public List<String> getRoles(String loginId) {
        List<String> roles = new ArrayList<>();
        roles.add("aa");
        roles.add("bb");
        roles.add("cc");
        return roles;
    }

    @Override
    public List<String> getPermissions(String loginId) {
        List<String> permissions = new ArrayList<>();
        permissions.add("aa");
        return permissions;
    }

}
```



> 此时，一个基本的程序已经创建完成，下面模拟进行登录和鉴权操作。

```java
@RestController
public class AuthController {

    /**
     * 登录
     *
     * @return token
     */
    @GetMapping("/login")
    public String login() {
        return AuthUtil.login("test").getToken();
    }

    /**
     * 退出登录
     *
     * @return 操作结果
     */
    @GetMapping("/logout")
    public String logout() {
        AuthUtil.logout();
        return "SUCCESS";
    }

    /**
     * 认证测试，没有登录时返回401错误
     *
     * @return 认证通过，返回字符串
     */
    @GetMapping("/test/auth")
    public String testAuth() {
        return "Authorized";
    }

    /**
     * 前置条件：认证通过<br/>
     * 基于注解的角色鉴权测试，Logical.AND表示要同时满足全部的角色，否则返回403错误
     *
     * @return 鉴权通过，返回字符串
     */
    @CheckRole(value = {"aa", "bb"}, logical = Logical.AND)
    @GetMapping("/test/role_and")
    public String testRoleAnd() {
        return "role passed";
    }

    /**
     * 前置条件：认证通过<br/>
     * 基于注解的权限鉴权测试，不满足时返回403错误
     *
     * @return 鉴权通过，返回字符串
     */
    @CheckPermission("gg")
    @GetMapping("/test/permission")
    public String testPermission() {
        return "permission passed";
    }

    /**
     * 前置条件：认证通过<br/>
     * 基于注解的权限鉴权测试，Logical.OR表示满足其中一个权限即可通过，完全不满足时返回403错误
     *
     * @return 鉴权通过，返回字符串
     */
    @CheckPermission(value = {"aa", "bb", "cc"}, logical = Logical.OR)
    @GetMapping("/test/permission_or")
    public String testPermissionOr() {
        return "permission passed";
    }

    /**
     * 不需要登录也可访问
     *
     * @return 成功响应的内容
     */
    @GetMapping("/guest/test")
    public String guest() {
        return "guest test";
    }

}
```





------


<div style="font-size:14pt; font-weight:bold;">下一步</div>

通过上面的示例，向你展示了venus-auth的基本使用方法，接下来的章节将根据此示例，进一步介绍venus-auth的用法。

# 基础

## 认证拦截

认证URL拦截有2中方式，除了上面示例中的过滤器方式，还支持spring拦截器方式，这两种方式均可以实现URL的权限拦截，可以任选其一。

### 过滤器配置

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



### 拦截器配置

```
@Configuration
public class ProjectConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                // 拦截的路径
                .addPathPatterns("/**")
                // 排除的路径
                .excludePathPatterns("/login")
                .excludePathPatterns("/guest")
                .excludePathPatterns("/favicon.ico")
        ;
    }

}
```



## 鉴权

<div style="font-size:16pt; font-weight:bold; ">注解鉴权</div>

认证检查：@CheckLogin

角色检查：@CheckRole

权限检查：@Permission



<div style="font-size:16pt; font-weight:bold; ">代码鉴权</div>

认证检查：AuthUtil.checkLogin("test")

角色检查：AuthUtil.checkRole("admin")

认证检查：AuthUtil.checkPermission("user:edit")



## 自定义缓存

框架默认实现了内存缓存，没有实现其他缓存，因为自行实现很简单，只需要实现一个Cache接口。

> **下面是常用的Redis缓存实现。**

```java
@Component
public class AuthRedisCache implements Cache<String, Object> {

    private final RedisTemplate<String, Object> redisTemplate;

    public AuthRedisCache(RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        // 创建JSON序列化器
        RedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        this.redisTemplate = redisTemplate;
    }


    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object remove(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isUnExpired(String key) {
        return Cache.super.isUnExpired(key);
    }
```



<div style="font-weight:bold; color:red;">需要注意isUnExpired方法，此处默认是根据当前key的剩余过期时间判断，当key已经过期时，默认剩余过期时间返回-2，当你使用的缓存返回其他值时，你需要重写这个方法。</div>



## 异常

AuthException：框架最外层的异常，其他异常均继承此类

NoAuthenticationException：认证失败异常

NoPermissionException：权限不足异常



## AuthUtil

通过上面的介绍，你会发现经常出现AuthUtil，此类提供了框架的所有认证和鉴权操作，下面通过代码做详细说明。

```java
/**
 * 登录
 *
 * @param loginId 登录唯一标识
 * @return token信息
 */
public static AuthToken login(String loginId) {
	return authManager.login(loginId);
}

/**
 * 登录
 *
 * @param loginId 登录唯一标识
 * @param addInfo 附加信息，附加信息，可将此信息存储到token缓存中
 * @return token信息
 */
public static AuthToken login(String loginId, Map<String, Object> addInfo) {
	return authManager.login(loginId, addInfo);
}

/**
 * 退出登录<br/>
 * 自动从Header或URL中获取token值
 */
public static void logout() {
	authManager.logout();
}

/**
 * 退出登录
 *
 * @param loginId 登录唯一标识
 */
public static void logout(String loginId) {
	authManager.logout(loginId);
}

/**
 * 退出登录
 *
 * @param token token
 */
public static void logoutByToken(String token) {
	authManager.logoutByToken(token);
}

/**
 * 获取当前登录的token值（登录）信息
 *
 * @return token值信息
 */
public AuthTokenValue getTokenValue() {
	return authManager.getTokenValue();
}

/**
 * 获取token值信息
 *
 * @param token token
 * @return token值信息
 */
public static AuthTokenValue getTokenValue(String token) {
	return authManager.getTokenValue(token);
}

/**
 * 获取token剩余过期时间<br/>
 * 内存缓存和redis缓存时，数据不存在返回-2；永不过期返回-1；其他值表示剩余过期时间
 *
 * @param token token
 * @return token剩余过期时间（毫秒）
 */
public static Long getTokenExpire(String token) {
	return authManager.getTokenExpire(token);
}

/**
 * 获取session信息
 *
 * @param loginId 登录唯一标识
 * @return session信息
 */
public static AuthSession getSession(String loginId) {
	return authManager.getSession(loginId);
}

/**
 * 获取session信息
 *
 * @param token token
 * @return session信息
 */
public static AuthSession getSessionByToken(String token) {
	return authManager.getSessionByToken(token);
}

/**
 * 将数据存放到session中
 *
 * @param data 数据
 */
public static void setSessionData(String loginId, Object data) {
	authManager.setSessionData(loginId, data);
}

/**
 * 将数据存放到session中
 *
 * @param token token
 * @param data  数据
 */
public static <T> void setSessionDataByToken(String token, T data) {
	authManager.setSessionDataByToken(token, data);
}

/**
 * 从session中获取数据
 */
public static <T> T getSessionData() {
	return authManager.getSessionData();
}

/**
 * 更新token和session的过期时间
 *
 * @param token   token
 * @param loginId 登录唯一标识
 * @param timeout 过期时间（毫秒）
 */
public static void updateTokenAndSessionTimeout(String token, String loginId, long timeout) {
	authManager.updateTokenAndSessionTimeout(token, loginId, timeout);
}

/**
 * 更新token和session的过期时间
 */
public static void updateTokenAndSessionTimeout() {
	authManager.updateTokenAndSessionTimeout();
}

/**
 * 是否通过认证<br/>
 * 自动从Header或URL中获取token值，根据token做判断
 *
 * @return true/false
 */
public static boolean isLogin() {
	return authManager.isLogin();
}

/**
 * 是否通过认证
 *
 * @param loginId 登录唯一标识
 * @return true/false
 */
public static boolean isLogin(String loginId) {
	return authManager.isLogin(loginId);
}

/**
 * 是否通过认证
 *
 * @param token token
 * @return true/false
 */
public static boolean isLoginByToken(String token) {
	return authManager.isLoginByToken(token);
}

/**
 * 是否有某个角色
 *
 * @param role 角色
 * @return true/false
 */
public static boolean isHasRole(String role) {
	return authManager.isHasRole(role);
}

/**
 * 是否有输入的全部角色
 *
 * @param roles 角色集合
 * @return true/false
 */
public static boolean isHasRoleAnd(List<String> roles) {
	return authManager.isHasRoleAnd(roles);
}

/**
 * 是否有输入的全部角色中的其中一个
 *
 * @param roles 角色集合
 * @return true/false
 */
public static boolean isHasRoleOr(List<String> roles) {
	return authManager.isHasRoleOr(roles);
}

/**
 * 是否有某个权限
 *
 * @param permission 权限
 * @return true/false
 */
public static boolean isHasPermission(String permission) {
	return authManager.isHasPermission(permission);
}

/**
 * 是否有输入的全部权限
 *
 * @param permissions 权限集合
 * @return true/false
 */
public static boolean isHasPermissionAnd(List<String> permissions) {
	return authManager.isHasPermissionAnd(permissions);
}

/**
 * 是否有输入的全部权限的其中一个
 *
 * @param permissions 权限集合
 * @return true/false
 */
public static boolean isHasPermissionOr(List<String> permissions) {
	return authManager.isHasPermissionOr(permissions);
}

/**
 * 登录检查，检查不通过抛出异常<br/>
 * 自动从Header或URL中获取token值，根据token做判断
 */
public static void checkLogin() {
	authManager.checkLogin();
}

/**
 * 登录检查，检查不通过抛出异常<br/>
 *
 * @param loginId 登录唯一标识
 */
public static void checkLogin(String loginId) {
	authManager.checkLogin(loginId);
}

/**
 * 登录检查，检查不通过抛出异常
 *
 * @param token token
 */
public static void checkLoginByToken(String token) {
	authManager.checkLoginByToken(token);
}

/**
 * 角色检查，检查不通过抛出异常
 *
 * @param role 角色
 */
public static void checkRole(String role) {
	authManager.checkRole(role);
}

/**
 * 角色检查，是否有输入的全部角色，检查不通过抛出异常
 *
 * @param roles 角色集合
 */
public static void checkRoleAnd(List<String> roles) {
	authManager.checkRoleAnd(roles);
}

/**
 * 角色检查，是否有输入的角色中的其中一个角色，检查不通过抛出异常
 *
 * @param roles 角色集合
 */
public static void checkRoleOr(List<String> roles) {
	authManager.checkRoleOr(roles);
}

/**
 * 权限检查，检查不通过抛出异常
 *
 * @param permission 权限
 */
public static void checkPermission(String permission) {
	authManager.checkPermission(permission);
}

/**
 * 权限检查，是否有输入的全部权限，检查不通过抛出异常
 *
 * @param permissions 权限集合
 */
public static void checkPermissionAnd(List<String> permissions) {
	authManager.checkPermissionAnd(permissions);
}

/**
 * 权限检查，是否有输入的权限中的其中一个权限，检查不通过抛出异常
 *
 * @param permissions 权限集合
 */
public static void checkPermissionOr(List<String> permissions) {
	authManager.checkPermissionOr(permissions);
}
```

