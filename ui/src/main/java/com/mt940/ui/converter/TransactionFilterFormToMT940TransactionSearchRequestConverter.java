package com.mt940.ui.converter;

import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.enums.MT940TransactionStatus;
import com.mt940.domain.mt940.MT940TransactionSearchRequest;
import com.mt940.ui.form.TransactionFilterForm;
import org.springframework.core.convert.converter.Converter;

import java.time.ZoneId;

public class TransactionFilterFormToMT940TransactionSearchRequestConverter
        implements Converter<TransactionFilterForm, MT940TransactionSearchRequest> {

    @Override
    public MT940TransactionSearchRequest convert(TransactionFilterForm source) {

        if (source == null) {
            return null;
        } else {
            MT940TransactionSearchRequest result = new MT940TransactionSearchRequest();

            result.statementId = source.getStatementId();
            result.fileId = source.getFileId();
            result.messageId = source.getMessageId();
            result.from = source.getFrom() == null ? null : source.getFrom().atZone(ZoneId.systemDefault());
            result.to = source.getTo() == null ? null : source.getTo().atZone(ZoneId.systemDefault());
            result.fundsCode = source.getFundsCode() == null ? null : MT940FundsCode.findByCode(source.getFundsCode());
            result.referenceForAccountOwner = source.getRefForAccountOwner();
            result.referenceForBank = source.getRefForBank();
            result.transactionDescription = source.getTransactionDescription();
            result.status = source.getStatus() == null ? null : MT940TransactionStatus.findByCode(source.getStatus());
            result.instance = source.getInstance() == null ? null : Instance.findByCode(source.getInstance());
            result.informationToAccountOwner = source.getInfoToAccountOwner();

            return result;
        }
    }
}
