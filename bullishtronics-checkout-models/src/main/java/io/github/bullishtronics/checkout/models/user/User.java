package io.github.bullishtronics.checkout.models.user;


public class User {
    private final String username;
    private final String password;
    private final Role role;
    private final String address;
    private final String userFullName;

    public User(String username, String password, Role role, String address, String userFullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.userFullName = userFullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getUserFullName() {
        return userFullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", address='" + address + '\'' +
                ", userFullName='" + userFullName + '\'' +
                '}';
    }
}
