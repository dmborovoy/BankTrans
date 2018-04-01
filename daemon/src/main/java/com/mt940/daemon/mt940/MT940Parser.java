package com.mt940.daemon.mt940;

import com.mt940.daemon.exceptions.MT940Exception;
import com.mt940.daemon.exceptions.MT940FormatException;
import com.mt940.daemon.exceptions.MT940MandatoryFieldException;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940BalanceType;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.mt940.MT940Balance;
import com.mt940.domain.mt940.MT940Statement;
import com.mt940.domain.mt940.MT940Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service("MT940Parser")
public class MT940Parser {
    
    private ZonedDateTime toZonedDateTime(String in) {
        return LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay(ZoneId.systemDefault());
    }

    String notTag = "([^:]|:[^\\d]|:\\d[^\\d]|:\\d\\d[^:])";
    //    private String dateFormat = "YYMMDD";
    private String dateFormat = "yyMMdd";
    private String amountReg = "([0-9]+(,){1}([0-9])*){1,15}";
    private String newLineReg = "\\r?\\n";
    private String newLineReg1 = "\\r\\n";
    private String mt940BalanceReg = String.format("^(C|D)([\\d]{6})([A-Z]{3})%s", amountReg);
    private String mt940TransactionReg = String.format("^([\\d]{6})([\\d]{4}){0,1}(C|D|RC|RD)%s([A-Z]{4})(.+){1,16}", amountReg);
    private String mt940SequenceNumberReg = String.format("^([\\d]{1,5})(/([\\d]{1,5})){0,1}", amountReg);

    private Pattern pattern20 = Pattern.compile(String.format(":20:([^%s]{1,16}){1}", newLineReg1));
    private Pattern pattern21 = Pattern.compile(String.format(":21:([^%s]{1,16}){1}", newLineReg1));
    private Pattern pattern25 = Pattern.compile(String.format(":25:([^%s]{1,35}){1}", newLineReg1));
    private Pattern pattern28C = Pattern.compile(String.format(":28C:([^%s]{1,11}){1}", newLineReg1));
//    private Pattern pattern28C = Pattern.compile(":28C:{1}([\\d]{1,5}(/[\\d]{1,5}){0,1}){1}");

    private Pattern patternMT940Statement = Pattern.compile("\\u0001\\{([^\\u0001\\u0003]+)-\\}\\u0003");
    private Pattern patternMT940Balance = Pattern.compile(mt940BalanceReg);
    private Pattern patternMT940Transaction = Pattern.compile(mt940TransactionReg);
    private Pattern patternMT940StatementNumber = Pattern.compile(mt940SequenceNumberReg);

    private Pattern pattern60F_extract = Pattern.compile(String.format(":60[F|M]:([^%s]{1,25}){1}", newLineReg1));
    private Pattern pattern62F_extract = Pattern.compile(String.format(":62[F|M]:([^%s]{1,25}){1}", newLineReg1));
    private Pattern pattern61_86_base = Pattern.compile(String.format("(:61:[\\w\\d\\W]*?):62[F|M]:"));
    private Pattern pattern61_86 = Pattern.compile(String.format(":61:(%s+):86:(%s+)", notTag, notTag));

    private Pattern patternHeader1 = Pattern.compile(String.format("1:([^:]+)\\}", newLineReg));
    private Pattern patternHeader2 = Pattern.compile(String.format("\\{2:([^:]+)\\}", newLineReg));
    private Pattern patternHeader3 = Pattern.compile(String.format("\\{3:([^%s]+)\\}", newLineReg));

    public List<String[]> extractListOfTransactionCommentPair(String in) {
        if (in == null) return null;
        List<String[]> result = new ArrayList<String[]>();
        Matcher matcher = pattern61_86.matcher(in);
        while (matcher.find()) {
            String[] pair = new String[2];
            pair[0] = matcher.group(1);
            pair[1] = matcher.group(3);
            result.add(pair);
        }
        return result;
    }

    public String extractHeader1(String in) {
        String result = null;
        Matcher matcher = patternHeader1.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractHeader2(String in) {
        String result = null;
        Matcher matcher = patternHeader2.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractHeader3(String in) {
        String result = null;
        Matcher matcher = patternHeader3.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractTransactionReferenceNumber20(String in) {
        if (in == null) return null;
        String result = null;
        Matcher matcher = pattern20.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractRelatedReferenceNumber21(String in) throws MT940Exception {
        if (in == null) return null;
        String result = null;
        Matcher matcher = pattern21.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractAccountId25(String in) {
        if (in == null) return null;
        String result = null;
        Matcher matcher = pattern25.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractStatementSequenceNumber28C(String in) {
        if (in == null) return null;
        String result = null;
        Matcher matcher = pattern28C.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractOpeningBalance60F(String in) {
        if (in == null) return null;
        String result = null;
        Matcher matcher = pattern60F_extract.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String extractClosingBalance62F(String in) {
        if (in == null) return null;
        String result = null;
        Matcher matcher = pattern62F_extract.matcher(in);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public MT940Balance mapMT940Balance(String in) throws MT940FormatException {
        if (in == null) return null;
        MT940Balance result = null;
        Matcher matcher = patternMT940Balance.matcher(in);
        if (matcher.find()) {
            result = new MT940Balance();
            result.setBalanceType(MT940BalanceType.findByCode(matcher.group(1)));
//            result.setDate(ZonedDateTime.parse(matcher.group(2), DateTimeFormatter.ofPattern(dateFormat)));
            result.setDate(toZonedDateTime(matcher.group(2)));
            result.setCurrency(matcher.group(3));
            result.setAmount(new BigDecimal(matcher.group(4).replace(',', '.')));
        } else
            throw new MT940FormatException(String.format("Invalid format of input string '%s' for Balance field", in));
        return result;
    }

    public MT940Transaction mapMT940Transaction(String in) throws MT940FormatException, MT940MandatoryFieldException {
        if (in == null) return null;
        MT940Transaction result = null;
        String[] split = in.split(newLineReg);
        if (split.length > 2 || split.length < 1)
            throw new MT940FormatException(String.format("Invalid format of input string '%s' for Transaction field", in));
        String transactionDescription = split.length == 2 ? split[1] : null;
        String rawTransaction = split[0];

        String[] split1 = rawTransaction.split("//");
        if (split1.length > 2 || split1.length < 1)
            throw new MT940FormatException(String.format("Invalid format of input string '%s' for Transaction field", in));
        String referenceForBank = split1.length == 2 ? split1[1] : null;
        String rawTransaction2 = split1[0];

        Matcher matcher = patternMT940Transaction.matcher(rawTransaction2);
        if (matcher.find()) {
            result = new MT940Transaction();
            result.setRawTransaction(matcher.group(0));
//            result.setDate(DateTime.parse(matcher.group(1), DateTimeFormat.forPattern(dateFormat)));
//            result.setDate(ZonedDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern(dateFormat)));
            result.setDate(toZonedDateTime(matcher.group(1)));
//            result.setEntryDate(matcher.group(2) == null ? null : ZonedDateTime.parse(result.getDate().format(DateTimeFormatter.ofPattern("YY")) + matcher.group(2), DateTimeFormatter.ofPattern(dateFormat)));
            result.setEntryDate(matcher.group(2) == null ? null : toZonedDateTime(result.getDate().format(DateTimeFormatter.ofPattern("yy")) + matcher.group(2)));
            result.setFundsCode(MT940FundsCode.findByCode(matcher.group(3)));
            result.setAmount(new BigDecimal(matcher.group(4).replace(',', '.')));
            result.setSwiftCode(matcher.group(7));
            result.setReferenceForAccountOwner(matcher.group(8));
            result.setReferenceForBank(referenceForBank);
            result.setTransactionDescription(transactionDescription);
        } else
            throw new MT940FormatException(String.format("Invalid format of input string '%s' for Transaction field", in));
//        validateMT940Transaction(result);
//        l.debug("{}", result);
        return result;
    }

    public String extractListOfTransactionsWithInformationToAccountOwner61_86(String in) throws MT940Exception {
        String result = null;
        Matcher matcher_base = pattern61_86_base.matcher(in);
        if (matcher_base.find()) {
            result = matcher_base.group(1);
        }
        return result;
    }

    public List<String[]> extractListOfTransactions(String in) throws MT940Exception {
        List<String[]> statementSet = extractListOfTransactionCommentPair(extractListOfTransactionsWithInformationToAccountOwner61_86(in));
        return statementSet;
    }

    public SortedSet<MT940Transaction> mapListOfTransactions(List<String[]> in) throws MT940Exception {
        SortedSet<MT940Transaction> transactionSet = new TreeSet<MT940Transaction>();
        if (in != null) {
            int counter = 0;
            for (String[] s : in) {
                log.debug("parsing pair MT940Transaction+Info #{}>", ++counter);
                MT940Transaction transaction = mapMT940Transaction(s[0]);
                transaction.setInformationToAccountOwner(s[1]);
                transaction.setEntryOrder(counter - 1);
//                transaction.setInstance(tryToParseInstance(transaction.getReferenceForAccountOwner()));
                transaction.setInstance(tryToParseInstance(transaction.getInformationToAccountOwner()));
                validateMT940Transaction(transaction);
                transactionSet.add(transaction);
            }
//            l.debug("<successfully parsed {} MT940Transactions+Info>", transactionSet.size());
        }
        return transactionSet;
    }

    public Instance tryToParseInstance(String in) {
        Instance instance = Instance.UNKNOWN;
        Instance[] values = Instance.values();
        for (Instance currentInstance : values) {
            if (in.startsWith(currentInstance.getStringCode())) {
                instance = currentInstance;
                break;
            }
        }
        return instance;
    }

    public List<String> extractListOfStatements(String in) throws MT940Exception {
        Matcher matcher = patternMT940Statement.matcher(in);
        List<String> statementSet = new ArrayList<String>();
        while (matcher.find()) {
            statementSet.add(matcher.group(1));
        }
        if (statementSet.size() == 0) {
            throw new MT940MandatoryFieldException("at least 1 statement must present in file");
        }
//        l.debug("<<founded {} MT940Statements>>", statementSet.size());
        return statementSet;
    }

    public SortedSet<MT940Statement> mapListOfStatements(List<String> in) throws MT940Exception {
        SortedSet<MT940Statement> statementSet = new TreeSet<MT940Statement>();
        int counter = 0;
        for (String s : in) {
            log.debug("parsing MT940Statement #{}>>", ++counter);
            MT940Statement statement = mapMT940Statement(s);
            statement.setEntryOrder(counter - 1);
            statementSet.add(statement);
        }
//        l.debug("<<successfully parsed {} MT940Statements>>", statementSet.size());
        return statementSet;
    }

    public String[] mapStatementSequenceNumber(String in) throws MT940FormatException {
        if (in == null) return null;
        String[] result = new String[2];
        Matcher matcher = patternMT940StatementNumber.matcher(in);
        if (matcher.find()) {
            result[0] = matcher.group(1);
            result[1] = matcher.group(3);
        } else
            throw new MT940FormatException(String.format("Invalid format of input string '%s' for StatementSequence field", in));
        return result;
    }

    public MT940Statement mapMT940Statement(String data) throws MT940Exception {
        MT940Statement statement = new MT940Statement();
        statement.setSWIFTHeader1(extractHeader1(data));
        statement.setSWIFTHeader2(extractHeader2(data));
        statement.setSWIFTHeader3(extractHeader3(data));
        statement.setTransactionReference(extractTransactionReferenceNumber20(data));
        statement.setRelatedReference(extractRelatedReferenceNumber21(data));
        statement.setAccountId(extractAccountId25(data));
        String[] statementSequence = mapStatementSequenceNumber(extractStatementSequenceNumber28C(data));
        if (statementSequence != null) {
            statement.setStatementNumber(statementSequence[0]);
            statement.setSequenceNumber(statementSequence[1]);
        }
        statement.setOpeningBalance(mapMT940Balance(extractOpeningBalance60F(data)));
        statement.setClosingBalance(mapMT940Balance(extractClosingBalance62F(data)));
        validateMT940Statement(statement);
        statement.setTransactionSet(mapListOfTransactions(extractListOfTransactions(data)));
        for (MT940Transaction transaction : statement.getTransactionSet()) {
            transaction.setCurrency(statement.getOpeningBalance().getCurrency());
        }
        return statement;
    }

    public void validateMT940Statement(MT940Statement statement) throws MT940MandatoryFieldException {
        if (statement == null) return;
        if (statement.getTransactionReference() == null)
            throw new MT940MandatoryFieldException("Transaction reference (20) is a mandatory field");
        if (statement.getAccountId() == null)
            throw new MT940MandatoryFieldException("Account Identification (25) is a mandatory field");
        if (statement.getStatementNumber() == null)
            throw new MT940MandatoryFieldException("Statement number (28C) is a mandatory field");
        if (statement.getOpeningBalance() == null)
            throw new MT940MandatoryFieldException("Opening Balance (60F|M) is a mandatory field");
        if (statement.getClosingBalance() == null)
            throw new MT940MandatoryFieldException("Closing Balance (62F|M) is a mandatory field");
    }

    public void validateMT940Transaction(MT940Transaction transaction) throws MT940MandatoryFieldException {
        if (transaction == null) return;
        if (transaction.getDate() == null)
            throw new MT940MandatoryFieldException("Date of transaction is a mandatory field");
        if (transaction.getFundsCode() == null)
            throw new MT940MandatoryFieldException("Funds code is a mandatory field");
        if (transaction.getAmount() == null) throw new MT940MandatoryFieldException("Amount is a mandatory field");
        if (transaction.getSwiftCode() == null)
            throw new MT940MandatoryFieldException("Swift code is a mandatory field");
        if (transaction.getReferenceForAccountOwner() == null)
            throw new MT940MandatoryFieldException("Reference for account holder is a mandatory field");
    }
}
