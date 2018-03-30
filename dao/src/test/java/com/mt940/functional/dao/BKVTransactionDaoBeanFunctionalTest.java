package com.mt940.functional.dao;

import com.mt940.dao.jpa.MT940TransactionDao;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.MT940Transaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BKVTransactionDaoBeanFunctionalTest extends AbstractBKVDaoBeanFunctionalTest {

//    @Autowired
//    @Qualifier(value = "mt940TransactionDaoBean")
//    com.nxsystems.bank.valletta.dao.MT940TransactionDao dao;

    @Autowired
    @Qualifier(value = "mt940TransactionDaoImpl")
    MT940TransactionDao dao1;

    @Test
    public void findByAll() throws Exception {
        MT940TransactionStatus status = MT940TransactionStatus.NEW;
        Instance instance = Instance.NXPAY_COM;
        MT940FundsCode fundsCode = MT940FundsCode.DEBIT;

        List<MT940Transaction> list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(14, list.size());

        list = dao1.findByAllNullable(status, null, null, null, null, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(9, list.size());

        list = dao1.findByAllNullable(null, instance, null, null, null, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(13, list.size());

        list = dao1.findByAllNullable(null, null, fundsCode, null, null, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(8, list.size());

        list = dao1.findByAllNullable(status, instance, fundsCode, null, null, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(2, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, null, null, 1L);
        l.info("total elements: {}", list.size());
        assertEquals(4, list.size());

        list = dao1.findByAllNullable(null, null, MT940FundsCode.CREDIT, null, null, null, null, null, null, null, null, 1L);
        l.info("total elements: {}", list.size());
        assertEquals(3, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, null, 1L, null);
        l.info("total elements: {}", list.size());
        assertEquals(6, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, null, 1L, 1L);
        l.info("total elements: {}", list.size());
        assertEquals(4, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, 100L, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(6, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, 100L, 1L, null);
        l.info("total elements: {}", list.size());
        assertEquals(6, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, null, 100L, 1L, 1L);
        l.info("total elements: {}", list.size());
        assertEquals(4, list.size());

        list = dao1.findByAllNullable(null, null, MT940FundsCode.CREDIT, null, null, null, null, null, null, 100L, 1L, 1L);
        l.info("total elements: {}", list.size());
        assertEquals(3, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, "ONIC", null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(2, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, "LTD", null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(2, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, "ltd", null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(2, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, "30", null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(2, list.size());

        list = dao1.findByAllNullable(null, null, null, null, null, null, null, null, "bla", null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(6, list.size());

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime from = ZonedDateTime.parse("2014-01-19 00:00:00 +1100", f);
        ZonedDateTime to = ZonedDateTime.parse("2014-01-20 00:00:00 +1100", f);
        list = dao1.findByAllNullable(null, null, null, from, to, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(8, list.size());

        from = ZonedDateTime.parse("2014-01-20 00:00:00 +1100", f);
        list = dao1.findByAllNullable(null, null, null, from, null, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(8, list.size());

        to = ZonedDateTime.parse("2014-01-19 00:00:00 +1100", f);
        list = dao1.findByAllNullable(null, null, null, null, to, null, null, null, null, null, null, null);
        l.info("total elements: {}", list.size());
        assertEquals(6, list.size());
    }


    @Test
    public void findAllWithSortingAndPaging() throws Exception {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        int startPage = 0;
        int pageSize = 2;
        PageRequest pageRequest = new PageRequest(startPage, pageSize, sort);

        Page<MT940Transaction> page = dao1.findAllByPage(pageRequest);

        l.info("total pages: {}", page.getTotalPages());
        l.info("total elements: {}", page.getTotalElements());

        while (true) {
            l.info("==============page idx: {}", page.getNumber());
            l.info("total elements per pages: {}", page.getNumberOfElements());
            for (MT940Transaction transaction : page.getContent()) {
                l.info("transaction: {}", transaction);
                l.info("mess.id: {}", transaction.getStatement().getSettlementFile().getMessage().getId());
                l.info("attach.id: {}", transaction.getStatement().getSettlementFile().getId());
                l.info("stat.id: {}", transaction.getStatement().getId());
                l.info("funds code: {}", transaction.getFundsCode());
            }
//            if (page.hasNextPage()) {
            if (page.hasNext()) {
                page = dao1.findAllByPage(page.nextPageable());
            } else
                break;
        }
    }

}