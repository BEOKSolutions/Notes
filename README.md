# Notes

**Notes** est une API REST moderne construite avec Spring Boot pour créer, organiser, rechercher et gérer des notes efficacement.

Elle propose une architecture claire, une documentation Swagger interactive, un stockage temporaire en H2, ainsi que des intégrations Redis et Apache Kafka pour profiter du cache et du traitement événementiel.

## Fonctionnalités

- Créer, consulter, modifier et supprimer des notes
- Identifier chaque note avec un code UUID
- Rechercher des notes par titre ou contenu
- Filtrer les notes par niveau : `LOW`, `MEDIUM` ou `HIGH`
- Valider automatiquement les données reçues par l'API
- Publier la création d'une note sur Kafka
- Mettre en cache les données avec Redis
- Consulter automatiquement la documentation OpenAPI/Swagger

## Technologies

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Redis
- Apache Kafka
- SpringDoc OpenAPI
- Maven
- Lombok
- Docker Compose

## Prérequis

- JDK 17 ou supérieur
- Maven (ou le wrapper Maven fourni)
- Docker et Docker Compose
- Redis disponible sur `localhost:6379`

## Démarrage

### 1. Lancer Kafka

```bash
docker compose up -d
```

Kafka sera disponible sur `localhost:9092` et Kafka UI sur `http://localhost:8090`.

### 2. Démarrer l'application

```bash
./mvnw spring-boot:run
```

Sur Windows :

```bash
mvnw.cmd spring-boot:run
```

L'application démarre par défaut sur le port `8082`.

## Documentation API

- Swagger UI : http://localhost:8082/swagger-ui.html
- OpenAPI JSON : http://localhost:8082/v3/api-docs
- Console H2 : http://localhost:8082/h2-console

Pour accéder à H2 Console, utilisez :

- JDBC URL : `jdbc:h2:mem:Db_notes`
- Utilisateur : `sa`
- Mot de passe : *(laisser vide)*

## Endpoints principaux

Base URL : `http://localhost:8082/api/notes`

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/notes` | Liste toutes les notes |
| `GET` | `/api/notes/{code}` | Récupère une note par UUID |
| `GET` | `/api/notes/filter?level=HIGH` | Filtre les notes par niveau |
| `GET` | `/api/notes/search?search=mot` | Recherche des notes |
| `POST` | `/api/notes` | Crée une nouvelle note |
| `PUT` | `/api/notes/{code}` | Modifie une note |
| `DELETE` | `/api/notes/{code}` | Supprime une note |

### Exemple de création

```bash
curl -X POST http://localhost:8082/api/notes \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Ma première note",
    "content": "Cette note a été créée avec l'API Notes.",
    "level": "HIGH"
  }'
```

## Structure du projet

```text
src/main/java/com/ensa/notes
├── config/         # Configuration Redis, Kafka, CORS et OpenAPI
├── controllers/    # Points d'entrée REST
├── dtos/           # Objets de requête et de réponse
├── entities/       # Modèle JPA
├── enums/          # Niveaux des notes
├── exceptions/     # Gestion centralisée des erreurs
├── mappers/        # Conversion entre entités et DTO
├── repositories/   # Accès aux données
└── services/       # Logique métier
```

## Tests

```bash
./mvnw test
```
