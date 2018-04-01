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
import com.mt940.daemon.email.EmailParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import javax.mail.MessagingException;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BKVEmailTransformer {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private String relativePath = "/";

    @Transformer
    public Message<?> transform(javax.mail.Message mailMessage) throws MessagingException {
        log.info("-----------------------");
        final List<EmailFragment> emailFragments = new ArrayList<EmailFragment>();
//        TODO DB: SI bug, update after fix arrives
//        http://stackoverflow.com/questions/30858250/spring-integation-and-received-date-issue
//        https://jira.spring.io/browse/INT-3744
        Date javaxReceived = mailMessage.getReceivedDate();
        ZonedDateTime currentReceivedDate = null;
        if (javaxReceived == null) {
            currentReceivedDate = tryToParseReceivedHeader(mailMessage.getHeader("Received"));
        } else {
            currentReceivedDate = javaxReceived.toInstant().atZone(ZoneId.systemDefault());
        }
        EmailParserUtils.handleMessage(new File(relativePath), mailMessage, emailFragments);
        log.info(String.format("Email contains %s fragments.", emailFragments.size()));
        return MessageBuilder.withPayload(emailFragments)
                .setHeader(BKVHeaders.MAIL_MESSAGE, mailMessage)
                .setHeader(BKVHeaders.CURRENT_RECEIVED_DATE, currentReceivedDate)
                .build();
    }//

    private ZonedDateTime tryToParseReceivedHeader(String[] in) {
        ZonedDateTime result = null;
        if (in == null || in[0] == null) {
            result = null;
        } else {
//            from ULYSSES.nxsys.ltd ([10.20.70.14]) by dub-exch-1.nxsys.ltd            ([::1]) with mapi id 14.02.0387.000; Thu, 28 May 2015 17:29:12 +0100
            String[] split = in[0].split(";");
            if (split.length == 2) {
                try {
//                  Thu, 28 May 2015 17:29:12 +0100
//                  Sat, 31 Mar 2018 19:54:15 -0700 (PDT)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
                    String str = split[1].trim().replace("\r\n", " ").replace("\r", " ").replace("\n", " ").replace("  ", " ");
                    log.debug("date to parse: {}", str);
                    result = ZonedDateTime.parse(str, formatter);
                    log.debug("parsed date: {}", result);
                } catch (Exception e) {
                    log.debug("error parsing");
                }
            }
        }
        return result;
    }
}
