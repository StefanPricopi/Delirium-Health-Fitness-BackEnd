package fontys.demo.security.token;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}