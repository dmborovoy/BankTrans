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
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("bkvEmailTransformer")
public class BKVEmailTransformer {

    private final String relativePath = "/";

    @Transformer
    public Message<?> transform(javax.mail.Message mailMessage) throws MessagingException {
        final List<EmailFragment> emailFragments = new ArrayList<EmailFragment>();
        ZonedDateTime currentReceivedDate = mailMessage.getReceivedDate().toInstant().atZone(ZoneId.systemDefault());
        EmailParserUtils.handleMessage(new File(relativePath), mailMessage, emailFragments);
        log.info(String.format("Email contains %s fragments", emailFragments.size()));
        return MessageBuilder.withPayload(emailFragments)
                .setHeader(BKVHeaders.MAIL_MESSAGE, mailMessage)
                .setHeader(BKVHeaders.CURRENT_RECEIVED_DATE, currentReceivedDate)
                .build();
    }
}
