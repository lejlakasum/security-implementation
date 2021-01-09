package com.example.ppis.controller;

import com.example.ppis.model.Certificate;
import com.example.ppis.repository.CertificateRepository;
import com.example.ppis.service.CertificateService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@RestController
public class CertificateController {

    @Autowired
    CertificateService certificateService;
    private final Bucket bucket2;

    public CertificateController() {

        Bandwidth limit2 = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        this.bucket2 = Bucket4j.builder()
                .addLimit(limit2)
                .build();
    }

    @PostMapping("/certificate")
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    ResponseEntity<Certificate> add(@RequestBody @Valid Certificate certificate) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(certificateService.addCertificate(certificate), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/certificate/{id}")
    ResponseEntity<Certificate> getCertificateById(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(certificateService.getCertificateById(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/certificate/all")
    ResponseEntity<List<Certificate>> getAllCertificates() throws Exception {
        if(bucket2.tryConsume(1)) {
            return  new ResponseEntity<>(certificateService.getAllCertificates(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    @DeleteMapping("/certificate/{id}")
    ResponseEntity<Void> deleteCertificate(@PathVariable Integer id) throws Exception {
        if(bucket2.tryConsume(1)) {
            certificateService.deleteCertificate(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
