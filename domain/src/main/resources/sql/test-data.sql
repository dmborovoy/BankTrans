DELETE FROM bkv.mt940_transaction CASCADE;
DELETE FROM bkv.mt940_statement CASCADE;
DELETE FROM bkv.mt940_balance CASCADE;
DELETE FROM bkv.ear_attachment CASCADE;
DELETE FROM bkv.ear_message CASCADE;

INSERT INTO bkv.ear_message (id, from_list, to_list, subject, sent_date, received_date, message_content, attachment_count, download_path, processing_date) VALUES
  (100, '_1utility.payments@bov.com', 'mt940@nxpay.eu', '_1Bank of Valletta Swift Statement', '2014-12-05 13:34:41+11', '2014-12-05 13:34:52+11', '_1Attached please find the Daily Swift Statement', 1, 'e:\Attachment\working', '2014-12-16 11:57:23.187+11'),
  (101, '_2utility.payments@bov.com', 'mt940@nxpay.eu', '_2Bank of Valletta Swift Statement', '2014-12-05 13:34:42+11', '2014-12-05 13:34:53+11', '_2Attached please find the Daily Swift Statement', 1, 'e:\Attachment\working', '2014-12-16 11:57:25.57+11'),
  (102, '_3utility.payments@bov.com', 'mt940@nxpay.eu', '_3Bank of Valletta Swift Statement', '2014-12-06 14:22:50+11', '2014-12-06 14:23:03+11', '_3Attached please find the Daily Swift Statement', 1, 'e:\Attachment\working', '2014-12-16 11:57:27.745+11'),
  (103, '_4utility.payments@bov.com', 'mt940@nxpay.eu', '_4Bank of Valletta Swift Statement', '2014-12-06 14:22:52+11', '2014-12-06 14:24:03+11', '_3Attached please find the Daily Swift Statement', 0, 'e:\Attachment\working', '2014-12-16 11:57:28.745+11'),
  (104, '_5utility.payments@bov.com', 'mt940@nxpay.eu', '_5Bank of Valletta Swift Statement', '2014-12-06 14:22:53+11', '2014-12-06 14:25:03+11', '_3Attached please find the Daily Swift Statement', 0, 'e:\Attachment\working', '2014-12-16 11:57:29.745+11'),
  (105, '_6utility.payments@bov.com', 'mt940@nxpay.eu', '_6Bank of Valletta Swift Statement', '2014-12-06 14:22:54+11', '2014-12-06 14:26:03+11', '_3Attached please find the Daily Swift Statement', 0, 'e:\Attachment\working', '2014-12-16 11:57:30.745+11'),
  (106, '_7utility.payments@bov.com', 'mt940@nxpay.eu', '_7Bank of Valletta Swift Statement', '2014-12-06 14:22:55+11', '2014-12-06 14:27:03+11', '_3Attached please find the Daily Swift Statement', 0, 'e:\Attachment\working', '2014-12-16 11:57:31.745+11');

INSERT INTO bkv.ear_attachment (id, original_name, unique_name, size, status, message_id) VALUES
  (1, '40022530424.dat', '2014-12-16-8cf74a74-e7f3-40d8-a1fb-393c2a806847-40022530424.dat', 506, 0, 100),
  (2, '40022530437.dat', '2014-12-16-61a4956e-7ce7-41fd-8517-ef362d81c7f7-40022530437.dat', 506, 0, 101),
  (3, '40022530453.dat', '2014-12-16-b5ab71dd-0dfb-4a02-84bc-2970b79ce39c-40022530453.dat', 500, 0, 102);

INSERT INTO bkv.mt940_balance (id, amount, balance_type, currency, date) VALUES
  (35, 251750.030000, 'C', 'EUR', '2014-01-19 00:00:00+11'),
  (36, 37218.830000, 'C', 'EUR', '2014-01-19 00:00:00+11'),
  (37, 90746.030000, 'C', 'EUR', '2014-01-19 00:00:00+11'),
  (38, 251750.030000, 'C', 'EUR', '2014-01-19 00:00:00+11'),
  (39, 90.580000, 'C', 'GBP', '2014-01-19 00:00:00+11'),
  (40, 90.580000, 'C', 'GBP', '2014-01-19 00:00:00+11'),
  (41, 90746.030000, 'C', 'EUR', '2014-01-20 00:00:00+11'),
  (42, 90746.030000, 'C', 'EUR', '2014-01-20 00:00:00+11');

INSERT INTO bkv.mt940_statement (id, swift_header1, swift_header2, swift_header3, account_id, statement_number, sequence_number, transaction_reference, related_reference, opening_balance_id, closing_balance_id, settlement_file_id, entry_order) VALUES
  (1, 'F01VALLMTMTAXXX.SS..SEQ..', 'I940BOVNXSYS1821N', '{108:STATEMENT}', '40022530424', '1580', '1', '219906', '', 36, 35, 1, 0),
  (2, 'F01VALLMTMTAXXX.SS..SEQ..', 'I940BOVNXSYS1821N', '{108:STATEMENT}', '40022530424', '1580', '2', '219906', '', 38, 37, 1, 1),
  (3, 'F01VALLMTMTAXXX.SS..SEQ..', 'I940BOVNXSYS1823N', '{108:STATEMENT}', '40022530453', '1580', '1', '219908', '', 40, 39, 2, 0),
  (4, 'F01VALLMTMTAXXX.SS..SEQ..', 'I940BOVNXSYS1821N', '{108:STATEMENT}', '40022530424', '1581', '1', '220104', '', 42, 41, 3, 0);

INSERT INTO bkv.mt940_transaction (id, amount, currency, date, entry_date, funds_code, info_to_account_owner, reference_for_account_owner, reference_for_bank, swift_code, transaction_description, statement_id, entry_order, status, instance) VALUES
  (10, 200000.000000, 'USD', '2014-01-19 00:00:00+11', '2014-01-19 00:00:00+11', 'C', 'TRANSFER TO 0000001019370127', 'MR GREEN LIMITED', '', 'NTRF', '', 1, 0, 0, 0),
  (11, 250000.000000, 'USD', '2014-01-19 00:00:00+11', '2014-01-19 00:00:00+11', 'C', 'INTERTRONIC - SUPERBAHIS', 'INTERTRONIC LTD', '', 'NTRF', '', 1, 1, 0, 0),
  (12, 100000.000000, 'USD', '2014-01-19 00:00:00+11', '2014-01-19 00:00:00+11', 'C', 'INTERTRONIC - BETBOO', 'INTERTRONIC LTD', '', 'NTRF', '', 1, 2, 0, 0),
  (13, 335468.800000, 'USD', '2014-01-19 00:00:00+11', '2014-01-19 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR335464.80\nZB3261', '.', '2930397', 'NTRF', '', 1, 3, 1, 0),
  (14, 164004.000000, 'USD', '2014-01-19 00:00:00+11', '2014-01-19 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR164000.00\nZB3243', '.', '2930430', 'NTRF', '', 2, 0, 0, 0),
  (15, 3000.000000, 'USD', '2014-01-19 00:00:00+11', '2014-01-19 00:00:00+11', 'C', 'MERCHANT ACCOUNT 000000106623\n', 'BLING CITY LIMIT', '', 'NTRF', '', 2, 1, 0, 0),
  (16, 180000.000000, 'USD', '2014-01-20 00:00:00+11', '2014-01-20 00:00:00+11', 'C', 'FULL AMT:USD180000.00\nRCVD AMT:USD180000.00\n1.0000ACCORDING TO THE\nAGREEMENT, TOP UP USD ACCOUNT\n', 'SOREX GROUP INC.', '', 'NTRF', '', 3, 0, 0, 0),
  (17, 180915.040000, 'USD', '2014-01-20 00:00:00+11', '2014-01-20 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nUSD180900.00\nZB3243', '.', '2933598', 'NTRF', '', 3, 1, 0, 0),
  (18, 200000.000000, 'USD', '2014-01-21 00:00:00+11', '2014-01-21 00:00:00+11', 'RC', 'TRANSFER TO NX PAY\n0000001019370127\n', 'MR GREEN LIMITED', '', 'NTRF', 'BLA', 4, 0, 0, 0),
  (19, 290704.000000, 'USD', '2014-01-21 00:00:00+11', '2014-01-21 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR290700.00\nZB3243\n', '.', '2935372', 'NTRF', 'bla', 4, 1, 0, 1),
  (20, 290705.000000, 'USD', '2014-01-21 00:00:00+11', '2014-01-21 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR290700.00\nZB3243\n', '.', '2935373', 'NTRF', 'bla', 4, 1, 2, 0),
  (21, 290706.000000, 'USD', '2014-01-21 00:00:00+11', '2014-01-21 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR290700.00\nZB3243\n', '.', '2935374', 'NTRF', 'bla', 4, 1, 2, 0),
  (22, 290707.000000, 'USD', '2014-01-21 00:00:00+11', '2014-01-21 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR290700.00\nZB3243\n', '.', '2935374', 'NTRF', 'bla', 4, 1, 2, 0),
  (23, 290708.000000, 'USD', '2014-01-21 00:00:00+11', '2014-01-21 00:00:00+11', 'D', 'GLOBAL REACH PARTNERS LIMITED\nEUR290700.00\nZB3243\n', '.', '2935374', 'NTRF', 'bla', 4, 1, 2, 0);


