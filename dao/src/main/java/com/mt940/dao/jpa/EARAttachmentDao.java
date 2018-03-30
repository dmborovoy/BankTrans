package com.mt940.dao.jpa;

import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EARAttachmentDao extends Dao<EARAttachment, Long> {

    public Page<EARAttachment> findAllByPage(Pageable pageable);

    public List<EARAttachment> findByAllNullable(EARAttachmentStatus status, String fileName, Long messageId);

    public Page<EARAttachment> findByAllNullable(EARAttachmentStatus status, String fileName, Long messageId, Pageable pageable);

    public EARAttachment findByUniqueName(String uniqueName);

    public EARAttachment findByName(String name);

    public List<EARAttachment> getAttachmentListByStatus(EARAttachmentStatus status);

    public void updateStatus(EARAttachment attachment, EARAttachmentStatus status);

}
