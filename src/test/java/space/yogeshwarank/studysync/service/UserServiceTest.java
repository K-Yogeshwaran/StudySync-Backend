package space.yogeshwarank.studysync.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.yogeshwarank.studysync.dto.UserResponse;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.exception.UserNotFoundException;
import space.yogeshwarank.studysync.repository.UserRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRespository;


    @Test
    public void getUserById_shouldReturnUser_whenUserExists(){

        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setId(uuid);
        user.setName("Admin");
        user.setEmail("admin@gmail.com");
        user.setPassword("admin1234");

        when(userRespository.findById(uuid)).thenReturn(Optional.of(user));

        User newUser = userService.getUserById(uuid);

        assertEquals(newUser.getId(), user.getId());
        assertEquals("Admin", newUser.getName());
        assertEquals("admin@gmail.com", newUser.getEmail());
        assertEquals("admin1234", newUser.getPassword());
    }

    @Test
    public void getUserById_shouldThrowUserNotFoundException_whenUserNotExists(){
        UUID uuid = UUID.randomUUID();

        when(userRespository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(uuid));
    }
}
