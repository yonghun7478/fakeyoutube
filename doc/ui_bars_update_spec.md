# 상단바 및 하단바 UI 개선 명세서

## 1. 개요
현재 구현된 앱의 상단바(Header)와 하단 내비게이션 바(Bottom Navigation)를 실제 YouTube 안드로이드 앱의 UI(제공된 두 번째 스크린샷)와 최대한 유사하게 개선한다.

## 2. 요구사항 (User Stories)

### 2.1 상단바 (Top Bar)
- **로고:** 좌측 상단에 YouTube 로고(텍스트 포함 또는 아이콘)가 표시되어야 한다.
- **아이콘:** 우측 상단에 다음 아이콘들이 순서대로 배치되어야 한다.
  1. Cast (전송/크롬캐스트 아이콘)
  2. Notification (알림/종 모양)
  3. Search (검색/돋보기)
  4. Profile (프로필/원형 아바타)
- **스타일:** 배경은 앱 테마를 따르며(Light/Dark), 고정된 높이와 적절한 패딩을 가진다.

### 2.2 하단 내비게이션 (Bottom Navigation)
- **메뉴 구성:** 다음 5가지 항목으로 구성한다.
  1. **Home (홈):** 집 모양 아이콘
  2. **Shorts:** Shorts 전용 아이콘
  3. **Create (+):** 원형 안에 플러스(+) 기호 또는 단순 플러스 아이콘
  4. **Subscriptions (구독):** 구독 목록 아이콘
  5. **You (나/보관함):** 프로필 사진 또는 라이브러리 아이콘
- **동작:** 탭 클릭 시 해당 화면으로 전환되어야 하며, 아이콘 및 라벨의 선택/비선택 상태 스타일이 적용되어야 한다.
- **레이아웃:** 화면 하단에 고정되며 스크린샷과 유사한 비율과 간격을 유지한다.

## 3. 기술적 세부사항 (Technical Details)

### 3.1 UI 컴포넌트 (Jetpack Compose)
- **TopAppBar:** `CenterAlignedTopAppBar` 혹은 `TopAppBar`를 커스터마이징하여 구현.
  - `actions` 슬롯을 활용하여 우측 아이콘 배치.
  - `title` 슬롯(또는 `navigationIcon` 슬롯)을 활용하여 좌측 로고 배치.
- **NavigationBar:** Material3 `NavigationBar` 및 `NavigationBarItem` 사용.
  - 아이콘 리소스는 `androidx.compose.material.icons` 또는 커스텀 Vector Asset(`res/drawable`)을 사용.

### 3.2 리소스
- 필요한 아이콘이 `Icons.Default`나 `Icons.Outlined`에 없는 경우, `res/drawable`에 벡터 이미지를 추가해야 함 (특히 YouTube 로고 및 Shorts 아이콘).

## 4. 구현 체크리스트 (Implementation Checklist)
- [ ] 필요한 아이콘 리소스(YouTube 로고, Shorts 아이콘 등) 확보 및 `res/drawable` 추가
- [ ] 상단바(`YouTubeTopAppBar`) Composable 구현
  - [ ] 좌측 로고 배치
  - [ ] 우측 액션 아이콘들(Cast, Notification, Search, Profile) 배치
- [ ] 하단바(`YouTubeBottomNavigation`) Composable 구현
  - [ ] 5개 탭(Home, Shorts, Create, Subscriptions, You) 구성
  - [ ] 선택/비선택 상태 처리
- [ ] 메인 스크린(`Scaffold`)에 개선된 상단바 및 하단바 적용
- [ ] UI 디자인 검수 (스크린샷과 비교)