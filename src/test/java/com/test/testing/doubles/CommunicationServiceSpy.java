package com.test.testing.doubles;

import com.test.service.CommunicationService;
import java.util.ArrayList;
import java.util.List;

public class CommunicationServiceSpy implements CommunicationService {
    private final List<SentMessage> sentMessages = new ArrayList<>();

    public record SentMessage(String email, String content) {}

    @Override
    public void sendReminder(String email, String content) {
        sentMessages.add(new SentMessage(email, content));
    }

    // Metody inspekcji dla asercji w teście
    public int sentCount() {
        return sentMessages.size();
    }

    public SentMessage getMessage(int index) {
        return sentMessages.get(index);
    }

    public boolean containsRecipient(String email) {
        return sentMessages.stream().anyMatch(m -> m.email().equals(email));
    }
}