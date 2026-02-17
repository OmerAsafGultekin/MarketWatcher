# Real-Time Crypto Trend Analysis (MarketWatcher)

A backend service built with Java 21 and Spring Boot that tracks cryptocurrency prices in real-time, calculates Simple Moving Average, and determines market trends.

## Motivation
As a 2nd-year computer engineering student, I built this project to move beyond standard CRUD applications. My goal was to gain hands-on experience with scheduled background tasks, asynchronous external API calls, in-memory data structures, and containerization using Docker and PostgreSQL.

## Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3 (basic)
* **Database:** PostgreSQL 15, Spring Data JPA
* **API Calls:** Spring WebFlux (WebClient)
* **Containerization:** Docker, Docker Compose (basic)

## Core Features
* **Scheduled Data Fetching:** Automatically pulls real-time prices for tracked symbols (BTC, ETH, SOL) from the Binance REST API every minute.
* **In-Memory Trend Analysis:** Uses standard Java `ArrayDeque` to maintain a rolling price history and calculate the SMA (Simple Moving Average) without hitting the database for every calculation.
* **Database Cleanup:** Includes a scheduled maintenance job that automatically deletes historical records older than 1 hour to prevent the database from bloating over time.
* **Dockerized Setup:** The application and PostgreSQL database are fully containerized. A health check ensures the database is ready before the application starts.

## How to Run

You only need Docker and Docker Compose installed on your system.

1. Clone the repository:
   ```bash
   git clone https://github.com/OmerAsafGultekin/MarketWatcher.git
   cd MarketWatcher
   ```
2. Start the services:
   ```bash
   docker-compose up -d --build
   ```
3. Check the application logs:
   ```bash
   docker logs -f market-app-container
   ```

## REST API

The service exposes an endpoint to get the latest calculated trend for each tracked cryptocurrency.

**GET** `http://localhost:8080/api/prices`

**Response Example:**
```json
[
  {
    "id": 18,
    "symbol": "SOLUSDT",
    "price": 84.77,
    "trend": "UPTREND",
    "createdAt": "2026-02-13T19:53:39.230882"
  },
  {
    "id": 17,
    "symbol": "ETHUSDT",
    "price": 2059.45,
    "trend": "UPTREND",
    "createdAt": "2026-02-13T19:53:38.96417"
  },
  {
    "id": 16,
    "symbol": "BTCUSDT",
    "price": 69019.48,
    "trend": "DOWNTREND",
    "createdAt": "2026-02-13T19:53:38.69564"
  }
]
```
