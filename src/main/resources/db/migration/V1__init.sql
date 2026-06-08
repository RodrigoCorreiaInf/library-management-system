CREATE TABLE books
(
    isbn      VARCHAR(255) PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    author    VARCHAR(255) NOT NULL,
    available BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE book_histories
(
    id            UUID PRIMARY KEY,
    book_isbn     VARCHAR(255) NOT NULL,
    borrowed_by   VARCHAR(255) NOT NULL,
    borrowed_at   TIMESTAMP    NOT NULL,
    returned_at   TIMESTAMP,
    returned_late BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_book FOREIGN KEY (book_isbn) REFERENCES books (isbn) ON DELETE CASCADE
);

CREATE TABLE users
(
    id       UUID PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    name     VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);