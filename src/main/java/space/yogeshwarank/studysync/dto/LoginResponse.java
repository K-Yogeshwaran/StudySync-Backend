package space.yogeshwarank.studysync.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Schema(description = "Response returned after successful login")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {

    @Schema(description = "JWT Token which is used to authorize the further requests")
    private String accessToken;
}
