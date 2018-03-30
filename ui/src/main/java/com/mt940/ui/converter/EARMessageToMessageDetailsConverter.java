package com.mt940.ui.converter;


import com.mt940.domain.EARMessage;
import com.mt940.ui.domain.MessageDetails;
import org.springframework.core.convert.converter.Converter;

public class EARMessageToMessageDetailsConverter implements Converter<EARMessage, MessageDetails> {
    @Override
    public MessageDetails convert(EARMessage source) {
        MessageDetails result = new MessageDetails();

        result.setId(source.getId());
        if (source.getSentDate() != null) {
            result.setSentDate(source.getSentDate().toLocalDateTime());
        }
        result.setFrom(source.getFrom());
        result.setTo(source.getTo());
        if (source.getReceivedDate() != null) {
            result.setReceivedDate(source.getReceivedDate().toLocalDateTime());
        }
        if (source.getProcessingDate() != null) {
            result.setProcessingDate(source.getProcessingDate().toLocalDateTime());
        }
        result.setSubject(source.getSubject());
        result.setMessage(source.getMessageContent());
        result.setAttachmentCount(source.getAttachmentsCount());
        result.setPath(source.getDownloadPath());

        return result;
    }
}
