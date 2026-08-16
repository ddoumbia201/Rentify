-- ============================================================
-- Données de test pour EventShare
-- Mot de passe pour les 3 utilisateurs : Password123!
-- (hash BCrypt valide, généré indépendamment de l'application)
-- ============================================================

-- 3 utilisateurs (rôle USER)
INSERT INTO Users (firstName, lastName, email, password, role) VALUES
('Julie', 'Dupont', 'julie.dupont@exemple.com', '$2b$10$7.ckTzdtOEdp0V4ejsnEE.5XYmySXItylCG3gcPRg/1UZyT/C3cuC', 'USER'),
('Marc', 'Petit', 'marc.petit@exemple.com', '$2b$10$7.ckTzdtOEdp0V4ejsnEE.5XYmySXItylCG3gcPRg/1UZyT/C3cuC', 'USER'),
('Sophie', 'Lambert', 'sophie.lambert@exemple.com', '$2b$10$7.ckTzdtOEdp0V4ejsnEE.5XYmySXItylCG3gcPRg/1UZyT/C3cuC', 'USER');

-- Récupère les id générés pour les utiliser comme owner_id ci-dessous.
-- Si tu as déjà d'autres utilisateurs en base, adapte les owner_id manuellement
-- via : SELECT id, email FROM Users;

-- 15 annonces, 3 par catégorie, réparties entre les 3 utilisateurs
-- (adapte les valeurs de owner_id si les id générés diffèrent chez toi)

-- Décoration
INSERT INTO Goods (title, description, priceperday, category, location, available, owner_id) VALUES
('Arche florale décorative', 'Arche fleurie idéale pour mariages et cérémonies, hauteur 2m.', 35.00, 'Décoration', 'Namur', 1, 1),
('Guirlandes lumineuses (20m)', 'Guirlandes guinguette à ampoules chaudes, parfaites pour extérieur.', 12.00, 'Décoration', 'Bruxelles', 1, 2),
('Fond de scène floral', 'Mur végétal artificiel 3x2m, fixation incluse.', 40.00, 'Décoration', 'Liège', 1, 3);

-- Audiovisuel
INSERT INTO Goods (title, description, priceperday, category, location, available, owner_id) VALUES
('Vidéoprojecteur HD', 'Projecteur Full HD 3000 lumens avec écran 2x1.5m inclus.', 25.00, 'Audiovisuel', 'Namur', 1, 1),
('Sono + micro sans fil', 'Enceinte 200W avec micro HF, idéale pour discours et petites soirées.', 30.00, 'Audiovisuel', 'Bruxelles', 1, 2),
('Machine à fumée', 'Machine à fumée légère avec télécommande, pour effet de piste.', 18.00, 'Audiovisuel', 'Liège', 1, 3);

-- Mobilier
INSERT INTO Goods (title, description, priceperday, category, location, available, owner_id) VALUES
('Set de 50 chaises pliantes', 'Chaises blanches pliantes, empilables, transport possible en camionnette.', 20.00, 'Mobilier', 'Namur', 1, 1),
('Table ronde (10 personnes)', 'Table ronde en bois avec nappe blanche fournie.', 15.00, 'Mobilier', 'Bruxelles', 1, 2),
('Tente de réception 6x4m', 'Tente pliable étanche, montage en 30 minutes.', 60.00, 'Mobilier', 'Liège', 1, 3);

-- Vaisselle
INSERT INTO Goods (title, description, priceperday, category, location, available, owner_id) VALUES
('Service de vaisselle (40 pers.)', 'Assiettes, couverts et verres en porcelaine, pour 40 personnes.', 25.00, 'Vaisselle', 'Namur', 1, 1),
('Verres à cocktail (x50)', 'Verres à pied élégants, lavés et livrés en caisses.', 10.00, 'Vaisselle', 'Bruxelles', 1, 2),
('Fontaine à chocolat', 'Fontaine à chocolat 3 étages, idéale pour buffet dessert.', 22.00, 'Vaisselle', 'Liège', 1, 3);

-- Animation
INSERT INTO Goods (title, description, priceperday, category, location, available, owner_id) VALUES
('Château gonflable "Château fort"', 'Château gonflable médiéval 4x4m, livré gonflé et installé.', 45.00, 'Animation', 'Namur', 1, 1),
('Photobooth avec accessoires', 'Cabine photo avec imprimante et déguisements, forfait journée.', 50.00, 'Animation', 'Bruxelles', 1, 2),
('Machine à popcorn', 'Machine à popcorn professionnelle avec ingrédients pour 50 portions.', 15.00, 'Animation', 'Liège', 1, 3);