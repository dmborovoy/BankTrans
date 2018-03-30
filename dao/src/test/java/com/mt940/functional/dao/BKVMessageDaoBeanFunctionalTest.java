package com.mt940.functional.dao;

import com.mt940.dao.jpa.EARMessageDao;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.EARMessage;
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
import static org.junit.Assert.assertNotNull;


public class BKVMessageDaoBeanFunctionalTest extends AbstractBKVDaoBeanFunctionalTest {


    @Autowired
    @Qualifier(value = "earMessageDaoImpl")
    EARMessageDao dao1;

    @Test
    public void findAllWithSortingAndPaging12() throws Exception {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime from = ZonedDateTime.parse("2014-12-05 13:34:42 +1100", f);
        ZonedDateTime to = ZonedDateTime.parse("2014-12-06 14:22:53 +1100", f);

        ZonedDateTime from1 = ZonedDateTime.parse("2014-12-05 13:34:53 +1100", f);
        ZonedDateTime to1 = ZonedDateTime.parse("2014-12-06 14:25:03 +1100", f);

        ZonedDateTime from2 = ZonedDateTime.parse("2014-12-16 11:57:25 +1100", f);
        ZonedDateTime to2 = ZonedDateTime.parse("2014-12-16 11:57:30 +1100", f);

        List<EARMessage> list = dao1.findByAllNullable(null, null, null, null, null, null);
        l.info("total elements0: {}", list.size());
        assertEquals(7, list.size());
        list = dao1.findByAllNullable(from, to, null, null, null, null);
        l.info("total elements1: {}", list.size());
        assertEquals(4, list.size());
        list = dao1.findByAllNullable(from, null, null, null, null, null);
        l.info("total elements2: {}", list.size());
        assertEquals(6, list.size());
        list = dao1.findByAllNullable(null, to, null, null, null, null);
        l.info("total elements3: {}", list.size());
        assertEquals(5, list.size());


        list = dao1.findByAllNullable(null, null, from1, to1, null, null);
        l.info("total elements1: {}", list.size());
        assertEquals(4, list.size());
        list = dao1.findByAllNullable(null, null, from1, null, null, null);
        l.info("total elements2: {}", list.size());
        assertEquals(6, list.size());
        list = dao1.findByAllNullable(null, null, null, to1, null, null);
        l.info("total elements3: {}", list.size());
        assertEquals(5, list.size());

        list = dao1.findByAllNullable(null, null, null, null, from2, to2);
        l.info("total elements1: {}", list.size());
        assertEquals(4, list.size());
        list = dao1.findByAllNullable(null, null, null, null, from2, null);
        l.info("total elements2: {}", list.size());
        assertEquals(6, list.size());
        list = dao1.findByAllNullable(null, null, null, null, null, to2);
        l.info("total elements3: {}", list.size());
        assertEquals(5, list.size());

        from = ZonedDateTime.parse("2014-12-05 13:34:42 +1100", f);
        to = ZonedDateTime.parse("2014-12-06 14:22:54 +1100", f);
        from1 = ZonedDateTime.parse("2014-12-05 13:34:59 +1100", f);
        to1 = ZonedDateTime.parse("2014-12-06 14:25:03 +1100", f);
        from2 = ZonedDateTime.parse("2014-12-16 11:57:27 +1100", f);
        to2 = ZonedDateTime.parse("2014-12-16 11:57:28 +1100", f);
        list = dao1.findByAllNullable(from, to, from1, to1, from2, to2);
        l.info("total elements1: {}", list.size());
        assertEquals(1, list.size());

        to2 = ZonedDateTime.parse("2014-12-16 11:57:27 +1100", f);
        list = dao1.findByAllNullable(from, to, from1, to1, from2, to2);
        l.info("total elements1: {}", list.size());
        assertEquals(0, list.size());
    }

    @Test
    public void findAllWithSortingAndPaging() throws Exception {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        ZonedDateTime from = ZonedDateTime.parse("2014-12-05 00:00:00 +1100", f);
        ZonedDateTime to = ZonedDateTime.parse("2014-12-06 14:22:53 +1100", f);

        ZonedDateTime from1 = ZonedDateTime.parse("2014-12-05 13:34:52 +1100", f);
        ZonedDateTime to1 = ZonedDateTime.parse("2014-12-06 14:24:03 +1100", f);

        ZonedDateTime from2 = ZonedDateTime.parse("2014-12-16 11:57:23 +1100", f);
        ZonedDateTime to2 = ZonedDateTime.parse("2015-12-16 11:57:28 +1100", f);

        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        int startPage = 0;
        int pageSize = 3;
        PageRequest pageRequest = new PageRequest(startPage, pageSize, sort);

        Page<EARMessage> page = dao1.findByAllNullable(from, to, from1, to1, from2, to2, pageRequest);

        l.info("total pages: {}", page.getTotalPages());
        l.info("total elements: {}", page.getTotalElements());

        while (true) {
            l.info("==============page idx: {}", page.getNumber());
            l.info("total elements per pages: {}", page.getNumberOfElements());
            for (EARMessage message : page.getContent()) {
                l.info("message: {}", message);
                l.info("att.size: {}", message.getAttachmentList().size());
            }
//            if (page.hasNextPage())
            if (page.hasNext())
                page = dao1.findByAllNullable(from, to, from1, to1, from2, to2, page.nextPageable());
            else
                break;
        }
    }

    @Test
    public void save3() throws Exception {
        EARMessage message = new EARMessage();
        message.setFrom("from");
        message.setReceivedDate(ZonedDateTime.now());
        message.setAttachmentList(null);
        dao1.save(message);

        EARMessage message1 = new EARMessage();
        message1.setFrom("from1");
        message1.setReceivedDate(ZonedDateTime.now());
        message1.setAttachmentList(null);

        dao1.save(message1);

    }

    @Test
    public void findAllByPage() throws Exception {
        PageRequest pageRequest = new PageRequest(1, 2);
        Page<EARMessage> byPage = dao1.findAllByPage(pageRequest);
        List<EARMessage> content = byPage.getContent();
        assertNotNull(content);
        List<EARAttachment> attachmentList = content.get(0).getAttachmentList();
        l.info("size: {}", attachmentList.size());
    }

    @Test
    public void testGetLastProcessedDate() throws Exception {
        ZonedDateTime lastReceivedDate = dao1.findLastReceivedDate();
        l.info("lastProcessedDate: {}", lastReceivedDate);
    }
}