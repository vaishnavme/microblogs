CREATE OR REPLACE FUNCTION generate_alphanum_id(prefix TEXT, total_length INT)
RETURNS TEXT AS $$
DECLARE
    chars TEXT := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    remaining_length INT := total_length - length(prefix);
    result TEXT := prefix;
    i INT;
BEGIN
    IF remaining_length <= 0 THEN
        RAISE EXCEPTION 'Total length must be greater than prefix length';
    END IF;

    FOR i IN 1..remaining_length LOOP
        result := result || substr(chars, floor(random() * length(chars) + 1)::int, 1);
    END LOOP;

    RETURN result;
END;
$$ LANGUAGE plpgsql;
