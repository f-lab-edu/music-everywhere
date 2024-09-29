package me.kong.paymentservice.controller;


import lombok.RequiredArgsConstructor;
import me.kong.paymentservice.service.PayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/payment")
    public ResponseEntity<HttpStatus> doPay() {
        payService.processPayRequest();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
