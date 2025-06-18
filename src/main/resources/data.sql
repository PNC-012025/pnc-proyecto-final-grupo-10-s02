CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
INSERT INTO role(id, name) VALUES (uuid_generate_v4(), 'USER') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name) VALUES (uuid_generate_v4(), 'ADMIN') ON CONFLICT DO NOTHING;

-- 2. Insertar usuario ADMIN si no existe
INSERT INTO user_data (
    id, username, first_name, last_name, dui, email, hashed_password, active
)
SELECT uuid_generate_v4(), 'adminuser', 'Admin', 'User', '12345678-9',
       'admin@example.com', '$2a$10$NwrGbR2aAbuZMX0QWT0sQOhWCD8Ebh1h8L0jxejx0.gCdDw0w4iPC', true
    WHERE NOT EXISTS (
    SELECT 1 FROM user_data WHERE username = 'adminuser'
);

-- 3. Insertar relación entre usuario y rol si no existe
INSERT INTO user_x_role (user_id, role_id)
SELECT u.id, r.id
FROM user_data u, role r
WHERE u.username = 'adminuser' AND r.name = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM user_x_role WHERE user_id = u.id AND role_id = r.id
);