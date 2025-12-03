# Build Variants 및 환경 설정 명세서

## 1. 개요
이 문서는 애플리케이션의 개발, 검증(Stage), 운영(Production) 환경을 분리하고, 각 환경에 맞는 빌드 설정을 관리하기 위한 명세이다. 사용자의 요구사항에 따라 `Stage` 환경은 `Production`과 최대한 유사한 환경(Minify Enabled, Debuggable False)을 보장해야 하며, 개발 효율성을 위해 `Dev` 환경을 추가로 구성한다.

## 2. 목표
*   **환경 분리:** `dev`, `stage`, `prod` 세 가지 환경으로 분리하여 관리한다.
*   **빌드 최적화:** `stage`와 `prod`는 코드 난독화 및 최적화(Minify)를 적용하여 배포 환경과 동일한 조건을 검증한다.
*   **개발 편의성:** `dev` 환경은 디버깅이 용이하고 빌드 속도가 빠른 설정을 유지한다.
*   **앱 식별:** 각 환경별로 `applicationIdSuffix`를 적용하여 하나의 기기에 여러 버전을 동시에 설치할 수 있도록 한다.

## 3. 상세 요구사항

### 3.1 Product Flavors (환경 구성)
`flavorDimensions`를 "environment"로 설정하고 다음 3가지 Flavor를 정의한다.

1.  **dev (Development)**
    *   **목적:** 로컬 개발 및 기능 구현.
    *   **특징:** 빠른 빌드 속도, 디버깅 용이.
    *   **Package Name:** `com.example.fakeyoutube.dev` (Suffix: `.dev`)
    
2.  **stage (Staging)**
    *   **목적:** 배포 전 최종 검증 (QA).
    *   **특징:** **Production과 동일한 난독화 및 서명 설정**을 따르되, 서버나 로깅 설정만 분리 가능.
    *   **Package Name:** `com.example.fakeyoutube.stage` (Suffix: `.stage`)

3.  **prod (Production)**
    *   **목적:** 실제 스토어 배포.
    *   **특징:** 최적화 적용, 보안 강화.
    *   **Package Name:** `com.example.fakeyoutube` (Suffix: 없음)

### 3.2 Build Types (빌드 타입 구성)
Android 기본 `debug`와 `release` 타입을 활용하되, 사용자의 요구사항(Stage 환경의 Minify 적용)을 만족하기 위해 다음과 같이 조합하여 사용한다.

*   **Debug Type (`debug`)**
    *   `isMinifyEnabled = false`
    *   `isDebuggable = true`
    *   주 사용처: `devDebug` (개발용)

*   **Release Type (`release`)**
    *   `isMinifyEnabled = true` (R8 적용)
    *   `isShrinkResources = true`
    *   `isDebuggable = false`
    *   `proguardFiles` 적용
    *   주 사용처: `stageRelease` (검증용), `prodRelease` (배포용)

> **결정 사항:** 사용자가 언급한 "Stage 환경은 minify 옵션을 true로 하고 디버깅을 불가능하게"라는 조건은 `stage` Flavor와 `release` Build Type의 조합인 **`stageRelease`** 변형(Variant)을 통해 달성한다.

## 4. 기술 스택 및 변경 대상
*   **파일:** `app/build.gradle.kts`
*   **API:** Android Gradle Plugin (AGP) 8.x 이상 DSL

## 5. 구현 코드 (Skeleton)

### 5.1 `app/build.gradle.kts` 설정

```kotlin
android {
    // ... 기존 설정 유지

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            // 추후 API URL 등을 buildConfigField로 주입 가능
        }

        create("stage") {
            dimension = "environment"
            applicationIdSuffix = ".stage"
            versionNameSuffix = "-stage"
            // prod와 동일한 코드 최적화를 적용받기 위해 stageRelease Variant를 사용함
        }

        create("prod") {
            dimension = "environment"
            // Suffix 없음
        }
    }
}
```

## 6. 검증 계획 (Tests)
*   **빌드 테스트:**
    *   `./gradlew assembleDevDebug` 성공 여부 확인.
    *   `./gradlew assembleStageRelease` 성공 여부 확인.
    *   `./gradlew assembleProdRelease` 성공 여부 확인.
*   **설치 테스트:**
    *   `devDebug`와 `stageRelease` 앱이 동시에 설치되는지 확인 (Application ID 분리 확인).
*   **속성 검증:**
    *   `stageRelease` 빌드 결과물의 `BuildConfig.DEBUG` 값이 `false`인지 확인.
    *   `stageRelease` 빌드 시 난독화(Mapping file 생성)가 수행되는지 확인.
