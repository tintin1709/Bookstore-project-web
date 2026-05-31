# API / Route Documentation

This project is mainly server-rendered MVC. The important routes are:

| Method | Route | Role | Purpose |
|---|---|---|---|
| GET | `/catalog` | Public | Search, filter, sort, paginate books |
| POST | `/register` | Public | Register customer account |
| POST | `/cart/add/{bookId}` | Authenticated | Add book to session cart |
| POST | `/cart/checkout` | Authenticated | Validate stock, apply coupon, create order |
| GET | `/orders` | Authenticated | View customer orders |
| POST | `/reservations/book/{bookId}` | Authenticated | Submit reservation request |
| GET | `/manager/books` | Staff/Manager/Admin | Manage books with filters |
| POST | `/manager/books` | Staff/Manager/Admin | Create or update book |
| POST | `/manager/books/{id}/delete` | Staff/Manager/Admin | Soft delete book |
| GET | `/admin/users` | Admin | User management |
| POST | `/admin/users/{id}` | Admin | Change user role/status |
