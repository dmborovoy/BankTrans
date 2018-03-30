package com.mt940.ui.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MessageFilterForm extends AbstractFilterForm {
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime sentDateFrom;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime sentDateTo;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime receivedDateFrom;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime receivedDateTo;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime processingDateFrom;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime processingDateTo;

    public LocalDateTime getSentDateFrom() {
        return sentDateFrom;
    }

    public void setSentDateFrom(LocalDateTime sentDateFrom) {
        this.sentDateFrom = sentDateFrom;
    }

    public LocalDateTime getSentDateTo() {
        return sentDateTo;
    }

    public void setSentDateTo(LocalDateTime sentDateTo) {
        this.sentDateTo = sentDateTo;
    }

    public LocalDateTime getReceivedDateFrom() {
        return receivedDateFrom;
    }

    public void setReceivedDateFrom(LocalDateTime receivedDateFrom) {
        this.receivedDateFrom = receivedDateFrom;
    }

    public LocalDateTime getReceivedDateTo() {
        return receivedDateTo;
    }

    public void setReceivedDateTo(LocalDateTime receivedDateTo) {
        this.receivedDateTo = receivedDateTo;
    }

    public LocalDateTime getProcessingDateFrom() {
        return processingDateFrom;
    }

    public void setProcessingDateFrom(LocalDateTime processingDateFrom) {
        this.processingDateFrom = processingDateFrom;
    }

    public LocalDateTime getProcessingDateTo() {
        return processingDateTo;
    }

    public void setProcessingDateTo(LocalDateTime processingDateTo) {
        this.processingDateTo = processingDateTo;
    }
}
