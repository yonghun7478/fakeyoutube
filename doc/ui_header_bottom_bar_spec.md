# 상단바 및 하단바 UI 개선 명세서

## 1. 개요
현재 구현된 상단바(Top Bar)와 하단바(Bottom Bar)가 참조 이미지(`doc/assets/header-bottom.png`)와 시각적으로 차이가 있다는 이슈가 제기됨.
본 명세서는 해당 스크린샷을 정밀 분석하여, 레이아웃, 아이콘 종류, 간격, 색상을 원본 유튜브 앱(스크린샷)과 최대한 동일하게 수정하는 것을 목표로 한다.

## 2. 요구사항 (User Stories)
*   **상단바 (Header):**
    *   좌측 상단에 **YouTube Premium** 로고(텍스트 포함)가 배치되어야 한다.
    *   우측 상단에 액션 아이콘들이 다음 순서로 배치되어야 한다: **Cast(전송) -> Notification(알림, 9+ 뱃지) -> Search(검색)**.
    *   **주의:** 상단바 우측 끝에 프로필 아이콘은 존재하지 않는다. (스크린샷 기준)
    *   배경색은 스크린샷에 맞춰 설정되어야 한다 (흰색 배경, Light Mode 기준).
*   **하단바 (Bottom Navigation):**
    *   5개의 주요 탭이 순서대로 배치되어야 한다: **홈(Home), 쇼츠(Shorts), 만들기(+), 구독(구독 채널), 마이페이지(My Page)**.
    *   **마이페이지** 탭은 사용자의 프로필 이미지(또는 이니셜 아이콘)를 아이콘으로 사용한다.
    *   가운데 '만들기(+)' 버튼은 회색 원형 배경에 `+` 기호가 있는 형태여야 한다.
    *   선택된 탭과 비선택된 탭의 아이콘 스타일 구분(Filled vs Outlined)이 명확해야 한다.
    *   라벨 텍스트의 크기(font size)와 아이콘과의 간격이 스크린샷과 유사해야 한다.

## 3. UI/UX 상세
*   **참조 자산:** `doc/assets/header-bottom.png`
*   **Top Bar:**
    *   높이: 표준 TopAppBar 높이 준수하되 시각적 비율 확인.
    *   Logo: `YouTube Premium` 로고 이미지 또는 벡터 사용.
    *   Actions: 아이콘 3개 (Cast, Notification, Search). Notification 아이콘 위에는 빨간색 뱃지(9+)가 있어야 함.
*   **Bottom Bar:**
    *   높이: 표준 NavigationBar 높이.
    *   Icons:
        1.  홈: 집 모양 (Filled/Outlined)
        2.  쇼츠: Shorts 전용 로고
        3.  만들기: 원형 회색 배경 + 십자가(+) 아이콘
        4.  구독: 구독함 아이콘 (Filled/Outlined)
        5.  마이페이지: 원형 프로필 이미지
    *   Label: '홈', '쇼츠', '', '구독', '마이페이지' (만들기 버튼은 라벨이 없을 수 있음, 스크린샷 확인 필요) -> *스크린샷 재확인 결과: 만들기 버튼 하단 라벨 없음. 나머지 탭은 라벨 존재.*

## 4. 기술적 상세 (Technical Details)
*   **Architecture:** Presentation Layer (Jetpack Compose).
*   **Components:**
    *   `MainScreen.kt`: `Scaffold` 구성.
    *   `HomeTopBar.kt`: 상단바 구현. `TopAppBar` 사용.
    *   `BottomNavigationBar.kt`: 하단바 구현. `NavigationBar` 및 `NavigationBarItem` 사용.
*   **Resources:**
    *   YouTube Premium 로고 (Drawable 확보 필요).
    *   Material Symbols (Cast, Notifications, Search, Home, Subscriptions).
    *   Custom Vector (Shorts Logo).

## 5. 구현 체크리스트 (Implementation Checklist)
- [ ] `doc/assets/header-bottom.png` 재분석 및 자산 준비
    - [ ] YouTube Premium 로고 확보
    - [ ] Shorts 아이콘 확보
- [ ] 상단바(TopBar) UI 수정 (`HomeTopBar.kt`)
    - [ ] 로고 교체 (YouTube -> YouTube Premium)
    - [ ] 우측 아이콘 구성 변경 (프로필 제거, 3개 아이콘 배치)
    - [ ] 알림 아이콘에 뱃지(Badge) 추가 구현
- [ ] 하단바(BottomBar) UI 수정 (`BottomNavigationBar.kt`)
    - [ ] NavigationItem 구조체 변경 (5개 탭)
    - [ ] 5번째 탭을 '마이페이지'로 변경하고 프로필 이미지 적용
    - [ ] 3번째 '만들기' 탭 스타일링 (원형 배경, 라벨 없음)
    - [ ] 탭 선택/미선택 아이콘 리소스 연결
- [ ] 레이아웃 검증
    - [ ] Scaffold 패딩 및 전체 배치 확인
    - [ ] 스크린샷과 대조 (Pixel Perfect 지향)