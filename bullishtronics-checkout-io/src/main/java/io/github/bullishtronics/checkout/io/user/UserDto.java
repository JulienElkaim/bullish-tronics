package io.github.bullishtronics.checkout.io.user;


public class UserDto {
    private final String username;
    private final RoleDto role;
    private final String address;
    private final String userFullName;

    public UserDto(String username, RoleDto role, String address, String userFullName) {
        this.username = username;
        this.role = role;
        this.address = address;
        this.userFullName = userFullName;
    }

    public String getUsername() {
        return username;
    }


    public RoleDto getRole() {
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
                ", role=" + role +
                ", address='" + address + '\'' +
                ", userFullName='" + userFullName + '\'' +
                '}';
    }
}
