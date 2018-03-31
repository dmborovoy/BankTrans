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
package com.mt940.daemon.email;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public final class EmailParserUtils {

//    TODO DB: add more warnings in log about data content
    private static final Logger log = LoggerFactory.getLogger(EmailParserUtils.class);

    /**
     * Prevent instantiation.
     */
    private EmailParserUtils() {
        throw new AssertionError();
    }

    /**
     * Parses a mail message. The respective message can either be the root message
     * or another message that is attached to another message.
     * <p>
     * If the mail message is an instance of {@link String}, then a {@link EmailFragment}
     * is being created using the email message's subject line as the file name,
     * which will contain the mail message's content.
     * <p>
     * If the mail message is an instance of {@link Multipart} then we delegate
     * to {@link #handleMultipart(File, Multipart, javax.mail.Message, List)}.
     *
     * @param directory      The directory for storing the message. If null this is the root message.
     * @param mailMessage    The mail message to be parsed. Must not be null.
     * @param emailFragments Must not be null.
     */
    public static void handleMessage(final File directory, final javax.mail.Message mailMessage, final List<EmailFragment> emailFragments) {
//        Assert.notNull(directory, "The directory to store must not be null.");
        Assert.notNull(mailMessage, "The mail message to be parsed must not be null.");
        Assert.notNull(emailFragments, "The collection of emailfragments must not be null.");
        final Object content;
        final String subject;
        try {
            content = mailMessage.getContent();
            subject = mailMessage.getSubject();
            log.debug("subject: {}", subject);

        } catch (IOException | MessagingException e) {
            throw new IllegalStateException("Error while retrieving the email contents.", e);
        }
        final File directoryToUse = directory;

//        if (directory == null) {
//            directoryToUse = new File(subject);
//        } else {
//            directoryToUse = new File(directory, subject);
//        }

        if (content instanceof String) {
//            log.debug("skipping body1...");
//            emailFragments.add(new EmailFragment(new File(subject), "message.txt", content));
//            emailFragments.add(new EmailFragment(directory, "message.txt", content));
//            log.warn("skipping message without attachment: {}", mailMessage);
        } else if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
//            handleMultipart(directoryToUse, multipart, mailMessage, emailFragments);
            handleMultipart(directory, multipart, mailMessage, emailFragments);
        } else {
            throw new IllegalStateException("This content type is not handled - " + content.getClass().getSimpleName());
        }
    }

    /**
     * Parses any {@link Multipart} instances that contain text or Html attachments,
     * {@link InputStream} instances, additional instances of {@link Multipart}
     * or other attached instances of {@link javax.mail.Message}.
     * <p>
     * Will create the respective {@link EmailFragment}s representing those attachments.
     * <p>
     * Instances of {@link javax.mail.Message} are delegated to
     * {@link #handleMessage(File, javax.mail.Message, List)}. Further instances
     * of {@link Multipart} are delegated to
     * {@link #handleMultipart(File, Multipart, javax.mail.Message, List)}.
     *
     * @param directory      Must not be null
     * @param multipart      Must not be null
     * @param mailMessage    Must not be null
     * @param emailFragments Must not be null
     */

    public static void handleMultipart(File directory, Multipart multipart, javax.mail.Message mailMessage, List<EmailFragment> emailFragments) {
        Assert.notNull(directory, "The directory must not be null.");
        Assert.notNull(multipart, "The multipart object to be parsed must not be null.");
        Assert.notNull(mailMessage, "The mail message to be parsed must not be null.");
        Assert.notNull(emailFragments, "The collection of emailfragments must not be null.");
        final int count;
        try {
            count = multipart.getCount();
            log.info(String.format("Number of enclosed BodyPart objects: %s.", count));
        } catch (MessagingException e) {
            throw new IllegalStateException("Error while retrieving the number of enclosed BodyPart objects.", e);
        }

        for (int i = 0; i < count; i++) {
            final BodyPart bp;
            try {
                bp = multipart.getBodyPart(i);
            } catch (MessagingException e) {
                throw new IllegalStateException("Error while retrieving body part.", e);
            }

            final String contentType;
            String filename;
            final String disposition;
            final String subject;

            try {
                contentType = bp.getContentType();
                filename = bp.getFileName();
                disposition = bp.getDisposition();
                subject = mailMessage.getSubject();
                if (filename == null && bp instanceof MimeBodyPart) {
                    filename = ((MimeBodyPart) bp).getContentID();
                }
            } catch (MessagingException e) {
                throw new IllegalStateException("Unable to retrieve body part meta data.", e);
            }
            log.info(String.format("BodyPart - Content Type: '%s', filename: '%s', disposition: '%s', subject: '%s'",
                    new Object[]{contentType, filename, disposition, subject}));

            if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
                log.info(String.format("1Handdling attachment '%s', type: '%s'", filename, contentType));
            }

            final Object content;

            try {
                content = bp.getContent();
            } catch (IOException | MessagingException e) {
                throw new IllegalStateException("Error while retrieving the email contents.", e);
            }

            if (content instanceof String) {
//                log.warn("skipping body...");
//                if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
//                    emailFragments.add(new EmailFragment(directory, i + "-" + filename, content));
//                    log.info(String.format("2Handdling attachment '%s', type: '%s'", filename, contentType));
//                } else {
//
//                    final String textFilename;
//                    final ContentType ct;
//
//                    try {
//                        ct = new ContentType(contentType);
//                    } catch (ParseException e) {
//                        throw new IllegalStateException("Error while parsing content type '" + contentType + "'.", e);
//                    }
//
//                    if ("text/plain".equalsIgnoreCase(ct.getBaseType())) {
//                        textFilename = "message.txt";
//                    } else if ("text/html".equalsIgnoreCase(ct.getBaseType())) {
//                        textFilename = "message.html";
//                    } else {
//                        textFilename = "message.other";
//                    }
//
//                    emailFragments.add(new EmailFragment(directory, textFilename, content));
//                }


            } else if (content instanceof InputStream) {

//                log.warn("InputStream...");
                final InputStream inputStream = (InputStream) content;
                final ByteArrayOutputStream bis = new ByteArrayOutputStream();

                try {
                    IOUtils.copy(inputStream, bis);
                } catch (IOException e) {
                    throw new IllegalStateException("Error while copying input stream to the ByteArrayOutputStream.", e);
                }

//                emailFragments.add(new EmailFragment(directory, filename, bis.toByteArray()));
                emailFragments.add(new EmailFragment(bis.toByteArray(), filename, generateName(filename), bis.size(), directory));

            } else if (content instanceof javax.mail.Message) {
//                handleMessage(directory, (javax.mail.Message) content, emailFragments);
                log.warn("handling of attached messages is not supported");
            } else if (content instanceof Multipart) {
                log.warn("Multipart...");
                final Multipart mp2 = (Multipart) content;
                handleMultipart(directory, mp2, mailMessage, emailFragments);
            } else {
                throw new IllegalStateException("Content type not handled: " + content.getClass().getSimpleName());
            }
        }
    }

    public static String generateName(String originalName) {
        return String.format("%s-%s-%s", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), UUID.randomUUID(), originalName);
    }
}
