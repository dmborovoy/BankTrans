package com.mt940.dao.jpa;

import com.mt940.dao.repository.EARAttachmentRepository;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.EARAttachment_;
import com.mt940.domain.EARMessage;
import com.mt940.domain.EARMessage_;
import com.mt940.domain.enums.EARAttachmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

@Repository("earAttachmentDaoImpl")
public class EARAttachmentDaoImpl extends AbstractDao<EARAttachment, Long> implements EARAttachmentDao {
    //    TODO DB: replace annonime methods with lambda after migration to java8
    @Autowired
    @Qualifier("earAttachmentRepository")
    private EARAttachmentRepository earAttachmentRepository;

    private static Specification<EARAttachment> fileNameCondition(final String fileName) {
        return new Specification<EARAttachment>() {
            @Override
            public Predicate toPredicate(Root<EARAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (fileName != null && fileName.length() > 0) {
                    return cb.like(cb.lower(root.get(EARAttachment_.originalName)), "%" + fileName.toLowerCase() + "%");
                } else
                    return null;
            }
        };
    }

    private static Specification<EARAttachment> statusCondition(final EARAttachmentStatus status) {
        return new Specification<EARAttachment>() {
            @Override
            public Predicate toPredicate(Root<EARAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (status != null) {
                    return cb.equal(root.get(EARAttachment_.status), status);
                } else
                    return null;
            }
        };
    }

    @Override
    public Class<EARAttachment> getEntityClass() {
        return EARAttachment.class;
    }

    @Override
    public EARAttachment save(EARAttachment earAttachment) {
        return earAttachmentRepository.save(earAttachment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EARAttachment> findAllByPage(Pageable pageable) {
        return earAttachmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EARAttachment> findByAllNullable(EARAttachmentStatus status, String fileName, Long messageId) {
        return findByAllNullable(status, fileName, messageId, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EARAttachment> findByAllNullable(EARAttachmentStatus status, String fileName, Long messageId, Pageable pageable) {
        return earAttachmentRepository.findAll(
                where(statusCondition(status)).
                        and(idCondition(messageId)).
                        and(fileNameCondition(fileName)),
                pageable);
    }

    @Override
    public EARAttachment findByUniqueName(String uniqueName) {
        return earAttachmentRepository.findTopByUniqueName(uniqueName);
    }

    @Override
    public EARAttachment findByName(String name) {
        return earAttachmentRepository.findTopByOriginalName(name);
    }

    @Override
    public List<EARAttachment> getAttachmentListByStatus(EARAttachmentStatus status) {
        return earAttachmentRepository.findByStatus(status);
    }

    @Override
    public void updateStatus(EARAttachment attachment, EARAttachmentStatus status) {
        earAttachmentRepository.setStatusForEARAttachment(status, attachment.getId());

    }

    private Specification<EARAttachment> idCondition(final Long messageId) {
        return new Specification<EARAttachment>() {
            @Override
            public Predicate toPredicate(Root<EARAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<EARAttachment, EARMessage> messageJoin = null;
                List<Predicate> predicateList = new ArrayList<Predicate>();
//NOTE DB: this complication is required to avoid multiple redundant inner joins
                if (messageId != null) {
                    messageJoin = root.join(EARAttachment_.message, JoinType.INNER);
                    predicateList.add(cb.equal(messageJoin.get(EARMessage_.id), messageId));
                }
                return predicateList.size() > 0 ? cb.and(predicateList.toArray(new Predicate[predicateList.size()])) : null;
            }
        };
    }
}
