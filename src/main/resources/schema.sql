CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE');
CREATE TYPE occupation_enum AS ENUM ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

CREATE TABLE collectivity (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    location VARCHAR(255) NOT NULL,
    agricultural_specialty VARCHAR(255) NOT NULL,
    creation_date DATE NOT NULL,
    federation_approval BOOLEAN DEFAULT FALSE,
    president_id VARCHAR(255),
    vice_president_id VARCHAR(255),
    treasurer_id VARCHAR(255),
    secretary_id VARCHAR(255)
);

CREATE TABLE member (
    id VARCHAR(255) PRIMARY KEY,
    collectivity_id VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender gender_enum NOT NULL,
    address VARCHAR(255) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    phone_number BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    adhesion_date DATE NOT NULL,
    occupation occupation_enum NOT NULL,
    registration_fee_paid BOOLEAN DEFAULT FALSE,
    membership_dues_paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (collectivity_id) REFERENCES collectivity(id)
);

ALTER TABLE collectivity
    ADD CONSTRAINT fk_president FOREIGN KEY (president_id) REFERENCES member(id),
    ADD CONSTRAINT fk_vice_president FOREIGN KEY (vice_president_id) REFERENCES member(id),
    ADD CONSTRAINT fk_treasurer FOREIGN KEY (treasurer_id) REFERENCES member(id),
    ADD CONSTRAINT fk_secretary FOREIGN KEY (secretary_id) REFERENCES member(id);

CREATE TABLE sponsorship (
     candidate_id VARCHAR(255) NOT NULL,
     sponsor_id VARCHAR(255) NOT NULL,
     relationship_nature VARCHAR(255) NOT NULL,
     PRIMARY KEY (candidate_id, sponsor_id),
     FOREIGN KEY (candidate_id) REFERENCES member(id),
     FOREIGN KEY (sponsor_id) REFERENCES member(id)
);

ALTER TABLE collectivity ADD COLUMN annual_dues BIGINT NOT NULL DEFAULT 0;