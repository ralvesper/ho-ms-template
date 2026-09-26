CREATE TABLE greeting (
    id BIGINT PRIMARY KEY,
    trace_id UUID NOT NULL,
    message VARCHAR(255) NOT NULL,
    recipient_email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
