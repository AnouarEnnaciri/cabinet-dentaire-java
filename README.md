# Cabinet Dentaire - Application de Gestion de Cabinet Dentaire
**Développé par : Anouar Ennaciri**


## Présentation

Application web complète de gestion de cabinet dentaire simulant un système professionnel utilisé en milieu médical.
Elle permet la gestion des patients, des rendez-vous, des dossiers médicaux, des prescriptions, de la facturation et des rôles utilisateurs avec contrôle d’accès.

## Objectif

Ce projet simule un système réel de gestion de cabinet dentaire afin de centraliser les opérations médicales et administratives dans une architecture sécurisée et modulaire.

## 🛠️ Technologies

| Catégorie | Technologies |
|-----------|--------------|
| Backend | Java 17, Jakarta Servlets, JSP |
| Base de données | SQLite 3.45 |
| Serveur | Apache Tomcat 7+ |
| Build | Maven 3.8+ |
| PDF | iTextPDF |
| Frontend | Bootstrap 5, Chart.js |

##  Installation

```bash
# Prérequis: Java 17 et Maven 3.8+

# Compiler
mvn clean package

# Lancer
mvn tomcat7:run
```

**Accès:** http://localhost:8080/

##  Identifiants

| Rôle | Login | Mot de passe |
|------|-------|--------------|
| Admin | admin | admin123 |
| Médecin | doctor | doctor123 |
| Secrétaire | secretaire | secret123 |

##  Fonctionnalités

| Module | Description |
|--------|-------------|
| **Authentification** | 3 rôles, SHA-256, filtre sécurité |
| **Patients** | CRUD complet + recherche |
| **Rendez-vous** | Demande publique, agenda médecin, validation secrétaire |
| **Dossiers médicaux** | Consultations, actes avec numéro de dent |
| **Prescriptions** | Catalogue médicaments, lignes dynamiques |
| **Certificats** | PDF (4 types: standard, arrêt, scolaire, sport) |
| **Facturation** | Génération auto, paiement partiel/total |
| **Statistiques** | Graphiques (admin global, médecin personnel) |
| **Admin utilisateurs** | Gestion médecins/secrétaires |

##  Structure simplifiée

```
src/main/java/com/cabinet/
├─ model/        # 12 classes entités
├─ dao/          # 12 classes accès données  
├─ servlet/      # 15 contrôleurs
├─ filter/       # AuthFilter
└─ util/         # DatabaseUtil

src/main/webapp/
├─ WEB-INF/views/  # 15 pages JSP
├─ css/, js/       # Styles et scripts
└─ index.jsp
```

## Architecture technique

- Architecture MVC (Servlets = contrôleurs)
- Couche DAO pour l’accès aux données
- Gestion des sessions HTTP
- Filtre de sécurité pour contrôle des accès
- Base de données SQLite embarquée

## Points techniques clés

- Authentification sécurisée avec hash SHA-256
- Gestion des rôles et permissions côté backend
- Génération de documents PDF médicaux (iTextPDF)
- Architecture modulaire DAO / Service / Controller
- Workflow métier complet des consultations

##  Configuration

Base de données SQLite auto-générée (`clinic.db`) au premier lancement.

##  Règles

- UI: français et Code: anglais
- Pas de SQL dans les Servlets
- Pas de Java dans les JSP (sauf affichage)

---

**URL:** http://localhost:8080/
