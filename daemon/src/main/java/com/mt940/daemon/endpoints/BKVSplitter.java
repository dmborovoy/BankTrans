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
import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public class BKVSplitter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Value("${bkv.daemon.dir.working}")
    private String workingDir;
    @Value("${bkv.daemon.dir.failed}")
    private String failedDir;
    @Value("${bkv.daemon.dir.done}")
    private String doneDir;

    @Splitter
    public List<Message<?>> splitIntoMessages(final Message<List<EARAttachment>> in) throws MessagingException {
        log.info("************ splitting file .... {}", in);
        List<EARAttachment> attachments = in.getPayload();
        final List<Message<?>> messages = new ArrayList<Message<?>>();
        for (EARAttachment attachment : attachments) {
            Message<?> message = MessageBuilder.withPayload(attachment.getRawData())
                    .setHeader(FileHeaders.FILENAME, attachment.getUniqueName())
                    .setHeader(BKVHeaders.DIRECTORY, outDir(attachment.getStatus()))
                    .build();
            messages.add(message);
        }
        if (messages.size() == 0) {
            Message<?> message = MessageBuilder.withPayload(null)
                    .build();
            messages.add(message);
        }
        return messages;
    }

    private String outDir(EARAttachmentStatus status) {
        String out = workingDir;
        if (status.equals(EARAttachmentStatus.PROCESSED)) {
            out = doneDir;
        } else if (status.equals(EARAttachmentStatus.ERROR)) {
            out = failedDir;
        }
        return out;
    }
}
