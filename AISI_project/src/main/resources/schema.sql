create table if not exists recipe (
    id identity,
    title varchar(100) not null unique,
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
)