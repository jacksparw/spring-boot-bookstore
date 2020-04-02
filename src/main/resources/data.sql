insert into Stock(BOOK_COUNT, VERSION) values(10, 0);
insert into Stock(BOOK_COUNT, VERSION) values(4, 0);
insert into Stock(BOOK_COUNT, VERSION) values(1, 0);
insert into Stock(BOOK_COUNT, VERSION) values(5, 0);
insert into Stock(BOOK_COUNT, VERSION) values(8, 0);
insert into Stock(BOOK_COUNT, VERSION) values(10, 0);

insert into Author(authorName, description, VERSION) values('Amish', 'Amish Tripathi. Amish Tripathi (born 18 October 1974) is an Indian diplomat, columnist and author', 0);

insert into book (isbn,title,author,price,stock_id, VERSION) values(1,'The Immortals of Meluha',1,200.50, 1, 0);

insert into book (isbn,title,author,price,stock_id, VERSION) values(2,'The Secret of the Nagas',1,177.80,2, 0);

insert into book (isbn,title,author,price,stock_id, VERSION) values(3,'The Oath of the Vayuputras',1,200.50,3, 0);

insert into Author(authorName, description, VERSION) values('Josh Long', 'Josh (@starbuxman) is the Spring Developer Advocate at Pivotal, an open-source hacker, books/video author and speaker', 0);

insert into book (isbn,title,author,price,stock_id, VERSION) values(5,'Cloud Native Java',2,520.80,4, 0);

insert into book (isbn,title,author,price,stock_id, VERSION) values(6,'Spring Enterprise Recipes',2,653.10,5, 0);

insert into book (isbn,title,author,price,stock_id, VERSION) values(7,'Pro Spring Integration',2,522.60,6, 0);

insert into customer(CUSTOMER_NAME, VERSION) values ('Tim', 0);