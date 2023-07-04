package io.github.bullishtronics.checkout.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.bullishtronics.checkout.models.user.Role;

import java.math.BigDecimal;

public class CreateProductDetailsMixIn {
    @JsonCreator
    public CreateProductDetailsMixIn(@JsonProperty("name") String name,
                                     @JsonProperty("price") BigDecimal price,
                                     @JsonProperty("description") String description) {
    }
}
