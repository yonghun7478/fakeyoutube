# 홈 화면 (HomeScreen) 구현 명세서

## 1. 개요 (Overview)
이 문서는 FakeYouTube 프로젝트의 메인 화면인 `HomeScreen`의 구현 명세이다.
사용자는 앱 실행 시 홈 화면을 통해 추천 영상 리스트와 쇼츠(Shorts) 섹션을 확인할 수 있다.
현재 단계에서는 실제 API 통신 대신 더미(Dummy) 데이터를 사용하여 UI 구조와 스크롤 동작을 우선적으로 구현한다.

## 2. 요구사항 (User Stories / Requirements)
- **화면 구조**:
    - 상단바(Top Bar), 쇼츠 섹션, 비디오 리스트로 구성된다.
    - `docs/assets/contents.png`의 레이아웃을 따른다.
- **스크롤 동작**:
    - 화면 전체가 스크롤 가능해야 한다.
    - **중요**: 스크롤 시 상단바도 컨텐츠와 함께 위로 올라가서 시야에서 사라져야 한다. 이를 위해 상단바는 `LazyColumn`의 첫 번째 아이템으로 배치되어야 한다.
- **데이터**:
    - 네트워크 통신 없이 더미 데이터를 사용하여 화면을 렌더링한다.
    - Clean Architecture 원칙에 따라 Data -> Domain -> Presentation 레이어로 데이터가 흐르도록 한다.
- **리소스**:
    - 썸네일 및 프로필 이미지는 NanoBanana MCP 등을 사용하여 생성 및 적용한다.

## 3. 기술 상세 (Technical Details)

### 3.1 아키텍처 (Architecture)
- **MVVM + Clean Architecture** 패턴을 준수한다.

#### Domain Layer (Pure Kotlin)
- **Entities**:
    - `VideoEntity`: 비디오 ID, 제목, 채널명, 조회수, 업로드 시간, 썸네일 URL, 채널 이미지 URL.
    - `ShortsEntity`: 쇼츠 ID, 제목, 조회수, 썸네일 URL.
    - `HomeFeedItem`: UI 리스트 구성을 위한 Sealed Interface (VideoType, ShortsSectionType).
- **UseCase**:
    - `GetHomeFeedUseCase`: Repository로부터 데이터를 가져와 `HomeFeedItem` 리스트로 변환하여 반환한다.

#### Data Layer
- **Repository Interface**: `HomeRepository` (Domain 패키지에 정의)
- **Repository Implementation**: `HomeRepositoryImpl`
- **DataSource**: `LocalDummyDataSource` (하드코딩된 더미 비디오/쇼츠 데이터 반환)

#### Presentation Layer
- **ViewModel**: `HomeViewModel`
    - `StateFlow<HomeUiState>`를 노출.
    - `HomeUiState`: Loading, Success(List<HomeFeedItem>), Error.
- **UI (Compose)**:
    - `HomeScreen`: 전체 화면을 구성.
    - `LazyColumn` 구조:
        - `item`: TopBar (로고, 아이콘 등).
        - `item`: ShortsSection (가로 스크롤).
        - `items`: VideoItem 리스트.

### 3.2 UI 컴포넌트 상세
- **HomeTopBar**:
    - 좌측: YouTube 로고.
    - 우측: Cast, Notification, Search, Profile 아이콘.
- **ShortsSection**:
    - 헤더: 쇼츠 아이콘 + "Shorts" 텍스트.
    - 바디: `LazyRow`를 이용한 가로 스크롤 리스트.
- **VideoItem**:
    - 썸네일(AspectRatio 16:9), 채널 프사, 제목, 메타데이터 텍스트.

## 4. 구현 체크리스트 (Implementation Checklist)
- [ ] **Assets**: NanoBanana MCP를 사용하여 더미 이미지(썸네일, 프로필) 생성 및 리소스 추가
- [ ] **Domain**: `VideoEntity`, `ShortsEntity`, `HomeFeedItem` 정의
- [ ] **Domain**: `HomeRepository` 인터페이스 정의
- [ ] **Data**: `HomeRepositoryImpl` 및 `LocalDummyDataSource` 구현
- [ ] **Domain**: `GetHomeFeedUseCase` 구현
- [ ] **Presentation**: `HomeViewModel` 구현 (StateFlow 연동)
- [ ] **UI**: `HomeTopBar` 컴포저블 구현
- [ ] **UI**: `ShortsItem` 및 `ShortsSection` 컴포저블 구현
- [ ] **UI**: `VideoItem` 컴포저블 구현
- [ ] **UI**: `HomeScreen` 구현 (`LazyColumn` 내부에 TopBar 및 리스트 통합)
- [ ] **DI**: Hilt 모듈 설정 (Repository 주입)