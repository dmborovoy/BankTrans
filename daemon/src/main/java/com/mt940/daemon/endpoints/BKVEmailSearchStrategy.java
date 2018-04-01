package com.mt940.daemon.endpoints;

import com.mt940.dao.jpa.EARMessageDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mail.SearchTermStrategy;
import org.springframework.stereotype.Service;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service("bkvEmailSearchStrategy")
public class BKVEmailSearchStrategy implements SearchTermStrategy {

    @Autowired
    @Qualifier(value = "earMessageDaoImpl")
    private EARMessageDao earMessageDaoBean;

    private ZonedDateTime lastReceivedDate;

    public ZonedDateTime getLastReceivedDate() {
        return lastReceivedDate;
    }

    public SearchTerm generateSearchTerm(Flags flags, Folder folder) {
        log.info("=====================");
        lastReceivedDate = earMessageDaoBean.findLastReceivedDate();
        if (lastReceivedDate == null) {
            log.warn("Did you launch it first time? Can't find last received date");
            lastReceivedDate = ZonedDateTime.now().minusYears(1000);
        }
        log.info("last received date was: {}", lastReceivedDate.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        //NOTE DB: IMAP LIMITATION - search by received date is limited to date. Hours, minutes and lower are ignored
        //http://stackoverflow.com/questions/6963256/javamail-search-by-receiveddate-doesnt-work-down-to-the-second
        Date roundedDate = Date.from(lastReceivedDate.truncatedTo(ChronoUnit.DAYS).toInstant());
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, roundedDate);
        log.info("start searching in INBOX folder messages newer than {}...", roundedDate);
        return newerThan;
    }
}
