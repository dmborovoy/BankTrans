package com.mt940.daemon.endpoints;

import com.mt940.daemon.email.EmailFragment;
import com.mt940.daemon.exceptions.MT940Exception;
import com.mt940.daemon.mt940.MT940Parser;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import com.mt940.domain.mt940.MT940Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by dimas on 6/15/2015.
 */
@Service("BKVParser")
public class BKVParser {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier(value = "MT940Parser")
    MT940Parser validator;

    public void execute(EARAttachment attachment, File file) throws IOException, MT940Exception {

    }

    public Message<List<EARAttachment>> handle(final Message<List<EmailFragment>> in) throws MessagingException, UnsupportedEncodingException, MT940Exception {
        log.info("=============Start MT940 parser processing=============");
        List<EARAttachment> result = new ArrayList<>();
        EARAttachmentStatus status = EARAttachmentStatus.NEW;
        List<EmailFragment> list = in.getPayload();
        for (EmailFragment fragment : list) {
            EARAttachment attachment = new EARAttachment();
            try {
                byte[] data = (byte[]) fragment.getData();
                attachment.setRawData(data);
                attachment.setOriginalName(fragment.getFilename());
                attachment.setSize(fragment.getSize());
                attachment.setUniqueName(fragment.getUniqueFileName());
                SortedSet<MT940Statement> statements = validator.mapListOfStatements(validator.extractListOfStatements(new String(data, "UTF-8")));
                attachment.setStatementSet(statements);
                status = EARAttachmentStatus.PROCESSED;
            } catch (Exception e) {
                status = EARAttachmentStatus.ERROR;
            } finally {
                attachment.setStatus(status);
                result.add(attachment);
            }
        }
        log.info("=============END MT940 parser processing=============");
        log.info("size {}", list.size());
        return MessageBuilder.withPayload(result)
                .copyHeaders(in.getHeaders())
                .build();
    }
}
