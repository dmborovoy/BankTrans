package com.mt940.domain;

import com.mt940.domain.enums.EARAttachmentStatus;
import com.mt940.domain.mt940.MT940Statement;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.util.SortedSet;


@Entity
@Table(schema = "bkv", name = "ear_attachment")
public class EARAttachment {

    private Long id;
    private String originalName;
    private String uniqueName;//yyyy-mm-dd-GUID-originalName
    private long size;
    private EARAttachmentStatus status;

    private EARMessage message;
    private SortedSet<MT940Statement> statementSet;
    private String rawData;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "original_name")
    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Column(name = "unique_name")
    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Column(name = "size")
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    public EARAttachmentStatus getStatus() {
        return status;
    }

    public void setStatus(EARAttachmentStatus status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    public EARMessage getMessage() {
        return message;
    }

    public void setMessage(EARMessage message) {
        this.message = message;
    }

    @JoinColumn(name = "settlement_file_id", referencedColumnName = "id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Sort(type = SortType.NATURAL)
    public SortedSet<MT940Statement> getStatementSet() {
        return statementSet;
    }

    public void setStatementSet(SortedSet<MT940Statement> statementSet) {
        this.statementSet = statementSet;
    }

    @Transient
    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    @Override
    public String toString() {
        return "EARAttachment{" +
                "id=" + id +
                ", originalName='" + originalName + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", size=" + size +
                ", status=" + status +
                ", message_id=" + (message == null ? null : message.getId()) +
                "}@" + Integer.toHexString(hashCode());
    }
}
