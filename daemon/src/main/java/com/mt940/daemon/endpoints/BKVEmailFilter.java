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

import com.mt940.daemon.BKVHeaders;
import com.mt940.daemon.email.EmailFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.Filter;
import org.springframework.messaging.Message;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BKVEmailFilter {

    private static final Logger log = LoggerFactory.getLogger(BKVEmailFilter.class);

    @Autowired
    @Qualifier(value = "bkvEmailSearchStrategy")
    private BKVEmailSearchStrategy strategy;

    @Filter
    public boolean accept(final Message<List<EmailFragment>> in) {
        if (in.getPayload().size() == 0) {
            log.info("Skipping messages without attachments...: {}", in);
            return false;
        }
        ZonedDateTime currentReceivedDate = (ZonedDateTime) in.getHeaders().get(BKVHeaders.CURRENT_RECEIVED_DATE);
        ZonedDateTime lastReceivedDate = strategy.getLastReceivedDate();
        log.info("current received date: {}; last received date: {}", currentReceivedDate == null ? null : currentReceivedDate.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), lastReceivedDate == null ? null : lastReceivedDate.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        if (lastReceivedDate == null || currentReceivedDate == null)
            return true;
        if (lastReceivedDate.isAfter(currentReceivedDate) || lastReceivedDate.isEqual(currentReceivedDate)) {
            log.info("Skipping already processed message...: {}", in);
            return false;
        }
        return true;
    }
}
