package ibrahim.compulynxtest.Auntentication.dao.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String username;
    @Column(length = 12)
    private String phone;
    @JsonIgnore
    private String password;
    @NotNull
    private String[] roles;
    @Column(nullable = false)
    private Long companyId;
}
