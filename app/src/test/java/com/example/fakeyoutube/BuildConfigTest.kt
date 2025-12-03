package com.example.fakeyoutube

import org.junit.Test
import org.junit.Assert.*

class BuildConfigTest {
    @Test
    fun verifyBuildConfigFieldsExist() {
        // 이 테스트는 Build Variants 설정에 따라 BuildConfig가 올바르게 생성되는지 검증합니다.
        // 컴파일 시 선택된 Variant(예: devDebug)에 따라 값이 달라지므로,
        // 여기서는 필드의 존재 여부와 예상되는 값의 범위를 확인합니다.
        
        // FLAVOR 필드 검증
        val flavor = BuildConfig.FLAVOR
        assertTrue(
            "Flavor should be one of dev, stage, prod",
            flavor == "dev" || flavor == "stage" || flavor == "prod"
        )

        // BUILD_TYPE 필드 검증
        val buildType = BuildConfig.BUILD_TYPE
        assertTrue(
            "Build Type should be debug or release",
            buildType == "debug" || buildType == "release"
        )

        // DEBUG flag 검증
        if (buildType == "debug") {
            assertTrue(BuildConfig.DEBUG)
        } else {
            assertFalse(BuildConfig.DEBUG)
        }
    }
}