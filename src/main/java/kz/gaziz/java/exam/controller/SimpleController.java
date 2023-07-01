package kz.gaziz.java.exam.controller;

import kz.gaziz.java.exam.annotation.Limit;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SimpleController {

    @GetMapping("/check")
    @Limit
    public ResponseEntity<Void> check() {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
