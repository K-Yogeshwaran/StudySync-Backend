package space.yogeshwarank.studysync.controller;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthControllerTest {

    @Test
    public void healthShouldReturnUp(){
        HealthController healthController = new HealthController();

        var response = healthController.getHealthStatus();

        assertEquals("UP", response.get("status"));
        assertEquals("studysync-backend", response.get("service"));
    }
}
