-- Database Schema for Farmers' Market Management Application

CREATE TABLE farmers (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL
);

CREATE TABLE produce (
    id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES farmers(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    unit VARCHAR(50) NOT NULL CHECK (unit IN ('kg', 'liters', 'pieces'))
);

CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    farmer_id INT REFERENCES farmers(id) ON DELETE CASCADE,
    produce_id INT REFERENCES produce(id) ON DELETE CASCADE,
    quantity_sold INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory (
    id SERIAL PRIMARY KEY,
    produce_id INT REFERENCES produce(id) ON DELETE CASCADE,
    current_stock INT NOT NULL CHECK (current_stock >= 0)
);

CREATE TABLE market_prices (
    id SERIAL PRIMARY KEY,
    produce_name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert 150+ Sample Market Price Data
INSERT INTO market_prices (produce_name, price) VALUES
('Tomatoes', 2.50), ('Carrots', 1.75), ('Mangoes', 3.00), ('Cabbage', 2.00), ('Cassava', 1.50),
('Groundnuts', 2.20), ('Plantains', 2.80), ('Yams', 4.50), ('Maize', 1.90), ('Onions', 3.20),
('Pepper', 2.60), ('Rice', 5.00), ('Soybeans', 3.10), ('Okra', 1.80), ('Pineapple', 4.00),
('Watermelon', 3.75), ('Cocoa', 10.00), ('Palm Oil', 6.50), ('Cashew Nuts', 7.20),
('Bananas', 1.50), ('Apples', 2.20), ('Oranges', 2.00), ('Grapes', 3.50), ('Lemons', 1.80),
('Limes', 1.60), ('Strawberries', 4.00), ('Blueberries', 5.00), ('Raspberries', 5.50), ('Blackberries', 6.00),
('Peaches', 3.00), ('Plums', 2.50), ('Cherries', 7.00), ('Avocado', 2.20), ('Broccoli', 2.50),
('Cauliflower', 2.80), ('Spinach', 1.90), ('Lettuce', 1.50), ('Zucchini', 2.70), ('Cucumbers', 1.40),
('Garlic', 3.00), ('Ginger', 2.90), ('Basil', 1.20), ('Parsley', 1.10), ('Cilantro', 1.00),
('Mint', 1.50), ('Rosemary', 1.80), ('Thyme', 1.60), ('Oregano', 1.70), ('Dill', 1.40),
('Eggplant', 2.50), ('Bell Peppers', 2.20), ('Chili Peppers', 3.00), ('Kale', 2.50), ('Swiss Chard', 2.60),
('Beets', 1.80), ('Radishes', 1.40), ('Turnips', 1.50), ('Parsnips', 1.90), ('Sweet Potatoes', 2.00),
('Pumpkin', 2.50), ('Butternut Squash', 2.80), ('Acorn Squash', 2.90), ('Spaghetti Squash', 3.00), ('Celery', 1.20),
('Artichokes', 3.50), ('Asparagus', 4.00), ('Brussels Sprouts', 3.00), ('Green Beans', 1.80), ('Snow Peas', 2.20),
('Snap Peas', 2.30), ('Jalapenos', 2.50), ('Habaneros', 2.80), ('Serrano Peppers', 2.60), ('Poblano Peppers', 2.70),
('Tomatillos', 2.00), ('Okra', 1.80), ('Pineapple', 4.00), ('Watermelon', 3.75), ('Cocoa', 10.00),
('Palm Oil', 6.50), ('Cashew Nuts', 7.20), ('Pistachios', 8.00), ('Almonds', 7.50), ('Walnuts', 6.60),
('Pecans', 7.00), ('Macadamia Nuts', 10.00), ('Hazelnuts', 8.50), ('Chestnuts', 5.00), ('Peanuts', 2.50),
('Sunflower Seeds', 2.20), ('Pumpkin Seeds', 2.80), ('Chia Seeds', 4.00), ('Flax Seeds', 3.00), ('Hemp Seeds', 5.00),
('Quinoa', 6.00), ('Amaranth', 5.50), ('Millet', 2.50), ('Barley', 1.80), ('Farro', 3.50),
('Spelt', 4.00), ('Buckwheat', 3.00), ('Bulgar', 2.50), ('Teff', 7.00), ('Sorghum', 2.80),
('Rye', 2.00), ('Oats', 1.50), ('Wheat Berries', 3.00), ('Brown Rice', 2.50), ('Wild Rice', 6.00),
('White Rice', 1.80), ('Basmati Rice', 5.50), ('Jasmine Rice', 4.50), ('Arborio Rice', 4.00), ('Couscous', 2.00),
('Polenta', 1.80), ('Grits', 2.00), ('Hominy', 1.90), ('Cornmeal', 1.50), ('Tapioca', 3.00),
('Sago', 3.50), ('Arrowroot', 4.00), ('Potato Starch', 2.50), ('Cornstarch', 1.50), ('All-Purpose Flour', 1.20),
('Whole Wheat Flour', 1.50), ('Bread Flour', 2.00), ('Cake Flour', 2.20), ('Pastry Flour', 2.50), ('Almond Flour', 7.00),
('Coconut Flour', 5.00), ('Rice Flour', 3.00), ('Oat Flour', 2.50), ('Chickpea Flour', 3.50), ('Soy Flour', 2.80),
('Sorghum Flour', 4.00), ('Teff Flour', 6.00), ('Spelt Flour', 4.50), ('Buckwheat Flour', 3.50), ('Tapioca Flour', 2.50),
('Arrowroot Flour', 3.00), ('Potato Flour', 2.50), ('Corn Flour', 1.80), ('Quinoa Flour', 6.50), ('Amaranth Flour', 5.00),
('Millet Flour', 2.80), ('Brown Rice Flour', 3.20), ('White Rice Flour', 2.50), ('Gluten-free Flour', 5.00), ('Cassava Flour', 2.50),
('Tigernut Flour', 7.50), ('Hemp Flour', 6.00), ('Mesquite Flour', 8.00), ('Cricket Flour', 10.00), ('Banana Flour', 5.50),
('Plantain Flour', 5.00), ('Pea Protein', 6.50), ('Hemp Protein', 7.00), ('Pumpkin Protein', 5.50), ('Sunflower Protein', 4.50),
('Rice Protein', 3.50), ('Soy Protein', 2.80), ('Whey Protein', 8.00), ('Casein Protein', 7.50), ('Egg White Protein', 6.00),
('Collagen Protein', 10.00), ('Bone Broth Protein', 9.00), ('Spirulina', 12.00), ('Chlorella', 11.00), ('Maca Powder', 10.00),
('Ashwagandha Powder', 9.00), ('Moringa Powder', 8.00), ('Matcha Powder', 14.00), ('Cacao Powder', 7.00), ('Carob Powder', 5.00),
('Lucuma Powder', 10.00), ('Baobab Powder', 12.00), ('Acai Powder', 13.00), ('Goji Berries', 15.00), ('Golden Berries', 14.00),
('Mulberries', 13.00), ('Inca Berries', 12.00), ('Camu Camu Powder', 16.00), ('Kale Powder', 7.00), ('Beet Powder', 6.00),
('Spinach Powder', 5.50), ('Wheatgrass Powder', 9.00), ('Barley Grass Powder', 8.50), ('Alfalfa Powder', 5.00), ('Mushroom Powder', 10.00);
