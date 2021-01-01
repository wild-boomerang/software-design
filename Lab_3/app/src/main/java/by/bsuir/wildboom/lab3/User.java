package by.bsuir.wildboom.lab3;

import java.io.Serializable;

public class User implements Serializable {
    private String nickname;
    private String email;
    private String password;
    private String imageUrl;
    private String status;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
