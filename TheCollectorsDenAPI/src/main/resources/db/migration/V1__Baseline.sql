-------------------------------------------------------------------
-- 1) Users
-------------------------------------------------------------------
CREATE TABLE users
(
    user_id             INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name                VARCHAR(255)        NOT NULL,
    email               VARCHAR(255) UNIQUE NOT NULL,
    encrypted_password  VARCHAR(255)        NOT NULL,
    country             VARCHAR(100),
    address             VARCHAR(255),
    city                VARCHAR(100),
    postal_code         VARCHAR(20),
    profile_picture_url VARCHAR(255),
    role                VARCHAR(50) DEFAULT 'buyer',
    created_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status              VARCHAR(50) DEFAULT 'active',
    phone_number        VARCHAR(20),
    preferences         TEXT, -- JSON String
    last_login          TIMESTAMP
);


-------------------------------------------------------------------
-- 2) Orders
-------------------------------------------------------------------
CREATE TABLE orders
(
    order_id    INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    buyer_id    INT            NOT NULL,       -- link to users (buyer)
    seller_id   INT            NOT NULL,       -- link to users (seller)
    total_price DECIMAL(10, 2) NOT NULL,       -- total cost (product + shipping)
    address     VARCHAR(255),                  -- shipping address
    city        VARCHAR(100),
    postal_code VARCHAR(20),
    country     VARCHAR(100),
    status      VARCHAR(50) DEFAULT 'pending', -- (pending, shipped, delivered, canceled)
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES users (user_id),
    FOREIGN KEY (seller_id) REFERENCES users (user_id)
);


-------------------------------------------------------------------
-- 3) Products
-------------------------------------------------------------------
CREATE TABLE products
(
    product_id     INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    vendor_id      INT          NOT NULL,
    order_id       INT,
    name           VARCHAR(255) NOT NULL,
    description    TEXT,
    category       VARCHAR(100),
    starting_price DECIMAL(10, 2),                  -- initial bid price
    buy_now_price  DECIMAL(10, 2),                  -- instant purchase price
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status         VARCHAR(50) DEFAULT 'available', -- (available, sold, withdrawn),
    FOREIGN KEY (vendor_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

-------------------------------------------------------------------
-- 4) Category
-------------------------------------------------------------------
CREATE TABLE category
(
    category_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

-------------------------------------------------------------------
-- 5) Products Category (many-to-many link table)
-------------------------------------------------------------------
CREATE TABLE products_category
(
    product_id  INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id),
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

-------------------------------------------------------------------
-- 6) Bids
-------------------------------------------------------------------
CREATE TABLE bids
(
    bid_id     INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    product_id INT            NOT NULL, -- Associated product
    user_id    INT            NOT NULL, -- Bidder
    bid_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-------------------------------------------------------------------
-- 7) Reviews
-------------------------------------------------------------------
CREATE TABLE reviews
(
    review_id        INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    reviewer_id      INT NOT NULL, -- who leaves the review
    reviewed_user_id INT NOT NULL, -- who is being reviewed
    rating           INT CHECK (rating BETWEEN 1 AND 5),
    comment          TEXT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reviewer_id) REFERENCES users (user_id),
    FOREIGN KEY (reviewed_user_id) REFERENCES users (user_id)
);


-------------------------------------------------------------------
-- 8) Payments
-------------------------------------------------------------------
CREATE TABLE payments
(
    payment_id     INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_id       INT            NOT NULL,         -- linked order
    amount         DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),                     -- (credit_card, PayPal, etc.)
    status         VARCHAR(50) DEFAULT 'completed', -- (completed, failed, pending)
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    UNIQUE (order_id)
);

-------------------------------------------------------------------
-- 9) Notifications
-------------------------------------------------------------------
CREATE TABLE notifications
(
    notification_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id         INT  NOT NULL, -- recipient
    message         TEXT NOT NULL,
    is_read         BOOLEAN   DEFAULT FALSE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-------------------------------------------------------------------
-- 10) Forum Posts
-------------------------------------------------------------------
CREATE TABLE forum_posts
(
    post_id    INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id  INT          NOT NULL,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users (user_id)
);

-------------------------------------------------------------------
-- 11) Forum Comments
-------------------------------------------------------------------
CREATE TABLE forum_comments
(
    comment_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id  INT  NOT NULL,
    post_id    INT  NOT NULL,
    content    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users (user_id),
    FOREIGN KEY (post_id) REFERENCES forum_posts (post_id)
);
