package com.example.ppis.controller;

import com.example.ppis.dto.RoleDTO;
import com.example.ppis.model.Role;
import com.example.ppis.model.SkillType;
import com.example.ppis.repository.SkillTypeRepository;
import com.example.ppis.service.SkillTypeService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

@RestController
public class SkillTypeController {

    @Autowired
    SkillTypeService skillTypeService;
    private final Bucket bucket2;

    public SkillTypeController() {

        Bandwidth limit2 = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        this.bucket2 = Bucket4j.builder()
                .addLimit(limit2)
                .build();
    }

    @GetMapping("/skill-types")
    ResponseEntity<List<SkillType>> getAll() {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(skillTypeService.getAll(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
