-- A generic trigger function that sets the UpdatedAt column to NOW()
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW."updated_at" = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_update_Users_timestamp
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER trg_update_Products_timestamp
    BEFORE UPDATE ON products
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER trg_update_Orders_timestamp
    BEFORE UPDATE ON orders
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER trg_update_ForumPosts_timestamp
    BEFORE UPDATE ON forum_posts
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER trg_update_ForumComments_timestamp
    BEFORE UPDATE ON forum_comments
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();
