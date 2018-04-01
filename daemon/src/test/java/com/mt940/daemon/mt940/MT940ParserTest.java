package com.mt940.daemon.mt940;

import com.mt940.daemon.exceptions.MT940FormatException;
import com.mt940.daemon.exceptions.MT940MandatoryFieldException;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.enums.MT940BalanceType;
import com.mt940.domain.enums.MT940FundsCode;
import com.mt940.domain.mt940.MT940Balance;
import com.mt940.domain.mt940.MT940Transaction;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class MT940ParserTest {

    private Logger l = LoggerFactory.getLogger(getClass());

    MT940Parser validator;
    private String dateFormat = "yyMMdd";
    private Locale currentLocale;

    @Before
    public void setUp() throws Exception {
        validator = new MT940Parser();
        currentLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @After
    public void after() {
        Locale.setDefault(currentLocale);
    }

    private String avoid(String in) {
        //workaround to avoid appearance ':' in the beginning and at the end of a random generated string
        String result = in;
        if (in == null) return null;
        if (in.length() == 0) return "";
        if (in.charAt(0) == ':') result = "_" + result.substring(1, result.length());
        if (in.charAt(in.length() - 1) == ':') result = result.substring(0, result.length() - 1) + "_";
        return result;
    }

    @Test
    public void testAvoid() {
        assertEquals(null, avoid(null));
        assertEquals("", avoid(""));
        assertEquals("a", avoid("a"));
        assertEquals("_", avoid(":"));
        assertEquals("__", avoid("::"));
        assertEquals("_a_", avoid(":a:"));
        assertEquals("abcde", avoid("abcde"));
        assertEquals("abcde_", avoid("abcde:"));
        assertEquals("abc:de_", avoid("abc:de:"));
        assertEquals("_abc:de_", avoid(":abc:de:"));
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

    private void assertStrings(String expected, String actual) {
        String newLineReg = "[\\r]";
        if (expected == null && actual == null) return;
        if (expected == null || actual == null) fail("\nExpected\t:" + expected + "\nActual\t\t:" + actual + "\n");
        assertTrue(expected.replaceAll(newLineReg, "").equals(actual.replaceAll(newLineReg, "")));
    }

    @Test
    public void testAssertStrings() {
        assertStrings(null, null);
        assertStrings("string1\r\n", "string1\n");
        assertStrings("string1\n", "string1\r\n");
        assertStrings("string1\r\n", "string1\r\n");
        assertStrings("string1\r\nstring1\r\n", "string1\nstring1\n");
        assertStrings("string1\r\nstring1\r\n", "string1\nstring1\n");
        assertStrings("string1\nstring1\r\n", "string1\r\nstring1\r\n");
        assertStrings("string1", "string1");
    }

    @Test
    public void testTryParseInstance() throws Exception {
        assertEquals(Instance.RUSSIA, validator.tryToParseInstance("RUSSIA12312321"));
        assertEquals(Instance.RUSSIA, validator.tryToParseInstance("RUSSIAabc12312321"));
        assertEquals(Instance.RUSSIA, validator.tryToParseInstance("RUSSIA 12312321"));
        assertEquals(Instance.EUROPE, validator.tryToParseInstance("EUROPE12312321"));
        assertEquals(Instance.ASIA, validator.tryToParseInstance("ASIA12312321"));
        assertEquals(Instance.AMERICA, validator.tryToParseInstance("AMERICA12312321"));
        assertEquals(Instance.UNKNOWN, validator.tryToParseInstance("AME RICA12312321"));
        assertEquals(Instance.UNKNOWN, validator.tryToParseInstance(" AMERICA12312321"));
        assertEquals(Instance.UNKNOWN, validator.tryToParseInstance("aAMERICA12312321"));
    }

    @Test
    public void testExtractListOfStatements() throws Exception {
        String r150 = RandomStringUtils.randomAscii(150);
        String rr150 = RandomStringUtils.randomAscii(150);
        String rrr150 = RandomStringUtils.randomAscii(150);
        List<String> list = validator.extractListOfStatements("\u0001{" + r150 + "-}\u0003");
        assertEquals(1, list.size());
        assertEquals(r150, list.get(0));
        list = validator.extractListOfStatements("\u0001{\u0001{" + r150 + "-}\u0003");
        assertEquals(1, list.size());
        assertEquals(r150, list.get(0));
        list = validator.extractListOfStatements("\u0001{\u0001{" + r150 + "-}\u0003-}\u0003");
        assertEquals(1, list.size());
        assertEquals(r150, list.get(0));
        list = validator.extractListOfStatements("\u0001{" + r150 + "\r\n" + "-}\u0003");
        assertEquals(1, list.size());
        assertEquals(r150 + "\r\n", list.get(0));
        list = validator.extractListOfStatements(r150 + "\u0001{" + r150 + "-}\u0003");
        assertEquals(1, list.size());
        assertEquals(r150, list.get(0));
        list = validator.extractListOfStatements("\u0001{" + r150 + "-}\u0003" + r150);
        assertEquals(1, list.size());
        assertEquals(r150, list.get(0));
        list = validator.extractListOfStatements(r150 + "\u0001{" + r150 + "-}\u0003" + r150);
        assertEquals(1, list.size());
        assertEquals(r150, list.get(0));
        list = validator.extractListOfStatements(r150 + "\u0001{ -}\u0003" + r150);
        assertEquals(1, list.size());
        assertEquals(" ", list.get(0));
        list = validator.extractListOfStatements("\u0001{" + r150 + "-}\u0003" + "\u0001{" + rr150 + "-}\u0003");
        assertEquals(2, list.size());
        assertEquals(r150, list.get(0));
        assertEquals(rr150, list.get(1));
        list = validator.extractListOfStatements("\u0001{" + r150 + "-}\u0003" + rrr150 + "\u0001{" + rr150 + "-}\u0003");
        assertEquals(2, list.size());
        assertEquals(r150, list.get(0));
        assertEquals(rr150, list.get(1));
        list = validator.extractListOfStatements("\u0001{" + r150 + "-}\u0003" + rrr150 + "\u0001{" + rr150 + "-}\u0003" + rrr150);
        assertEquals(2, list.size());
        assertEquals(r150, list.get(0));
        assertEquals(rr150, list.get(1));
        list = validator.extractListOfStatements(rrr150 + "\u0001{" + r150 + "-}\u0003" + rrr150 + "\u0001{" + rr150 + "-}\u0003");
        assertEquals(2, list.size());
        assertEquals(r150, list.get(0));
        assertEquals(rr150, list.get(1));
        list = validator.extractListOfStatements(rrr150 + "\u0001{" + r150 + "-}\u0003" + rrr150 + "\u0001{" + rr150 + "-}\u0003" + rrr150);
        assertEquals(2, list.size());
        assertEquals(r150, list.get(0));
        assertEquals(rr150, list.get(1));
        list = validator.extractListOfStatements(rrr150 + "\u0001{" + r150 + "-}\u0003" + rrr150 + "\r\n" + "\u0001{" + rr150 + "-}\u0003" + rrr150);
        assertEquals(2, list.size());
        assertEquals(r150, list.get(0));
        assertEquals(rr150, list.get(1));
//        assertEquals(0, validator.extractListOfStatements("\u0001{" + r150 + "}\u0003").size());
//        assertEquals(0, validator.extractListOfStatements("\u0001{" + r150 + "-\u0003").size());
//        assertEquals(0, validator.extractListOfStatements("\u0001" + r150 + "-}\u0003").size());
//        assertEquals(0, validator.extractListOfStatements("\u0001{" + r150 + "-}").size());
//        assertEquals(0, validator.extractListOfStatements("{" + r150 + "-}\u0003").size());
//        assertEquals(0, validator.extractListOfStatements("\u0001{-}\u0003").size());
//        assertEquals(0, validator.extractListOfStatements("\u0003{ -}\u0001").size());
    }

    @Test
    public void testExtractHeader1() throws Exception {
        assertEquals("F01VALLMTMTAXXX.SS..SEQ..", validator.extractHeader1("1:F01VALLMTMTAXXX.SS..SEQ..}{2:I940BOVNXSYS1822N}{3:{108:STATEMENT}}{4:"));
        assertEquals("F01VALLMTMTAXXX.SS..SEQ..", validator.extractHeader1("1:F01VALLMTMTAXXX.SS..SEQ..}"));
//        assertNull(validator.extractHeader1("11:F01VALLMTMTAXXX.SS..SEQ..}"));
        assertNull(validator.extractHeader1("1::F01VALLMTMTAXXX.SS..SEQ..}"));
    }

    @Test
    public void testExtractHeader2() throws Exception {
        assertEquals("I940BOVNXSYS1822N", validator.extractHeader2("{1:F01VALLMTMTAXXX.SS..SEQ..}{2:I940BOVNXSYS1822N}{3:{108:STATEMENT}}{4:"));
        assertEquals("I940BOVNXSYS1822N", validator.extractHeader2("{2:I940BOVNXSYS1822N}"));
        assertNull(validator.extractHeader2("{22:F01VALLMTMTAXXX.SS..SEQ..}"));
        assertNull(validator.extractHeader2("{2::F01VALLMTMTAXXX.SS..SEQ..}"));
    }

    @Test
    public void testExtractHeader3() throws Exception {
        assertEquals("{108:STATEMENT}", validator.extractHeader3("{1:F01VALLMTMTAXXX.SS..SEQ..}{2:I940BOVNXSYS1822N}{3:{108:STATEMENT}}{4:"));
        assertEquals("{108:STATEMENT}", validator.extractHeader3("{3:{108:STATEMENT}}"));
        assertNull(validator.extractHeader3("{33:F01VALLMTMTAXXX.SS..SEQ..}"));
    }

    @Test
    public void testExtractTransactionReferenceNumber20() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String r16 = avoid(RandomStringUtils.randomAscii(16));
        String r5 = avoid(RandomStringUtils.randomAscii(5));
        assertEquals(r16, validator.extractTransactionReferenceNumber20(":20:" + r16));
        assertEquals(r16, validator.extractTransactionReferenceNumber20(r150 + ":20:" + r16));
        assertEquals(r16, validator.extractTransactionReferenceNumber20(r150 + ":20:" + r16 + r150));
        assertEquals(r5, validator.extractTransactionReferenceNumber20(r150 + ":20:" + r5 + "\r\n" + r150));
        assertEquals(r5, validator.extractTransactionReferenceNumber20(r150 + ":20:" + r5 + "\n" + r150));
        assertNull(validator.extractTransactionReferenceNumber20(":21:" + r16));
        assertNull(validator.extractTransactionReferenceNumber20(":20" + r16));
        assertNull(validator.extractTransactionReferenceNumber20("20:" + r16));
        assertNull(validator.extractTransactionReferenceNumber20(":120:" + r16));
    }

    @Test
    public void testExtractRelatedReferenceNumber21() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String r16 = avoid(RandomStringUtils.randomAscii(16));
        String r5 = avoid(RandomStringUtils.randomAscii(5));
        assertEquals(r16, validator.extractRelatedReferenceNumber21(":21:" + r16));
        assertEquals(r16, validator.extractRelatedReferenceNumber21(r150 + ":21:" + r16));
        assertEquals(r16, validator.extractRelatedReferenceNumber21(r150 + ":21:" + r16 + r150));
        assertEquals(r5, validator.extractRelatedReferenceNumber21(r150 + ":21:" + r5 + "\r\n" + r150));
        assertEquals(r5, validator.extractRelatedReferenceNumber21(r150 + ":21:" + r5 + "\n" + r150));
        assertNull(validator.extractRelatedReferenceNumber21(":22:" + r16));
        assertNull(validator.extractRelatedReferenceNumber21(":21" + r16));
        assertNull(validator.extractRelatedReferenceNumber21("21:" + r16));
        assertNull(validator.extractRelatedReferenceNumber21(":121:" + r16));
    }

    @Test
    public void testExtractAccountId25() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String r35 = avoid(RandomStringUtils.randomAscii(35));
        String r5 = avoid(RandomStringUtils.randomAscii(5));
        assertEquals(r35, validator.extractAccountId25(":25:" + r35));
        assertEquals(r35, validator.extractAccountId25(r150 + ":25:" + r35));
        assertEquals(r35, validator.extractAccountId25(r150 + ":25:" + r35 + r150));
        assertEquals(r5, validator.extractAccountId25(r150 + ":25:" + r5 + "\r\n" + r150));
        assertEquals(r5, validator.extractAccountId25(r150 + ":25:" + r5 + "\n" + r150));
        assertNull(validator.extractAccountId25(":26:" + r35));
        assertNull(validator.extractAccountId25(":25" + r35));
        assertNull(validator.extractAccountId25("25:" + r35));
        assertNull(validator.extractAccountId25(":125:" + r35));
    }

    @Test
    public void testExtractStatementSequenceNumber28C() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String r11 = avoid(RandomStringUtils.randomAscii(11));
        String r5 = avoid(RandomStringUtils.randomAscii(5));
        String n5 = RandomStringUtils.randomNumeric(5);
        assertEquals(r11, validator.extractStatementSequenceNumber28C(":28C:" + r11));
        assertEquals(r11, validator.extractStatementSequenceNumber28C(":28C:" + r11 + r150));
        assertEquals(r11, validator.extractStatementSequenceNumber28C(r150 + ":28C:" + r11 + r150));
        assertEquals(r5, validator.extractStatementSequenceNumber28C(r150 + ":28C:" + r5 + "\n" + r150));
        assertEquals(r5, validator.extractStatementSequenceNumber28C(r150 + ":28C:" + r5 + "\r\n" + r150));
        assertNull(validator.extractStatementSequenceNumber28C(":28:" + n5));
        assertNull(validator.extractStatementSequenceNumber28C(":29C:" + n5));
        assertNull(validator.extractStatementSequenceNumber28C(":28C" + n5));
        assertNull(validator.extractStatementSequenceNumber28C("28C:" + n5));
        assertNull(validator.extractStatementSequenceNumber28C(":128C:" + n5));
    }

    @Test
    public void testExtractOpeningBalance60F() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String r25 = avoid(RandomStringUtils.randomAscii(25));
        String r5 = avoid(RandomStringUtils.randomAscii(5));
        assertEquals(r25, validator.extractOpeningBalance60F(":60F:" + r25));
        assertEquals(r25, validator.extractOpeningBalance60F(r150 + ":60F:" + r25));
        assertEquals(r25, validator.extractOpeningBalance60F(r150 + ":60F:" + r25 + r150));
        assertEquals(r5, validator.extractOpeningBalance60F(r150 + ":60F:" + r5 + "\r\n" + r150));
        assertEquals(r5, validator.extractOpeningBalance60F(r150 + ":60F:" + r5 + "\n" + r150));
        assertEquals(r5, validator.extractOpeningBalance60F(r150 + ":60M:" + r5 + "\n" + r150));
        assertNull(validator.extractOpeningBalance60F(":61F:" + r25));
        assertNull(validator.extractOpeningBalance60F(":60F" + r25));
        assertNull(validator.extractOpeningBalance60F("60F:" + r25));
        assertNull(validator.extractOpeningBalance60F(":160F:" + r25));
        assertNull(validator.extractOpeningBalance60F(":60Y:" + r25));
    }

    @Test
    public void testExtractClosingBalance62F() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String r25 = avoid(RandomStringUtils.randomAscii(25));
        String r5 = avoid(RandomStringUtils.randomAscii(5));
        assertEquals(r25, validator.extractClosingBalance62F(":62F:" + r25));
        assertEquals(r25, validator.extractClosingBalance62F(r150 + ":62F:" + r25));
        assertEquals(r25, validator.extractClosingBalance62F(r150 + ":62F:" + r25 + r150));
        assertEquals(r5, validator.extractClosingBalance62F(r150 + ":62F:" + r5 + "\r\n" + r150));
        assertEquals(r5, validator.extractClosingBalance62F(r150 + ":62F:" + r5 + "\n" + r150));
        assertEquals(r5, validator.extractClosingBalance62F(r150 + ":62M:" + r5 + "\n" + r150));
        assertNull(validator.extractClosingBalance62F(":63F:" + r25));
        assertNull(validator.extractClosingBalance62F(":62F" + r25));
        assertNull(validator.extractClosingBalance62F("62F:" + r25));
        assertNull(validator.extractClosingBalance62F(":162F:" + r25));
        assertNull(validator.extractClosingBalance62F(":62Y:" + r25));
    }

    @Test
    public void testExtractListOfTransactionCommentPair() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        String rr150 = avoid(RandomStringUtils.randomAscii(150));
        List<String[]> pairs = validator.extractListOfTransactionCommentPair(":61:" + r150 + ":86:" + rr150);
        assertEquals(1, pairs.size());
        assertEquals(r150, pairs.get(0)[0]);
        assertEquals(rr150, pairs.get(0)[1]);
        assertEquals(1, validator.extractListOfTransactionCommentPair(":61:" + r150 + ":86:" + rr150).size());
        assertEquals(1, validator.extractListOfTransactionCommentPair(":61:" + r150 + ":86:" + rr150).size());
        assertEquals(1, validator.extractListOfTransactionCommentPair(":61:" + r150 + "\n" + ":86:" + rr150 + "\n").size());
        assertEquals(2, validator.extractListOfTransactionCommentPair(":61:" + r150 + "\n" + ":86:" + rr150 + "\n" + ":61:" + r150 + "\n" + ":86:" + rr150 + "\n").size());
        assertEquals(2, validator.extractListOfTransactionCommentPair(r150 + ":61:" + r150 + "\n" + ":86:" + rr150 + "\n" + ":61:" + r150 + "\n" + ":86:" + rr150 + "\n").size());
        assertEquals(2, validator.extractListOfTransactionCommentPair(r150 + ":61:" + r150 + "\r\n" + ":86:" + rr150 + "\r\n" + ":61:" + r150 + "\r\n" + ":86:" + rr150 + "\r\n").size());
        assertEquals(3, validator.extractListOfTransactionCommentPair(r150 + ":61:" + r150 + "\r\n" + ":86:" + rr150 + "\r\n" + ":61:" + r150 + "\r\n" + ":86:" + rr150 + "\r\n" + ":61:" + r150 + "\r\n" + ":86:" + rr150 + "\r\n").size());
        pairs = validator.extractListOfTransactionCommentPair(r150 + ":61:" + r150 + "1\r\n" + ":86:" + rr150 + "2\r\n" + ":61:" + r150 + "3\r\n" + ":86:" + rr150 + "4\r\n" + ":61:" + r150 + "5\r\n" + ":86:" + rr150 + "6\r\n");
        assertEquals(3, pairs.size());
        assertEquals(r150 + "1\r\n", pairs.get(0)[0]);
        assertEquals(rr150 + "2\r\n", pairs.get(0)[1]);
        assertEquals(r150 + "3\r\n", pairs.get(1)[0]);
        assertEquals(rr150 + "4\r\n", pairs.get(1)[1]);
        assertEquals(r150 + "5\r\n", pairs.get(2)[0]);
        assertEquals(rr150 + "6\r\n", pairs.get(2)[1]);
    }

    private void exceptionWasThrownForBalance(String in) {
        try {
            validator.mapMT940Balance(in);
            fail("Expected MT940FormatException");
        } catch (MT940FormatException e) {
        }
    }

    private void exceptionWasThrownForTransaction(String in) throws MT940MandatoryFieldException {
        try {
            validator.mapMT940Transaction(in);
            fail("Expected MT940FormatException");
        } catch (MT940FormatException e) {
        }
    }

    private void exceptionWasThrownForStatementNumber(String in) {
        try {
            validator.mapStatementSequenceNumber(in);
            fail("Expected MT940FormatException");
        } catch (MT940FormatException e) {
        }
    }

    @Test
    public void testFailureMapMT940Balance() throws Exception {
        exceptionWasThrownForBalance("141122USD15,85");
        exceptionWasThrownForBalance("A141122USD15,85");
        exceptionWasThrownForBalance("C14112USD15,85");
        exceptionWasThrownForBalance("C14112_USD15,85");
        exceptionWasThrownForBalance("C14112aUSD15,85");
        exceptionWasThrownForBalance("C141122SD15,85");
        exceptionWasThrownForBalance("C1411221SD15,85");
        exceptionWasThrownForBalance("C141122USD15");
        exceptionWasThrownForBalance("C141122USD15.85");
    }

//    private DateTime toZoned(String in) {
//        return DateTime.parse(in, DateTimeFormat.forPattern(dateFormat));
//    }

    private ZonedDateTime toZoned(String in) {
//        return LocalDateTime.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atZone(ZoneId.systemDefault());
//        return LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atTime(0,0).atZone(ZoneId.systemDefault());
        return LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay(ZoneId.systemDefault());
//        return LocalDate.parse(in, new DateTimeFormatterBuilder().
//                .parseCaseInsensitive()
//                .appendValue(YEAR, 4)
//                .appendValue(MONTH_OF_YEAR, 2)
//                .appendValue(DAY_OF_MONTH, 2)
//                .optionalStart()
//                .appendOffset("+HHMMss", "Z")
//                .toFormatter());
    }

    private ZonedDateTime toZonedDateTime(String in) {
//        return ZonedDateTime.parse(in, DateTimeFormatter.ofPattern(dateFormat));
//        return LocalDateTime.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atZone(ZoneId.systemDefault());
//        return LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atTime(0,0).atZone(ZoneId.systemDefault());
        return LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay(ZoneId.systemDefault());
    }

    @Test
    public void test123() {
        String in = "141122";
        String format =  "yyMMdd";
        LocalDate localDate = LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat));
        LocalDateTime localDateTime = LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay();
        ZonedDateTime zonedDateTime = LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay(ZoneId.systemDefault());
        l.info("{}", localDate);
        l.info("{}", localDateTime);
        l.info("{}", zonedDateTime);
//        in = "1122";
//        format =  "MMdd";
//        localDate = LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat));
//        localDateTime = LocalDate.parse(in, DateTimeFormatter.ofPattern(dateFormat)).atStartOfDay();
//        zonedDateTime = LocalDate.parse(in, DateTimeFormatter..ofPattern(dateFormat)).atStartOfDay(ZoneId.systemDefault());
////        l.info("{}", localDate.format());
//        l.info("{}", localDateTime);
//        l.info("{}", zonedDateTime);
    }
    @Test
    public void testMapMT940Balance() throws Exception {
        MT940Balance expected = new MT940Balance(MT940BalanceType.CREDIT, toZonedDateTime("141122"), "USD", new BigDecimal("15.85"));
        MT940Balance actual = validator.mapMT940Balance("C141122USD15,85");
        areMT940BalancesEqual(expected, actual);
        expected = new MT940Balance(MT940BalanceType.DEBIT, toZonedDateTime("141122"), "USD", new BigDecimal("15.85"));
        actual = validator.mapMT940Balance("D141122USD15,85");
        areMT940BalancesEqual(expected, actual);
        expected = new MT940Balance(MT940BalanceType.DEBIT, toZonedDateTime("141122"), "EUR", new BigDecimal("15."));
        actual = validator.mapMT940Balance("D141122EUR15,");
        areMT940BalancesEqual(expected, actual);
        expected = new MT940Balance(MT940BalanceType.DEBIT, toZonedDateTime("141122"), "EUR", new BigDecimal("123456789.12345"));
        actual = validator.mapMT940Balance("D141122EUR123456789,12345");
        areMT940BalancesEqual(expected, actual);
    }

    @Test
    public void testMapMT940Transaction() throws Exception {
        MT940Transaction expected = new MT940Transaction(toZoned("030628"), toZoned("030628"), MT940FundsCode.DEBIT, new BigDecimal("21.00"), "FMSC", "NONREF.", "8327000090031789", "Card Transaction", null);
        MT940Transaction actual = validator.mapMT940Transaction("0306280628D21,00FMSCNONREF.//8327000090031789\r\nCard Transaction");
        areMT940TransactionsEqual(expected, actual);
        expected = new MT940Transaction(toZoned("030628"), toZoned("030628"), MT940FundsCode.CREDIT, new BigDecimal("21.00"), "FMSC", "NONREF", "8327000090031789", "Card Transaction", null);
        actual = validator.mapMT940Transaction("0306280628C21,00FMSCNONREF//8327000090031789\nCard Transaction");
        areMT940TransactionsEqual(expected, actual);
        expected = new MT940Transaction(toZoned("030628"), toZoned("030628"), MT940FundsCode.CREDIT_REVERS, new BigDecimal("21."), "FMSC", "NO/NREF", "8327000090031789", null, null);
        actual = validator.mapMT940Transaction("0306280628RC21,FMSCNO/NREF//8327000090031789");
        areMT940TransactionsEqual(expected, actual);
        expected = new MT940Transaction(toZoned("030628"), toZoned("030628"), MT940FundsCode.DEBIT, new BigDecimal("21.00"), "FMSC", "NO123 NREF", null, null, null);
        actual = validator.mapMT940Transaction("0306280628D21,00FMSCNO123 NREF");
        areMT940TransactionsEqual(expected, actual);
        expected = new MT940Transaction(toZoned("030628"), toZoned("030628"), MT940FundsCode.DEBIT, new BigDecimal("21.00"), "FMSC", "NONREF", null, "Card Transaction", null);
        actual = validator.mapMT940Transaction("0306280628D21,00FMSCNONREF\nCard Transaction");
        areMT940TransactionsEqual(expected, actual);
        String r16 = RandomStringUtils.randomAscii(16);
        String rr16 = RandomStringUtils.randomAscii(16);
        expected = new MT940Transaction(toZoned("030628"), toZoned("030628"), MT940FundsCode.DEBIT, new BigDecimal("21.00"), "FMSC", r16, rr16, rr16, null);
        actual = validator.mapMT940Transaction("0306280628D21,00FMSC" + r16 + "//" + rr16 + "\n" + rr16);
        areMT940TransactionsEqual(expected, actual);
        expected = new MT940Transaction(toZoned("030628"), null, MT940FundsCode.DEBIT, new BigDecimal("21.00"), "FMSC", r16, rr16, rr16, null);
        actual = validator.mapMT940Transaction("030628D21,00FMSC" + r16 + "//" + rr16 + "\n" + rr16);
        areMT940TransactionsEqual(expected, actual);
    }

    @Test
    public void testFailureMapStatementSequenceNumber() throws Exception {
        exceptionWasThrownForStatementNumber("a2345/12345");
        exceptionWasThrownForStatementNumber("abcd");
        exceptionWasThrownForStatementNumber("abcd/abcd");
    }

    @Test
    public void testMapStatementSequenceNumber() throws Exception {
        String n5 = RandomStringUtils.randomNumeric(5);
        String nn5 = RandomStringUtils.randomNumeric(5);
        String[] pairs = validator.mapStatementSequenceNumber(n5 + "/" + nn5);
        assertEquals(2, pairs.length);
        assertEquals(n5, pairs[0]);
        assertEquals(nn5, pairs[1]);
        pairs = validator.mapStatementSequenceNumber(n5);
        assertEquals(2, pairs.length);
        assertEquals(n5, pairs[0]);
        assertEquals(null, pairs[1]);
        pairs = validator.mapStatementSequenceNumber(n5 + "/");
        assertEquals(2, pairs.length);
        assertEquals(n5, pairs[0]);
        assertEquals(null, pairs[1]);
    }

    @Test
    public void testFailureMapMT940Transaction() throws Exception {
        exceptionWasThrownForTransaction("0306280628D21,00FMSCNONREF//8327000090031789\nCard Transaction\nCardTransaction");
        exceptionWasThrownForTransaction("0306280628D21,00FMSCNONREF//8327000//090031789\nCard Transaction");
        exceptionWasThrownForTransaction("0a0628D21,00FMSCNONREF//8327000090031789\nCard Transaction");
        exceptionWasThrownForTransaction("030628D21.00FMSCNONREF//8327000090031789\nCard Transaction");
        exceptionWasThrownForTransaction("030628D21,00FmSCNONREF//8327000090031789\nCard Transaction");
        exceptionWasThrownForTransaction("030628D21,00F1SCNONREF//8327000090031789\nCard Transaction");
        exceptionWasThrownForTransaction("030628Q21,00FMSCNONREF//8327000090031789\nCard Transaction");
        exceptionWasThrownForTransaction("030628C,01FMSCNONREF//8327000090031789\nCard Transaction");
    }

    @Test
    public void testExtractListOfTransactionsWithInformationToAccountOwner61_86() throws Exception {
        String r150 = avoid(RandomStringUtils.randomAscii(150));
        assertEquals(":61:" + r150, validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + ":62F:"));
        assertEquals(":61:" + r150, validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + ":62F:" + r150));
        assertEquals(":61:" + r150, validator.extractListOfTransactionsWithInformationToAccountOwner61_86(r150 + ":61:" + r150 + ":62F:"));
        assertEquals(":61:" + r150 + "\n", validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\n" + ":62F:"));
        assertEquals(":61:" + r150 + "\r\n", validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\r\n" + ":62F:"));
        assertEquals(":61:" + r150 + "\r\n", validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\r\n" + ":62M:"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61" + r150 + "\r\n" + ":62F:"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86("61:" + r150 + "\r\n" + ":62F:"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\r\n" + "62F:"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\r\n" + ":62F"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":161:" + r150 + "\r\n" + ":62F:"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\r\n" + ":162F:"));
        assertNull(validator.extractListOfTransactionsWithInformationToAccountOwner61_86(":61:" + r150 + "\r\n" + ":62Q:"));
    }
}