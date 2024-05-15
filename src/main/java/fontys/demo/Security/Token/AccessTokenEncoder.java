package fontys.demo.Security.Token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
