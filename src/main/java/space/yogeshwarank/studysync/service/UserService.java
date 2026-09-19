package space.yogeshwarank.studysync.service;


import org.springframework.stereotype.Service;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.exception.UserNotFoundException;
import space.yogeshwarank.studysync.repository.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public User getUserById(UUID uuid){
        return userRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
