package com.springproject27.springproject.config;

import com.springproject27.springproject.user.User;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class UsersIdMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    public UsersIdMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean hasId(Long id){
        User user = (User)this.authentication.getPrincipal();
        return user.getId().equals(id);
    }

    @Override
    public void setFilterObject(Object filterObject) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }
}
