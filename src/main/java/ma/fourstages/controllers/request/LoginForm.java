package ma.fourstages.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginForm {
    @NotBlank
    @Size(min=3, max = 60)
    private String email;
	@NotBlank
    @Size(min = 6, max = 40)
    private String password;
}