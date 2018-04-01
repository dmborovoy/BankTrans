package com.mt940.dao.jpa;

import com.mt940.dao.repository.MT940TransactionRepository;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.EARAttachment_;
import com.mt940.domain.EARMessage;
import com.mt940.domain.EARMessage_;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

@Repository("mt940TransactionDaoImpl")
public class MT940TransactionDaoImpl extends AbstractDao<MT940Transaction, Long> implements MT940TransactionDao {
    //    TODO DB: replace annonime methods with lambda after migration to java8
    @Autowired
    @Qualifier("mt940TransactionRepository")
    private MT940TransactionRepository mt940TransactionRepository;

    private static Specification<MT940Transaction> statusCondition(final MT940TransactionStatus status) {
        return (root, query, cb) -> {
            if (status != null) {
                return cb.equal(root.get(MT940Transaction_.status), status);
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> instanceCondition(final Instance instance) {
        return (root, query, cb) -> {
            if (instance != null) {
                return cb.equal(root.get(MT940Transaction_.instance), instance);
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> fundsCodeCondition(final MT940FundsCode fundsCode) {
        return (root, query, cb) -> {
            if (fundsCode != null) {
                return cb.equal(root.get(MT940Transaction_.fundsCode), fundsCode);
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> dateCondition(final ZonedDateTime from, final ZonedDateTime to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get(MT940Transaction_.date), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get(MT940Transaction_.date), from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get(MT940Transaction_.date), to);
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> infoToAccountOwnerCondition(final String infoToAccountOwner) {
        return (root, query, cb) -> {
            if (infoToAccountOwner != null && infoToAccountOwner.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Transaction_.informationToAccountOwner)), "%" + infoToAccountOwner.toLowerCase() + "%");
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> referenceForAccountOwnerCondition(final String referenceForAccountOwner) {
        return (root, query, cb) -> {
            if (referenceForAccountOwner != null && referenceForAccountOwner.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Transaction_.referenceForAccountOwner)), "%" + referenceForAccountOwner.toLowerCase() + "%");
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> referenceForBankCondition(final String referenceForBank) {
        return (root, query, cb) -> {
            if (referenceForBank != null && referenceForBank.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Transaction_.referenceForBank)), "%" + referenceForBank.toLowerCase() + "%");
            } else
                return null;
        };
    }

    private static Specification<MT940Transaction> transactionDescriptionCondition(final String transactionDescription) {
        return (root, query, cb) -> {
            if (transactionDescription != null && transactionDescription.length() > 0) {
                return cb.like(cb.lower(root.get(MT940Transaction_.transactionDescription)), "%" + transactionDescription.toLowerCase() + "%");
            } else
                return null;
        };
    }

    @Override
    public Class<MT940Transaction> getEntityClass() {
        return MT940Transaction.class;
    }

    @Override
    public MT940Transaction save(MT940Transaction mt940Transaction) {
        return mt940TransactionRepository.save(mt940Transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MT940Transaction> findAllByPage(Pageable pageable) {
        return mt940TransactionRepository.findAll(pageable);
    }

    private Specification<MT940Transaction> idsCondition(final Long emailId, final Long fileId, final Long statementId) {
        return (root, query, cb) -> {
            Join<MT940Transaction, MT940Statement> statementJoin = null;
            Join<MT940Statement, EARAttachment> attachmentJoin = null;
            Join<EARAttachment, EARMessage> messageJoin = null;

            List<Predicate> predicateList = new ArrayList<Predicate>();
//NOTE DB: this complication is required to avoid multiple redundant inner joins
            if (statementId != null) {
                statementJoin = root.join(MT940Transaction_.statement, JoinType.INNER);
                predicateList.add(cb.equal(statementJoin.get(MT940Statement_.id), statementId));
            }
            if (fileId != null) {
                if (statementJoin == null)
                    statementJoin = root.join(MT940Transaction_.statement, JoinType.INNER);
                attachmentJoin = statementJoin.join(MT940Statement_.settlementFile, JoinType.INNER);
                predicateList.add(cb.equal(attachmentJoin.get(EARAttachment_.id), fileId));
            }
            if (emailId != null) {
                if (statementJoin == null)
                    statementJoin = root.join(MT940Transaction_.statement, JoinType.INNER);
                if (attachmentJoin == null)
                    attachmentJoin = statementJoin.join(MT940Statement_.settlementFile, JoinType.INNER);
                messageJoin = attachmentJoin.join(EARAttachment_.message, JoinType.INNER);
                predicateList.add(cb.equal(messageJoin.get(EARMessage_.id), emailId));
            }
            return predicateList.size() > 0 ? cb.and(predicateList.toArray(new Predicate[predicateList.size()])) : null;
        };
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> findByAllNullable(MT940TransactionStatus status, Instance instance, MT940FundsCode fundsCode, ZonedDateTime from, ZonedDateTime to, String infoToAccountOwner, String referenceForAccountOwner, String referenceForBank, String transactionDescription, Long emailId, Long fileId, Long statementId) {
        return findByAllNullable(status, instance, fundsCode, from, to, infoToAccountOwner, referenceForAccountOwner, referenceForBank, transactionDescription, emailId, fileId, statementId, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MT940Transaction> findByAllNullable(MT940TransactionStatus status, Instance instance, MT940FundsCode fundsCode, ZonedDateTime from, ZonedDateTime to, String infoToAccountOwner, String referenceForAccountOwner, String referenceForBank, String transactionDescription, Long emailId, Long fileId, Long statementId, Pageable pageable) {
        return mt940TransactionRepository.findAll(
                where(statusCondition(status)).
                        and(instanceCondition(instance)).
                        and(fundsCodeCondition(fundsCode)).
                        and(dateCondition(from, to)).
                        and(infoToAccountOwnerCondition(infoToAccountOwner)).
                        and(referenceForAccountOwnerCondition(referenceForAccountOwner)).
                        and(referenceForBankCondition(referenceForBank)).
                        and(transactionDescriptionCondition(transactionDescription)).
                        and(idsCondition(emailId, fileId, statementId)),
                pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> findByAllNullable(final MT940TransactionSearchRequest request) {
        return findByAllNullable(request.status, request.instance, request.fundsCode, request.from, request.to, request.informationToAccountOwner, request.referenceForAccountOwner, request.referenceForBank, request.transactionDescription, request.messageId, request.fileId, request.statementId, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MT940Transaction> findByAllNullable(final MT940TransactionSearchRequest request, Pageable pageable) {
        return findByAllNullable(request.status, request.instance, request.fundsCode, request.from, request.to, request.informationToAccountOwner, request.referenceForAccountOwner, request.referenceForBank, request.transactionDescription, request.messageId, request.fileId, request.statementId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> getTransactionListByDateTransactionTypeInstanceAndStatus(Instance instance, ZonedDateTime fromDate, ZonedDateTime toDate, MT940FundsCode fundsCode, MT940TransactionStatus status) {
        return findByAllNullable(status, instance, fundsCode, fromDate, toDate, null, null, null, null, null, null, null, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> getOrphanTransactionList(ZonedDateTime toDate, MT940FundsCode fundsCode, MT940TransactionStatus status) {
        return findByAllNullable(status, null, fundsCode, null, toDate, null, null, null, null, null, null, null, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> getReadTransactionList(MT940FundsCode fundsCode, MT940TransactionStatus status) {
        return findByAllNullable(status, null, fundsCode, null, null, null, null, null, null, null, null, null, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> getErrorTransactionList(MT940FundsCode fundsCode, MT940TransactionStatus status) {
        return mt940TransactionRepository.findByFundsCodeAndStatusGreaterThan(fundsCode, status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MT940Transaction> getUnknownInstanceTransactionList(MT940FundsCode fundsCode) {
        return findByAllNullable(null, Instance.UNKNOWN, fundsCode, null, null, null, null, null, null, null, null, null, null).getContent();
    }

    @Override
    public void updateStatus(MT940Transaction transaction, MT940TransactionStatus status, String errorDescription, Instance instance) {
        transaction.setStatus(status);
        transaction.setErrorDescription(errorDescription);
        transaction.setInstance(instance);
        mt940TransactionRepository.save(transaction);
    }

    @Override
    public void updateStatus(Long id, MT940TransactionStatus status, String errorDescription, Instance instance) {
        MT940Transaction transaction = mt940TransactionRepository.findOne(id);
        transaction.setStatus(status);
        transaction.setErrorDescription(errorDescription);
        transaction.setInstance(instance);
        mt940TransactionRepository.save(transaction);
    }
}
