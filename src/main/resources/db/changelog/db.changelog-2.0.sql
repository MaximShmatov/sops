CREATE OR REPLACE FUNCTION set_orders_updated_date() RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_date := now();
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER order_update_trigger
    BEFORE UPDATE
    ON orders
    FOR EACH STATEMENT
EXECUTE FUNCTION set_orders_updated_date();
