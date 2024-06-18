package fontys.demo.Security.Token;

public interface AccessToken {
    String getSubject();

    Long getUserId();

    String getRoles();


}