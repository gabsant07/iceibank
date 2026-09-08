INSERT INTO agencies (id, name, base_url) SELECT 0, 'Agency 0', 'http://localhost:8080' WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE id = 0);
INSERT INTO agencies (id, name, base_url) SELECT 1, 'Agency 1', 'http://localhost:8081' WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE id = 1);
INSERT INTO agencies (id, name, base_url) SELECT 2, 'Agency 2', 'http://localhost:8082' WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE id = 2);
