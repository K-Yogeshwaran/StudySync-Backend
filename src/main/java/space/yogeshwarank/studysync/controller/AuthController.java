package space.yogeshwarank.studysync.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.yogeshwarank.studysync.dto.LoginRequest;
import space.yogeshwarank.studysync.dto.LoginResponse;
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
        User user = authService.register(request);

        UserResponse userResponse = new UserResponse(user.getId(),
                                                    user.getName(),
                                                    user.getEmail(),
                                                    user.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


    @Operation(summary = "Logins the registered user", description = "Authenticates a user using email and password and returns a JWT Access Token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Login"),
            @ApiResponse(responseCode = "401" , description = "Invalid email or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest loginRequest
            ){
        LoginResponse loginResponse = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

}
