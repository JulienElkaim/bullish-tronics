package io.github.bullishtronics.checkout.app.security.impl;

import io.github.bullishtronics.checkout.core.user.StringEncrypter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class StringEncrypterImpl implements StringEncrypter {
    private final PasswordEncoder encoder;

    public StringEncrypterImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encrypt(String string) {
        return this.encoder.encode(string);
    }
}