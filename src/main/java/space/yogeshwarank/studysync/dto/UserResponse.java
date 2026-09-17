package space.yogeshwarank.studysync.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response returned after successful user creation")
@Data
@AllArgsConstructor
public class UserResponse {

    private UUID id;

    private String name;

    private String email;

    private Instant createdAt;

}
