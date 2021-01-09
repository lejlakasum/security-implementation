package com.example.ppis.controller;

import com.example.ppis.model.Employee;
import com.example.ppis.model.Skill;
import com.example.ppis.service.SkillService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

@RestController
public class SkillController {

    @Autowired
    SkillService skillService;
    private final Bucket bucket2;

    public SkillController() {

        Bandwidth limit2 = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        this.bucket2 = Bucket4j.builder()
                .addLimit(limit2)
                .build();
    }

    @PostMapping("/skills")
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    ResponseEntity<Skill> add(@RequestBody @Valid Skill skill) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(skillService.add(skill), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }


    @DeleteMapping("/skills/{id}")
    ResponseEntity<HashMap<String,String>> delete(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(skillService.delete(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/skills")
    ResponseEntity<List<Skill>> getAll() {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(skillService.getAll(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("skills/{id}/employees")
    ResponseEntity<List<Employee>> getEmployees(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(skillService.getEmployeesBySkill(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
