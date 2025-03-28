package ru.testPr.test.Service;

import java.io.IOException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String toEmail, String firstName, String lastName, String patronymic, 
            String team, Integer auditorium, Integer computer, Integer participantId) throws MessagingException, IOException, Exception {

		// Генерация QR-кода с уникальной ссылкой
		String qrCodeUrl = "https://localhost:8080/?id=" + participantId;
		byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage(qrCodeUrl);
		
		// Создаем письмо с HTML-форматированием
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setFrom("it-olymp@ugntu.com");
		helper.setTo(toEmail);
		helper.setSubject("Информация о вашем участии");
		
		String htmlContent = "<html><body>"
		+ "<h2>Здравствуйте, " + firstName + " " + lastName + " " + patronymic + "</h2>"
		+ "<p><strong>Команда:</strong> " + team + "</p>"
		+ "<p><strong>Аудитория:</strong> " + auditorium + "</p>"
		+ "<p><strong>Компьютер:</strong> " + computer + "</p>"
		+ "<p><strong>QR-код для очной регистрации:</strong></p>"
		+ "<p><img src='cid:qrCode' alt='QR Code' /></p>" // Ссылка на QR-код
		+ "</body></html>";
		
		// Устанавливаем контент и QR-код
		helper.setText(htmlContent, true);
		ByteArrayDataSource qrCodeDataSource = new ByteArrayDataSource(qrCodeImage, "image/png");
        
        // Добавляем изображение как inline
        helper.addInline("qrCode", qrCodeDataSource);
		
		// Отправляем письмо
		emailSender.send(message);
    }
}
