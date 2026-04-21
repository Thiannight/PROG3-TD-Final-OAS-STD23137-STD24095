INSERT INTO collectivity (id, name, location, agricultural_specialty, creation_date, federation_approval,
                          president_id, vice_president_id, treasurer_id, secretary_id)
VALUES
    ('COL-001', 'Ny Tantsaha Malagasy', 'Antananarivo', 'Riziculture',        '2022-01-15', TRUE,  NULL, NULL, NULL, NULL),
    ('COL-002', 'Vokatra Tsara',        'Fianarantsoa',  'Maraîchage',         '2022-06-10', TRUE,  NULL, NULL, NULL, NULL),
    ('COL-003', 'Harena Voajanahary',   'Toamasina',     'Culture de vanille', '2023-03-20', TRUE,  NULL, NULL, NULL, NULL);

INSERT INTO member (id, collectivity_id, first_name, last_name, birth_date, gender, address, profession,
                    phone_number, email, adhesion_date, occupation, registration_fee_paid, membership_dues_paid)
VALUES
    ('MEM-001', 'COL-001', 'Rakoto',    'Andriamaro',     '1980-05-10', 'MALE',   '12 Rue Indépendance, Antananarivo', 'Agriculteur',  340001001, 'rakoto.andriamaro@email.mg',    '2022-01-15', 'PRESIDENT',      TRUE, TRUE),
    ('MEM-002', 'COL-001', 'Voahangy',  'Rasoa',          '1985-08-22', 'FEMALE', '45 Av Liberté, Antananarivo',       'Agricultrice', 340001002, 'voahangy.rasoa@email.mg',       '2022-01-15', 'VICE_PRESIDENT', TRUE, TRUE),
    ('MEM-003', 'COL-001', 'Hery',      'Rakotondrabe',   '1978-11-03', 'MALE',   '8 Rue du Marché, Antananarivo',     'Cultivateur',  340001003, 'hery.rakotondrabe@email.mg',    '2022-01-15', 'TREASURER',      TRUE, TRUE),
    ('MEM-004', 'COL-001', 'Lalao',     'Raharison',      '1990-02-17', 'FEMALE', '23 Cité Ampefiloha, Antananarivo',  'Secrétaire',   340001004, 'lalao.raharison@email.mg',      '2022-01-15', 'SECRETARY',      TRUE, TRUE),
    ('MEM-005', 'COL-001', 'Fidy',      'Randria',        '1988-07-30', 'MALE',   '56 Rue Pasteur, Antananarivo',      'Agriculteur',  340001005, 'fidy.randria@email.mg',         '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('MEM-006', 'COL-001', 'Miora',     'Rasolofonirina', '1992-04-11', 'FEMALE', '3 Allée des Jacarandas, Tana',      'Agricultrice', 340001006, 'miora.rasolofonirina@email.mg', '2022-01-15', 'SENIOR',         TRUE, TRUE),
    ('MEM-007', 'COL-001', 'Tiana',     'Razafindrabe',   '1995-09-25', 'MALE',   '17 Rue Rainitovo, Antananarivo',    'Étudiant',     340001007, 'tiana.razafindrabe@email.mg',   '2022-02-01', 'SENIOR',         TRUE, TRUE),
    ('MEM-008', 'COL-001', 'Nasolo',    'Rabemananjara',  '1997-12-05', 'MALE',   '99 Av de France, Antananarivo',     'Cultivateur',  340001008, 'nasolo.rabemananjara@email.mg', '2022-03-10', 'SENIOR',         TRUE, TRUE),
    ('MEM-009', 'COL-001', 'Hasina',    'Randriamasy',    '2000-06-18', 'FEMALE', '7 Rue Solombavambahoaka, Tana',     'Agricultrice', 340001009, 'hasina.randriamasy@email.mg',   '2022-04-15', 'SENIOR',         TRUE, TRUE),
    ('MEM-010', 'COL-001', 'Toky',      'Ramaroson',      '2001-03-22', 'MALE',   '34 Cité Universitaire, Tana',       'Étudiant',     340001010, 'toky.ramaroson@email.mg',       '2022-05-01', 'JUNIOR',         TRUE, TRUE),
    ('MEM-011', 'COL-001', 'Anja',      'Ratsimbazafy',   '2002-08-14', 'FEMALE', '21 Rue Ny Haja, Antananarivo',      'Étudiante',    340001011, 'anja.ratsimbazafy@email.mg',    '2022-06-10', 'JUNIOR',         TRUE, TRUE),
    ('MEM-012', 'COL-001', 'Tafita',    'Rakotondrazaka', '1999-11-30', 'MALE',   '60 Bd Ratsimilaho, Antananarivo',   'Cultivateur',  340001012, 'tafita.rakotondrazaka@email.mg','2022-07-20', 'JUNIOR',         TRUE, TRUE),

    ('MEM-013', 'COL-002', 'Solo',      'Andriantsoa',    '1975-01-08', 'MALE',   '5 Rue de la Paix, Fianarantsoa',    'Agriculteur',  340002001, 'solo.andriantsoa@email.mg',     '2022-06-10', 'PRESIDENT',      TRUE, TRUE),
    ('MEM-014', 'COL-002', 'Noro',      'Rafaralahimana', '1983-05-19', 'FEMALE', '12 Av Gallieni, Fianarantsoa',       'Agricultrice', 340002002, 'noro.rafaralahimana@email.mg',  '2022-06-10', 'VICE_PRESIDENT', TRUE, TRUE),
    ('MEM-015', 'COL-002', 'Mamy',      'Razafindrakoto', '1980-09-14', 'MALE',   '33 Rue Rainandriamampandry, Fiana', 'Comptable',    340002003, 'mamy.razafindrakoto@email.mg',  '2022-06-10', 'TREASURER',      TRUE, TRUE),
    ('MEM-016', 'COL-002', 'Fanja',     'Rakotoniary',    '1987-03-27', 'FEMALE', '8 Cité Ambalakely, Fianarantsoa',   'Secrétaire',   340002004, 'fanja.rakotoniary@email.mg',    '2022-06-10', 'SECRETARY',      TRUE, TRUE),
    ('MEM-017', 'COL-002', 'Lanto',     'Andrianaivo',    '1991-07-06', 'MALE',   '45 Rue Ny Fitiavana, Fiana',        'Agriculteur',  340002005, 'lanto.andrianaivo@email.mg',    '2022-06-10', 'SENIOR',         TRUE, TRUE),
    ('MEM-018', 'COL-002', 'Vatosoa',   'Raharinoro',     '1993-12-21', 'FEMALE', '19 Av de la Réunification, Fiana',  'Cultivatrice', 340002006, 'vatosoa.raharinoro@email.mg',   '2022-06-10', 'SENIOR',         TRUE, TRUE),
    ('MEM-019', 'COL-002', 'Mendrika',  'Rasoamanarivo',  '1989-04-09', 'MALE',   '27 Rue Ranaivo, Fianarantsoa',      'Agriculteur',  340002007, 'mendrika.rasoamanarivo@email.mg','2022-07-01','SENIOR',         TRUE, TRUE),
    ('MEM-020', 'COL-002', 'Tahiry',    'Rakotonirina',   '1996-10-15', 'FEMALE', '11 Cité Tanambao, Fianarantsoa',    'Étudiante',    340002008, 'tahiry.rakotonirina@email.mg',  '2022-08-05', 'SENIOR',         TRUE, TRUE),
    ('MEM-021', 'COL-002', 'Njaka',     'Andriamahefa',   '1998-02-28', 'MALE',   '3 Rue du Progrès, Fianarantsoa',    'Cultivateur',  340002009, 'njaka.andriamahefa@email.mg',   '2022-09-12', 'SENIOR',         TRUE, TRUE),
    ('MEM-022', 'COL-002', 'Diary',     'Rajaonah',       '2001-06-17', 'FEMALE', '50 Bd Lyautey, Fianarantsoa',       'Étudiante',    340002010, 'diary.rajaonah@email.mg',       '2022-10-20', 'JUNIOR',         TRUE, TRUE),
    ('MEM-023', 'COL-002', 'Tsiry',     'Andrianjafy',    '2003-08-04', 'MALE',   '6 Rue Ny Fanantenana, Fiana',       'Étudiant',     340002011, 'tsiry.andrianjafy@email.mg',    '2022-11-01', 'JUNIOR',         TRUE, TRUE),

    ('MEM-024', 'COL-003', 'Benja',     'Rabenilaina',    '1977-03-12', 'MALE',   '14 Rue de la Mer, Toamasina',        'Agriculteur',  340003001, 'benja.rabenilaina@email.mg',    '2023-03-20', 'PRESIDENT',      TRUE, TRUE),
    ('MEM-025', 'COL-003', 'Mirana',    'Ralaimihoatra',  '1984-07-25', 'FEMALE', '8 Av de l''Indépendance, Toamasina', 'Agricultrice', 340003002, 'mirana.ralaimihoatra@email.mg', '2023-03-20', 'VICE_PRESIDENT', TRUE, TRUE),
    ('MEM-026', 'COL-003', 'Erick',     'Rasolondraibe',  '1981-11-18', 'MALE',   '22 Rue du Port, Toamasina',          'Comptable',    340003003, 'erick.rasolondraibe@email.mg',  '2023-03-20', 'TREASURER',      TRUE, TRUE),
    ('MEM-027', 'COL-003', 'Zo',        'Rakotomalala',   '1990-05-07', 'FEMALE', '37 Cité Ambalakely, Toamasina',      'Secrétaire',   340003004, 'zo.rakotomalala@email.mg',      '2023-03-20', 'SECRETARY',      TRUE, TRUE),
    ('MEM-028', 'COL-003', 'Haja',      'Andriantsalama', '1986-08-30', 'MALE',   '5 Rue Rainitovo, Toamasina',         'Cultivateur',  340003005, 'haja.andriantsalama@email.mg',  '2023-03-20', 'SENIOR',         TRUE, TRUE),
    ('MEM-029', 'COL-003', 'Saholy',    'Rakotondravo',   '1994-01-14', 'FEMALE', '60 Bd Ratsimilaho, Toamasina',       'Agricultrice', 340003006, 'saholy.rakotondravo@email.mg',  '2023-03-20', 'SENIOR',         TRUE, TRUE),
    ('MEM-030', 'COL-003', 'Feno',      'Andriamasy',     '1992-04-22', 'MALE',   '18 Rue Ny Tanindrazana, Toamasina',  'Agriculteur',  340003007, 'feno.andriamasy@email.mg',      '2023-04-01', 'SENIOR',         TRUE, TRUE),
    ('MEM-031', 'COL-003', 'Kanto',     'Ramarolahy',     '1988-09-03', 'FEMALE', '9 Av Gallieni, Toamasina',           'Cultivatrice', 340003008, 'kanto.ramarolahy@email.mg',     '2023-04-15', 'SENIOR',         TRUE, TRUE),
    ('MEM-032', 'COL-003', 'Nivo',      'Razafimaharo',   '1997-12-28', 'MALE',   '43 Rue du Marché, Toamasina',        'Cultivateur',  340003009, 'nivo.razafimaharo@email.mg',    '2023-05-10', 'SENIOR',         TRUE, TRUE),
    ('MEM-033', 'COL-003', 'Rindra',    'Rakotozafy',     '2000-03-16', 'FEMALE', '25 Cité Tanambao, Toamasina',        'Étudiante',    340003010, 'rindra.rakotozafy@email.mg',    '2023-06-01', 'JUNIOR',         TRUE, TRUE),
    ('MEM-034', 'COL-003', 'Arj',       'Andriamandroso', '2002-07-09', 'MALE',   '7 Rue du Progrès, Toamasina',        'Étudiant',     340003011, 'arj.andriamandroso@email.mg',   '2023-07-15', 'JUNIOR',         TRUE, TRUE);

UPDATE collectivity SET
                        president_id      = 'MEM-001',
                        vice_president_id = 'MEM-002',
                        treasurer_id      = 'MEM-003',
                        secretary_id      = 'MEM-004'
WHERE id = 'COL-001';

UPDATE collectivity SET
                        president_id      = 'MEM-013',
                        vice_president_id = 'MEM-014',
                        treasurer_id      = 'MEM-015',
                        secretary_id      = 'MEM-016'
WHERE id = 'COL-002';

UPDATE collectivity SET
                        president_id      = 'MEM-024',
                        vice_president_id = 'MEM-025',
                        treasurer_id      = 'MEM-026',
                        secretary_id      = 'MEM-027'
WHERE id = 'COL-003';

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-010', 'MEM-005', 'Collègues'),
    ('MEM-010', 'MEM-006', 'Amis');

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-011', 'MEM-007', 'Famille'),
    ('MEM-011', 'MEM-017', 'Collègues');

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-012', 'MEM-008', 'Voisins'),
    ('MEM-012', 'MEM-009', 'Amis');

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-022', 'MEM-017', 'Amis'),
    ('MEM-022', 'MEM-018', 'Famille');

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-023', 'MEM-019', 'Collègues'),
    ('MEM-023', 'MEM-020', 'Amis');

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-033', 'MEM-028', 'Voisins'),
    ('MEM-033', 'MEM-029', 'Amis');

INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES
    ('MEM-034', 'MEM-030', 'Famille'),
    ('MEM-034', 'MEM-031', 'Voisins');

UPDATE collectivity SET annual_dues = 200000 WHERE id = 'COL-001';
UPDATE collectivity SET annual_dues = 150000 WHERE id = 'COL-002';
UPDATE collectivity SET annual_dues = 180000 WHERE id = 'COL-003';