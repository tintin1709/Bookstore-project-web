# Design Notes

The original ERD uses a bookstore domain with user, role, book, category, author, cart, order, payment, shipment, reservation, notification, and audit log tables.

Small implementation adjustments:

- `user` was renamed to `app_user` because `USER` is a reserved keyword in many SQL engines.
- `order` was renamed to `customer_order` because `ORDER` is also a SQL keyword.
- JSON columns were represented as `VARCHAR` in the runnable demo to keep H2/MySQL compatibility.
- Soft delete is implemented with `status='INACTIVE'` for books and users.
- Many-to-many relationships are implemented through `user_role`, `role_permission`, `book_author`, and `wishlist_item`.
