package com.mt940.ui.converter;


import com.mt940.domain.mt940.MT940Transaction;
import com.mt940.ui.domain.Transaction;
import org.springframework.core.convert.converter.Converter;

public class MT940TransactionToTransactionConverter implements Converter<MT940Transaction, Transaction> {
    @Override
    public Transaction convert(MT940Transaction source) {
        Transaction result = new Transaction();

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
            result.setAmount(source.getAmount());
            result.setCurrency(source.getCurrency());
        }
        if (source.getDate() != null) {
            result.setDate(source.getDate().toLocalDate());
        }
        result.setTransactionDescription(source.getTransactionDescription());
        result.setStatus(source.getStatus());
        result.setInstance(source.getInstance());
        result.setFundsCode(source.getFundsCode());
        result.setReferenceForAccountOwner(source.getReferenceForAccountOwner());

        return result;
    }
}
