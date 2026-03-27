# CallApp — Calling App

A modern, high-performance Android Dialer and Contacts application built with Jetpack Compose and Clean Architecture principles. This app provides a seamless interface for managing contacts and viewing call history with real-time synchronization.

## Architecture

**Pattern:** MVVM with a unidirectional data flow via `StateFlow`.

**Key design decision:** A single shared `CallViewModel` is scoped to the nav graph and owns the `CallState` sealed class. This is the single source of truth for all call transitions. Individual screen ViewModels (`DialPadViewModel`, `ActiveCallViewModel`) only manage their own local UI state (toggles, input).

## Call State Machine

# 📞 NightDial — Real Calling App

A fully functional Android calling application built in **Kotlin + Jetpack Compose** as part of an internship assignment. The app places **real phone calls** using Android's native telephony APIs — no fake screens, no mocked call flows.

---

## 📸 Screens

| Keypad | Recents | Contacts | Active Call |
|--------|---------|----------|-------------|
| Dial any number with formatted input | Full call log with type, duration & timestamp | Device contacts with search | Live timer, mute, speaker controls |

---

## ✅ Features

### 📱 Dialer / Keypad
- Full numeric keypad (0–9, `*`, `#`)
- Real-time number formatting as you type
- Long-press backspace to clear entire input
- Call button activates only when a number is entered
- Places a **real phone call** via `Intent.ACTION_CALL`

### 📋 Call Logs (Recents)
- Reads device's actual call history via `CallLog.Calls` content provider
- Displays contact name (if available), phone number, call type (Incoming / Outgoing / Missed), date, time, and duration
- **Auto-refreshes after every call ends** — listens to `TelephonyManager` call state and reloads when `CALL_STATE_IDLE` is detected post-call
- Tap any log entry to call that number again

### 👤 Contacts
- Fetches real device contacts via `ContactsContract`
- Alphabetically sorted, deduplicated by number
- Live search with 300ms debounce
- Avatar initials generated from contact name
- Tap the call icon to place a real call instantly

### ⏱️ Active Call Screen
- Shown when a call is initiated from any screen
- Detects actual call state via `TelephonyManager` / `TelephonyCallback`
- **Live call duration timer** starts automatically when call connects (`CALL_STATE_OFFHOOK`)
- Timer stops and resets on call end (`CALL_STATE_IDLE`)
- Pulsing avatar animation while call is active
- Mute and Speaker toggles (UI state)
- End call button — system handles the actual termination

---

## 🏗️ Architecture

Clean Architecture + MVVM, structured as:

```
com.example.democallapp
├── data
│   └── source
│       ├── CallLogDataSource.kt       # Reads CallLog.Calls via ContentResolver
│       ├── ContactsDataSource.kt      # Reads ContactsContract via ContentResolver
│       └── TelephonyDataSource.kt     # Observes call state via TelephonyManager
├── domain
│   ├── model
│   │   ├── Contact.kt
│   │   ├── CallLogsEntry.kt
│   │   └── RealCallState.kt           # Idle | Ringing | Active
│   ├── repos
│   │   ├── ContactRepository.kt
│   │   ├── CallLogRepository.kt
│   │   └── CallStateRepository.kt
│   └── usecase
│       ├── GetContactsUseCase.kt
│       ├── GetCallLogsUseCase.kt
│       ├── ObserveCallStateUseCase.kt
│       └── BuildCallIntentUseCase.kt
├── ui
│   ├── screens
│   │   ├── dialpad/                   # DialPadScreen + DialPadViewModel
│   │   ├── calllogs/                  # CallLogsScreen + CallLogsViewModel
│   │   ├── contacts/                  # ContactsScreen + ContactsViewModel
│   │   ├── active/                    # ActiveCallScreen + ActiveCallViewModel
│   │   └── components/                # Shared UI components
│   └── theme/                         # NightDial color system + typography
└── modules
├── AppModule.kt                   # ContentResolver provision
└── RepositoryModule.kt            # Hilt bindings
```

**Data flow:** `DataSource → Repository → UseCase → ViewModel → UI (StateFlow)`

---

## 🔐 Permissions

| Permission | Purpose |
|---|---|
| `CALL_PHONE` | Place real phone calls via `Intent.ACTION_CALL` |
| `READ_CONTACTS` | Fetch device contacts from `ContactsContract` |
| `READ_CALL_LOG` | Read call history from `CallLog.Calls` |
| `READ_PHONE_STATE` | Observe live call state for timer and auto-refresh |

All permissions are requested at **runtime** at the point of use. Denied permissions show a prompt to grant access — no crashes.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| DI | Hilt |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines + Flow |
| Architecture | MVVM + Clean Architecture |
| State | `StateFlow` + `SharedFlow` |
| Telephony | `TelephonyManager`, `TelephonyCallback` (API 31+), `PhoneStateListener` (legacy fallback) |
| Data | Android Content Providers (`ContactsContract`, `CallLog.Calls`) |
| Font | DM Sans |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- Android device or emulator with **API 26+**
- A physical device is strongly recommended for real call testing

### Build & Run
```bash
git clone <repo-url>
cd democallapp
```
Open in Android Studio → Run on device (`Shift+F10`)

### First Launch
1. Grant **Contacts** permission on the Contacts tab
2. Grant **Call Log** + **Phone State** permissions on the Recents tab
3. Dial a number on the Keypad and tap the green call button — grant **Call Phone** permission when prompted

---

## 📐 Design System

Custom dark theme — **NightDial** — with a consistent color palette:


Background     #060202   Deep near-black
Surface        #141418   Card/bar backgrounds
AccentGreen    #00E676   Primary action color
DestructiveRed #FF3B30   End call
OnSurface      #EEEEF2   Primary text
OnSurfaceMuted #888899   Secondary text


Typography uses **DM Sans** across all weights (Light → Bold).

---

## 📋 Assignment Compliance

| Requirement | Status |
|---|---|
| Real calls via `Intent.ACTION_CALL` | ✅ |
| Numeric keypad (0–9, *, #) | ✅ |
| Call logs with name, number, type, date, duration | ✅ |
| Tap log entry to redial | ✅ |
| Device contacts via `ContactsContract` | ✅ |
| Tap contact to call | ✅ |
| Live call duration timer | ✅ |
| Auto-refresh call logs after call ends | ✅ |
| Runtime permission handling | ✅ |
| Jetpack Compose UI | ✅ |
| MVVM architecture | ✅ |
| Kotlin only | ✅ |
| No fake/mock calling | ✅ |

---

## ⚠️ Known Limitations

- **Mute / Speaker** toggles update UI state only — actual audio routing requires `AudioManager` integration (out of scope for this assignment)
- **Incoming call screen** is not implemented — Android handles incoming calls natively via the system dialer
- Auto-refresh only triggers for calls made while the app is open; calls made outside the app require manual navigation to Recents to see updated logs

Idle → Calling → Ringing → Active → Ended → Idle

- `Idle`: Default state, dial pad visible
- `Calling`: Outgoing call screen shown, simulated incoming triggers after 3s
- `Ringing`: Incoming call screen shown
- `Active`: Call accepted, timer starts
- `Ended`: Brief ended state, auto-resets to Idle

Navigation between call screens is driven entirely by `LaunchedEffect` observing `CallState` — no screen manually navigates itself.

## Project Structure
```
app/
├── data/
│   └── repository/
│       └── CallRepositoryImpl.kt
├── di/
│   └── RepositoryModule.kt
├── domain/
│   ├── model/
│   │   ├── CallState.kt
│   │   └── Contact.kt
│   └── repository/
│       └── CallRepository.kt
├── navigation/
│   ├── NavigationState.kt
│   └── CallApp.kt
└── ui/
    ├── call/
    │   └── CallViewModel.kt          ← shared, nav-graph scoped
    ├── components/
    │   ├── CallActionButton.kt
    │   ├── CallButtons.kt
    │   ├── ContactAvatar.kt
    │   ├── DialKey.kt
    │   └── EncryptedBadge.kt
    ├── screens/
    │   ├── dialpad/
    │   │   ├── DialPadViewModel.kt
    │   │   └── DialPadScreen.kt
    │   ├── outgoing/
    │   │   └── OutgoingCallScreen.kt
    │   ├── incoming/
    │   │   └── IncomingCallScreen.kt
    │   └── activecall/
    │       ├── ActiveCallViewModel.kt
    │       └── ActiveCallScreen.kt
    └── theme/
        └── CallAppTheme.kt
```

## Fonts

Place DM Sans font files in `res/font/`:
- `dm_sans_light.ttf`
- `dm_sans_regular.ttf`
- `dm_sans_medium.ttf`
- `dm_sans_semibold.ttf`
- `dm_sans_bold.ttf`

Download from [Google Fonts — DM Sans](https://fonts.google.com/specimen/DM+Sans)

## Simulating Incoming Call

Tap the video icon (left of call button on dial pad) to trigger an immediate simulated incoming call. Alternatively, dial any number and press Call — an incoming call will be auto-simulated after 3 seconds.

## Screenshots

| Dial Pad | Outgoing Call | Incoming Call | Active Call |
|----------|---------------|---------------|-------------|
| <img src="app/src/main/res/screenshots/dialpad.png" width="200"/> | <img src="app/src/main/res/screenshots/outgoing.png" width="200"/> | <img src="app/src/main/res/screenshots/incoming.png" width="200"/> | <img src="app/src/main/res/screenshots/active_call.png" width="200"/> |

## Bonus Features Implemented

- Jetpack Compose ✓
- Contact name mapping via `CallRepositoryImpl.contactDirectory` ✓
- Pulse animation on avatar during ringing and active call ✓
- Smooth animated dot on "Calling..." screen ✓
- Encrypted connection badge (both incoming and active screens) ✓