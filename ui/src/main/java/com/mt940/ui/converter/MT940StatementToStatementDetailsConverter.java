package com.mt940.ui.converter;


import com.mt940.domain.mt940.MT940Statement;
import com.mt940.ui.domain.Amount;
import com.mt940.ui.domain.StatementDetails;
import org.springframework.core.convert.converter.Converter;

public class MT940StatementToStatementDetailsConverter implements Converter<MT940Statement, StatementDetails> {
    @Override
    public StatementDetails convert(MT940Statement source) {
        StatementDetails result = new StatementDetails();

        result.setId(source.getId());
        if (source.getSettlementFile() != null) {
            result.setAttachmentId(source.getSettlementFile().getId());
        }
        if (source.getSettlementFile() != null && source.getSettlementFile().getMessage() != null) {
            result.setMessageId(source.getSettlementFile().getMessage().getId());
        }
        result.setAccountId(source.getAccountId());
        result.setStatementNumber(source.getStatementNumber());
        result.setSequenceNumber(source.getSequenceNumber());
        result.setTransactionReference(source.getTransactionReference());
        if (source.getOpeningBalance() != null) {
            result.setOpeningBalance(new Amount(source.getOpeningBalance().getAmount(), source.getOpeningBalance().getCurrency()));
        }
        if (source.getClosingBalance() != null) {
            result.setClosingBalance(new Amount(source.getClosingBalance().getAmount(), source.getClosingBalance().getCurrency()));
        }

        result.setSWIFTHeader1(source.getSWIFTHeader1());
        result.setSWIFTHeader2(source.getSWIFTHeader2());
        result.setSWIFTHeader3(source.getSWIFTHeader3());
        result.setTransactionReference(source.getTransactionReference());
        result.setRelatedReference(source.getRelatedReference());
        result.setEntryOrder(source.getEntryOrder());

        return result;
    }
}
