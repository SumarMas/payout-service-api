-- =============================================================
-- Database: payout_service
-- Description: Handles payout requests from NGOs
-- =============================================================

CREATE DATABASE IF NOT EXISTS payout_service CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE payout_service;

-- =============================================================
-- Table: payout_requests
-- =============================================================
CREATE TABLE payout_requests (
                                 payout_request_id BINARY(16) NOT NULL,
                                 organization_id BINARY(16) NOT NULL,
                                 total_amount DECIMAL(11,2) NOT NULL DEFAULT 0.00,
                                 status ENUM('PENDING', 'APPROVED') NOT NULL DEFAULT 'PENDING',
                                 proof_file_id BINARY(16) DEFAULT NULL,
                                 request_datetime DATETIME NOT NULL DEFAULT NOW(),
                                 approval_datetime DATETIME DEFAULT NULL,
                                 created_datetime DATETIME NOT NULL,
                                 created_user BINARY(16) NOT NULL,
                                 last_updated_datetime DATETIME NOT NULL,
                                 last_updated_user BINARY(16) NOT NULL,
                                 enabled BOOLEAN DEFAULT TRUE,
                                 PRIMARY KEY (payout_request_id)
) ENGINE=InnoDB;

-- =============================================================
-- Table: payout_request_donations
-- =============================================================
CREATE TABLE payout_request_donations (
                                          payout_request_id BINARY(16) NOT NULL,
                                          donation_id BINARY(16) NOT NULL,
                                          campaign_id BINARY(16) NOT NULL,
                                          amount DECIMAL(11,2) NOT NULL,
                                          created_datetime DATETIME NOT NULL,
                                          created_user BINARY(16) NOT NULL,
                                          last_updated_datetime DATETIME NOT NULL,
                                          last_updated_user BINARY(16) NOT NULL,
                                          enabled BOOLEAN DEFAULT TRUE,
                                          PRIMARY KEY (payout_request_id, donation_id),
                                          CONSTRAINT fk_payout_request FOREIGN KEY (payout_request_id) REFERENCES payout_requests(payout_request_id)
) ENGINE=InnoDB;

-- =============================================================
-- Table: payout_requests_audit
-- =============================================================
CREATE TABLE payout_requests_audit (
                                       payout_request_id BINARY(16) NOT NULL,
                                       organization_id BINARY(16) NOT NULL,
                                       total_amount DECIMAL(11,2) NOT NULL,
                                       status ENUM('PENDING', 'APPROVED') NOT NULL,
                                       proof_file_id BINARY(16),
                                       request_datetime DATETIME NOT NULL,
                                       approval_datetime DATETIME,
                                       created_datetime DATETIME NOT NULL,
                                       created_user BINARY(16) NOT NULL,
                                       last_updated_datetime DATETIME NOT NULL,
                                       last_updated_user BINARY(16) NOT NULL,
                                       enabled BOOLEAN DEFAULT TRUE,
                                       version INT NOT NULL,
                                       PRIMARY KEY (payout_request_id, version)
) ENGINE=InnoDB;

-- =============================================================
-- Table: payout_request_donations_audit
-- =============================================================
CREATE TABLE payout_request_donations_audit (
                                                payout_request_id BINARY(16) NOT NULL,
                                                donation_id BINARY(16) NOT NULL,
                                                campaign_id BINARY(16) NOT NULL,
                                                amount DECIMAL(11,2) NOT NULL,
                                                created_datetime DATETIME NOT NULL,
                                                created_user BINARY(16) NOT NULL,
                                                last_updated_datetime DATETIME NOT NULL,
                                                last_updated_user BINARY(16) NOT NULL,
                                                enabled BOOLEAN DEFAULT TRUE,
                                                version INT NOT NULL,
                                                PRIMARY KEY (payout_request_id, donation_id, version)
) ENGINE=InnoDB;

-- =============================================================
-- Triggers for payout_requests
-- =============================================================

DELIMITER $$

CREATE TRIGGER after_insert_payout_requests
    AFTER INSERT ON payout_requests
    FOR EACH ROW
BEGIN
    INSERT INTO payout_requests_audit (
        payout_request_id, organization_id, total_amount, status, proof_file_id,
        request_datetime, approval_datetime,
        created_datetime, created_user, last_updated_datetime, last_updated_user, enabled, version
    )
    VALUES (
               NEW.payout_request_id, NEW.organization_id, NEW.total_amount, NEW.status, NEW.proof_file_id,
               NEW.request_datetime, NEW.approval_datetime,
               NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user, NEW.enabled, 1
           );
    END$$

    CREATE TRIGGER before_update_payout_requests
        BEFORE UPDATE ON payout_requests
        FOR EACH ROW
    BEGIN
        DECLARE last_version INT;
    SET NEW.last_updated_datetime = NOW();
    SET NEW.created_user = OLD.created_user;

        SELECT COALESCE(MAX(version), 0) INTO last_version
        FROM payout_requests_audit
        WHERE payout_request_id = NEW.payout_request_id;

        INSERT INTO payout_requests_audit (
            payout_request_id, organization_id, total_amount, status, proof_file_id,
            request_datetime, approval_datetime,
            created_datetime, created_user, last_updated_datetime, last_updated_user, enabled, version
        )
        VALUES (
                   NEW.payout_request_id, NEW.organization_id, NEW.total_amount, NEW.status, NEW.proof_file_id,
                   NEW.request_datetime, NEW.approval_datetime,
                   NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user, NEW.enabled, last_version + 1
               );
        END$$

        -- =============================================================
-- Triggers for payout_request_donations
-- =============================================================

        CREATE TRIGGER after_insert_payout_request_donations
            AFTER INSERT ON payout_request_donations
            FOR EACH ROW
        BEGIN
            INSERT INTO payout_request_donations_audit (
                payout_request_id, donation_id, campaign_id,amount,
                created_datetime, created_user, last_updated_datetime, last_updated_user, enabled, version
            )
            VALUES (
                       NEW.payout_request_id, NEW.donation_id, NEW.campaign_id, NEW.amount,
                       NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user, NEW.enabled, 1
                   );
            END$$

            CREATE TRIGGER before_update_payout_request_donations
                BEFORE UPDATE ON payout_request_donations
                FOR EACH ROW
            BEGIN
                DECLARE last_version INT;
    SET NEW.last_updated_datetime = NOW();
    SET NEW.created_user = OLD.created_user;

                SELECT COALESCE(MAX(version), 0) INTO last_version
                FROM payout_request_donations_audit
                WHERE payout_request_id = NEW.payout_request_id AND donation_id = NEW.donation_id;

                INSERT INTO payout_request_donations_audit (
                    payout_request_id, donation_id, campaign_id, amount,
                    created_datetime, created_user, last_updated_datetime, last_updated_user, enabled, version
                )
                VALUES (
                           NEW.payout_request_id, NEW.donation_id, NEW.campaign_id, NEW.amount,
                           NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user, NEW.enabled, last_version + 1
                       );
                END$$

                DELIMITER ;
