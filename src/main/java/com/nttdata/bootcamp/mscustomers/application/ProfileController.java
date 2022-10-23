package com.nttdata.bootcamp.mscustomers.application;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.mscustomers.interfaces.IProfileService;
import com.nttdata.bootcamp.mscustomers.model.Profile;

@RestController
public class ProfileController {

    @Autowired
    private IProfileService service;

    @PostMapping("/profiles")
    public ResponseEntity<?> create(@RequestBody List<Profile> profiles) {
        try {
            final List<Profile> response = service.createFromList(profiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/profiles")
    public ResponseEntity<?> findAll() {
        final List<Profile> response = service.findAll();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/profiles/{profile}")
    public ResponseEntity<?> findByProfile(@PathVariable String profile) {
        Optional<Profile> optProfile = service.findByProfile(profile);
        if (optProfile.isPresent()) {
            return ResponseEntity.ok(optProfile.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("No se encontró el perfil: %s", profile));
    }
}
