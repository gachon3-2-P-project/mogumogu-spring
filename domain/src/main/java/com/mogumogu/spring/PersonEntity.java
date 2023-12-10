package com.mogumogu.spring;


import com.mogumogu.spring.constant.Role;

public interface PersonEntity {

    public Long getId();
    public String getUsername();

    public Role getRole();

    public String getPassword();

    public void setUsername(String username);


    public void setPassword(String password);
}
