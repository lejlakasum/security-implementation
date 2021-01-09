package com.example.ppis.controller;

import com.example.ppis.dto.SkillDTO;
import com.example.ppis.model.Employee;
import com.example.ppis.model.EmployeeSkill;
import com.example.ppis.service.EmployeeService;
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
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    private final Bucket bucket2;

    public EmployeeController() {

        Bandwidth limit2 = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        this.bucket2 = Bucket4j.builder()
                .addLimit(limit2)
                .build();
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    ResponseEntity<Employee> add(@RequestBody @Valid Employee employee) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(employeeService.add(employee), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<HashMap<String,String>> delete(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(employeeService.delete(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/employees")
    ResponseEntity<List<Employee>> getAll() {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(employeeService.getAll(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/employees/{id}/skills")
    ResponseEntity<List<SkillDTO>> getAllSkills(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(employeeService.getSkillsByEmployee(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @PostMapping("/employees/{id}/skills")
    @Valid
    ResponseEntity<EmployeeSkill> addSkillToEmployee(@RequestBody @Valid SkillDTO skillDTO, @PathVariable Integer id) throws  Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(employeeService.addSkillToEmployee(skillDTO, id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
