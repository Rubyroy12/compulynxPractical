package ibrahim.compulynxtest.Auntentication.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
    private String frstName;
    private String lastName;
    private String username;
    private String phone;
    private Long id;
    private List<String> roles;
}
