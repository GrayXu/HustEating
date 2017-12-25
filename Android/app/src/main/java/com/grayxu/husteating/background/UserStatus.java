package com.grayxu.husteating.background;

/**
 * Created by Administrator on 2017/12/22.
 * 本单例模式类用来救急，保证注册后的信息能够被抽屉头成功读取。
 */

public class UserStatus {

    private String name;
    private String email;
    private String sex;
    private boolean isLogin;
    private String province;
    private String major;

    public void clean(){
        name = null;
        email =null;
        email = null;
        sex = null;
        isLogin = false;
        province = null;
        major = null;
    }

    private static UserStatus userStatus = new UserStatus();

    public static UserStatus getUserStatus(){
        return userStatus;
    }

    private UserStatus(){}

    public void setProvince(String province) {
        this.province = province;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getProvince() {

        return province;
    }

    public String getMajor() {
        return major;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {

        return sex;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
