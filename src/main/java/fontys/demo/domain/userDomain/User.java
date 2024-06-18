package fontys.demo.domain.userDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String roles;
    private String email;
}
