package space.yogeshwarank.studysync.controller;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import space.yogeshwarank.studysync.dto.RegisterRequest;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.service.AuthService;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_shouldReturn201_whenRequestIsValid() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Yogeshwaran K");
        registerRequest.setEmail("yogeshwarank@gmail.com");
        registerRequest.setPassword("yogesh1234");

        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        user.setName("Yogeshwaran K");
        user.setEmail("yogeshwarank@gmail.com");
        user.setPassword("hashed-password");


        when(authService.register(any(RegisterRequest.class))).thenReturn(user);

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("Yogeshwaran K"))
                .andExpect(jsonPath("$.email").value("yogeshwarank@gmail.com"))
                .andExpect(jsonPath("$.password").doesNotExist());

    }

}
