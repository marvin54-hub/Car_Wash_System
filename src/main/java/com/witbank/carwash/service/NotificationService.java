package com.witbank.carwash.service;

import com.witbank.carwash.model.NotificationLog;
import com.witbank.carwash.repository.NotificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationLogRepository notificationLogRepository;

    public void dispatchSms(String cellphone, String messageContent) {
        if (cellphone == null || cellphone.trim().isEmpty())
            return;
        NotificationLog log = new NotificationLog(
                null,
                cellphone.trim(),
                "SMS",
                messageContent);
        log.setStatus("Outbox Logged (Simulated SMS)");
        notificationLogRepository.save(log);
        System.out.println("[INTERNAL DISPATCHER] Auto-SMS broadcast to phone [" + cellphone + "]: " + messageContent);
    }

    public void dispatchEmail(String email, String subject, String bodyContent) {
        if (email == null || email.trim().isEmpty())
            return;
        String formattedMessage = "Subject: " + subject + "\n\n" + bodyContent;
        NotificationLog log = new NotificationLog(
                null,
                email.trim(),
                "EMAIL",
                formattedMessage);
        log.setStatus("Relayed via Client EmailJS SDK");
        notificationLogRepository.save(log);
        System.out.println("[EmailJS HANDOFF] Outbox entry generated. Email dispatch delegated to browser layer.");
    }

    public List<NotificationLog> getAllDispatchedLogs() {
        return notificationLogRepository.findAll();
    }
}
