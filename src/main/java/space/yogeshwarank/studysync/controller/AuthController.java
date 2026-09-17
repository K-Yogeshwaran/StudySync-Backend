package space.yogeshwarank.studysync.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.yogeshwarank.studysync.dto.RegisterRequest;
import space.yogeshwarank.studysync.dto.UserResponse;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.service.AuthService;


/**
 * Exposes the endpoints related to authentication
 *
 * List of exposed endpoints
 * 1. /api/auth/register -> For registration of a user
 *
 * @since 17.09.2026
 * @author Yogeshwaran K
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @Operation(summary = "Adds a new user", description = "Creates a new studysync user account")
    @ApiResponses({
            @ApiResponse(responseCode =  "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterRequest request){
        System.out.println("Controller reached");
        User user = authService.register(request);

        UserResponse userResponse = new UserResponse(user.getId(),
                                                    user.getName(),
                                                    user.getEmail(),
                                                    user.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

}
