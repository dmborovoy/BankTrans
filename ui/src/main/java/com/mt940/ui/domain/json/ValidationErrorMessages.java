package com.mt940.ui.domain.json;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorMessages {
    List<ValidationErrorMessage> messages;

    public List<ValidationErrorMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ValidationErrorMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(String fieldName, String message) {
        if (messages == null) {
            messages = new ArrayList<ValidationErrorMessage>();
        }

        messages.add(new ValidationErrorMessage(fieldName, message));
    }
}
