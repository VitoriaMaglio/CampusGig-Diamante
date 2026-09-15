INSERT INTO users (name, email, password_hash, role, created_at)
VALUES (
           'Admin CampusGigs',
           'admin@campusgigs.com',
           '$2b$10$BTwzeo5VONG5NmotE7Nna.ZaereJpVAesU4OmR.DJh2eoKo5QToua',
           'ADMIN',
           SYSTIMESTAMP
       );