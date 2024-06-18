package fontys.demo.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
