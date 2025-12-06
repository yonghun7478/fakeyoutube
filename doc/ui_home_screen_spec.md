# 홈 화면 UI 구현 명세

## 1. 개요

이 문서는 FakeYouTube 애플리케이션의 홈 화면 UI 구현에 대한 명세입니다. 홈 화면은 사용자에게 추천 영상, Shorts 영상 등 다양한 콘텐츠를 표시하는 핵심적인 화면입니다.

## 2. 상세 요구사항

-   UI는 `doc/assets/contents.png` 디자인을 기반으로 합니다.
-   상단 탭 바(HomeTopBar)는 스크롤과 함께 움직여야 합니다.
-   화면은 크게 Shorts 섹션과 일반 영상 콘텐츠 섹션으로 구성됩니다.
-   실제 API 통신 전까지 UI 개발을 위해 임시 데이터(더미 데이터)를 사용합니다.
-   데이터는 데이터 레이어 -> 도메인 레이어 -> 프리젠테이션 레이어 순으로 전달되는 Clean Architecture를 따릅니다.
-   필요한 이미지 리소스는 '나노바나나 mcp'를 사용하여 생성합니다.

## 3. 기술 명세

### 3.1. 데이터 모델

-   **VideoInfo (일반 영상 정보)**
    -   `id`: String (고유 식별자)
    -   `thumbnailUrl`: String (썸네일 이미지 URL)
    -   `channelIconUrl`: String (채널 아이콘 이미지 URL)
    -   `title`: String (영상 제목)
    -   `channelName`: String (채널 이름)
    -   `views`: Int (조회수)
    -   `uploadDate`: String (업로드 날짜)

-   **ShortsInfo (Shorts 영상 정보)**
    -   `id`: String (고유 식별자)
    -   `thumbnailUrl`: String (썸네일 이미지 URL)
    -   `title`: String (Shorts 제목)
    -   `views`: Int (조회수)

### 3.2. 로직 흐름

1.  **ViewModel (`HomeViewModel`)**
    -   `FakeVideoRepository`로부터 영상 목록(`List<VideoInfo>`)과 Shorts 목록(`List<ShortsInfo>`)을 가져옵니다.
    -   가져온 데이터를 UI가 관찰할 수 있는 `StateFlow` 또는 `LiveData`에 저장합니다.

2.  **Repository (`FakeVideoRepository`)**
    -   `FakeVideoDataSource`로부터 더미 데이터를 가져옵니다.
    -   가져온 DTO를 Domain Model(Entity)로 변환하여 ViewModel에 전달합니다. (이번 단계에서는 DTO와 Entity를 동일하게 간주)

3.  **DataSource (`FakeVideoDataSource`)**
    -   미리 정의된 더미 `VideoInfo` 및 `ShortsInfo` 리스트를 반환합니다.

## 4. UI/UX

-   **컴포저블 구조**
    -   `HomeScreen` (Screen)
        -   `LazyColumn`
            -   `item { HomeTopBar() }`
            -   `items(videoList) { VideoItem() }` (Shorts 섹션 전)
            -   `item { ShortsSection(shortsList) }`
            -   `items(videoList) { VideoItem() }` (Shorts 섹션 후)

-   **컴포넌트**
    -   `HomeTopBar`: 상단 필터링 탭 (전체, 게임, 음악, ...)
    -   `VideoItem`: 일반 영상 썸네일, 제목, 채널 정보 등을 표시하는 카드
    -   `ShortsSection`: Shorts 로고와 함께 `LazyRow`로 Shorts 아이템 목록을 표시
    -   `ShortsItem`: Shorts 썸네일, 제목, 조회수를 표시하는 카드

## 5. 구현 체크리스트

-   [ ] `feature/home-screen` 브랜치 생성
-   [ ] 데이터 레이어: `FakeVideoDataSource` 구현 (더미 데이터 포함)
-   [ ] 데이터 레이어: `FakeVideoRepository` 구현
-   [ ] 도메인 레이어: `GetVideosUseCase` 구현
-   [ ] 도메인 레이어: `GetShortsUseCase` 구현
-   [ ] 프리젠테이션 레이어: `HomeViewModel` 구현 (UseCase와 연결)
-   [ ] UI: `VideoItem` 컴포저블 구현
-   [ ] UI: `ShortsItem` 컴포저블 구현
-   [ ] UI: `ShortsSection` 컴포저블 구현
-   [ ] UI: `HomeScreen`에서 `LazyColumn`을 사용하여 전체 레이아웃 구현
-   [ ] UI: `HomeScreen`과 `HomeViewModel` 연결
-   [ ] `HomeScreen` 미리보기(Preview) 구현
