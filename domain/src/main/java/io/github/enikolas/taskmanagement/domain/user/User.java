package io.github.enikolas.taskmanagement.domain.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private UserId id;
    private String fullName;
    private Email email;
    private PasswordHash passwordHash;

    public User(String fullName,
                Email email,
                PasswordHash passwordHash) {
        this.id = UserId.newId();
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
