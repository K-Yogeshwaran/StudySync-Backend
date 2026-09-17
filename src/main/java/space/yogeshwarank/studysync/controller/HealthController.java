package space.yogeshwarank.studysync.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * This controller is sending the health status of this service
 * @since 24-08-2026
 */


@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "Gives the health of the backend")
    @GetMapping("/health")
    public Map<String, String> getHealthStatus(){
        return Map.of(
                "status", "UP",
                "service" , "studysync-backend"
        );
    }
}
