package com.opensef.auth.authz;

import java.util.List;

public interface PermissionHandler {

    /**
     * 获取角色
     *
     * @return 角色
     */
    List<String> getRoles(String loginId);

    /**
     * 获取权限
     *
     * @return 权限
     */
    List<String> getPermissions(String loginId);

}
