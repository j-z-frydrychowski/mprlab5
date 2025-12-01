package com.test.service;

import com.test.model.Certificate;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TrainingReminderService {
    private final CertificateRepository certificateRepository;
    private final CommunicationService communicationService;
    private final Logger logger; // Dummy field

    public TrainingReminderService(CertificateRepository certificateRepository,
                                   CommunicationService communicationService,
                                   Logger logger) {
        this.certificateRepository = certificateRepository;
        this.communicationService = communicationService;
        this.logger = logger;
    }

    public void processReminders(LocalDate today) {
        List<Certificate> allCertificates = certificateRepository.getAllCertificates();

        for (Certificate cert : allCertificates) {
            // Sprawdzamy czy wygasa w ciągu najbliższych 30 dni (i jest jeszcze ważny dzisiaj)
            if (cert.isExpiringIn(today, 30)) {

                long daysLeft = ChronoUnit.DAYS.between(today, cert.expiryDate());

                String message = String.format(
                        "Przypomnienie: Twoje szkolenie %s wygasa za %d dni (%s).",
                        cert.type(),
                        daysLeft,
                        cert.expiryDate()
                );

                communicationService.sendReminder(cert.owner().getEmail(), message);
            }
        }
    }
}