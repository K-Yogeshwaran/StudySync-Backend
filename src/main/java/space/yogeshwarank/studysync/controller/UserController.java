package space.yogeshwarank.studysync.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.yogeshwarank.studysync.dto.UserResponse;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @Operation(summary = "Gets the current authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "For request after authentication and with correct jwt token"),
            @ApiResponse(responseCode = "403", description = "For requests after authentication but without jwt token "),
            @ApiResponse(responseCode = "401", description = "For request without authentication")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication){
        String userId = authentication.getPrincipal().toString();

        User user = userService.getUserById(UUID.fromString(userId));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(userResponse);
    }
}
