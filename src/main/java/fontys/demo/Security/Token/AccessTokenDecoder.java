package fontys.demo.Security.Token;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}