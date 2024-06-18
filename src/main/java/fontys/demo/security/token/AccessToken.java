package fontys.demo.security.token;

public interface AccessToken {
    String getSubject();

    Long getUserId();

    String getRoles();

    boolean hasRole(String roleName);
}