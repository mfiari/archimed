
DROP TABLE IF EXISTS traduction;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS produits;

CREATE TABLE users (
	id int PRIMARY KEY,
	nom varchar(50),
	prenom varchar(50)
);

INSERT INTO users (id, nom, prenom) VALUES
(1, 'test', 'test'),
(2, 'fiari', 'mike'),
(3, 'farot', 'charly');

CREATE TABLE produits (
	id int PRIMARY KEY,
	nom varchar(50),
	prix real,
	quantite int
);

INSERT INTO produits (id, nom, prix, quantite) VALUES
(1, 'DELL computer', 580, 10);

CREATE TABLE traduction (
	id_produit int,
	code varchar(10),
	texte text,
	constraint fk_traduction_produits foreign key (id_produit) references produits(id)
);

INSERT INTO traduction (id_produit, code, texte) VALUES
(1, 'fr', 'machine électronique pour calculs'),
(1, 'en', 'electronic machine for calculations');