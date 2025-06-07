CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
INSERT INTO role(id, name) VALUES (uuid_generate_v4(), 'USER') ON CONFLICT DO NOTHING;
INSERT INTO role(id, name) VALUES (uuid_generate_v4(), 'ADMIN') ON CONFLICT DO NOTHING;