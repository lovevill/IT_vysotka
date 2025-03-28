package ru.testPr.test.Service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailSenderService {

    private final EmailService emailService;
    private final ParticipantService participantService;

    public EmailSenderService(EmailService emailService, ParticipantService participantService) {
        this.emailService = emailService;
        this.participantService = participantService;
    }

    @Async
    public void sendEmails() {
    	List<Map<String, Object>> participants = participantService.getOfflineParticipants();
    	
        for (Map<String, Object> participant: participants) {
            try {
            	// Извлекаем данные из мапы
                String email = (String) participant.get("email");
                String lastName = (String) participant.get("lastName");
                String name = (String) participant.get("name");
                String middleName = (String) participant.get("middleName");
                String teamName = (String) participant.get("teamName");
                Integer audienceNumber = (Integer) participant.get("audience");
                Integer computerNumber = (Integer) participant.get("computerNumber");
                Integer id = (Integer) participant.get("id");

                // Отправляем письмо через emailService
                emailService.sendEmail(email, lastName, name, middleName, teamName, audienceNumber, computerNumber, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

