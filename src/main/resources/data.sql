INSERT INTO collectivity (id, number, name, location, agricultural_specialty, creation_date,
                          federation_approval, annual_dues,
                          president_id, vice_president_id, treasurer_id, secretary_id)
VALUES
    -- FIX: annual_dues corrected to 100000 for col-1 and col-2 (was 200000)
    -- Source: membership_fee table has cot-1 and cot-2 both at amount=100000
    ('col-1', '1', 'Mpanorina',      'Ambatondrazaka', 'Riziculture', '2022-01-15', TRUE, 100000, NULL, NULL, NULL, NULL),
    ('col-2', '2', 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture','2022-01-15', TRUE, 100000, NULL, NULL, NULL, NULL),
    ('col-3', '3', 'Tantely mamy',   'Brickaville',    'Apiculture',  '2022-01-15', TRUE,  50000, NULL, NULL, NULL, NULL);

INSERT INTO member (id, collectivity_id, first_name, last_name, birth_date, gender,
                    address, profession, phone_number, email,
                    adhesion_date, occupation,
                    registration_fee_paid, membership_dues_paid)
VALUES
    ('C1-M1', 'col-1', 'Prénom membre 1',  'Nom membre 1',  '1980-02-01', 'MALE',   'Lot II V M Ambato.',  'Riziculteur', 341234567,  'member.1@fed-agri.mg',  '2022-01-15', 'PRESIDENT',      TRUE, TRUE),
    ('C1-M2', 'col-1', 'Prénom membre 2',  'Nom membre 2',  '1982-03-05', 'MALE',   'Lot II F Ambato.',    'Agriculteur', 321234567,  'member.2@fed-agri.mg',  '2022-01-15', 'VICE_PRESIDENT', TRUE, TRUE),
    ('C1-M3', 'col-1', 'Prénom membre 3',  'Nom membre 3',  '1992-03-10', 'MALE',   'Lot II J Ambato.',    'Collecteur',  331234567,  'member.3@fed-agri.mg',  '2022-01-15', 'SECRETARY',      TRUE, TRUE),
    ('C1-M4', 'col-1', 'Prénom membre 4',  'Nom membre 4',  '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.',  'Distributeur',381234567,  'member.4@fed-agri.mg',  '2022-01-15', 'TREASURER',      TRUE, TRUE),
    ('C1-M5', 'col-1', 'Prénom membre 5',  'Nom membre 5',  '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',   'Riziculteur', 373434567,  'member.5@fed-agri.mg',  '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('C1-M6', 'col-1', 'Prénom membre 6',  'Nom membre 6',  '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',    'Riziculteur', 372234567,  'member.6@fed-agri.mg',  '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('C1-M7', 'col-1', 'Prénom membre 7',  'Nom membre 7',  '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',    'Riziculteur', 374234567,  'member.7@fed-agri.mg',  '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('C1-M8', 'col-1', 'Prénom membre 8',  'Nom membre 8',  '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',    'Riziculteur', 370234567,  'member.8@fed-agri.mg',  '2022-01-15', 'SENIOR',         TRUE, TRUE),

    ('C3-M1', 'col-3', 'Prénom membre 9',  'Nom membre 9',  '1988-01-02', 'MALE',   'Lot 33 J Antsirabe',  'Apiculteur',  34034567,   'member.9@fed-agri.mg',  '2022-01-15', 'PRESIDENT',      TRUE, TRUE),
    ('C3-M2', 'col-3', 'Prénom membre 10', 'Nom membre 10', '1982-03-05', 'MALE',   'Lot 2 J Antsirabe',   'Agriculteur', 338634567,  'member.10@fed-agri.mg', '2022-01-15', 'VICE_PRESIDENT', TRUE, TRUE),
    ('C3-M3', 'col-3', 'Prénom membre 11', 'Nom membre 11', '1992-03-12', 'MALE',   'Lot 8 KM Antsirabe',  'Collecteur',  338234567,  'member.11@fed-agri.mg', '2022-01-15', 'SECRETARY',      TRUE, TRUE),
    ('C3-M4', 'col-3', 'Prénom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe','Distributeur',382334567,  'member.12@fed-agri.mg', '2022-01-15', 'TREASURER',      TRUE, TRUE),
    ('C3-M5', 'col-3', 'Prénom membre 13', 'Nom membre 13', '1999-08-11', 'MALE',   'Lot UV 80 Antsirabe', 'Apiculteur',  373365567,  'member.13@fed-agri.mg', '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('C3-M6', 'col-3', 'Prénom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe',  'Apiculteur',  378234567,  'member.14@fed-agri.mg', '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('C3-M7', 'col-3', 'Prénom membre 15', 'Nom membre 15', '1998-01-13', 'MALE',   'Lot UV 7 Antsirabe',  'Apiculteur',  374914567,  'member.15@fed-agri.mg', '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('C3-M8', 'col-3', 'Prénom membre 16', 'Nom membre 16', '1975-08-02', 'MALE',   'Lot UV 8 Antsirabe',  'Apiculteur',  370634567,  'member.16@fed-agri.mg', '2022-01-15', 'SENIOR',         TRUE, TRUE);


UPDATE collectivity SET president_id='C1-M1', vice_president_id='C1-M2', treasurer_id='C1-M4', secretary_id='C1-M3' WHERE id='col-1';
UPDATE collectivity SET president_id='C1-M5', vice_president_id='C1-M6', treasurer_id='C1-M8', secretary_id='C1-M7' WHERE id='col-2';
UPDATE collectivity SET president_id='C3-M1', vice_president_id='C3-M2', treasurer_id='C3-M4', secretary_id='C3-M3' WHERE id='col-3';

INSERT INTO member_collectivity (member_id, collectivity_id, occupation, adhesion_date) VALUES

                                                                                            ('C1-M1', 'col-1', 'PRESIDENT',      '2022-01-15'),
                                                                                            ('C1-M2', 'col-1', 'VICE_PRESIDENT', '2022-01-15'),
                                                                                            ('C1-M3', 'col-1', 'SECRETARY',      '2022-01-15'),
                                                                                            ('C1-M4', 'col-1', 'TREASURER',      '2022-01-15'),
                                                                                            ('C1-M5', 'col-1', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M6', 'col-1', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M7', 'col-1', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M8', 'col-1', 'SENIOR',         '2022-01-15'),

                                                                                            ('C1-M1', 'col-2', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M2', 'col-2', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M3', 'col-2', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M4', 'col-2', 'SENIOR',         '2022-01-15'),
                                                                                            ('C1-M5', 'col-2', 'PRESIDENT',      '2022-01-15'),
                                                                                            ('C1-M6', 'col-2', 'VICE_PRESIDENT', '2022-01-15'),
                                                                                            ('C1-M7', 'col-2', 'SECRETARY',      '2022-01-15'),
                                                                                            ('C1-M8', 'col-2', 'TREASURER',      '2022-01-15'),

                                                                                            ('C3-M1', 'col-3', 'PRESIDENT',      '2022-01-15'),
                                                                                            ('C3-M2', 'col-3', 'VICE_PRESIDENT', '2022-01-15'),
                                                                                            ('C3-M3', 'col-3', 'SECRETARY',      '2022-01-15'),
                                                                                            ('C3-M4', 'col-3', 'TREASURER',      '2022-01-15'),
                                                                                            ('C3-M5', 'col-3', 'SENIOR',         '2022-01-15'),
                                                                                            ('C3-M6', 'col-3', 'SENIOR',         '2022-01-15'),
                                                                                            ('C3-M7', 'col-3', 'SENIOR',         '2022-01-15'),
                                                                                            ('C3-M8', 'col-3', 'SENIOR',         '2022-01-15');


INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
                                                                            ('C1-M3', 'C1-M1', 'Non précisé'), ('C1-M3', 'C1-M2', 'Non précisé'),
                                                                            ('C1-M4', 'C1-M1', 'Non précisé'), ('C1-M4', 'C1-M2', 'Non précisé'),
                                                                            ('C1-M5', 'C1-M1', 'Non précisé'), ('C1-M5', 'C1-M2', 'Non précisé'),
                                                                            ('C1-M6', 'C1-M1', 'Non précisé'), ('C1-M6', 'C1-M2', 'Non précisé'),
                                                                            ('C1-M7', 'C1-M1', 'Non précisé'), ('C1-M7', 'C1-M2', 'Non précisé'),
                                                                            ('C1-M8', 'C1-M6', 'Non précisé'), ('C1-M8', 'C1-M7', 'Non précisé'),
                                                                            ('C3-M3', 'C3-M1', 'Non précisé'), ('C3-M3', 'C3-M2', 'Non précisé'),
                                                                            ('C3-M4', 'C3-M1', 'Non précisé'), ('C3-M4', 'C3-M2', 'Non précisé'),
                                                                            ('C3-M5', 'C3-M1', 'Non précisé'), ('C3-M5', 'C3-M2', 'Non précisé'),
                                                                            ('C3-M6', 'C3-M1', 'Non précisé'), ('C3-M6', 'C3-M2', 'Non précisé'),
                                                                            ('C3-M7', 'C3-M1', 'Non précisé'), ('C3-M7', 'C3-M2', 'Non précisé'),
                                                                            ('C3-M8', 'C3-M1', 'Non précisé'), ('C3-M8', 'C3-M2', 'Non précisé');


INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status) VALUES
                                                                                                      ('cot-1', 'col-1', '2026-01-01', 'ANNUALLY', 100000, 'Cotisation annuelle', 'ACTIVE'),
                                                                                                      ('cot-2', 'col-2', '2026-01-01', 'ANNUALLY', 100000, 'Cotisation annuelle', 'ACTIVE'),
                                                                                                      ('cot-3', 'col-3', '2026-01-01', 'ANNUALLY',  50000, 'Cotisation annuelle', 'ACTIVE');


INSERT INTO cash_account (id, amount) VALUES
                                          ('C1-A-CASH', 0),
                                          ('C2-A-CASH', 0),
                                          ('C3-A-CASH', 0);

INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number, amount) VALUES
                                                                                                        ('C1-A-MOBILE-1', 'Mpanorina',      'ORANGE_MONEY', 370489612, 0),
                                                                                                        ('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', 320489612, 0);

INSERT INTO collectivity_account (collectivity_id, account_id, account_type) VALUES
                                                                                 ('col-1', 'C1-A-CASH',     'CASH'),
                                                                                 ('col-1', 'C1-A-MOBILE-1', 'MOBILE_BANKING'),
                                                                                 ('col-2', 'C2-A-CASH',     'CASH'),
                                                                                 ('col-2', 'C2-A-MOBILE-1', 'MOBILE_BANKING'),
                                                                                 ('col-3', 'C3-A-CASH',     'CASH');


INSERT INTO member_payment (id, member_id, amount, payment_mode, account_credited_id, creation_date) VALUES
                                                                                                         ('pay-col1-m1', 'C1-M1', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m2', 'C1-M2', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m3', 'C1-M3', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m4', 'C1-M4', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m5', 'C1-M5', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m6', 'C1-M6', 100000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m7', 'C1-M7',  60000, 'CASH', 'C1-A-CASH', '2026-01-01'),
                                                                                                         ('pay-col1-m8', 'C1-M8',  90000, 'CASH', 'C1-A-CASH', '2026-01-01'),

                                                                                                         ('pay-col2-m1', 'C1-M1',  60000, 'CASH',           'C2-A-CASH',     '2026-01-01'),
                                                                                                         ('pay-col2-m2', 'C1-M2',  90000, 'CASH',           'C2-A-CASH',     '2026-01-01'),
                                                                                                         ('pay-col2-m3', 'C1-M3', 100000, 'CASH',           'C2-A-CASH',     '2026-01-01'),
                                                                                                         ('pay-col2-m4', 'C1-M4', 100000, 'CASH',           'C2-A-CASH',     '2026-01-01'),
                                                                                                         ('pay-col2-m5', 'C1-M5', 100000, 'CASH',           'C2-A-CASH',     '2026-01-01'),
                                                                                                         ('pay-col2-m6', 'C1-M6', 100000, 'CASH',           'C2-A-CASH',     '2026-01-01'),
                                                                                                         ('pay-col2-m7', 'C1-M7',  40000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01'),
                                                                                                         ('pay-col2-m8', 'C1-M8',  60000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', '2026-01-01');


INSERT INTO collectivity_transaction
(id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id)
VALUES
    ('txn-col1-m1', 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M1'),
    ('txn-col1-m2', 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M2'),
    ('txn-col1-m3', 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M3'),
    ('txn-col1-m4', 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M4'),
    ('txn-col1-m5', 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M5'),
    ('txn-col1-m6', 'col-1', '2026-01-01', 100000, 'CASH', 'C1-A-CASH', 'C1-M6'),
    ('txn-col1-m7', 'col-1', '2026-01-01',  60000, 'CASH', 'C1-A-CASH', 'C1-M7'),
    ('txn-col1-m8', 'col-1', '2026-01-01',  90000, 'CASH', 'C1-A-CASH', 'C1-M8'),

    ('txn-col2-m1', 'col-2', '2026-01-01',  60000, 'CASH',           'C2-A-CASH',     'C1-M1'),
    ('txn-col2-m2', 'col-2', '2026-01-01',  90000, 'CASH',           'C2-A-CASH',     'C1-M2'),
    ('txn-col2-m3', 'col-2', '2026-01-01', 100000, 'CASH',           'C2-A-CASH',     'C1-M3'),
    ('txn-col2-m4', 'col-2', '2026-01-01', 100000, 'CASH',           'C2-A-CASH',     'C1-M4'),
    ('txn-col2-m5', 'col-2', '2026-01-01', 100000, 'CASH',           'C2-A-CASH',     'C1-M5'),
    ('txn-col2-m6', 'col-2', '2026-01-01', 100000, 'CASH',           'C2-A-CASH',     'C1-M6'),
    ('txn-col2-m7', 'col-2', '2026-01-01',  40000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', 'C1-M7'),
    ('txn-col2-m8', 'col-2', '2026-01-01',  60000, 'MOBILE_BANKING', 'C2-A-MOBILE-1', 'C1-M8');


UPDATE cash_account           SET amount = 750000 WHERE id = 'C1-A-CASH';
UPDATE cash_account           SET amount = 550000 WHERE id = 'C2-A-CASH';
UPDATE mobile_banking_account SET amount = 100000 WHERE id = 'C2-A-MOBILE-1';



INSERT INTO bank_account (id, holder_name, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key, amount)
VALUES
    ('C3-A-BANK-1', 'Tantely mamy', 'BMOI', 12345, 67890, 11223344, 55, 0),
    ('C3-A-BANK-2', 'Tantely mamy', 'BOA', 54321, 98765, 55443322, 11, 0);

INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number, amount)
VALUES
    ('C3-A-MOBILE-1', 'Tantely mamy', 'MVOLA', 340123456, 0);

INSERT INTO collectivity_account (collectivity_id, account_id, account_type)
VALUES
    ('col-3', 'C3-A-BANK-1', 'BANK_TRANSFER'),
    ('col-3', 'C3-A-BANK-2', 'BANK_TRANSFER'),
    ('col-3', 'C3-A-MOBILE-1', 'MOBILE_BANKING');