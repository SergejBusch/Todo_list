CREATE TABLE items (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR NOT NULL,
                         description VARCHAR NOT NULL,
                         created timestamp,
                         done boolean
);

create table user_item (
                           id serial primary key,
                           email text unique not null,
                           password text not null
);

alter table items add column user_id int references user_item

