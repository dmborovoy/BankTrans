package com.mt940.daemon.endpoints;

import com.mt940.daemon.BKVHeaders;
import com.mt940.dao.jpa.EARMessageDao;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.EARMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component("bkvFinalizer")
public class BKVFinalizer {

    @Autowired
    @Qualifier(value = "earMessageDaoImpl")
    private EARMessageDao earMessageDaoBean;

    @Value("${bkv.daemon.dir.root}")
    private String rootDir;

    public Message<?> handle(final Message<List<EARAttachment>> in) throws MessagingException {
        MessageHeaders headers = in.getHeaders();
        List<EARAttachment> attachments = in.getPayload();
        javax.mail.Message email = (javax.mail.Message) headers.get(BKVHeaders.MAIL_MESSAGE);
        EARMessage earMessage = map(email, attachments);
        EARMessage save = earMessageDaoBean.save(earMessage);
        Long id = save.getId();
        log.info("Email id: {}, attachment count: {}, sent date: {}", id, save.getAttachmentsCount(), save.getSentDate());
        attachments.forEach(a -> {
            try {
                FileUtils.writeStringToFile(new File(rootDir + "/" + a.getUniqueName()), a.getRawData(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("{}", e);
            }
        });
        return MessageBuilder
                .withPayload(attachments)
                .build();
    }

    private EARMessage map(javax.mail.Message email, List<EARAttachment> attachments) throws MessagingException {
        EARMessage message = new EARMessage();
        message.setFrom(StringUtils.join(email.getFrom(), ";"));
        message.setTo(StringUtils.join(email.getRecipients(javax.mail.Message.RecipientType.TO), ";"));
        message.setSubject(email.getSubject());
        message.setSentDate(ZonedDateTime.ofInstant(email.getSentDate().toInstant(), ZoneId.systemDefault()));
        message.setReceivedDate(ZonedDateTime.ofInstant(email.getReceivedDate().toInstant(), ZoneId.systemDefault()));
        message.setMessageContent("-skipped-");//TODO DB: remove from db scheme - redundant field
        message.setDownloadPath(rootDir);
        message.setProcessingDate(ZonedDateTime.now());
        message.setAttachmentsCount(attachments.size());
        message.setAttachmentList(attachments);
        return message;
    }

}
