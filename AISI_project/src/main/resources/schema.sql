create table if not exists recipe (
    id identity,
    title varchar(100) not null,
    instructions varchar(500) not null,
    preparationTime int not null,
    difficulty varchar(100) not null,
    primary key (id)
);

create table if not exists recipeIngredient(
    recipeId int not null,
    ingredient varchar(100),
    primary key(recipeId, ingredient),
    foreign key(recipeId) references recipe(id)
);


create table if not exists `user` (
    id identity,
    username varchar(100) not null unique,
    password varchar(1000) not null
    );

create table if not exists authority (
    id   identity,
    authority_name varchar(100) not null unique
    );

create table if not exists user_authority (
    user_id      bigint not null,
    authority_id bigint not null,
    constraint fk_user foreign key (user_id) references user(id),
    constraint fk_authority foreign key (authority_id) references authority(id)
    );