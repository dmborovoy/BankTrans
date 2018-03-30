package com.mt940.dao.repository;

import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("earAttachmentRepository")
public interface EARAttachmentRepository extends PagingAndSortingRepository<EARAttachment, Long>, JpaSpecificationExecutor {

    EARAttachment findTopByUniqueName(String uniqueName);

    EARAttachment findTopByOriginalName(String originalName);

    List<EARAttachment> findByStatus(EARAttachmentStatus status);

    @Modifying @Query("update EARAttachment ear set ear.status = :status where ear.id = :id")
    int setStatusForEARAttachment(@Param("status") EARAttachmentStatus status, @Param("id") Long id);
}
