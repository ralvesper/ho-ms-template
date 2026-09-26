INSERT INTO greeting (id, trace_id, message, recipient_email, created_at)
VALUES (1, '01900000-0000-7000-8000-000000000001', 'Hello, HighOnline!', 'hello@highonline.com', now())
ON CONFLICT (id) DO NOTHING;
