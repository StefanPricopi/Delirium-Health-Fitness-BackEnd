package fontys.demo.Security.Token.impl;

import fontys.demo.Security.Token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

