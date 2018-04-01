package com.mt940.dao.jpa;

import com.mt940.dao.repository.MT940StatementRepository;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.EARAttachment_;
import com.mt940.domain.EARMessage;
import com.mt940.domain.EARMessage_;
import com.mt940.domain.mt940.MT940Statement;
import com.mt940.domain.mt940.MT940Statement_;
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

@Repository("mt940StatementDaoImpl")
public class MT940StatementDaoImpl extends AbstractDao<MT940Statement, Long> implements MT940StatementDao {
    @Autowired
    @Qualifier("mt940StatementRepository")
    private MT940StatementRepository mt940StatementRepository;

    private static Specification<MT940Statement> accountIdCondition(final String accountId) {
        return (root, query, cb) -> {
            if (accountId != null && accountId.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Statement_.accountId)), "%" + accountId.toLowerCase() + "%");
            } else
                return null;
        };
    }

    private static Specification<MT940Statement> relatedReferenceCondition(final String relatedReference) {
        return (root, query, cb) -> {
            if (relatedReference != null && relatedReference.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Statement_.relatedReference)), "%" + relatedReference.toLowerCase() + "%");
            } else
                return null;
        };
    }

    private static Specification<MT940Statement> transactionReferenceCondition(final String transactionReference) {
        return (root, query, cb) -> {
            if (transactionReference != null && transactionReference.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Statement_.transactionReference)), "%" + transactionReference.toLowerCase() + "%");
            } else
                return null;
        };
    }

    private static Specification<MT940Statement> idsCondition(final Long messageId, final Long fileId) {
        return (root, query, cb) -> {
            Join<MT940Statement, EARAttachment> attachmentJoin = null;
            Join<EARAttachment, EARMessage> messageJoin = null;
            List<Predicate> predicateList = new ArrayList<Predicate>();
//NOTE DB: this complication is required to avoid multiple redundant inner joins
            if (fileId != null) {
                attachmentJoin = root.join(MT940Statement_.settlementFile, JoinType.INNER);
                predicateList.add(cb.equal(attachmentJoin.get(EARAttachment_.id), fileId));
            }
            if (messageId != null) {
                if (attachmentJoin == null)
                    attachmentJoin = root.join(MT940Statement_.settlementFile, JoinType.INNER);
                messageJoin = attachmentJoin.join(EARAttachment_.message, JoinType.INNER);
                predicateList.add(cb.equal(messageJoin.get(EARMessage_.id), messageId));
            }
            return predicateList.size() > 0 ? cb.and(predicateList.toArray(new Predicate[predicateList.size()])) : null;
        };
    }

    @Override
    public Class<MT940Statement> getEntityClass() {
        return MT940Statement.class;
    }

    @Override
    public MT940Statement save(MT940Statement mt940Statement) {
        return mt940StatementRepository.save(mt940Statement);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MT940Statement> findAllByPage(Pageable pageable) {
        return mt940StatementRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Statement> findByAllNullable(String accountId, String relatedReference, String transactionReference, Long messageId, Long fileId) {
        return findByAllNullable(accountId, relatedReference, transactionReference, messageId, fileId, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MT940Statement> findByAllNullable(String accountId, String relatedReference, String transactionReference, Long messageId, Long fileId, Pageable pageable) {
        return mt940StatementRepository.findAll(
                where(accountIdCondition(accountId)).
                        and(relatedReferenceCondition(relatedReference)).
                        and(transactionReferenceCondition(transactionReference)).
                        and(idsCondition(messageId, fileId)),
                pageable);
    }
}
