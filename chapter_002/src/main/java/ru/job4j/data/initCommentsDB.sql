CREATE TABLE IF NOT EXISTS comment (id INTEGER PRIMARY KEY AUTOINCREMENT, authorName VARCHAR(20) NOT NULL, createDate VARCHAR(20) NOT NULL, commentText VARCHAR(100), itemId INTEGER NOT NULL, FOREIGN KEY (itemId) REFERENCES item(id));