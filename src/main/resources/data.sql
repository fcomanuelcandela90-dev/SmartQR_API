INSERT INTO users (name, email, password, role)
VALUES
('SmartQR Admin', 'admin@smartqr.com', '{bcrypt}$2a$10$SGouXeRL3GEpOh7d.Z20BehTI6kEbhEEFuHPo8KT7ZAOb5B5X.I8u', 'ADMIN'),
('SmartQR Employee', 'employee@smartqr.com', '{bcrypt}$2a$10$kn9ccmVTZjraeaDRtrHqH.yu49uKg4Qkam984tl1xop68atjCiedK', 'EMPLOYEE');

-- ===================================================================
--                              BEBIDAS
-- ===================================================================
INSERT INTO products (name, description, price, available, category, image_url)
VALUES
    ('Jarra de Cerveza', 'Cerveza de barril helada en jarra de cristal', 2.00, TRUE, 'DRINK', 'http://images.com/beer.png'),
    ('Copa de Cerveza', 'Cerveza de barril clasica en copa tirada', 1.50, TRUE, 'DRINK', 'http://images.com/copa_beer.png'),
    ('Tinto de Verano', 'Combinado tradicional con rodaja de limon', 2.20, TRUE, 'DRINK', 'http://images.com/tinto.png'),
    ('Refresco de Cola', 'Bebida refrescante de cola sin azucares', 1.80, TRUE, 'DRINK', 'http://images.com/cola.png'),
    ('Refresco de Naranja', 'Bebida refrescante con gas sabor naranja', 1.80, TRUE, 'DRINK', 'http://images.com/orange.png'),
    ('Agua Mineral', 'Agua mineral natural sin gas embotellada', 1.20, TRUE, 'DRINK', 'http://images.com/agua.png'),
    ('Zumo de Pina', 'Zumo de fruta natural embotellado', 1.90, TRUE, 'DRINK', 'http://images.com/pina.png'),
    ('Agua con Gas', 'Agua mineral con gas refrescante', 1.40, TRUE, 'DRINK', 'http://images.com/aguagas.png'),
    ('Monster Energy', 'Bebida energetica estimulante', 2.50, TRUE, 'DRINK', 'http://images.com/monster.png'),
    ('Cafe Solo', 'Cafe espresso intenso recien molido', 1.10, TRUE, 'DRINK', 'http://images.com/cafe.png');

-- ===================================================================
--                      APERITIVOS Y ENSALADAS
-- ===================================================================
INSERT INTO products (name, description, price, available, category, image_url)
VALUES
    ('Patatas Bravas', 'Crujientes patatas con nuestra salsa brava casera picante', 3.50, TRUE, 'APPETIZER', 'http://images.com/bravas.png'),
    ('Aros de Cebolla', 'Anillos de cebolla rebozados acompanados de salsa barbacoa', 2.90, TRUE, 'APPETIZER', 'http://images.com/aros.png'),
    ('Nachos con Queso', 'Tortillas de maiz con queso fundido y jalapenos', 4.20, TRUE, 'APPETIZER', 'http://images.com/nachos.png'),
    ('Ensalada Cesar', 'Lechuga, pollo crujiente, picatostes y salsa cesar suave', 4.80, TRUE, 'SALAD', 'http://images.com/cesar.png'),
    ('Ensalada Vegana', 'Lechuga, tomate, cebolla, tofu crunchy y salsa verde', 4.50, TRUE, 'SALAD', 'http://images.com/mixta.png');

-- ===================================================================
--                              MONTADITOS
-- ===================================================================
INSERT INTO products (name, description, price, available, category, image_url)
VALUES
    ('Montadito 01 - Clasico', 'Jamon gran reserva con aceite de oliva virgen extra', 1.50, TRUE, 'MONTADITO', 'http://images.com/m01.png'),
    ('Montadito 02 - Tradicion', 'Tortilla de patatas con pimiento verde frito', 1.50, TRUE, 'MONTADITO', 'http://images.com/m02.png'),
    ('Montadito 04 - Al Ajillo', 'Lomo al ajillo con mayonesa ligera y tomate natural', 1.50, TRUE, 'MONTADITO', 'http://images.com/m04.png'),
    ('Montadito 10 - Pringa', 'Pringa tradicional andaluza de lomo, chorizo y morcilla', 1.80, TRUE, 'MONTADITO', 'http://images.com/m10.png'),
    ('Montadito 15 - Campero', 'Lomo asado, queso fundido, pimiento verde y tomate', 1.60, TRUE, 'MONTADITO', 'http://images.com/m15.png'),
    ('Montadito 23 - Mostaza y Miel', 'Pollo crujiente con salsa de mostaza y miel', 1.60, TRUE, 'MONTADITO', 'http://images.com/m23.png'),
    ('Montadito 34 - Iberico', 'Chorizo iberico al vino con queso manchego fundido', 1.70, TRUE, 'MONTADITO', 'http://images.com/m34.png'),
    ('Montadito 45 - Cebolla Crujiente', 'Salchicha de picadillo con cebolla crujiente y ketchup', 1.50, TRUE, 'MONTADITO', 'http://images.com/m45.png'),
    ('Montadito 56 - BBQ', 'Burguer de vacuno con bacon crujiente y salsa barbacoa', 1.80, TRUE, 'MONTADITO', 'http://images.com/m56.png'),
    ('Montadito 67 - Costillas', 'Costilla deshilachada a la barbacoa con queso cheddar', 1.90, TRUE, 'MONTADITO', 'http://images.com/m67.png'),
    ('Montadito 72 - Salmon', 'Salmon ahumado con crema de queso y un toque de eneldo', 2.10, TRUE, 'MONTADITO', 'http://images.com/m72.png'),
    ('Montadito 85 - Caprese', 'Queso mozzarella fresco, tomate rodajas y salsa pesto', 1.60, TRUE, 'MONTADITO', 'http://images.com/m85.png'),
    ('Montadito 90 - Serranito', 'Cinta de lomo, jamon serrano y pimiento frito crujiente', 1.90, TRUE, 'MONTADITO', 'http://images.com/m90.png'),
    ('Montadito 100 - Gourmet', 'Solomillo al whisky con crujiente de bacon selecto', 2.20, TRUE, 'MONTADITO', 'http://images.com/m100.png'),
    ('Montadito 101 - Vegano', 'Hamburguesa vegana con queso fresco y vegetales frescos', 2.50, TRUE, 'MONTADITO', 'http://images.com/m101.png');

-- ===================================================================
--                  PRODUCTOS AGOTADOS PARA PRUEBAS
-- ===================================================================
INSERT INTO products (name, description, price, available, category, image_url)
VALUES
    ('Perrito Especial 100M', 'Perrito caliente con salchicha Frankfurt, cebolla crujiente, ketchup y mostaza', 2.50, FALSE, 'MONTADITO', 'http://images.com/perrito.png'),
    ('Montadito 99 - Iberico Premium', 'Paleta iberica de cebo con tomate rallado (AGOTADO)', 3.00, FALSE, 'MONTADITO', 'http://images.com/m99.png');
