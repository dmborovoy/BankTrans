/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mt940.daemon.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component("bkvEmailFilter")
public class BKVEmailFilter {

    @Autowired
    @Qualifier(value = "bkvEmailSearchStrategy")
    private BKVEmailSearchStrategy strategy;

    @Filter
    public boolean accept(final javax.mail.Message in) throws MessagingException {
        ZonedDateTime lastReceivedDate = strategy.getLastReceivedDate();
        if (lastReceivedDate == null || in.getReceivedDate() == null) return true;
        ZonedDateTime currentReceivedDate = in.getReceivedDate().toInstant().atZone(ZoneId.systemDefault());
        log.info("email: {}", in.getSubject());
        log.info("current received date: {}; last received date: {}", toStringSafe(currentReceivedDate), toStringSafe(lastReceivedDate));
        if (lastReceivedDate.isAfter(currentReceivedDate) || lastReceivedDate.isEqual(currentReceivedDate)) {
            log.info("Skipping already processed message...");
            return false;
        }
        return true;
    }

    private String toStringSafe(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
