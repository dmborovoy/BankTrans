package com.mt940.dao.jpa;

import com.mt940.dao.repository.EARMessageRepository;
import com.mt940.domain.EARMessage;
import com.mt940.domain.EARMessage_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

@Repository("earMessageDaoImpl")
public class EARMessageDaoImpl extends AbstractDao<EARMessage, Long> implements EARMessageDao {
    //    TODO DB: replace annonime methods with lambda after migration to java8
    @Autowired
    @Qualifier("earMessageRepository")
    private EARMessageRepository earMessageRepository;

    private static Specification<EARMessage> sentDateCondition(final ZonedDateTime from, final ZonedDateTime to) {
        return new Specification<EARMessage>() {
            @Override
            public Predicate toPredicate(Root<EARMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (from != null && to != null) {
                    return cb.between(root.get(EARMessage_.sentDate), from, to);
                } else if (from != null) {
                    return cb.greaterThanOrEqualTo(root.get(EARMessage_.sentDate), from);
                } else if (to != null) {
                    return cb.lessThanOrEqualTo(root.get(EARMessage_.sentDate), to);
                } else
                    return null;
            }
        };
    }

    private static Specification<EARMessage> receivedDateCondition(final ZonedDateTime from, final ZonedDateTime to) {
        return new Specification<EARMessage>() {
            @Override
            public Predicate toPredicate(Root<EARMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (from != null && to != null) {
                    return cb.between(root.get(EARMessage_.receivedDate), from, to);
                } else if (from != null) {
                    return cb.greaterThanOrEqualTo(root.get(EARMessage_.receivedDate), from);
                } else if (to != null) {
                    return cb.lessThanOrEqualTo(root.get(EARMessage_.receivedDate), to);
                } else
                    return null;
            }
        };
    }

    private static Specification<EARMessage> processingDateCondition(final ZonedDateTime from, final ZonedDateTime to) {
        return new Specification<EARMessage>() {
            @Override
            public Predicate toPredicate(Root<EARMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (from != null && to != null) {
                    return cb.between(root.get(EARMessage_.processingDate), from, to);
                } else if (from != null) {
                    return cb.greaterThanOrEqualTo(root.get(EARMessage_.processingDate), from);
                } else if (to != null) {
                    return cb.lessThanOrEqualTo(root.get(EARMessage_.processingDate), to);
                } else
                    return null;
            }
        };
    }

    @Override
    public Class<EARMessage> getEntityClass() {
        return EARMessage.class;
    }

    @Override
    public EARMessage save(EARMessage earMessage) {
        return earMessageRepository.save(earMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EARMessage> findAllByPage(Pageable pageable) {
        return earMessageRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EARMessage> findByAllNullable(ZonedDateTime from, ZonedDateTime to, ZonedDateTime from1, ZonedDateTime to1, ZonedDateTime from2, ZonedDateTime to2) {
        return findByAllNullable(from, to, from1, to1, from2, to2, null).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EARMessage> findByAllNullable(ZonedDateTime from, ZonedDateTime to, ZonedDateTime from1, ZonedDateTime to1, ZonedDateTime from2, ZonedDateTime to2, Pageable pageable) {
        return earMessageRepository.findAll(
                where(sentDateCondition(from, to)).
                        and(receivedDateCondition(from1, to1)).
                        and(processingDateCondition(from2, to2)),
                pageable);
    }

////    TODO DB: remove
//    @Transactional(readOnly = true)
//    @Override
//    public ZonedDateTime getLastProcessedDate() {
//        ZonedDateTime result;
//        try {
//            result = (ZonedDateTime) getEntityManager().createQuery("select a.receivedDate from EARMessage a order by a.receivedDate DESC").
//                    setMaxResults(1).getSingleResult();
//        } catch (NoResultException e) {
//            result = null;
//        }
//        return result;
//    }

    @Transactional(readOnly = true)
    @Override
    public ZonedDateTime findLastReceivedDate() {
        ZonedDateTime result;
        try {
            result = (ZonedDateTime) getEntityManager().createQuery("select a.receivedDate from EARMessage a order by a.receivedDate DESC").
                    setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    }
}
