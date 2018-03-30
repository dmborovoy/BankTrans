package com.mt940.ui.domain;



import com.mt940.ui.domain.json.FieldName;
import com.mt940.ui.domain.json.UISerializable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MessageDetails implements UISerializable {
    @FieldName("Id")
    Long id;
    @FieldName("From")
    String from;
    @FieldName("To")
    String to;
    @FieldName("Send Date")
    LocalDateTime sentDate;
    @FieldName("Received Date")
    LocalDateTime receivedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @FieldName("Processing Date")
    LocalDateTime processingDate;
    @FieldName("Subject")
    String subject;
    @FieldName("Message")
    String message;
    @FieldName("Attachment Count")
    Integer attachmentCount;
    @FieldName("Path")
    String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public LocalDateTime getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(LocalDateTime processingDate) {
        this.processingDate = processingDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sentDate=" + sentDate +
                ", receivedDate=" + receivedDate +
                ", processingDate=" + processingDate +
                ", subject='" + subject + '\'' +
                '}';
    }
}
