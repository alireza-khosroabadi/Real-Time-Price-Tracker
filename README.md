## Real‑Time Price Tracker App

Small Android app that shows **live stock prices** for 25 symbols using a **WebSocket echo server** and **Jetpack Compose**. Tap a stock in the list to see its details.

---

## Features

- 25 predefined stock symbols (AAPL, GOOG, TSLA, NVDA, etc.)
- Real‑time price updates every 2 seconds via WebSocket
- Sorted by current price (highest first)
- Price direction with arrows (↑ / ↓ / →)
- Detail screen with price + description
- Connection indicator + Start/Stop toggle in the top bar
- Price flash (green/red) for 1s after a change
- Deep link: `stocks://symbol/{symbol}` → opens detail screen

---

## Tech Stack

- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Kotlin Flow + immutable UI state
- **Navigation**: Navigation Compose `NavHost` (feed + details)
- **Data**: OkHttp WebSocket, Kotlinx Serialization
- **DI**: Hilt
- **Testing**: JUnit4, Mockito, kotlinx‑coroutines‑test, Turbine

---

## Architecture Overview

- `OkHttpWebSocketDataSource`  
  Handles WebSocket connect/reconnect, generates random prices, sends them, and emits echoed updates as a `Flow`.

- `StockPriceRepositoryImpl`  
  Combines:
    - price updates,
    - connection status,
    - running flag  
      into domain models (`StockPriceFeed`, `Connection`) wrapped in `AppResult`.

- **Use cases**
    - `ObserveStockPricesUseCase`
    - `ObserveConnectionStatusUseCase`
    - `ObserveStockDetailsUseCase`
    - `StartStreamStockPricesUseCase`
    - `StopStreamStockPricesUseCase`

- **ViewModels**
    - `StockPriceFeedViewModel`
        - Exposes feed + connection UI state.
        - Starts the feed on init, toggles start/stop.
    - `StockDetailViewModel`
        - Uses `SavedStateHandle` to read `symbol` from nav args.
        - Observes a single stock’s updates.

- **UI**
    - `StockPricesFeedScreen` – list of stocks in a `LazyColumn`.
    - `StockDetailScreen` – price card + description.
    - Reusable price components (`PriceText`, `PercentageText`).

---

## How to Run

### Requirements

- Android Studio (recent)
- JDK 11+
- Android SDK:
    - `compileSdk = 36`
    - `minSdk = 24`

### Build & Run

./gradlew assembleDebug