# --- !Ups

create table subscriptions (
    id int serial primary key,
    email text not null,
    created_at timestamp default now()
);


# --- !Downs

drop table subscriptions;
