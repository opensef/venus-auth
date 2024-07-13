package com.opensef.auth.authz;

import java.util.List;

public class DefaultPermissionHandler implements PermissionHandler {


    @Override
    public List<String> getRoles(String loginId) {
        return null;
    }

    @Override
    public List<String> getPermissions(String loginId) {
        return null;
    }

}
