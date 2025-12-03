# 기반 코드 및 네비게이션 구조 명세 (Base Setup Spec)

## 1. 개요
이 명세서는 'FakeYouTube' 프로젝트의 초기 기반 코드를 정의합니다. 사용자가 제공한 스크린샷을 바탕으로 UI 레이어의 뼈대를 구축하며, 테마 설정, 네비게이션 구조, 그리고 주요 탭 화면의 스켈레톤 코드를 포함합니다. MVVM 아키텍처를 지향하나, 현 단계에서는 ViewModel과 Data Layer를 제외한 UI 전용 코드만 작성합니다.

## 2. 목표
*   **Theme Setup:** YouTube 스타일의 색상과 타이포그래피를 적용한 Material3 테마 구성.
*   **Navigation:** Jetpack Compose Navigation을 사용한 5개의 메인 탭(홈, 쇼츠, 업로드, 구독, 보관함) 이동 구조 구현.
*   **Layout:** `Scaffold`를 활용하여 상단 앱바(`TopAppBar`)와 하단 네비게이션 바(`BottomNavigationBar`) 배치.
*   **Skeleton UI:** 각 화면은 `Text` 컴포저블을 사용하여 현재 화면의 이름을 출력하는 최소한의 형태로 구현.

## 3. 화면 분석 및 요구사항
스크린샷 분석 결과, 앱은 다음과 같은 구조를 가집니다.

### 3.1 하단 네비게이션 (Bottom Navigation)
총 5개의 탭으로 구성됩니다.
1.  **홈 (Home):** 메인 비디오 피드.
2.  **쇼츠 (Shorts):** 짧은 동영상 피드.
3.  **업로드 (+):** 동영상 업로드 (스크린샷 중앙의 `+` 아이콘).
4.  **구독 (Subscriptions):** 구독한 채널의 영상 목록.
5.  **보관함/나 (You):** 프로필 및 시청 기록 (우측 하단 프로필 아이콘).

### 3.2 상단 앱바 (Top App Bar) - 홈 화면 기준
*   **좌측:** YouTube 로고.
*   **우측:** 캐스트(Cast), 알림(Notification), 검색(Search), 프로필(Profile) 아이콘.

## 4. 상세 설계

### 4.1 패키지 구조
```text
com.example.fakeyoutube
├── MainActivity.kt
├── ui
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── navigation
│   │   └── AppNavigation.kt
│   ├── screens
│   │   ├── HomeScreen.kt
│   │   ├── ShortsScreen.kt
│   │   ├── UploadScreen.kt
│   │   ├── SubscriptionScreen.kt
│   │   └── LibraryScreen.kt
│   └── components
│       ├── BottomNavigationBar.kt
│       └── HomeTopBar.kt
```

### 4.2 Route 정의
Navigation Route는 Type-safe하게 관리하거나 상수로 정의합니다.
```kotlin
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Shorts : Screen("shorts", "Shorts", Icons.Filled.PlayArrow) // 아이콘 대체 필요 가능
    object Upload : Screen("upload", "Upload", Icons.Filled.AddCircle)
    object Subscription : Screen("subscription", "Subscription", Icons.Filled.Subscriptions)
    object Library : Screen("library", "You", Icons.Filled.AccountCircle)
}
```

### 4.3 메인 화면 구성 (MainScreen)
`Scaffold`를 사용하여 TopBar와 BottomBar를 배치합니다.
*   `TopBar`: 현재 Route가 `Home`일 때만 표시하도록 조건부 렌더링을 고려합니다 (또는 화면별 Scaffold 적용).
*   `BottomBar`: 항상 표시.

```kotlin
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { 
            // 현재 목적지에 따라 가시성 조정 로직 필요할 수 있음
            HomeTopBar()
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Shorts.route) { ShortsScreen() }
            // ... 기타 화면들
        }
    }
}
```

### 4.4 컴포넌트 스텁 (Component Stubs)
각 화면은 중앙에 화면 이름을 표시하는 텍스트만 포함합니다.
```kotlin
@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Home Screen")
    }
}
```

## 5. 구현 계획 (Step-by-Step)
1.  **Project Setup:** `build.gradle` 의존성 확인 (Navigation Compose, Material3).
2.  **Theme:** YouTube 브랜드 컬러(Red #FF0000 등)를 포함한 Color Palette 및 Theme 설정.
3.  **Navigation:** `Screen` sealed class 정의 및 `BottomNavigationBar` 컴포저블 구현.
4.  **Screens:** 5개의 빈 화면(Composable) 생성.
5.  **Main Composition:** `MainActivity`에 `NavHost`와 `Scaffold` 연결.
6.  **TopBar:** 홈 화면 상단 바 구현 (아이콘 배치).

## 6. 테스트 시나리오
*   앱 실행 시 'Home Screen' 텍스트가 중앙에 표시되어야 한다.
*   하단 'Shorts' 탭 클릭 시 화면 중앙 텍스트가 'Shorts Screen'으로 변경되어야 한다.
*   상단 바에 로고와 4개의 우측 아이콘이 표시되어야 한다.

## 7. 시각적 참조 (Visual References)
다음은 구현의 기준이 되는 UI 스크린샷입니다.

### 7.1 헤더 및 하단 네비게이션 (Header & Bottom Navigation)
상단 앱바(TopAppBar)와 하단 네비게이션 바(BottomNavigationBar)의 디자인 및 배치를 참조합니다.
![Header and Bottom Bar](assets/header-bottom.png)

### 7.2 메인 콘텐츠 영역 (Content Area)
비디오 피드 리스트 등 메인 콘텐츠 영역의 레이아웃을 참조합니다.
![Main Contents](assets/contents.png)