package com.mt940.daemon.endpoints;

import com.mt940.daemon.BKVHeaders;
import com.mt940.domain.EARAttachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.integration.mail.MailHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Component("bkvAggregator")
public class BKVAggregator {

    @Value("${bkv.daemon.email.sender.max-message-to-send}")
    private int maxMessageToSend;

    @Value("${bkv.daemon.email.sender.subject.ok}")
    private String subjectOk;

    @Value("${bkv.daemon.email.sender.subject.error}")
    private String subjectError;

    @Aggregator
    public Message<?> aggregate(final List<Message<List<EARAttachment>>> in) throws MessagingException {
        log.info("$$$$$ aggregating....");
        log.info("Message count: {}", in.size());
        return MessageBuilder.withPayload(okBody(in))
                .setHeader(MailHeaders.SUBJECT, subjectOk)
                .build();
    }

    private String okBody(List<Message<List<EARAttachment>>> in) throws MessagingException {
        StringBuilder builder = new StringBuilder("===============================================\n");
        builder.append(String.format("Was processed '%d' new email(s):\n", in.size()));
        builder.append("===============================================\n\n");
        for (Message<List<EARAttachment>> message : in) {
            String subj = "Subject: " + ((MimeMessage) message.getHeaders().get(BKVHeaders.MAIL_MESSAGE)).getSubject();
            String sent = String.format("Sent date: %s", ((MimeMessage) message.getHeaders().get(BKVHeaders.MAIL_MESSAGE)).getSentDate());
            log.debug(subj);
            log.debug(sent);
            builder.append(String.format("%s\n%s\nFile list (%d) :\n", subj, sent, message.getPayload().size()));
            for (EARAttachment el : message.getPayload()) {
                String out = String.format("\t%-70s-\t[%s];\n", el.getUniqueName(), el.getStatus());
                log.debug(out);
                builder.append(out);
            }
            builder.append("---------------------------------\n");
        }
        return builder.append("\nFor details visit bkv-ui site").toString();
    }


    @CorrelationStrategy
    public String correlateBy(@Header("id") String id) {
        log.info("id: {}; correlation id: {}", id, id);
        return "BKVGroup";
    }

    @ReleaseStrategy
    public boolean release(List<Message<?>> messages) {
        return messages.size() == maxMessageToSend;
    }
}
