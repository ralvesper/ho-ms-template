INSERT INTO greeting (id, message, created_at)
VALUES ('01900000-0000-7000-8000-000000000001', 'Hello, HighOnline!', now())
ON CONFLICT (id) DO NOTHING;
