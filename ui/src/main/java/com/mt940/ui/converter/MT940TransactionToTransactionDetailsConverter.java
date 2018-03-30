package com.mt940.ui.converter;

import com.mt940.domain.mt940.MT940Transaction;
import com.mt940.ui.domain.Amount;
import com.mt940.ui.domain.TransactionDetails;
import org.springframework.core.convert.converter.Converter;

public class MT940TransactionToTransactionDetailsConverter implements Converter<MT940Transaction, TransactionDetails> {
    @Override
    public TransactionDetails convert(MT940Transaction source) {
        TransactionDetails result = new TransactionDetails();

        result.setId(source.getId());
        if(source.getStatement() != null) {
            result.setStatementId(source.getStatement().getId());
           if (source.getStatement().getSettlementFile() != null) {
               if (source.getStatement().getSettlementFile().getMessage() != null) {
                   result.setMessageId(source.getStatement().getSettlementFile().getMessage().getId());
               }
               result.setFileId(source.getStatement().getSettlementFile().getId());
           }
        }
        if (source.getStatement() != null) {
            result.setStatementId(source.getStatement().getId());
        }
        if (source.getAmount() != null) {
            result.setAmount(new Amount(source.getAmount(), source.getCurrency()));
        }
        if (source.getDate() != null) {
            result.setDate(source.getDate().toLocalDate());
        }
        result.setTransactionDescription(source.getTransactionDescription());
        result.setStatus(source.getStatus());
        result.setReferenceForAccountOwner(source.getReferenceForAccountOwner());
        result.setInstance(source.getInstance());
        result.setFundsCode(source.getFundsCode());
        result.setEntryOrder(source.getEntryOrder());
        result.setSwiftCode(source.getSwiftCode());
        result.setEntryDate(source.getEntryDate() == null ? null : source.getEntryDate().toLocalDate());
        result.setReferenceForBank(source.getReferenceForBank());
        result.setErrorDescription(source.getErrorDescription());
        result.setRawTransaction(source.getRawTransaction());
        result.setInformationToAccountOwner(source.getInformationToAccountOwner());

        return result;
    }
}
