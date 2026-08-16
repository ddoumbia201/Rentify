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
- **Maven** (généralement inclus avec votre IDE — IntelliJ IDEA, Eclipse, VS Code)
- **XAMPP** (ou toute distribution incluant MySQL) — [télécharger](https://www.apachefriends.org/)

Aucune autre installation globale n'est nécessaire : toutes les dépendances Java sont gérées automatiquement par Maven au premier lancement.

## Installation

### 1. Cloner le projet

```bash
git clone <url-du-dépôt>
cd location-biens
```

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

### 4. Vérifier la configuration de connexion

Le fichier `src/main/resources/application.properties` contient déjà la configuration par défaut d'XAMPP :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/location_biens
spring.datasource.username=root
spring.datasource.password=
```

Si votre installation MySQL utilise des identifiants différents, ajustez ces deux dernières lignes en conséquence.

## Lancer l'application

Depuis la racine du projet :

```bash
mvn spring-boot:run
```

Attendez que le terminal affiche une ligne du type :

```
Started LocationBiensApplication in X.XXX seconds
```

L'application est alors accessible à l'adresse :

```
http://localhost:8080
```

## Comptes de test

À l'inscription (`/register.html`), tout nouveau compte reçoit automatiquement le rôle **USER**.

Pour obtenir un compte **ADMIN**, inscrivez-vous normalement puis exécutez cette requête dans phpMyAdmin (onglet SQL de la base `location_biens`) :

```sql
UPDATE Users SET role = 'ADMIN' WHERE email = 'votre-email@exemple.com';
```

Reconnectez-vous ensuite avec ce compte pour accéder au back-office administrateur (`/admin.html`).

## Structure du projet

```
location-biens/
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
│   └── application.properties
└── pom.xml
```

## Fonctionnalités

- Inscription et connexion, avec distinction de rôle (USER / ADMIN)
- Consultation du catalogue d'annonces, filtrable par catégorie
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