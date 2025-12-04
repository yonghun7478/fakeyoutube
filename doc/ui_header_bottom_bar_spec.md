# 상단바 및 하단바 UI 개선 명세서

## 1. 개요
현재 구현된 상단바(Top Bar)와 하단바(Bottom Bar)가 참조 이미지(`doc/assets/header-bottom.png`)와 시각적으로 차이가 있다는 이슈가 제기됨.
본 명세서는 해당 스크린샷을 정밀 분석하여, 레이아웃, 아이콘 종류, 간격, 색상을 원본 유튜브 앱(스크린샷)과 최대한 동일하게 수정하는 것을 목표로 한다.

## 2. 요구사항 (User Stories)
*   **상단바 (Header):**
    *   좌측 상단에 YouTube 로고(텍스트 포함)가 스크린샷과 동일한 비율과 위치에 배치되어야 한다.
    *   우측 상단에 액션 아이콘들(Cast/전송, 알림, 검색)이 정확한 순서로 배치되어야 한다.
    *   우측 끝에 프로필 아이콘(원형)이 배치되어야 한다.
    *   배경색은 스크린샷에 맞춰 설정되어야 한다 (일반적으로 다크모드/라이트모드에 따름).
*   **하단바 (Bottom Navigation):**
    *   5개의 주요 탭이 순서대로 배치되어야 한다: 홈(Home), 쇼츠(Shorts), 만들기(Create/Plus), 구독(Subscriptions), 보관함/나(You).
    *   가운데 '만들기(+)' 버튼이 스크린샷의 스타일(원형 테두리 유무 등)을 따라야 한다.
    *   선택된 탭과 비선택된 탭의 아이콘 스타일 구분(Filled vs Outlined)이 명확해야 한다.
    *   라벨 텍스트의 크기(font size)와 아이콘과의 간격이 스크린샷과 유사해야 한다.

## 3. UI/UX 상세
*   **참조 자산:** `doc/assets/header-bottom.png`
*   **Top Bar:**
    *   높이: 표준 TopAppBar 높이 준수하되 시각적 비율 확인.
    *   Logo: 벡터 드로어블 사용 권장.
    *   Actions: 아이콘 간 패딩 값 조정 필요.
*   **Bottom Bar:**
    *   높이: 표준 NavigationBar 높이.
    *   Icons: Material Symbols 또는 커스텀 벡터 사용.
    *   Shorts 아이콘: 유튜브 특유의 로고 형상 확인 필요.

## 4. 기술적 상세 (Technical Details)
*   **Architecture:** Presentation Layer (Jetpack Compose).
*   **Components:**
    *   `MainScreen.kt` (또는 `AppScaffold`): `Scaffold`의 `topBar`, `bottomBar` 파라미터 구성.
    *   `TopBar.kt`: Composable 함수로 분리하여 구현.
    *   `BottomNavBar.kt`: Composable 함수로 분리하여 구현.
*   **Dependencies:**
    *   `androidx.compose.material3` 사용.
    *   아이콘 리소스: `androidx.compose.material.icons` 또는 `res/drawable`.

## 5. 구현 체크리스트 (Implementation Checklist)
- [ ] `doc/assets/header-bottom.png` 정밀 분석 (아이콘 순서, 여백 확인)
- [ ] 필요한 벡터 아이콘 리소스 확보 (Shorts 아이콘 등)
- [ ] 상단바(TopBar) UI 수정 구현
    - [ ] 로고 배치 및 크기 조정
    - [ ] 우측 아이콘 3종(Cast, Noti, Search) + 프로필 아이콘 배치 및 간격 조정
- [ ] 하단바(BottomBar) UI 수정 구현
    - [ ] NavigationItem 5개 구성 (Home, Shorts, Create, Subscriptions, You)
    - [ ] 중앙 (+) 버튼 스타일링 (원형 테두리 등 특이사항 반영)
    - [ ] 탭 선택 시/미선택 시 아이콘 변경 로직 확인
- [ ] 전체 화면(Scaffold) 통합 및 레이아웃 검증
- [ ] 최종 빌드 후 스크린샷과 비교 확인