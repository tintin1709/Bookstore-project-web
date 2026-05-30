package com.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;

    public DatabaseSeeder(JdbcTemplate jdbc, PasswordEncoder encoder) {
        this.jdbc = jdbc;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM role", Integer.class);
        if (count != null && count > 0)
            return;
        seedRoles();
        seedUsers();
        seedCatalog();
        seedCoupons();
        seedOrdersAndReservations();
    }

    private void seedRoles() {
        for (String[] r : List.of(new String[] { "ADMIN", "Administrator" }, new String[] { "MANAGER", "Manager" },
                new String[] { "STAFF", "Staff" }, new String[] { "CUSTOMER", "Customer" })) {
            jdbc.update("INSERT INTO role(code,name) VALUES(?,?)", r[0], r[1]);
        }
        for (String p : List.of("BOOK_MANAGE", "ORDER_MANAGE", "USER_MANAGE", "REPORT_VIEW", "CUSTOMER_BUY"))
            jdbc.update("INSERT INTO permission(code,name) VALUES(?,?)", p, p.replace('_', ' '));
    }

    private void createUser(String email, String pass, String name, String phone, String role) {
        jdbc.update("INSERT INTO app_user(email,password_hash,full_name,phone,status,email_verified) VALUES(?,?,?,?,?,?)",
                email, encoder.encode(pass), name, phone, "ACTIVE", true);

        Long uid = jdbc.queryForObject("SELECT id FROM app_user WHERE email=?", Long.class, email);
        Long rid = jdbc.queryForObject("SELECT id FROM role WHERE code=?", Long.class, role);

        jdbc.update("INSERT INTO user_role(user_id,role_id) VALUES(?,?)", uid, rid);
        jdbc.update("INSERT INTO address(user_id,address_type,recipient_name,phone,line1,ward,district,city,is_default) VALUES(?,?,?,?,?,?,?,?,?)",
                uid, "HOME", name, phone, "123 Nguyen Van Linh", "Tan Phong", "District 7", "Ho Chi Minh City", true);
    }

    private void seedUsers() {
        createUser("admin@test.com", "Admin123!", "System Admin", "0900000001", "ADMIN");
        createUser("manager@test.com", "Manager123!", "Bookstore Manager", "0900000002", "MANAGER");
        createUser("staff@test.com", "Staff123!", "Store Staff", "0900000003", "STAFF");
        createUser("customer@test.com", "Customer123!", "Nguyen Thai Bao", "0900000004", "CUSTOMER");
        for (int i = 1; i <= 24; i++)
            createUser("customer" + i + "@test.com", "Customer123!", "Customer " + i,
                    "09123" + String.format("%05d", i), "CUSTOMER");
    }

    private void seedCatalog() {
        String[] cats = {"Programming","Database","Web Development","AI & Data","Business","English Learning","Novel","Design"};
        for (String c: cats){
             jdbc.update("INSERT INTO category(name,slug,description,status) VALUES(?,?,?,?)", c, c.toLowerCase().replace(" & ","-").replace(" ","-"), "Books about " + c, "ACTIVE");
        }

        String[] authors = {"Robert Martin","Martin Fowler","Joshua Bloch","Andrew Hunt","Thomas Cormen","Eric Freeman","Kathy Sierra","James Gosling","Donald Knuth","Bjarne Stroustrup","Brian Kernighan","Steve McConnell"};
        for (String a: authors) {
            jdbc.update("INSERT INTO author(name,slug,biography,country,status) VALUES(?,?,?,?,?)", a, a.toLowerCase().replace(" ","-"), "Well-known author in technology and education.", "International", "ACTIVE");
        }
        
        String[][] books = {
            {"Clean Code","Clean Code - Writing Code for Humans","Robert Martin","/images/books/clean-code.jpg"},
            {"Refactoring","Improving the Design of Existing Code","Martin Fowler","/images/books/refactoring.jpg"},
            {"Effective Java","Essential Techniques for Java Programmers","Joshua Bloch","/images/books/effective-java.jpg"},
            {"The Pragmatic Programmer","From Journeyman to Master","Andrew Hunt","/images/books/pragmatic.jpg"},
            {"Introduction to Algorithms","The Complete Reference","Thomas Cormen","/images/books/algorithms.jpg"},
            {"Head First Design Patterns","Design Patterns Simplified","Eric Freeman","/images/books/design-patterns.jpg"},
            {"Java Concurrency","Writing Thread-Safe Code","Brian Kernighan","/images/books/concurrency.jpg"},
            {"Code Complete","A Practical Handbook of Software Construction","Steve McConnell","/images/books/code-complete.jpg"},
            {"The C Programming Language","The Definitive Guide","Brian Kernighan","/images/books/c-language.jpg"},
            {"The Art of Computer Programming","Fundamental Algorithms Volume 1","Donald Knuth","/images/books/taocp.jpg"},
            {"Database Design","From Theory to Practice","Thomas Cormen","/images/books/db-design.jpg"},
            {"SQL Performance","Query Optimization Techniques","Martin Fowler","/images/books/sql-perf.jpg"},
            {"MongoDB Guide","Document Database Mastery","Joshua Bloch","/images/books/mongodb.jpg"},
            {"Web Design","Modern Techniques and Best Practices","Eric Freeman","/images/books/web-design.jpg"},
            {"React in Action","Advanced Component Patterns","Andrew Hunt","/images/books/react.jpg"},
            {"Node.js Design","Building Scalable Applications","Steve McConnell","/images/books/nodejs.jpg"},
            {"Angular Mastery","Complete Framework Guide","Kathy Sierra","/images/books/angular.jpg"},
            {"Vue.js Essentials","Progressive JavaScript Framework","James Gosling","/images/books/vuejs.jpg"},
            {"Machine Learning","From Theory to Practice","Thomas Cormen","/images/books/ml.jpg"},
            {"Deep Learning","Neural Networks and Beyond","Bjarne Stroustrup","/images/books/deeplearning.jpg"},
        };
        
        Random random = new Random(7);
        for (int i = 0; i < books.length; i++) {
            String title = books[i][0];
            String desc = books[i][1];
            String author = books[i][2];
            String imageUrl = books[i][3];
            
            long catId = 1 + random.nextInt(cats.length);
            BigDecimal price = BigDecimal.valueOf(15 + random.nextInt(50)).multiply(BigDecimal.valueOf(10000));
            int stock = 5 + random.nextInt(30);
            
            jdbc.update("INSERT INTO book(category_id,sku,isbn13,title,description,publisher,publication_year,language,list_price,stock_on_hand,reorder_level,status,image_url,created_by,updated_by) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    catId, "SKU-"+String.format("%04d",i+1), "978"+String.format("%010d",i+1), title, desc, "IU Press", 2015 + random.nextInt(11), "English", price, stock, 5, "ACTIVE", imageUrl, 1, 1);
            
            long bookId = jdbc.queryForObject("SELECT id FROM book WHERE sku=?", Long.class, "SKU-"+String.format("%04d",i+1));
            long authorId = 0;
            for(int j = 0; j < authors.length; j++){
                if(authors[j].equals(author)){
                    authorId = j + 1;
                    break;
                }
            }
            if(authorId > 0) {
                jdbc.update("INSERT INTO book_author(book_id,author_id,author_order) VALUES(?,?,?)", bookId, authorId, 1);
            }
        }
    }

    private void seedCoupons() {
        jdbc.update(
                "INSERT INTO coupon(code,discount_type,discount_value,min_order_amount,starts_at,ends_at,status) VALUES(?,?,?,?,?,?,?)",
                "STUDENT10", "PERCENT", new BigDecimal("10"), new BigDecimal("100000"),
                LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(60), "ACTIVE");
        jdbc.update(
                "INSERT INTO coupon(code,discount_type,discount_value,min_order_amount,starts_at,ends_at,status) VALUES(?,?,?,?,?,?,?)",
                "WELCOME50", "AMOUNT", new BigDecimal("50000"), new BigDecimal("200000"),
                LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(60), "ACTIVE");
    }

    private void seedOrdersAndReservations() {
        Long customer = jdbc.queryForObject("SELECT id FROM app_user WHERE email='customer@test.com'", Long.class);
        jdbc.update("INSERT INTO reservation(user_id,book_id,quantity,status,expires_at) VALUES(?,?,?,?,DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY))", customer, 1, 1, "WAITING");
        
        jdbc.update(
                "INSERT INTO notification(user_id,reservation_id,notification_type,title,message) VALUES(?,?,?,?,?)", customer, 1, "WELCOME", "Welcome to IU Bookstore",
                "Your account is ready. You can browse books, place orders, and reserve unavailable items.");
        
        jdbc.update("INSERT INTO audit_log(actor_user_id,entity_type,entity_id,action,new_data) VALUES(?,?,?,?,?)", 1, "SYSTEM", 1, "SEED", "Initial demo data generated");
    }
}
