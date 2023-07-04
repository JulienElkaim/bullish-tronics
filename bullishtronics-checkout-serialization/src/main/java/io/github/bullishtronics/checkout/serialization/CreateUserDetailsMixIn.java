package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.bullishtronics.checkout.models.user.Role;

public class CreateUserDetailsMixIn {
    @JsonCreator
    public CreateUserDetailsMixIn(@JsonProperty("username") String username,
                                     @JsonProperty("password") String password,
                                     @JsonProperty("address") String address,
                                     @JsonProperty("role") Role role,
                                     @JsonProperty("userFullName") String userFullName){}
}
