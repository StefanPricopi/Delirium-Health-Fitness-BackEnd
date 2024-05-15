package fontys.demo.Security.Token;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Long getUserId();

    String getRoles();

    boolean hasRole(String roleName);
}