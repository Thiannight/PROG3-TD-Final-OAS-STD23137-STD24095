CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE');
CREATE TYPE occupation_enum AS ENUM ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

-- FIX: added missing columns agricultural_specialty, creation_date, federation_approval, annual_dues
-- These are used by the application (getCollectivityAnnualDues, CollectivityRepository.save, etc.)
-- and referenced in data.sql — their absence caused every collectivity INSERT to fail.
CREATE TABLE collectivity
(
    id                     VARCHAR(255) PRIMARY KEY,
    number                 VARCHAR(255) UNIQUE,
    name                   VARCHAR(255),
    location               VARCHAR(255),
    agricultural_specialty VARCHAR(255),
    creation_date          DATE,
    federation_approval    BOOLEAN      NOT NULL DEFAULT FALSE,
    annual_dues            BIGINT       NOT NULL DEFAULT 0,
    president_id           VARCHAR(255),
    vice_president_id      VARCHAR(255),
    treasurer_id           VARCHAR(255),
    secretary_id           VARCHAR(255)
);

CREATE TABLE member
(
    id                    VARCHAR(255) PRIMARY KEY,
    collectivity_id       VARCHAR(255)    NOT NULL,
    first_name            VARCHAR(255)    NOT NULL,
    last_name             VARCHAR(255)    NOT NULL,
    birth_date            DATE            NOT NULL,
    gender                gender_enum     NOT NULL,
    address               VARCHAR(255)    NOT NULL,
    profession            VARCHAR(255)    NOT NULL,
    phone_number          BIGINT          NOT NULL,
    email                 VARCHAR(255)    NOT NULL,
    adhesion_date         DATE            NOT NULL,
    occupation            occupation_enum NOT NULL,
    registration_fee_paid BOOLEAN DEFAULT FALSE,
    membership_dues_paid  BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (collectivity_id) REFERENCES collectivity (id)
);

ALTER TABLE collectivity
    ADD CONSTRAINT fk_president FOREIGN KEY (president_id) REFERENCES member (id),
    ADD CONSTRAINT fk_vice_president FOREIGN KEY (vice_president_id) REFERENCES member (id),
    ADD CONSTRAINT fk_treasurer FOREIGN KEY (treasurer_id) REFERENCES member (id),
    ADD CONSTRAINT fk_secretary FOREIGN KEY (secretary_id) REFERENCES member (id);

CREATE TABLE sponsorship
(
    candidate_id        VARCHAR(255) NOT NULL,
    sponsor_id          VARCHAR(255) NOT NULL,
    relationship_nature VARCHAR(255) NOT NULL,
    PRIMARY KEY (candidate_id, sponsor_id),
    FOREIGN KEY (candidate_id) REFERENCES member (id),
    FOREIGN KEY (sponsor_id) REFERENCES member (id)
);

CREATE TABLE member_collectivity
(
    member_id       VARCHAR(255)    NOT NULL,
    collectivity_id VARCHAR(255)    NOT NULL,
    occupation      occupation_enum NOT NULL,
    adhesion_date   DATE            NOT NULL,
    PRIMARY KEY (member_id, collectivity_id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (collectivity_id) REFERENCES collectivity (id)
);

-- ----------------------------------------------------------------

CREATE TYPE frequency_enum AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status_enum AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE payment_mode_enum AS ENUM ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');
CREATE TYPE mobile_banking_service_enum AS ENUM ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY');
CREATE TYPE bank_enum AS ENUM ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM');

CREATE TABLE cash_account
(
    id     VARCHAR(255) PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL DEFAULT 0
);

CREATE TABLE mobile_banking_account
(
    id                     VARCHAR(255) PRIMARY KEY,
    holder_name            VARCHAR(255)                NOT NULL,
    mobile_banking_service mobile_banking_service_enum NOT NULL,
    mobile_number          BIGINT                      NOT NULL,
    amount                 DOUBLE PRECISION            NOT NULL DEFAULT 0
);

CREATE TABLE bank_account
(
    id                  VARCHAR(255) PRIMARY KEY,
    holder_name         VARCHAR(255)     NOT NULL,
    bank_name           bank_enum        NOT NULL,
    bank_code           INT              NOT NULL,
    bank_branch_code    INT              NOT NULL,
    bank_account_number INT              NOT NULL,
    bank_account_key    INT              NOT NULL,
    amount              DOUBLE PRECISION NOT NULL DEFAULT 0
);

CREATE TABLE membership_fee
(
    id              VARCHAR(255) PRIMARY KEY,
    collectivity_id VARCHAR(255)         NOT NULL,
    eligible_from   DATE                 NOT NULL,
    frequency       frequency_enum       NOT NULL,
    amount          DOUBLE PRECISION     NOT NULL,
    label           VARCHAR(255),
    status          activity_status_enum NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (collectivity_id) REFERENCES collectivity (id)
);

CREATE TABLE collectivity_transaction
(
    id                  VARCHAR(255) PRIMARY KEY,
    collectivity_id     VARCHAR(255)      NOT NULL,
    creation_date       DATE              NOT NULL,
    amount              DOUBLE PRECISION  NOT NULL,
    payment_mode        payment_mode_enum NOT NULL,
    account_credited_id VARCHAR(255)      NOT NULL,
    member_debited_id   VARCHAR(255)      NOT NULL,
    FOREIGN KEY (collectivity_id) REFERENCES collectivity (id),
    FOREIGN KEY (member_debited_id) REFERENCES member (id)
);

CREATE TABLE member_payment
(
    id                  VARCHAR(255) PRIMARY KEY,
    member_id           VARCHAR(255)      NOT NULL,
    amount              BIGINT            NOT NULL,
    payment_mode        payment_mode_enum NOT NULL,
    account_credited_id VARCHAR(255)      NOT NULL,
    creation_date       DATE              NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE collectivity_account
(
    collectivity_id VARCHAR(255) NOT NULL,
    account_id      VARCHAR(255) NOT NULL,
    account_type    VARCHAR(50)  NOT NULL,
    PRIMARY KEY (collectivity_id, account_id),
    FOREIGN KEY (collectivity_id) REFERENCES collectivity (id)
);

-- ----------------------------------------------------------------

CREATE TYPE activity_type_enum AS ENUM ('MEETING', 'TRAINING', 'PUNCTUAL', 'OTHER');
CREATE TYPE attendance_status_enum AS ENUM ('MISSING', 'ATTENDED', 'UNDEFINED');
CREATE TYPE day_of_week_enum AS ENUM ('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU');

CREATE TABLE collectivity_activity
(
    id                          VARCHAR(255) PRIMARY KEY,
    collectivity_id             VARCHAR(255)       NOT NULL,
    label                       VARCHAR(255),
    activity_type               activity_type_enum NOT NULL,
    member_occupation_concerned TEXT,
    recurrence_week_ordinal     INT,
    recurrence_day_of_week      day_of_week_enum,
    executive_date              DATE,
    FOREIGN KEY (collectivity_id) REFERENCES collectivity (id),
    CONSTRAINT chk_recurrence_or_date CHECK (
        (recurrence_week_ordinal IS NOT NULL AND recurrence_day_of_week IS NOT NULL AND executive_date IS NULL)
            OR
        (recurrence_week_ordinal IS NULL AND recurrence_day_of_week IS NULL AND executive_date IS NOT NULL)
        )
);

-- BONUS : colonne activity_date ajoutée pour enregistrer la date réelle de chaque séance
CREATE TABLE activity_attendance
(
    id                VARCHAR(255) PRIMARY KEY,
    activity_id       VARCHAR(255)           NOT NULL,
    member_id         VARCHAR(255)           NOT NULL,
    attendance_status attendance_status_enum NOT NULL DEFAULT 'UNDEFINED',
    activity_date     DATE                   NOT NULL,
    UNIQUE (activity_id, member_id, activity_date),
    FOREIGN KEY (activity_id) REFERENCES collectivity_activity (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);