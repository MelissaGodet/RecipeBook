delete from recipe;
delete from recipeIngredient;

insert into recipe (title, instructions, preparationTime, difficulty)
    values ('Chocolat Chip Cookies',
            '1. In a bowl, combine the flour, sugar and softened butter.\n2. Add the eggs and vanilla, then mix well.\n3. Stir in the chocolate paste.\n4. Bake for 10 to 12 minutes.\n7. Leave to cool before eating.',
            30,
            'EASY');

insert into recipe (title, instructions, preparationTime, difficulty)
    values ( 'Chicken Stir-Fry',
            '1. In a skillet, cook the chicken cut into pieces until golden brown.\n2. Add the chopped onion and garlic\n3. Add the vegetables.\n4. Season to taste.\n5. Serve with rice.',
            40, 'MEDIUM');

insert into recipe (title, instructions, preparationTime, difficulty)
    values ('Spinach Salad',
            '1. Wash the spinach and tomatoes, then cut them into pieces.\n2. In a salad bowl, mix the spinach, tomatoes and cheese.\n3. Season with olive oil and balsamic vinegar.\n4. Mix well and serve chilled.',
            15, 'EASY');

insert into recipeIngredient(recipeId, ingredient)
    values
        (1,'FLOUR'),
        (1,'SUGAR'),
        (1,'EGGS'),
        (1,'BUTTER'),
        (1,'VANILLA_EXTRACT'),
        (1,'CHOCOLATE_CHIPS'),
        (2,'CHICKEN'),
        (2,'BROCCOLI'),
        (2,'CARROT'),
        (2,'ONION'),
        (2,'GARLIC'),
        (2,'SALT'),
        (2,'PEPPER'),
        (3,'SPINACH'),
        (3,'TOMATOES'),
        (3,'OLIVE_OIL'),
        (3,'BALSAMIC_VINEGAR');