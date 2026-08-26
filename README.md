# Platforma gier kasynowych

> Projekt w trakcie rozwoju.

Platforma gier kasynowych – aplikacja webowa zbudowana w architekturze
mikroserwisowej.

## Technologie

- Java 21
- Spring Boot
- Spring Security
- Spring WebFlux
- JWT
- Apache Kafka
- PostgreSQL
- Redis
- Angular
- Nginx
- Docker / Docker Compose

## Architektura

Frontend został zbudowany w Angularze i udostępniony przez Nginx.
Komunikacja z backendem odbywa się przez API Gateway, który stanowi
punkt wejścia do mikroserwisów.

APIGateway - Spring webflux. Odpowiada za sprawdzenie cookies i tokenów oraz przekierowywanie requestów do serwisów schowanych za gateway'em
AuthService - Obsługa autentykacji użytkownika
UserService - Zarządzanie użytkownikami
GameService - Główna logika gier. Obecnie buduję moduł pokera