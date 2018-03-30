package com.mt940.ui.domain;


import com.mt940.domain.enums.EARAttachmentStatus;
import com.mt940.ui.domain.json.FieldName;
import com.mt940.ui.domain.json.UISerializable;

public class AttachmentDetails implements UISerializable {
    @FieldName("Id")
    Long id;
    @FieldName("Email Id")
    Long messageId;
    @FieldName("Original Name")
    String originalName;
    @FieldName("Unique Name")
    String uniqueName;
    @FieldName("Size")
    Long size;
    @FieldName("Status")
    EARAttachmentStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public EARAttachmentStatus getStatus() {
        return status;
    }

    public void setStatus(EARAttachmentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", originalName='" + originalName + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", size=" + size +
                ", status=" + status +
                '}';
    }
}
