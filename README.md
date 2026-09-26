# Kasyno – platforma do gry w Makao

Aplikacja webowa, w której zalogowani użytkownicy mogą tworzyć pokoje i grać ze sobą
online w karcianą grę Makao.


## Z czego to jest zbudowane

Projekt jest podzielony na kilka niezależnych serwisów:

- **API Gateway** – przyjmuje wszystkie zapytania z frontendu i kieruje je dalej.
- **Auth Service** – logowanie, rejestracja, tokeny.
- **User Service** – dane użytkowników.
- **Game Service** – pokoje i cała logika gry w Makao.
- **frontend** – aplikacja w Angularze.

Serwisy komunikują się ze sobą przez REST, WebSocket (żeby gra aktualizowała się na
żywo) i Kafkę (przekazywanie zdarzeń między Auth Service a User Service). Każdy
serwis ma swoją bazę danych.

## Technologie

- Java 21 + Spring Boot (backend)
- Angular (frontend)
- PostgreSQL (baza danych)
- Redis (tokeny)
- Kafka (komunikacja między serwisami)
- Docker / Docker Compose

## Jak odpalić

docker compose up --build


Frontend wystartuje pod `http://localhost`, API pod `http://localhost:7777`.

