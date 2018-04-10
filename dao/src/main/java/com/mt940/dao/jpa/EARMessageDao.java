package com.mt940.dao.jpa;

import com.mt940.domain.EARMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface EARMessageDao extends Dao<EARMessage, Long> {

    public Page<EARMessage> findAllByPage(Pageable pageable);

    public List<EARMessage> findByAllNullable(ZonedDateTime from, ZonedDateTime to, ZonedDateTime from1, ZonedDateTime to1, ZonedDateTime from2, ZonedDateTime to2);

    public Page<EARMessage> findByAllNullable(ZonedDateTime from, ZonedDateTime to, ZonedDateTime from1, ZonedDateTime to1, ZonedDateTime from2, ZonedDateTime to2, Pageable pageable);

//    public ZonedDateTime getLastProcessedDate();

    public ZonedDateTime findLastReceivedDate();

    List<String> findDistinctSenders();

}
