package com.example.ppis.controller;

import com.example.ppis.dto.RoleDTO;
import com.example.ppis.model.Role;
import com.example.ppis.model.SkillType;
import com.example.ppis.repository.SkillTypeRepository;
import com.example.ppis.service.SkillTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class SkillTypeController {

    @Autowired
    SkillTypeService skillTypeService;

    @GetMapping("/skill-types")
    List<SkillType> getAll() {
        return skillTypeService.getAll();
    }
}
