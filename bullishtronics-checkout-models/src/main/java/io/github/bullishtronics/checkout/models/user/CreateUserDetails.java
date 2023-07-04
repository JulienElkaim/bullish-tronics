package io.github.bullishtronics.checkout.models.user;

public class CreateUserDetails {
    private final String username;
    private final String password;
    private final String address;
    private final Role role;
    private final String userFullName;

    public CreateUserDetails(String username, String password, String address, Role role, String userFullName) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.role = role;
        this.userFullName = userFullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }

    public String getUserFullName() {
        return userFullName;
    }
}
