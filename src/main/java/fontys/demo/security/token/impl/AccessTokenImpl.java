package fontys.demo.security.token.impl;

import fontys.demo.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter

public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final String roles;

    public AccessTokenImpl(String subject, Long userId, String roles) {
        this.subject = subject;
        this.userId = userId;
        this.roles = roles;
    }



    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}

