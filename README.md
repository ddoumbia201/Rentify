# EventShare

Plateforme web de location de matériel événementiel entre particuliers et petites entreprises (décoration, audiovisuel, mobilier, vaisselle, animation).

Projet réalisé dans le cadre du cours **INFOB238 — Technologies Web**.

## Sommaire

- [Prérequis](#prérequis)
- [Installation](#installation)
- [Lancer l'application](#lancer-lapplication)
- [Comptes de test](#comptes-de-test)
- [Structure du projet](#structure-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Stack technique](#stack-technique)

## Prérequis

- **Java 17** ou supérieur ([télécharger](https://adoptium.net/))
- **XAMPP** (ou toute distribution incluant MySQL) — [télécharger](https://www.apachefriends.org/)

Aucune installation globale de Maven n'est nécessaire : le projet inclut un **wrapper Maven** (`mvnw` / `mvnw.cmd`) qui télécharge et utilise automatiquement la bonne version au premier lancement.

## Installation

### 1. Récupérer le projet

```bash
git clone <url-du-dépôt>
cd Rentify
```

> Le nom exact du dossier dépend de la façon dont vous avez cloné/téléchargé le projet — vérifiez-le avec `dir` (Windows) ou `ls` (Mac/Linux) si `cd` échoue.

### 2. Démarrer MySQL via XAMPP

1. Ouvrez le **panneau de contrôle XAMPP**
2. Démarrez le module **MySQL**
3. Ouvrez **phpMyAdmin** (`http://localhost/phpmyadmin`)

### 3. Créer la base de données

Dans phpMyAdmin, créez une nouvelle base de données nommée exactement :

```
location_biens
```

Aucune table n'est à créer manuellement — elles sont générées automatiquement au premier lancement de l'application (via Hibernate/JPA).

### 4. (Optionnel) Charger des données de test

Le fichier `donnees_test.sql` fourni dans les ressources du projet contient 3 utilisateurs et 15 annonces de démonstration. Dans phpMyAdmin, onglet **SQL** de la base `location_biens`, collez et exécutez son contenu **après** le premier lancement de l'application (étape suivante), une fois les tables créées.

### 5. Vérifier la configuration de connexion

Le fichier `src/main/resources/application.properties` contient déjà la configuration par défaut d'XAMPP :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/location_biens
spring.datasource.username=root
spring.datasource.password=
```

Si votre installation MySQL utilise des identifiants différents, ajustez ces deux dernières lignes en conséquence.

## Lancer l'application

Depuis la racine du projet, **deux méthodes possibles** :

### Méthode 1 — Ligne de commande (via le wrapper Maven inclus)

**Sur Windows** (CMD ou PowerShell) :
```bash
mvnw.cmd spring-boot:run
```

**Sur Mac/Linux** :
```bash
./mvnw spring-boot:run
```

### Méthode 2 — Depuis votre IDE (IntelliJ, Eclipse, VS Code)

Ouvrez le fichier `src/main/java/com/project/locationbiens/LocationBiensApplication.java` et cliquez sur le bouton **Run** (▶) à côté de la méthode `main`. C'est la méthode la plus fiable si la ligne de commande pose problème.

---

Dans les deux cas, attendez que le terminal (ou la console de l'IDE) affiche une ligne du type :

```
Started LocationBiensApplication in X.XXX seconds
```

L'application est alors accessible à l'adresse :

```
http://localhost:8080
```

## Comptes de test

Si vous avez chargé `donnees_test.sql` (voir étape 4 de l'installation), 3 comptes sont disponibles :

| Email | Mot de passe |
|---|---|
| julie.dupont@exemple.com | Password123! |
| marc.petit@exemple.com | Password123! |
| sophie.lambert@exemple.com | Password123! |

Pour obtenir un compte **ADMIN**, exécutez cette requête dans phpMyAdmin (onglet SQL de la base `location_biens`) :

```sql
UPDATE Users SET role = 'ADMIN' WHERE email = 'julie.dupont@exemple.com';
```

Reconnectez-vous ensuite avec ce compte pour accéder au back-office administrateur (lien "Admin" dans la barre de navigation, ou directement `/admin.html`).

## Structure du projet

```
<racine du projet>/
├── src/main/java/com/project/locationbiens/
│   ├── config/          # Configuration Spring Security, CORS
│   ├── controller/      # Contrôleurs REST (Auth, Good, Rental, Admin)
│   ├── dto/              # Objets de transfert de données (validation des entrées)
│   ├── filter/           # Middleware (journalisation des requêtes API)
│   ├── model/            # Entités JPA (User, Good, Rental)
│   └── repository/       # Accès aux données (Spring Data JPA)
├── src/main/resources/
│   ├── static/            # Frontend (HTML, CSS, JS)
│   │   ├── css/
│   │   ├── js/
│   │   └── *.html
│   ├──donnees_test.sql  # Jeu de données de démonstration (optionnel)
│   └── application.properties
└── pom.xml
```

## Fonctionnalités

- Inscription et connexion, avec distinction de rôle (USER / ADMIN)
- Consultation du catalogue d'annonces, filtrable par catégorie et par lieu
- Création, modification et suppression d'une annonce par son propriétaire
- Réservation d'un bien avec calcul automatique du prix total
- Consultation de ses propres annonces et réservations
- Back-office administrateur (gestion des utilisateurs et des annonces)

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Java 17, Spring Boot (Web, Security, Data JPA, Validation) |
| Base de données | MySQL |
| Frontend | HTML5, CSS3, JavaScript (vanilla) |
| Sécurité | Spring Security (authentification par session, CSRF, hachage BCrypt) |

## Sécurité implémentée

- Protection contre les injections SQL (requêtes préparées via Spring Data JPA)
- Protection XSS (échappement systématique du contenu dynamique affiché)
- Protection CSRF (token vérifié sur chaque requête modifiant des données)
- Politique CORS explicite
- Mots de passe hachés (BCrypt), jamais stockés en clair

## Limites connues

- Pas de vérification des chevauchements de dates sur une même annonce
- Pas de gestion réelle des photos (images d'illustration génériques)
- La recherche par date sur la page d'accueil n'est pas implémentée (seuls la catégorie et le lieu filtrent réellement)
- La suppression d'une annonce ayant des réservations associées est volontairement bloquée (intégrité des données), sans mécanisme d'annulation de réservation pour débloquer ce cas