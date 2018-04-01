package com.mt940.functional.dao;

import com.mt940.dao.jpa.MT940StatementDao;
import com.mt940.domain.mt940.MT940Statement;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
public class BKVStatementDaoBeanFunctionalTest extends AbstractBKVDaoBeanFunctionalTest {

    @Autowired
    @Qualifier(value = "mt940StatementDaoImpl")
    MT940StatementDao dao1;

    @Test
    public void findAllWithSortingAndPaging() throws Exception {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        int startPage = 0;
        int pageSize = 2;
        PageRequest pageRequest = new PageRequest(startPage, pageSize, sort);

        Page<MT940Statement> page = dao1.findAllByPage(pageRequest);

        log.info("total pages: {}", page.getTotalPages());
        log.info("total elements: {}", page.getTotalElements());

        while (true) {
            log.info("==============page idx: {}", page.getNumber());
            log.info("total elements per pages: {}", page.getNumberOfElements());
            for (MT940Statement attachment : page.getContent()) {
                log.info("statement: {}", attachment);
                log.info("mess.id: {}", attachment.getSettlementFile().getMessage().getId());
                log.info("stat.id: {}", attachment.getSettlementFile().getId());
                log.info("trans.size: {}", attachment.getTransactionSet().size());
            }
//            if (page.hasNextPage())
            if (page.hasNext())
                page = dao1.findAllByPage(page.nextPageable());
            else
                break;
        }
    }

    @Test
    public void findAll() throws Exception {
        List<MT940Statement> page = dao1.findByAllNullable(null, null, null, null, null);
        assertEquals(4, page.size());

        page = dao1.findByAllNullable(null, null, null, 100L, null);
        assertEquals(2, page.size());

        page = dao1.findByAllNullable(null, null, null, null, 2L);
        assertEquals(1, page.size());

        page = dao1.findByAllNullable(null, null, null, null, 2L);
        assertEquals(1, page.size());

        page = dao1.findByAllNullable("4002253042", null, null, null, null);
        assertEquals(3, page.size());

        page = dao1.findByAllNullable(null, null, "99", null, null);
        assertEquals(3, page.size());
    }
}