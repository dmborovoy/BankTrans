//package com.mt940.rest.converters;
//
///**
// * Created by Mike S. on 26.04.2018.
// */
//
//import com.mt940.domain.enums.Instance;
//import com.mt940.domain.enums.MT940FundsCode;
//import com.mt940.domain.enums.MT940TransactionStatus;
//import com.mt940.domain.mt940.MT940TransactionSearchRequest;
//import com.mt940.rest.dto.TransactionView;
//import org.springframework.core.convert.converter.Converter;
//
//import java.time.ZoneId;
//
//public class TransactionViewToMT940TransactionSearchRequestConverter
//        implements Converter<TransactionView, MT940TransactionSearchRequest> {
//
//    @Override
//    public MT940TransactionSearchRequest convert(TransactionView source) {
//
//        if (source == null) {
//            return null;
//        } else {
//            MT940TransactionSearchRequest result = new MT940TransactionSearchRequest();
//
//            result.statementId = source.getStatementId();
//            result.fileId = source.getId();
//            //result.messageId = source.getMessageId();
//            result.from = source.getEntryDate() == null ? null : source.getEntryDate().toLocalDateTime().atZone(ZoneId.systemDefault());
//            result.to = source.getDate() == null ? null : source.getDate().toLocalDateTime().atZone(ZoneId.systemDefault());
//            result.fundsCode = source.getFundsCode() == null ? null : MT940FundsCode.findByCode(source.getFundsCode());
//            result.referenceForAccountOwner = source.getRefForAccountOwner();
//            result.referenceForBank = source.getRefForBank();
//            result.transactionDescription = source.getTransactionDescription();
//            result.status = source.getStatus() == null ? null : MT940TransactionStatus.findByCode(source.getStatus());
//            result.instance = source.getInstance() == null ? null : Instance.findByCode(source.getInstance());
//            result.informationToAccountOwner = source.getInfoToAccountOwner();
//
//            return result;
//        }
//    }
//}
