package com.mt940.daemon.mt940.functional;


import com.mt940.daemon.email.EmailFragment;
import com.mt940.daemon.endpoints.BKVParser;
import com.mt940.daemon.exceptions.MT940MandatoryFieldException;
import com.mt940.domain.EARAttachment;
import com.mt940.domain.enums.EARAttachmentStatus;
import com.mt940.domain.enums.MT940BalanceType;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.mt940.MT940Balance;
import com.mt940.domain.mt940.MT940Statement;
import com.mt940.domain.mt940.MT940Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/functional/bkv-parser-functional-test-context.xml")
public class BKVParserFunctionalTest {
    private String dateFormat = "yyMMdd";

    @Autowired
    BKVParser parser;

    @Test
    public void testName() throws Exception {


    }

    private void assertStrings(String expected, String actual) {
        String newLineReg = "[\\r]";
        if (expected == null && actual == null) return;
        if (expected == null || actual == null) fail("\nExpected\t:" + expected + "\nActual\t:" + actual + "\n");
        assertEquals(expected.replaceAll(newLineReg, ""), actual.replaceAll(newLineReg, ""));
    }

    private void areMT940BalancesEqual(MT940Balance expected, MT940Balance actual) {
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getBalanceType(), actual.getBalanceType());
        assertEquals(expected.getCurrency(), actual.getCurrency());
        assertEquals(expected.getDate(), actual.getDate());
    }

    private void areMT940TransactionsEqual(MT940Transaction expected, MT940Transaction actual) {
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getEntryDate(), actual.getEntryDate());
        assertEquals(expected.getFundsCode(), actual.getFundsCode());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertStrings(expected.getSwiftCode(), actual.getSwiftCode());
        assertStrings(expected.getReferenceForAccountOwner(), actual.getReferenceForAccountOwner());
        assertStrings(expected.getReferenceForBank(), actual.getReferenceForBank());
        assertStrings(expected.getTransactionDescription(), actual.getTransactionDescription());
        assertStrings(expected.getInformationToAccountOwner(), actual.getInformationToAccountOwner());
    }

    private EARAttachment initEARAttachment(String fileName) {
        EARAttachment attachment = new EARAttachment();
        attachment.setUniqueName(fileName);
        attachment.setOriginalName(fileName);
        attachment.setStatus(EARAttachmentStatus.NEW);
        return attachment;
    }

    private Message<List<EmailFragment>> init(String fileName) throws IOException {
        File file = new File(getClass().getResource("/data/" + fileName).getPath());
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int size = fis.read(data);
        log.info("Read {} bytes", size);
        fis.close();
        List<EmailFragment> list = new ArrayList<>();
        list.add(new EmailFragment(data, fileName, null, size, null));
        return MessageBuilder.withPayload(list).build();
    }

    private EARAttachment extract(Message<List<EARAttachment>> message) throws IOException {
        List<EARAttachment> payload = message.getPayload();
        EARAttachment file = payload.get(0);
        return file;
    }

    private ZonedDateTime toZonedDateTime(String in) {
        return LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay(ZoneId.systemDefault());
    }


    @Test
    public void testSWIFT_MT940_Standard() throws Exception {
        String fileName = "SWIFT_MT940_Standard_27May.dat";
        Message<List<EARAttachment>> messageOut = parser.handle(init(fileName));
        EARAttachment file = extract(messageOut);
        log.info("{}", file);
        Set<MT940Statement> statementSet = file.getStatementSet();
        assertNotNull(statementSet);
        assertEquals(1, statementSet.size());

        List<MT940Statement> statementList = new ArrayList<MT940Statement>(statementSet);
        assertNotNull(statementSet);
        assertEquals(1, statementSet.size());
        MT940Statement statement1 = statementList.get(0);
        log.info("{}", statement1);
        assertNotNull(statement1.getTransactionSet());
        assertEquals(19, statement1.getTransactionSet().size());
    }

    @Test
    public void testFile40022530437() throws Exception {
        String fileName = "40022530437.dat";
        Message<List<EARAttachment>> messageOut = parser.handle(init(fileName));
        EARAttachment file = extract(messageOut);

        Set<MT940Statement> statementSet = file.getStatementSet();
        assertNotNull(statementSet);
        assertEquals(1, statementSet.size());

        List<MT940Statement> statementList = new ArrayList<MT940Statement>(statementSet);
        MT940Statement statement1 = statementList.get(0);
        assertEquals("F01VALLMTMTAXXX.SS..SEQ..", statement1.getSWIFTHeader1());
        assertEquals("I940BOVNXSYS1822N", statement1.getSWIFTHeader2());
        assertEquals("{108:STATEMENT}", statement1.getSWIFTHeader3());
        assertEquals("220501", statement1.getTransactionReference());
        assertEquals("40022530437", statement1.getAccountId());
        assertEquals("1583", statement1.getStatementNumber());
        assertEquals("1", statement1.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141122"), "USD", new BigDecimal("15.85")), statement1.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141122"), "USD", new BigDecimal("15.85")), statement1.getClosingBalance());
        assertNull(statement1.getRelatedReference());
        assertNotNull(statement1.getTransactionSet());
        assertEquals(0, statement1.getTransactionSet().size());
    }

    @Test
    public void testFile40022530453() throws Exception {
        String fileName = "40022530453.dat";
        Message<List<EARAttachment>> messageOut = parser.handle(init(fileName));
        EARAttachment file = extract(messageOut);

        Set<MT940Statement> statementSet = file.getStatementSet();
        assertNotNull(statementSet);
        assertEquals(1, statementSet.size());

        List<MT940Statement> statementList = new ArrayList<MT940Statement>(statementSet);
        MT940Statement statement1 = statementList.get(0);
        assertEquals("F01VALLMTMTAXXX.SS..SEQ..", statement1.getSWIFTHeader1());
        assertEquals("I940BOVNXSYS1823N", statement1.getSWIFTHeader2());
        assertEquals("{108:STATEMENT}", statement1.getSWIFTHeader3());
        assertEquals("220502", statement1.getTransactionReference());
        assertEquals("40022530453", statement1.getAccountId());
        assertEquals("1583", statement1.getStatementNumber());
        assertEquals("1", statement1.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141122"), "GBP", new BigDecimal("90.58")), statement1.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141122"), "GBP", new BigDecimal("90.58")), statement1.getClosingBalance());
        assertNull(statement1.getRelatedReference());
        assertNotNull(statement1.getTransactionSet());
        assertEquals(0, statement1.getTransactionSet().size());
    }

    @Test
    public void testFile40022530424() throws Exception {
        String fileName = "40022530424.dat";
        Message<List<EARAttachment>> messageOut = parser.handle(init(fileName));
        EARAttachment file = extract(messageOut);

        Set<MT940Statement> statementSet = file.getStatementSet();
        assertNotNull(statementSet);
        assertEquals(2, statementSet.size());

        List<MT940Statement> statementList = new ArrayList<MT940Statement>(statementSet);
        MT940Statement statement1 = statementList.get(0);
        assertEquals("F01VALLMTMTAXXX.SS..SEQ..", statement1.getSWIFTHeader1());
        assertEquals("I940BOVNXSYS1821N", statement1.getSWIFTHeader2());
        assertEquals("{108:STATEMENT}", statement1.getSWIFTHeader3());
        assertEquals("221094", statement1.getTransactionReference());
        assertEquals("40022530424", statement1.getAccountId());
        assertEquals("1586", statement1.getStatementNumber());
        assertEquals("1", statement1.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141126"), "EUR", new BigDecimal("10838.03")), statement1.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141126"), "EUR", new BigDecimal("552338.03")), statement1.getClosingBalance());
        assertNull(statement1.getRelatedReference());
        assertNotNull(statement1.getTransactionSet());
        assertEquals(4, statement1.getTransactionSet().size());

        List<MT940Transaction> transactionList1 = new ArrayList<MT940Transaction>(statement1.getTransactionSet());
        MT940Transaction transaction1 = transactionList1.get(0);
        MT940Transaction expectedTransaction1 = new MT940Transaction(toZonedDateTime("141126"), toZonedDateTime("141126"), MT940FundsCode.CREDIT, new BigDecimal("300000."), "NTRF", "MR GREEN LIMITED", null, null, "0000001019370127\r\n");
        areMT940TransactionsEqual(expectedTransaction1, transaction1);
        MT940Transaction transaction2 = transactionList1.get(1);
        MT940Transaction expectedTransaction2 = new MT940Transaction(toZonedDateTime("141126"), toZonedDateTime("141126"), MT940FundsCode.CREDIT, new BigDecimal("140000."), "NTRF", "INTERTRONIC LTD", null, null, "INTERTRONIC - SUPERBAHIS\r\n");
        areMT940TransactionsEqual(expectedTransaction2, transaction2);
        MT940Transaction transaction3 = transactionList1.get(2);
        MT940Transaction expectedTransaction3 = new MT940Transaction(toZonedDateTime("141126"), toZonedDateTime("141126"), MT940FundsCode.CREDIT, new BigDecimal("100000."), "NTRF", "INTERTRONIC LTD", null, null, "INTERTRONIC BETBOO\r\n");
        areMT940TransactionsEqual(expectedTransaction3, transaction3);
        MT940Transaction transaction4 = transactionList1.get(3);
        MT940Transaction expectedTransaction4 = new MT940Transaction(toZonedDateTime("141126"), toZonedDateTime("141126"), MT940FundsCode.CREDIT, new BigDecimal("1500."), "NTRF", "BLING CITY LIMIT", null, null, "MERCHANT ACCOUNT 000000106623\r\n");
        areMT940TransactionsEqual(expectedTransaction4, transaction4);

        MT940Statement statement2 = statementList.get(1);
        assertEquals("F01VALLMTMTAXXX.SS..SEQ..", statement2.getSWIFTHeader1());
        assertEquals("I940BOVNXSYS1821N", statement2.getSWIFTHeader2());
        assertEquals("{108:STATEMENT}", statement2.getSWIFTHeader3());
        assertEquals("221094", statement2.getTransactionReference());
        assertEquals("40022530424", statement2.getAccountId());
        assertEquals("1586", statement2.getStatementNumber());
        assertEquals("2", statement2.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141126"), "EUR", new BigDecimal("552338.03")), statement2.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141126"), "EUR", new BigDecimal("319534.03")), statement2.getClosingBalance());
        assertNull(statement2.getRelatedReference());
        assertNotNull(statement2.getTransactionSet());
        assertEquals(1, statement2.getTransactionSet().size());

        List<MT940Transaction> transactionList2 = new ArrayList<MT940Transaction>(statement2.getTransactionSet());
        MT940Transaction transaction1_statement2 = transactionList2.get(0);
        MT940Transaction expectedTransaction1_statement2 = new MT940Transaction(toZonedDateTime("141126"), toZonedDateTime("141126"), MT940FundsCode.DEBIT, new BigDecimal("232804."), "NTRF", ".", "2944154", null, "GLOBAL REACH PARTNERS LIMITED\r\n" +
                "EUR232800.00\r\n" +
                "ZB3261\r\n");
        areMT940TransactionsEqual(expectedTransaction1_statement2, transaction1_statement2);
    }

    @Test
    public void testFileTest1() throws Exception {
        String fileName = "test1.dat";
        Message<List<EARAttachment>> messageOut = parser.handle(init(fileName));
        EARAttachment file = extract(messageOut);

        Set<MT940Statement> statementSet = file.getStatementSet();
        assertNotNull(statementSet);
        assertEquals(2, statementSet.size());

        List<MT940Statement> statementList = new ArrayList<MT940Statement>(statementSet);
        MT940Statement statement1 = statementList.get(0);
        assertEquals("F01BPHKPLPKXXX0000000000", statement1.getSWIFTHeader1());
        assertEquals("I940BOFAUS6BXBAMN", statement1.getSWIFTHeader2());
        assertEquals(null, statement1.getSWIFTHeader3());
        assertEquals("TELEWIZORY S.A.", statement1.getTransactionReference());
        assertEquals("BPHKPLPK/320000546101", statement1.getAccountId());
        assertEquals("00084", statement1.getStatementNumber());
        assertEquals("001", statement1.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("031002"), "PLN", new BigDecimal("40000.00")), statement1.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("020325"), "PLN", new BigDecimal("50040.00")), statement1.getClosingBalance());
        assertNull(statement1.getRelatedReference());
        assertNotNull(statement1.getTransactionSet());
        assertEquals(3, statement1.getTransactionSet().size());

        List<MT940Transaction> transactionList1 = new ArrayList<MT940Transaction>(statement1.getTransactionSet());
        MT940Transaction transaction1 = transactionList1.get(0);
        MT940Transaction expectedTransaction1 = new MT940Transaction(toZonedDateTime("031020"), toZonedDateTime("031020"), MT940FundsCode.CREDIT, new BigDecimal("20000.00"), "FMSC", "NONREF", "8327000090031789", "Card transaction", " 020?00Wyplata-(dysp/przel)?2008106000760000777777777777?2115617?\n" +
                "22INFO INFO INFO INFO INFO INFO 1 END?23INFO INFO INFO INFO INFO\n" +
                "INFO 2 END?24ZAPLATA ZA FABRYKATY DO TUB?25 - 200 S ZTUK, TRANZY\n" +
                "STORY-?26300 SZT GR544 I OPORNIKI-5?2700 SZT GTX847 FAKTURA 333/\n" +
                "2?28003.?3010600076?310000777777777777?32HUTA SZKLA TOPIC UL\n" +
                "PRZEMY?33SLOWA 67 32-669 WROCLAW?38PL081060007600007777777\n" +
                "77777\n");
        areMT940TransactionsEqual(expectedTransaction1, transaction1);
        MT940Transaction transaction2 = transactionList1.get(1);
        MT940Transaction expectedTransaction2 = new MT940Transaction(toZonedDateTime("031020"), toZonedDateTime("031020"), MT940FundsCode.DEBIT, new BigDecimal("10000.00"), "FTRF", "REF 25611247", "8327000090031790", "Transfer", " 020?00Wyplata-(dysp/przel)?2008106000760000777777777777?2115617?\n" +
                "22INFO INFO INFO INFO INFO INFO 1 END?23INFO INFO INFO INFO INFO\n" +
                "INFO 2 END?24ZAPLATA ZA FABRYKATY DO TUB?25 - 200 S ZTUK, TRANZY\n" +
                "STORY-?26300 SZT GR544 I OPORNIKI-5?2700 SZT GTX847 FAKTURA 333/\n" +
                "2?28003.?3010600076?310000777777777777?38PL081060007600007777777\n" +
                "77777\n");
        areMT940TransactionsEqual(expectedTransaction2, transaction2);
        MT940Transaction transaction3 = transactionList1.get(2);
        MT940Transaction expectedTransaction3 = new MT940Transaction(toZonedDateTime("031020"), toZonedDateTime("031020"), MT940FundsCode.CREDIT, new BigDecimal("40.00"), "FTRF", "NONREF", "8327000090031791", "Interest credit", " 844?00Uznanie kwotą odsetek?20Odsetki od lokaty nr 101000?21022086\n");
        areMT940TransactionsEqual(expectedTransaction3, transaction3);

        MT940Statement statement2 = statementList.get(1);
        assertEquals("F01BPHKPLPKXXXX0312092220", statement2.getSWIFTHeader1());
        assertEquals("I940AABSDE31XXXXN", statement2.getSWIFTHeader2());
        assertEquals(null, statement2.getSWIFTHeader3());
        assertEquals(" NETMEXID", statement2.getTransactionReference());
        assertEquals("BPHKPLPK/666666666666", statement2.getAccountId());
        assertEquals("00035", statement2.getStatementNumber());
        assertEquals("001", statement2.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("031209"), "PLN", new BigDecimal("95.03")), statement2.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("031209"), "PLN", new BigDecimal("14615.03")), statement2.getClosingBalance());
        assertNull(statement2.getRelatedReference());
        assertNotNull(statement2.getTransactionSet());
        assertEquals(4, statement2.getTransactionSet().size());

        List<MT940Transaction> transactionList2 = new ArrayList<MT940Transaction>(statement2.getTransactionSet());
        MT940Transaction transaction1_2 = transactionList2.get(0);
        MT940Transaction expectedTransaction1_2 = new MT940Transaction(toZonedDateTime("031209"), toZonedDateTime("031209"),
                MT940FundsCode.CREDIT, new BigDecimal("20000."), "FBAR", "NONREF", "1010001272972001", "Payment of funds to own account", "082?00Wplata wlasna?2115616?24Rach.wplacajac. 101000\n");
        areMT940TransactionsEqual(expectedTransaction1_2, transaction1_2);

        MT940Transaction transaction2_2 = transactionList2.get(1);
        MT940Transaction expectedTransaction2_2 = new MT940Transaction(toZonedDateTime("031209"), toZonedDateTime("031209"),
                MT940FundsCode.DEBIT, new BigDecimal("4000."), "FTRF", "REF:BPHPBK/081203/0001", "59512092914002", "Transfer of funds", " 020?00Wyplata-(dysp/przel)?2008106000760000777777777777?2115617?\n" +
                "22INFO INFO INFO INFO INFO INFO 1 END?23INFO INFO INFO INFO INFO\n" +
                "INFO 2 END?24ZAPLATA ZA FABRYKATY DO TUB?25 - 200 S ZTUK, TRANZY\n" +
                "STORY-?26300 SZT GR544 I OPORNIKI-5?2700 SZT GTX847 FAKTURA 333/\n" +
                "2?28003.?3010600076?310000777777777777?32HUTA SZKLA TOPIC UL\n" +
                "PRZEMY?33SLOWA 67 32-669 WROCLAW?38PL081060007600007777777\n" +
                "77777\n");
        areMT940TransactionsEqual(expectedTransaction2_2, transaction2_2);

        MT940Transaction transaction3_2 = transactionList2.get(2);
        MT940Transaction expectedTransaction3_2 = new MT940Transaction(toZonedDateTime("031209"), toZonedDateTime("031209"),
                MT940FundsCode.DEBIT, new BigDecimal("880."), "FTRF", "REF:BPHPBK/081203/0003", "59512092915002", "ZUS-social security payment", "030?00Wyplata-(dysp/przel)?2010101023-26-139-51?2115618?24Deklar\n" +
                "acja:200309?25Numer deklaracji:09?26Typ wplaty:S?27NIP Platnika:\n" +
                "6792496639?28Typ id uzup.:1?26Id uzup.:DD8012790?3010101023?3126\n" +
                "-139-51?32ZAKLAD UBEZPIECZEN SPOLECZN?33YCH\n");
        areMT940TransactionsEqual(expectedTransaction3_2, transaction3_2);

        MT940Transaction transaction4_2 = transactionList2.get(3);
        MT940Transaction expectedTransaction4_2 = new MT940Transaction(toZonedDateTime("031209"), toZonedDateTime("031209"),
                MT940FundsCode.DEBIT, new BigDecimal("600."), "FTRF", "REF:BPHPBK/081203/0002", "59512092916002", "Internal Revenue Service payment", "031?00Wyplata-(dysp/przel)?2069101012700004592221000000?2115619?\n" +
                "24Wplata na organ podatkowy?25Typ identyfikatora:N?26Zawartosc I\n" +
                "D:6792496639?27Okres:03M09?27Symbol formularza:PIT4?28Opis:ZAPL.\n" +
                "POD. DAFIK?3010101270?310004592221000000?32Urzad Skarbowy Krakow\n" +
                "-Star?33e Miasto Krakow?38PL69101012700004592221000000\n");
        areMT940TransactionsEqual(expectedTransaction4_2, transaction4_2);
    }

    @Test
    public void testFileTest2() throws Exception {
        String fileName = "test2.dat";
        Message<List<EARAttachment>> messageOut = parser.handle(init(fileName));
        EARAttachment file = extract(messageOut);

        Set<MT940Statement> statementSet = file.getStatementSet();
        assertNotNull(statementSet);
        assertEquals(1, statementSet.size());

        List<MT940Statement> statementList = new ArrayList<MT940Statement>(statementSet);
        MT940Statement statement1 = statementList.get(0);
        assertEquals("F01BPHKPLPKXXX0000000000", statement1.getSWIFTHeader1());
        assertEquals("I940BOFAUS6BXBAMN", statement1.getSWIFTHeader2());
        assertEquals(null, statement1.getSWIFTHeader3());
        assertEquals("TELEWIZORY S.A.", statement1.getTransactionReference());
        assertEquals("BPHKPLPK/320000546101", statement1.getAccountId());
        assertEquals("00084", statement1.getStatementNumber());
        assertEquals("001", statement1.getSequenceNumber());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("031002"), "EUR", new BigDecimal("5000.00")), statement1.getOpeningBalance());
        areMT940BalancesEqual(new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("031020"), "EUR", new BigDecimal("3891.59")), statement1.getClosingBalance());
        assertNull(statement1.getRelatedReference());
        assertNotNull(statement1.getTransactionSet());
        assertEquals(1, statement1.getTransactionSet().size());

        List<MT940Transaction> transactionList1 = new ArrayList<MT940Transaction>(statement1.getTransactionSet());
        MT940Transaction transaction1 = transactionList1.get(0);
        MT940Transaction expectedTransaction1 = new MT940Transaction(toZonedDateTime("031020"), toZonedDateTime("031020"), MT940FundsCode.DEBIT, new BigDecimal("1088.41"), "FTRF", "REF12345678/2003", "8327000090031790", "Transfer", "020?00Wyplata/przelew?20DEUTSCHE ELEKTROAPPARATUR?21OBENSTRAS\n" +
                "SE 4 MUNCHEN?22OCMT/EUR1088,41?23CHGS/SHA/EUR20,00?24FAKTURA 333\n" +
                "/2003 ZAPLATA ZA?25FABRYKATY DO TUB 200 SZTUK?26GZX 76 I 300 SZT\n" +
                "UK GZY 77 T?27RANZYSTORY 300 SZTUK BT34SX?28OPORNIKI 500 SZTUK W\n" +
                "Q2?29232FX?30HYVEDEMM700?31701890012872?38DE09700202701890012872\n");
        areMT940TransactionsEqual(expectedTransaction1, transaction1);
    }

    //TODO
    @Ignore
    @Test(expected = MT940MandatoryFieldException.class)
    public void testFileTestFail_omit20() throws Exception {
        String fileName = "testFail_omit20.dat";
        parser.handle(init(fileName));
    }

    //TODO
    @Ignore
    @Test(expected = MT940MandatoryFieldException.class)
    public void testFileTestFail_omit25() throws Exception {
        String fileName = "testFail_omit25.dat";
        parser.handle(init(fileName));
    }

    //TODO
    @Ignore
    @Test(expected = MT940MandatoryFieldException.class)
    public void testFileTestFail_omit28C() throws Exception {
        String fileName = "testFail_omit28C.dat";
        parser.handle(init(fileName));
    }

    //TODO
    @Ignore
    @Test(expected = MT940MandatoryFieldException.class)
    public void testFileTestFail_omit60F() throws Exception {
        String fileName = "testFail_omit60F.dat";
        parser.handle(init(fileName));
    }

    //TODO
    @Ignore
    @Test(expected = MT940MandatoryFieldException.class)
    public void testFileTestFail_omit62F() throws Exception {
        String fileName = "testFail_omit62F.dat";
        parser.handle(init(fileName));
    }
}
