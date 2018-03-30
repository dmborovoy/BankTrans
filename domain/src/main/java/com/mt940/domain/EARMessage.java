package com.mt940.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by dimas on 25.11.2014.
 */
@Entity
@Table(schema = "bkv", name = "ear_message")
public class EARMessage {

    private Long id;
    private String from;//string of email with ; as delimiter
    private String to;//string of email with ; as delimiter
    private String subject;
    private ZonedDateTime sentDate;
    private ZonedDateTime receivedDate;
    private String messageContent;
    private int attachmentsCount;
    private List<EARAttachment> attachmentList;
    private String downloadPath;//full path where files were located from this email
    private ZonedDateTime processingDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "from_list")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Column(name = "to_list")
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "sent_date")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    public ZonedDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(ZonedDateTime sentDate) {
        this.sentDate = sentDate;
    }

    @Column(name = "received_date")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    public ZonedDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(ZonedDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Column(name = "message_content")
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Column(name = "attachment_count")
    public int getAttachmentsCount() {
        return attachmentsCount;
    }

    public void setAttachmentsCount(int attachmentsCount) {
        this.attachmentsCount = attachmentsCount;
    }

    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<EARAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<EARAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Column(name = "download_path")
    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Column(name = "processing_date")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    public ZonedDateTime getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(ZonedDateTime processingDate) {
        this.processingDate = processingDate;
    }

    @Override
    public String toString() {
        return "EARMessage{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", sentDate=" + sentDate +
                ", receivedDate=" + receivedDate +
                ", attachmentsCount=" + attachmentsCount +
//                ", attachmentList=" + attachmentList +
                ", downloadPath='" + downloadPath + '\'' +
                ", processingDate=" + processingDate +
                "}@" + Integer.toHexString(hashCode());
    }
}
