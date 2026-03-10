# Real‑Time Price Tracker App

Android app for tracking **live stock prices** with a modern **Jetpack Compose** UI, modular architecture, and a reactive, clean‑style domain layer.

---

## Features

- **Real‑time stock price feed**
    - List of stocks with current price and percentage change
    - Tapping an item opens the stock detail screen
- **Stock detail screen**
    - Live price updates for a single symbol
    - Description / about section for the company
- **Modern UI**
    - Jetpack Compose + Material 3
    - Error banner / error screen components (`core:ui`)
    - Edge‑to‑edge layout
- **Reactive data flow**
    - Domain `ObserveStockDetailsUseCase` exposing flows of domain `Stock` models
    - ViewModels using Kotlin coroutines + StateFlow
- **DI & modularization**
    - Hilt for dependency injection
    - Separate modules for domain, data, network, UI, and the stock‑price feature

---

## Architecture

The project is split into multiple Gradle modules:

- **`app`**
    - Android application module (`com.mbank.price`)
    - Hosts `MainActivity`, navigation setup, and app theme wiring
    - Depends on `:domain`, `:data`, `:core:ui`, `:feature:stock-price`
- **`domain`**
    - Pure Kotlin module
    - Contains:
        - Domain models (e.g. `Stock`)
        - Use cases (e.g. `ObserveStockDetailsUseCase`)
        - Params (e.g. `StockParam`)
    - No Android dependency
- **`data`**
    - Android library
    - Implements repositories using `:core:network`
    - Bridges domain layer to network / persistence
- **`core:common`**
    - Pure Kotlin module for cross‑cutting concerns
    - Example: `AppResult` sealed type and shared models/utilities
- **`core:network`**
    - Android library for networking concerns
    - Uses OkHttp + Kotlinx Serialization
    - Hilt‑provided network layer abstractions
- **`core:ui`**
    - Android library with shared Compose components and theme:
        - `ErrorBanner`, `ErrorScreen`, typography / colors, etc.
- **`feature:stock-price`**
    - Android library implementing the stock‑price feature:
        - `StockPricesFeedScreen` (list/feed of stocks)
        - `StockDetailScreen` (detail view)
        - `StockDetailViewModel` + UI state
    - Depends on `:domain`, `:core:common`, `:core:ui`

High‑level data flow:

`Network` → `Data` (repositories) → `Domain` (use cases) → `Feature` ViewModels → Compose `UI`.

---

## Tech Stack

- **Language**
    - Kotlin
- **UI**
    - Jetpack Compose
    - Material 3
- **Navigation**
    - `navigation3` (experimental strongly‑typed navigation)
- **DI**
    - Hilt (`@AndroidEntryPoint`, `@HiltViewModel`)
- **Concurrency / Reactivity**
    - Kotlin coroutines
    - `StateFlow`
- **Networking & Serialization**
    - OkHttp
    - Kotlinx Serialization (`kotlinx.serialization.json`)
- **Other**
    - Java 11 bytecode target
    - Modular Gradle setup with version catalogs (`libs.*`)

---

## Project Structure

RealTimePriceTrackerApp/
app/                  # Main Android app module
domain/               # Domain models & use cases
data/                 # Data layer & repositories
core/
common/             # Shared models & utilities (e.g. AppResult)
network/            # Networking abstractions, OkHttp, serialization
ui/                 # Reusable Compose UI components and theme
feature/
stock-price/        # Stock feed + stock detail feature
