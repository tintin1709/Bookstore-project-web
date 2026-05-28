DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS shipment;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS inventory_transaction;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS customer_order;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS wishlist_item;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS promotion;
DROP TABLE IF EXISTS coupon;
DROP TABLE IF EXISTS book_author;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(30),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_perm_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_perm_perm FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

CREATE TABLE address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    address_type VARCHAR(50) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    line1 VARCHAR(255) NOT NULL,
    ward VARCHAR(100),
    district VARCHAR(100),
    city VARCHAR(100) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE author (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    biography VARCHAR(1000),
    country VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    isbn13 VARCHAR(13) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1500),
    publisher VARCHAR(255),
    publication_year INT,
    language VARCHAR(50) DEFAULT 'English',
    list_price DECIMAL(12, 2) NOT NULL CHECK (list_price >= 0),
    stock_on_hand INT NOT NULL CHECK (stock_on_hand >= 0),
    reorder_level INT NOT NULL DEFAULT 5 CHECK (reorder_level >= 0),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT fk_book_created_by FOREIGN KEY (created_by) REFERENCES app_user(id),
    CONSTRAINT fk_book_updated_by FOREIGN KEY (updated_by) REFERENCES app_user(id)
);

CREATE TABLE book_author (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    author_order INT NOT NULL DEFAULT 1,
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_book_author_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    CONSTRAINT fk_book_author_author FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE,
    discount_type VARCHAR(50) NOT NULL CHECK (discount_type IN ('PERCENT','AMOUNT')),
    discount_value DECIMAL(12, 2) NOT NULL CHECK (discount_value > 0),
    min_order_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE promotion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    promotion_type VARCHAR(50) NOT NULL CHECK (promotion_type IN ('PERCENT','AMOUNT')),
    discount_value DECIMAL(12, 2) NOT NULL CHECK (discount_value > 0),
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_promotion_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

CREATE TABLE cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE cart_item (
    cart_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price_snapshot DECIMAL(12, 2) NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id, book_id),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_book FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE wishlist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE wishlist_item (
    wishlist_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (wishlist_id, book_id),
    CONSTRAINT fk_wishlist_item_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlist(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_item_book FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE reservation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(50) NOT NULL DEFAULT 'WAITING',
    reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    notified_at TIMESTAMP,
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_reservation_book FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE customer_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(100) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    payment_status VARCHAR(50) NOT NULL DEFAULT 'UNPAID',
    shipment_status VARCHAR(50) NOT NULL DEFAULT 'NOT_SHIPPED',
    subtotal DECIMAL(12, 2) NOT NULL DEFAULT 0,
    discount_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    grand_total DECIMAL(12, 2) NOT NULL CHECK (grand_total >= 0),
    shipping_address_snapshot VARCHAR(1000) NOT NULL,
    placed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_order_coupon FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(12, 2) NOT NULL CHECK (unit_price >= 0),
    final_line_total DECIMAL(12, 2) NOT NULL CHECK (final_line_total >= 0),
    title_snapshot VARCHAR(255) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_book FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    provider VARCHAR(100) NOT NULL,
    method VARCHAR(100) NOT NULL,
    provider_txn_id VARCHAR(255) NOT NULL UNIQUE,
    amount DECIMAL(12, 2) NOT NULL CHECK (amount >= 0),
    status VARCHAR(50) NOT NULL DEFAULT 'PAID',
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE
);

CREATE TABLE inventory_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    order_item_id BIGINT,
    reservation_id BIGINT,
    tx_type VARCHAR(50) NOT NULL,
    quantity_delta INT NOT NULL,
    balance_after INT NOT NULL CHECK (balance_after >= 0),
    note VARCHAR(500),
    occurred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_book FOREIGN KEY (book_id) REFERENCES book(id),
    CONSTRAINT fk_inventory_order_item FOREIGN KEY (order_item_id) REFERENCES order_item(id),
    CONSTRAINT fk_inventory_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);

CREATE TABLE shipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    carrier VARCHAR(255) NOT NULL,
    tracking_no VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    shipped_at TIMESTAMP,
    delivered_at TIMESTAMP,
    CONSTRAINT fk_shipment_order FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE
);

CREATE TABLE review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating SMALLINT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment VARCHAR(1000),
    status VARCHAR(50) NOT NULL DEFAULT 'PUBLISHED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    reservation_id BIGINT,
    channel VARCHAR(50) NOT NULL DEFAULT 'IN_APP',
    notification_type VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'UNREAD',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE SET NULL
);

CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    actor_user_id BIGINT,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    old_data VARCHAR(2000),
    new_data VARCHAR(2000),
    occurred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_user FOREIGN KEY (actor_user_id) REFERENCES app_user(id) ON DELETE SET NULL
);

CREATE INDEX idx_book_category ON book(category_id);
CREATE INDEX idx_book_title_status ON book(title, status);
CREATE INDEX idx_book_price_stock ON book(list_price, stock_on_hand);
CREATE INDEX idx_order_user_status ON customer_order(user_id, status);
CREATE INDEX idx_order_placed ON customer_order(placed_at);
CREATE INDEX idx_reservation_user_status ON reservation(user_id, status);
CREATE INDEX idx_notification_user_status ON notification(user_id, status);
CREATE INDEX idx_audit_entity ON audit_log(entity_type, entity_id);
