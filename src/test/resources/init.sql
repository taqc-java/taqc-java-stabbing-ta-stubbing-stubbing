create table if not exists categories (id bigserial primary key, avatar varchar not null, title varchar not null);
create table if not exists club (id bigserial primary key,title varchar not null,description varchar not null,image_url varchar,category_id int8 references categories(id), unique(title,category_id));
create table if not exists child (id bigserial primary key,first_name varchar not null,last_name varchar not null,birth_date date);
create table if not exists club_child(club_id int8 references club(id),child_id int8 references child(id));
truncate table categories, club_child, club, child restart identity cascade;
insert into categories (title, avatar) values ('Old Category', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII');
insert into categories (title, avatar) values ('For Delete Category', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII');
insert into categories (title, avatar) values ('Unique Category', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII');
insert into categories (title, avatar) values ('Second unique Category', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII');
insert into child (first_name, last_name) values ('First name child', 'Last Name child');
insert into child (first_name, last_name, birth_date) values ('First name 2 child', 'Last Name 2 child', '2000-01-01');
insert into child (first_name, last_name, birth_date) values ('For update with null dob', 'Last Name 2 child', '2020-01-01');
insert into child (first_name, last_name, birth_date) values ('For delete', 'Last Name 2 child', '2020-01-01');

