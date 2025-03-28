package ru.testPr.test.Controllers;

import org.springframework.web.bind.annotation.*;

import ru.testPr.test.Service.EmailSenderService;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class EmailController {

    private final EmailSenderService emailSenderService;


    @Autowired
    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-emails")
    public ResponseEntity<String> sendEmails() {

        // Запускаем асинхронную задачу для отправки писем
        emailSenderService.sendEmails();

        // Возвращаем успешный ответ
        return ResponseEntity.ok().body("Рассылка началась");
    }
}

