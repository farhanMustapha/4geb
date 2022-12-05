package ma.fourstages.controllers.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponse {
	private String token;
	private String type;
	private String username;
	private String email;
	private Collection<String> authorities;
}