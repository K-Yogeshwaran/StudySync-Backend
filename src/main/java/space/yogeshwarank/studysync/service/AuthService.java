package space.yogeshwarank.studysync.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.yogeshwarank.studysync.dto.RegisterRequest;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.exception.EmailAlreadyExistsException;
import space.yogeshwarank.studysync.repository.UserRepository;

@Service
public class AuthService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
