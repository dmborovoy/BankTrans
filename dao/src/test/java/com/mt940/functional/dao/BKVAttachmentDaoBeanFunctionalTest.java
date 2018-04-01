package com.mt940.functional.dao;

import com.mt940.dao.jpa.EARAttachmentDao;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class BKVAttachmentDaoBeanFunctionalTest extends AbstractBKVDaoBeanFunctionalTest {

    @Autowired
    @Qualifier(value = "earAttachmentDaoImpl")
    EARAttachmentDao dao1;


    @Test
    public void testName() throws Exception {

        int a = 10;

        int b = 6;

        a = a + b;

        b = a % 3;

        int d = a - b;
        log.info("" + d);
    }

    @Test
    public void findAllWithSortingAndPaging() throws Exception {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        int startPage = 0;
        int pageSize = 2;
        PageRequest pageRequest = new PageRequest(startPage, pageSize, sort);

        Page<EARAttachment> page = dao1.findAllByPage(pageRequest);

        log.info("total pages: {}", page.getTotalPages());
        log.info("total elements: {}", page.getTotalElements());

        while (true) {
            log.info("==============page idx: {}", page.getNumber());
            log.info("total elements per pages: {}", page.getNumberOfElements());
            for (EARAttachment attachment : page.getContent()) {
                log.info("attachment: {}", attachment);
                log.info("mess.id: {}", attachment.getMessage().getId());
                log.info("stat.size: {}", attachment.getStatementSet().size());
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
        List<EARAttachment> page = dao1.findByAllNullable(null, null, 100L);
        assertEquals(1, page.size());
        page = dao1.findByAllNullable(null, "400225304", null);
        assertEquals(3, page.size());
    }

    @Test
    public void findByUniqueName() throws Exception {
        EARAttachment page = dao1.findByUniqueName("2014-12-16-8cf74a74-e7f3-40d8-a1fb-393c2a806847-40022530424.dat");
        assertNotNull(page);
        assertEquals("40022530424.dat", page.getOriginalName());
    }

    @Test
    public void findByOriginalName() throws Exception {
        EARAttachment page = dao1.findByName("40022530424.dat");
        assertNotNull(page);
        assertEquals("2014-12-16-8cf74a74-e7f3-40d8-a1fb-393c2a806847-40022530424.dat", page.getUniqueName());
    }

    @Test
    public void findByStatus() throws Exception {
        List<EARAttachment> page = dao1.getAttachmentListByStatus(EARAttachmentStatus.NEW);
        assertNotNull(page);
        assertEquals(3, page.size());
    }

    @Test
    public void updateStatus() throws Exception {
        EARAttachment page = dao1.findById(1L);
        assertNotNull(page);
        assertEquals(EARAttachmentStatus.NEW, page.getStatus());
        dao1.updateStatus(page, EARAttachmentStatus.ERROR);
        dao1.flush();
        dao1.clear();

        page = dao1.findById(1L);
        assertNotNull(page);
        assertEquals(EARAttachmentStatus.ERROR, page.getStatus());

    }

//    @Ignore
//    @Test
//    public void findAll1() throws Exception {
//        List<EARAttachment> all = dao.findAll();
//        assertNotNull(all);
//        for (EARAttachment attachment : all) {
//            l.info("WTF: {}", attachment);
//        }
//        assertEquals(0, all.size());
//    }
//
//    @Ignore
//    @Test
//    public void testSave() throws Exception {
//        List<EARAttachment> all1 = dao.findAll();
//        assertNotNull(all1);
//        for (EARAttachment attachment : all1) {
//            l.info("WTF: {}", attachment);
//        }
//        assertEquals(0, all1.size());
//
//        EARAttachment file = new EARAttachment();
//        file.setStatus(EARAttachmentStatus.NEW);
//        file.setUniqueName("unique1");
//        file.setOriginalName("name");
//        file.setSize(123456);
//
//        EARAttachment file1 = new EARAttachment();
//        file1.setStatus(EARAttachmentStatus.NEW);
//        file1.setUniqueName("unique2");
//        file1.setOriginalName("name");
//        file1.setSize(654321);
//
//
//        dao.persist(file);
//        dao.persist(file1);
//
//        dao.flush();
//        dao.clear();
//
//        List<EARAttachment> all = dao.findAll();
//        assertNotNull(all);
//        assertEquals(2, all.size());
//
//        l.info("list: {}", all);
//
//        EARAttachment file3 = dao.findByUniqueName("unique2");
//        l.info("getByUniqueName: {}", file3);
//        assertEquals(654321, file3.getSize());
//
//        dao.updateStatus(file3, EARAttachmentStatus.ERROR);
//        dao.flush();
//        dao.clear();
//
//        EARAttachment file4 = dao.findByUniqueName("unique2");
//        l.info("getByUniqueName: {}", file4);
//        assertEquals(EARAttachmentStatus.ERROR, file4.getStatus());
//
//    }
}