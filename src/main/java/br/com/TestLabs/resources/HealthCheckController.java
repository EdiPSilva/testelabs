package br.com.TestLabs.resources;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/health-check", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthCheckController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping()
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
