package com.example.recipefront.service;

import com.example.recipefront.model.Authority;

import java.util.Set;

public class AuthorityService {

    public boolean isAUser(Object role){
        String authority = (String) role;
        if (authority.equals("ROLE_USER")){
            return true;
        }
        return false;
    }

    public boolean isAnAdmin(Object role){
        String authority = (String) role;
        if (authority.equals("ROLE_ADMIN")){
            return true;
        }
        return false;
    }

}
