package space.yogeshwarank.studysync.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.yogeshwarank.studysync.dto.LoginRequest;
import space.yogeshwarank.studysync.dto.LoginResponse;
import space.yogeshwarank.studysync.dto.RegisterRequest;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.exception.EmailAlreadyExistsException;
import space.yogeshwarank.studysync.exception.InvalidCredentialException;
import space.yogeshwarank.studysync.repository.UserRepository;
import space.yogeshwarank.studysync.security.JwtService;


@Service
public class AuthService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public User register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists");

        User user = new User();
        user.setName(request.getName());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);


        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }


    /**
     * Logins the incoming login request
     *
     *
     * <p>The flow of the login check is
     *      -> LoginRequest reaches service layer
     *      -> Checks whether the user exists using email
     *      -> Checks if the password mathces
     *      -> Returns the user object
     * </p>
     *
     * @param loginRequest
     * @return  user
     *
     * @author Yogeshwaran K
     * @since 18.09.2026
     */
    public LoginResponse login(LoginRequest loginRequest){

       User user = userRepository.findByEmail(loginRequest.getEmail())
               .orElseThrow( () -> new InvalidCredentialException("Invalid Credentials") );

       if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
           throw new InvalidCredentialException("Invalid Credentials");
       }

       String accessToken = jwtService.generateToken(user);
       LoginResponse loginResponse = new LoginResponse();
       loginResponse.setAccessToken(accessToken);
       return loginResponse;
    }
}
