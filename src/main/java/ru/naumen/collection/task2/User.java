package ru.naumen.collection.task2;

import java.util.Arrays;
import java.util.Objects;

/**
 * Пользователь
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class User {
    private String username;
    private String email;
    private byte[] passwordHash;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.deepEquals(passwordHash, user.passwordHash);
    }

    /**
     * Переопределил хеш функцию, чтобы она искалась на основе содержимого User
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, email, Arrays.hashCode(passwordHash));
    }
}
