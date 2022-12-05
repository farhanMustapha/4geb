package ma.fourstages.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpForm {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 40)
    private String city;

    @NotBlank
    @Size(min = 6, max = 40)
    private String phone;

    @NotBlank
    @Size(min = 6, max = 40)
    private String studyLevel;

    @NotBlank
    @Size(min = 6, max = 40)
    private String field;

}