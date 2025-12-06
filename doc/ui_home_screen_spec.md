# 홈 화면 컨텐츠 구성 및 UI 구현 명세서

## 1. 개요
본 명세서는 `HomeScreen`에 더미 데이터를 활용하여 실제 유튜브 앱과 유사한 컨텐츠 목록을 표시하는 기능을 정의한다.
참조 이미지(`doc/assets/contents.png`)를 기반으로 UI를 구성하며, 특히 상단바(TopBar)가 스크롤 시 컨텐츠와 함께 이동하도록 구조를 변경한다.

## 2. 요구사항 (User Stories)
*   **스크롤 동작 (Scroll Behavior):**
    *   상단바(`HomeTopBar`)는 화면 최상단에 고정되지 않고, 컨텐츠 스크롤 시 함께 위로 올라가야 한다.
    *   즉, `Scaffold`의 `topBar` 슬롯이 아닌, `LazyColumn`의 첫 번째 아이템으로 배치되어야 한다.
*   **컨텐츠 구성 (Content Structure):**
    *   **상단바:** 기존 `HomeTopBar` 재사용 (위치만 변경).
    *   **카테고리 칩(선택사항):** 스크린샷에 있다면 구현, 언급이 없다면 우선순위 낮음 (이번 이슈에서는 상단바와 영상 리스트 위주). *이슈 설명에 명시되지 않았으므로 제외하거나 추후 구현.*
    *   **쇼츠 섹션 (Shorts Section):**
        *   가로 스크롤 가능한 쇼츠 아이템 리스트.
        *   "Shorts" 로고와 아이콘이 섹션 헤더에 표시됨.
    *   **영상 리스트 (Video List):**
        *   일반적인 유튜브 영상 아이템이 세로로 나열됨.
        *   썸네일, 채널 아바타, 제목, 조회수, 업로드 시간, 메뉴 버튼 포함.
*   **데이터 (Data):**
    *   실제 API 통신 대신 **더미 데이터(Dummy Data)**를 생성하여 사용한다.
    *   `Domain Layer`에서 데이터를 정의하고 `Presentation Layer`로 전달한다.

## 3. UI/UX 상세
*   **참조 자산:** `doc/assets/contents.png`
*   **레이아웃 (HomeScreen):**
    *   `LazyColumn` 사용.
    *   Item 1: `HomeTopBar` (상단바)
    *   Item 2: `VideoItem` (영상 1)
    *   Item 3: `ShortsCarousel` (쇼츠 섹션)
    *   Item 4~N: `VideoItem` (영상 2 ~ N)
*   **컴포넌트 상세:**
    *   `VideoItem`:
        *   `Image` (썸네일, 16:9 비율, Placeholder 색상/아이콘 사용).
        *   `Row` (메타데이터 영역):
            *   `Image` (채널 프로필, 원형).
            *   `Column` (제목, 정보 텍스트).
            *   `IconButton` (메뉴 더보기).
    *   `ShortsCarousel`:
        *   Header: "Shorts" 텍스트 및 로고.
        *   Body: `LazyRow`로 쇼츠 아이템(`ShortsItem`) 배치.
    *   `ShortsItem`:
        *   세로 긴 직사각형 (9:16 비율).
        *   썸네일, 하단 텍스트(제목, 조회수).

## 4. 기술적 상세 (Technical Details)
*   **Architecture:** Clean Architecture + MVVM
*   **Data Layer:**
    *   `Video` Entity: `id`, `title`, `thumbnailUrl`, `channelName`, `channelAvatarUrl`, `views`, `postedAt`, `duration`.
    *   `Shorts` Entity: `id`, `title`, `thumbnailUrl`, `views`.
    *   `HomeRepository`: `getHomeContent(): Flow<List<HomeContent>>` (HomeContent는 Sealed Interface로 Video와 ShortsSection을 포함).
*   **Presentation Layer:**
    *   `HomeViewModel`: `StateFlow<HomeUiState>` 노출.
    *   `HomeUiState`: `Loading`, `Success(items: List<HomeContent>)`, `Error`.
*   **Refactoring:**
    *   `MainScreen.kt`: `Screen.Home`일 때 `Scaffold`의 `topBar`를 비활성화(null 처리)하고, `HomeScreen` 내부에서 그리도록 수정.

## 5. 구현 체크리스트 (Implementation Checklist)
- [ ] **Data Model 정의**
    - [ ] `domain/model/Video.kt` 생성
    - [ ] `domain/model/Shorts.kt` 생성
    - [ ] `domain/repository/HomeRepository.kt` 인터페이스 생성
- [ ] **Data Layer (Mock) 구현**
    - [ ] `data/repository/HomeRepositoryImpl.kt` (더미 데이터 제공) 생성
    - [ ] DI 설정 (`AppModule` or `RepositoryModule`)
- [ ] **ViewModel 구현**
    - [ ] `ui/viewmodel/HomeViewModel.kt` 생성
    - [ ] State 관리 (Loading, Success, Error)
- [ ] **UI 컴포넌트 구현**
    - [ ] `ui/components/VideoItem.kt` 구현
    - [ ] `ui/components/ShortsItem.kt` 및 `ShortsCarousel.kt` 구현
- [ ] **화면 조립 및 리팩토링**
    - [ ] `MainScreen.kt`: 홈 화면일 때 TopBar 숨김 처리
    - [ ] `HomeScreen.kt`: `LazyColumn` 적용, `HomeTopBar` 포함, ViewModel 연결
