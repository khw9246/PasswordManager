package com.cookandroid.password;

public class PasswordModel {
    private int id;
    private String siteName;
    private String userId;
    private String password;

    // 생성자 (새로운 데이터를 DB에 입력할 때 사용 - ID 불필요)
    public PasswordModel(String siteName, String userId, String password) {
        this.siteName = siteName;
        this.userId = userId;
        this.password = password;
    }

    // 생성자 (DB에서 데이터를 조회할 때 사용 - ID 포함)
    public PasswordModel(int id, String siteName, String userId, String password) {
        this.id = id;
        this.siteName = siteName;
        this.userId = userId;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
