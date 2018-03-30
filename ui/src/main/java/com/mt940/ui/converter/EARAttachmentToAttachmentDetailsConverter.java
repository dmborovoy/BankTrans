package com.mt940.ui.converter;

import com.mt940.domain.EARAttachment;
import com.mt940.ui.domain.AttachmentDetails;
import org.springframework.core.convert.converter.Converter;

public class EARAttachmentToAttachmentDetailsConverter implements Converter<EARAttachment, AttachmentDetails> {
    @Override
    public AttachmentDetails convert(EARAttachment source) {
        AttachmentDetails result = new AttachmentDetails();

        result.setId(source.getId());
        if (source.getMessage() != null) {
            result.setMessageId(source.getMessage().getId());
        }
        result.setOriginalName(source.getOriginalName());
        result.setUniqueName(source.getUniqueName());
        result.setSize(source.getSize());
        result.setStatus(source.getStatus());

        return result;
    }
}
