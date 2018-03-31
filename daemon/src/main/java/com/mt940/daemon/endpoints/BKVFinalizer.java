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
import com.mt940.dao.jpa.EARMessageDao;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.EARMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.mail.MessagingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class BKVFinalizer {

    private static final Logger log = LoggerFactory.getLogger(BKVFinalizer.class);

    ZonedDateTime currentReceivedDate;

    @Autowired @Qualifier(value = "earMessageDaoImpl")
    private EARMessageDao earMessageDaoBean;

    @Value("${bkv.daemon.dir.root}")
    private String rootDir;

    public Message<List<EARAttachment>> handle(final Message<List<EARAttachment>> in) throws MessagingException {
        MessageHeaders headers = in.getHeaders();
        currentReceivedDate = (ZonedDateTime) in.getHeaders().get(BKVHeaders.CURRENT_RECEIVED_DATE);
        List<EARAttachment> attachments = in.getPayload();
        javax.mail.Message email = (javax.mail.Message) headers.get(BKVHeaders.MAIL_MESSAGE);
        EARMessage earMessage = map(email, attachments);
        EARMessage save = earMessageDaoBean.save(earMessage);
        Long id = save.getId();
        log.info("Email id: {}, attachment count: {}, sent date: {}", id, save.getAttachmentsCount(), save.getSentDate());
        return MessageBuilder.withPayload(attachments)
                .build();
    }

    private EARMessage map(javax.mail.Message email, List<EARAttachment> attachments) throws MessagingException {
        EARMessage message = new EARMessage();
        message.setFrom(StringUtils.join(email.getFrom(), ";"));
        message.setTo(StringUtils.join(email.getRecipients(javax.mail.Message.RecipientType.TO), ";"));
        message.setSubject(email.getSubject());
//        message.setSentDate(new DateTime(email.getSentDate()));
        message.setSentDate(ZonedDateTime.ofInstant(email.getSentDate().toInstant(), ZoneId.systemDefault()));
//        message.setReceivedDate((new DateTime(email.getReceivedDate())));
//        TODO DB: remove this workaround after byg fix
        Date receivedDate = email.getReceivedDate();
        if (receivedDate == null) {
            if (currentReceivedDate != null) {
//                DateTimeZone zone = DateTimeZone.forID(currentReceivedDate.getZone().getId());
//                DateTime dt = new DateTime(currentReceivedDate.toInstant().toEpochMilli(), zone);
//                DateTime dt = new DateTime(currentReceivedDate.toInstant().toEpochMilli());
//                message.setReceivedDate(dt);
                message.setReceivedDate(currentReceivedDate);
            } else {
                message.setReceivedDate(null);
            }
        } else {
//            message.setReceivedDate(new DateTime(receivedDate));
            message.setReceivedDate(ZonedDateTime.ofInstant(receivedDate.toInstant(), ZoneId.systemDefault()));
        }
        message.setMessageContent("-skipped-");//TODO DB: remove from db scheme - redundant field
        message.setDownloadPath(rootDir);
        message.setProcessingDate(ZonedDateTime.now());
        message.setAttachmentsCount(attachments.size());
        message.setAttachmentList(attachments);
        return message;
    }
}
