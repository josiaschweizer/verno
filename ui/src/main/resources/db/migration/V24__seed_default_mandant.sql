INSERT INTO mandants (id, slug)
VALUES (7777, 'default')
ON CONFLICT (id) DO NOTHING;