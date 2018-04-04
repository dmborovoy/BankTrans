package com.mt940.daemon.endpoints;

import com.mt940.dao.jpa.MT940TransactionDao;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.MT940Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mail.MailHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service("bkvTransactionSearcher")
public class BKVTransactionSearcher {

    @Autowired
    @Qualifier(value = "mt940TransactionDaoImpl")
    MT940TransactionDao transactionDao;

    @Value("${bkv.daemon.email.sender.subject.ok}")
    private String subjectOk;

    @Value("${bkv.daemon.email.sender.subject.error}")
    private String subjectError;

    @Value("${bkv.daemon.error.search.enabled}")
    private boolean errorTransactionSearchEnabled;

    public Message<?> startSearchErrorTransactions() {
        String totalMessage = "";
        if (errorTransactionSearchEnabled) {
            log.info("error transaction search is initiated...");
            List<MT940Transaction> errorTransactionList = transactionDao.getErrorTransactionList(MT940FundsCode.DEBIT, MT940TransactionStatus.ERROR_BUSINESS);
            List<MT940Transaction> readTransactionList = transactionDao.getReadTransactionList(MT940FundsCode.DEBIT, MT940TransactionStatus.READ);
            List<MT940Transaction> unknownInstanceTransactionList = transactionDao.getUnknownInstanceTransactionList(MT940FundsCode.DEBIT);

            log.info("Founded '{}' error transactions", errorTransactionList == null ? null : errorTransactionList.size());
            log.info("Founded '{}' transactions with unknown instance", unknownInstanceTransactionList == null ? null : unknownInstanceTransactionList.size());
            log.info("Founded '{}' read but not confirmed transactions", readTransactionList == null ? null : readTransactionList.size());
            String errorTransactionMessage = "";
            String unknownInstanceTransactionMessage = "";
            String readTransactionMessage = "";

            if ((errorTransactionList != null && errorTransactionList.size() > 0)) {
                errorTransactionMessage = String.format("\nWe've found '%s' error transactions. Transaction list:\n%s", errorTransactionList.size(), printTransactionList(errorTransactionList));
            }
            if ((unknownInstanceTransactionList != null && unknownInstanceTransactionList.size() > 0)) {
                unknownInstanceTransactionMessage = String.format("\nWe've found '%s' transactions with unknown instance. Transaction list:\n%s", unknownInstanceTransactionList.size(), printTransactionList(unknownInstanceTransactionList));
            }
            if ((readTransactionList != null && readTransactionList.size() > 0)) {
                readTransactionMessage = String.format("\nWe've found '%s' transactions with status READ. Transaction list:\n%s", readTransactionList.size(), printTransactionList(readTransactionList));
            }
            totalMessage = errorTransactionMessage + unknownInstanceTransactionMessage + readTransactionMessage;
        } else {
            log.warn("error transaction search is disabled...");
        }
        if (totalMessage.length() > 0)
            return MessageBuilder.withPayload(totalMessage)
                    .setHeader(MailHeaders.SUBJECT, subjectError)
                    .build();
        else
            return null;
    }

    public String printTransactionList(List<MT940Transaction> list) {
        StringBuilder builder = new StringBuilder();
        for (MT940Transaction transaction : list) {
            builder.append("{id=" + transaction.getId() + "; ");
            builder.append("instance=" + transaction.getInstance() + "; ");
            builder.append("status=" + transaction.getStatus() + "; ");
            builder.append("funds=" + transaction.getFundsCode() + "; ");
            builder.append("date=" + transaction.getDate().format(DateTimeFormatter.ISO_DATE) + "; ");
            builder.append("amount=" + transaction.getAmount() + "; ");
            builder.append("currency=" + transaction.getCurrency() + "; ");
            builder.append("info=" + transaction.getInformationToAccountOwner() + "}\n");
        }
        return builder.toString();
    }

}
