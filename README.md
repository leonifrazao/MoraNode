<a id="readme-top"></a>

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/leonifrazao/MoraNode">
    <img src="docs/logo.png" alt="Logo" width="100" height="auto">
  </a>

  <h3 align="center">MoraNode Engine</h3>

  <p align="center">
    Real Estate Management System built with Hexagonal Architecture, Event-Driven messaging via Kafka and a Dark Mode interface with Glassmorphism.
    <br />
    <a href="https://github.com/leonifrazao/MoraNode"><strong>Explore the docs</strong></a>
    <br />
    <br />
    <a href="https://github.com/leonifrazao/MoraNode/issues/new?labels=bug">Report Bug</a>
    &middot;
    <a href="https://github.com/leonifrazao/MoraNode/issues/new?labels=enhancement">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#architecture">Architecture</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#features">Features</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#running-with-docker-full-stack">Running with Docker (Full Stack)</a></li>
        <li><a href="#running-locally-for-development">Running Locally (For Development)</a></li>
      </ul>
    </li>
    <li><a href="#project-structure">Project Structure</a></li>
    <li><a href="#api-endpoints">API Endpoints</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

MoraNode Engine is a comprehensive real estate management system designed to handle properties and their rental or sales contracts. The backend was built by rigorously applying **Hexagonal Architecture (Ports & Adapters)**, ensuring the business domain is completely independent of frameworks, databases, or messaging systems.

Data consistency between Properties and Contracts is maintained in an **event-driven** fashion: when a contract is created or has its status changed, events are published to Apache Kafka. An internal consumer processes these events and automatically updates the linked property's availability, without any direct coupling between use cases.

The frontend is a SPA built with React 19 and Vite, served in production via Nginx with a reverse proxy to the API. The entire stack (backend, frontend, database, kafka) boots up with a single `docker-compose up` command.

### Screenshots

<details>
  <summary>Dashboard</summary>
  <br />
  <img src="docs/dashboard.png" alt="Dashboard" />
</details>

<details>
  <summary>Property Management</summary>
  <br />
  <img src="docs/imoveis.png" alt="Properties" />
</details>

<details>
  <summary>Contract Management</summary>
  <br />
  <img src="docs/contratos.png" alt="Contracts" />
</details>

<details>
  <summary>New Property Modal</summary>
  <br />
  <img src="docs/novoimovel.png" alt="New Property" />
</details>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Architecture

The backend follows Hexagonal Architecture. The central domain (Use Cases, Models, Events) is fully isolated. Driving adapters (REST Controllers, Kafka Consumer) and driven adapters (Postgres Adapters, Kafka Producer) connect the domain to the outside world through interfaces (Ports).

```mermaid
flowchart LR
    UI[React / Vite] --> API[REST Controllers]
    API --> UC[Use Cases / Services]
    SUB[Kafka Consumer] --> UC

    UC -.-> DOM[Domain Models & Events]

    UC --> REPO[Postgres Adapters]
    UC --> PUB[Kafka Producer]

    REPO --> DB[(PostgreSQL)]
    PUB --> KF[[Apache Kafka]]

    KF -. Events .-> SUB

    classDef ui fill:#f3f4f6,stroke:#6b7280,color:#000
    classDef api fill:#e0f2fe,stroke:#0ea5e9,color:#000
    classDef core fill:#dbeafe,stroke:#3b82f6,color:#000
    classDef data fill:#f3e8ff,stroke:#a855f7,color:#000
    classDef ext fill:#fee2e2,stroke:#ef4444,color:#000

    class UI ui
    class API,SUB api
    class UC,DOM core
    class REPO,PUB data
    class DB,KF ext
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

**Backend**
* [![Spring Boot][Spring.boot]][Spring-url] &mdash; Java 21, Spring Boot 4.0.3 (REST API, Data JPA, Validation)
* [![Kafka][Kafka.apache.org]][Kafka-url] &mdash; Confluent 7.4.0 (Async Event Streaming)
* [![PostgreSQL][PostgreSQL.org]][PostgreSQL-url] &mdash; PostgreSQL 15 (Relational Persistence)

**Frontend**
* [![React][React.js]][React-url] &mdash; React 19 with react-router-dom (SPA)
* [![Vite][Vite.js]][Vite-url] &mdash; Vite 6 (Build tooling)
* [![TypeScript][TypeScript.org]][TypeScript-url] &mdash; Strict typing mirroring Java DTOs
* [![TailwindCSS][TailwindCSS.com]][TailwindCSS-url] &mdash; Styling with Dark Mode and Glassmorphism
* [![Shadcn/UI][Shadcn.ui]][Shadcn-url] + [![Radix UI][Radix.ui]][Radix-url] &mdash; Component library
* **Lucide React** &mdash; Icons
* **Axios** &mdash; HTTP client

**DevOps**
* [![Docker][Docker.com]][Docker-url] &mdash; Multi-stage builds (Maven + Bun + Nginx)
* **Kafdrop** &mdash; Kafka topics monitoring UI

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Features

**Property Management**
- Table listing with availability status (visual badges)
- Registration via modal with validation
- Editing blocked when an active contract is linked
- Physical deletion with active contract verification
- Availability automatically updated via Kafka events

**Contract Management**
- Listing with color-coded status badges (ACTIVE, FINISHED, CANCELED, IN_DISPUTE)
- Contract creation linked to existing properties
- Cancellation and finalization via logical status change (PATCH)
- Business rule validation (dates, values, property availability)
- Automatic renewal only for active rental contracts

**Interface**
- Native Dark Mode with consistent theming
- Glassmorphism (backdrop-blur) on cards and containers
- Dashboard with computed metrics (revenue, occupancy rate, average ticket, total area)
- Portfolio occupancy progress bar
- Loading states with Skeletons
- Standardized empty states with icons and clear messages
- Fixed sidebar navigation

**Architecture and Infrastructure**
- Hexagonal Architecture with IN Ports (6 use cases) and OUT Ports (3 interfaces)
- Kafka Events: `imovel-ocupado-topic` and `imovel-desocupado-topic`
- CORS configured in Spring Boot to accept frontend requests
- Nginx as reverse proxy in production (`/api/` proxied to `app:8080`)
- Docker Compose orchestrating 6 services on a single network (`moranode-network`)
- Healthchecks configured for PostgreSQL and Kafka

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* [Docker](https://docker.com) and Docker Compose
* For local development: Java 21+, Maven, Node.js 18+

### Running with Docker (Full Stack)

A single command boots up the entire infrastructure (PostgreSQL, Zookeeper, Kafka, Kafdrop, Backend and Frontend):

1. Clone the repository
   ```sh
   git clone https://github.com/leonifrazao/MoraNode.git
   cd MoraNode
   ```
2. Set environment variables in `.env` (default values already included)
   ```env
   DB_PASSWORD=password
   DB_USER=postgres
   DB_NAME=moranode_db
   ```
3. Spin up the entire stack
   ```sh
   docker-compose up -d
   ```
4. Access the application

   | Service   | URL                        |
   |-----------|----------------------------|
   | Frontend  | http://localhost:3000       |
   | API       | http://localhost:8080       |
   | Kafdrop   | http://localhost:9000       |

### Running Locally (For Development)

1. Start only the infrastructure services (database, kafka, etc.)
   ```sh
   docker-compose up -d db zookeeper kafka kafdrop
   ```

2. Start the Spring Boot backend
   ```sh
   ./mvnw spring-boot:run
   ```

3. In another terminal, start the frontend (port 5173 in dev mode)
   ```sh
   cd web
   npm install
   npm run dev
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Project Structure

```
MoraNode/
  ├── src/main/java/com/leonifrazao/MoraNode/
  │   ├── domain/
  │   │   ├── model/          # ImovelDomain, ContratoDomain, Enums, Events
  │   │   ├── port/in/        # Use Case interfaces (6 ports)
  │   │   ├── port/out/       # Repository & Notification interfaces (3 ports)
  │   │   └── usecase/        # Service implementations
  │   └── infrastructure/
  │       ├── adapter/web/    # REST Controllers (Imovel, Contrato)
  │       ├── adapter/in/     # Kafka Consumer
  │       ├── adapter/out/    # Postgres Adapters, Kafka Producer
  │       ├── database/       # JPA Entities, Spring Data Repositories
  │       ├── mappers/        # Domain <-> Entity mappers
  │       ├── config/         # Bean wiring (manual, no @Service on domain)
  │       └── exceptions/     # Global handler, custom exceptions
  ├── web/
  │   ├── src/
  │   │   ├── pages/          # Dashboard, Imoveis, Contratos
  │   │   ├── components/     # Modals, UI (Shadcn), Layout
  │   │   ├── services/       # Axios API config
  │   │   ├── types/          # TypeScript interfaces mirroring Java DTOs
  │   │   └── hooks/          # Custom hooks (toast, feedback)
  │   ├── dockerfile          # Bun build + Nginx
  │   └── nginx.conf          # SPA routing + reverse proxy /api/
  ├── docker-compose.yml      # 6 services, 1 network
  ├── Dockerfile              # Maven multi-stage build (Java 21)
  └── .env                    # Database credentials
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## API Endpoints

### Properties `/imoveis`

| Method   | Path           | Description                    |
|----------|----------------|--------------------------------|
| `GET`    | `/imoveis`     | List all properties            |
| `GET`    | `/imoveis/{id}`| Get property by ID             |
| `POST`   | `/imoveis`     | Register new property          |
| `PUT`    | `/imoveis/{id}`| Edit property by ID            |
| `DELETE` | `/imoveis/{id}`| Delete property by ID          |

### Contracts `/contratos`

| Method   | Path                      | Description                     |
|----------|---------------------------|---------------------------------|
| `GET`    | `/contratos`              | List all contracts              |
| `GET`    | `/contratos/{id}`         | Get contract by ID              |
| `POST`   | `/contratos`              | Register new contract           |
| `PATCH`  | `/contratos/{id}/status`  | Update contract status          |

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Roadmap

- [x] Domain modeled with Hexagonal Architecture (Ports & Adapters)
- [x] Full CRUD for Properties with domain validation
- [x] Contract management with business rules (edit blocking, renewal)
- [x] Kafka Producer/Consumer for occupancy/vacancy events
- [x] React 19 + Vite frontend with Dark Mode and Glassmorphism
- [x] Dashboard with real-time computed metrics
- [x] Full-stack Docker Compose (6 services)
- [x] Nginx reverse proxy with static asset caching
- [ ] Redis caching for property listing endpoints
- [ ] Clients page
- [ ] Authentication and authorization (Spring Security)
- [ ] Unit and integration tests

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

leonifrazao - Project Link: [https://github.com/leonifrazao/MoraNode](https://github.com/leonifrazao/MoraNode)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/leonifrazao/MoraNode.svg?style=for-the-badge
[contributors-url]: https://github.com/leonifrazao/MoraNode/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/leonifrazao/MoraNode.svg?style=for-the-badge
[forks-url]: https://github.com/leonifrazao/MoraNode/network/members
[stars-shield]: https://img.shields.io/github/stars/leonifrazao/MoraNode.svg?style=for-the-badge
[stars-url]: https://github.com/leonifrazao/MoraNode/stargazers
[issues-shield]: https://img.shields.io/github/issues/leonifrazao/MoraNode.svg?style=for-the-badge
[issues-url]: https://github.com/leonifrazao/MoraNode/issues
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew

[React.js]: https://img.shields.io/badge/React_19-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://react.dev/
[Spring.boot]: https://img.shields.io/badge/Spring_Boot_4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot
[Vite.js]: https://img.shields.io/badge/Vite_6-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white
[Vite-url]: https://vitejs.dev/
[PostgreSQL.org]: https://img.shields.io/badge/PostgreSQL_15-4169e1?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/
[Kafka.apache.org]: https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white
[Kafka-url]: https://kafka.apache.org/
[TailwindCSS.com]: https://img.shields.io/badge/Tailwind_CSS-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white
[TailwindCSS-url]: https://tailwindcss.com/
[Docker.com]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://docker.com/
[TypeScript.org]: https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=TypeScript&logoColor=FFF
[TypeScript-url]: https://www.typescriptlang.org/
[Shadcn.ui]: https://img.shields.io/badge/shadcn%2Fui-000?style=for-the-badge&logo=shadcnui&logoColor=fff
[Shadcn-url]: https://ui.shadcn.com/
[Radix.ui]: https://img.shields.io/badge/Radix_UI-161618?style=for-the-badge&logo=radixui&logoColor=white
[Radix-url]: https://www.radix-ui.com/
