package com.dreamtracker.app.infrastructure.mail;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private JavaMailSender javaMailSender;



//    public sendRegistrationMail(String email){
//
//    }




}
