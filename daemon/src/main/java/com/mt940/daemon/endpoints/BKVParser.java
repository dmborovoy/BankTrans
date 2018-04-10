package com.mt940.daemon.endpoints;

import com.mt940.daemon.email.EmailFragment;
import com.mt940.daemon.mt940.MT940Parser;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import com.mt940.domain.mt940.MT940Statement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

@Slf4j
@Service("bkvParser")
public class BKVParser {

    private final MT940Parser validator;

    @Autowired
    public BKVParser(@Qualifier(value = "MT940Parser") MT940Parser validator) {
        this.validator = validator;
    }

    public Message<List<EARAttachment>> handle(final Message<List<EmailFragment>> in) {
        log.info("===Start MT940 parser processing===");
        List<EARAttachment> result = in.getPayload().stream()
                .map(this::map)
                .collect(Collectors.toList());
        log.info("===End MT940 parser processing===");
        return MessageBuilder.withPayload(result)
                .copyHeaders(in.getHeaders())
                .build();
    }

    private EARAttachment map(EmailFragment fragment) {
        EARAttachment attachment = new EARAttachment();
        try {
            String body = getPayload(fragment.getData());
            attachment.setRawData(body);
            attachment.setOriginalName(fragment.getFilename());
            attachment.setOriginalName(fragment.getFilename());
            attachment.setSize(fragment.getSize());
            attachment.setUniqueName(fragment.getUniqueFileName());
            SortedSet<MT940Statement> statements = validator.mapListOfStatements(validator.extractListOfStatements(body));
            attachment.setStatementSet(statements);
            attachment.setStatus(EARAttachmentStatus.PROCESSED);
        } catch (Exception e) {
            log.error("{}", e);
            attachment.setStatus(EARAttachmentStatus.ERROR);
        }
        return attachment;
    }

    private String getPayload(Object obj) {
        String result = null;
        if (obj instanceof String) {
            result = (String) obj;
        } else {
            result = new String((byte[]) obj);
        }
        return result;
    }
}
