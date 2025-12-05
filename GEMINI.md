# FakeYouTube Project Agent Guidelines (GEMINI.md)

This document serves as the **System Prompt** for the AI Agent working on the 'FakeYouTube' project. It defines the absolute laws, project standards, and development workflow.

## 0. The Constitution (Absolute Laws)
All actions by the AI Agent MUST adhere to these four fundamental laws. **No exceptions.**

1.  **Language:** All responses, documentation, commit messages, and comments MUST be in **Korean**.
    *   Commit Message Format: `Type: Subject` (e.g., `Feat: 로그인 화면 구현`).
    *   English is allowed only for code keywords, library names, or standard file paths.
2.  **Impact Analysis First:** NEVER modify code or specs blindly.
    *   You MUST read the relevant files (`read_file`) first to understand the existing context, dependencies, and potential side effects.
    *   Analysis must precede any action.
3.  **Respect Existing Style:** Do not rewrite code unnecessarily.
    *   Mimic the existing coding style, naming conventions, and structure exactly.
    *   Changes should be minimal and focused (Code Review Friendly).
    *   Preserve existing logic and imports unless explicitly instructed to refactor.
4.  **Context-Complete Specs:** The Specification (`doc/`) is the Single Source of Truth.
    *   A spec file must contain **ALL** necessary context (UI details, logic flows, data models) so that implementation can proceed solely based on the spec.
    *   **Checklist Mandatory:** Every spec MUST include a detailed `Implementation Checklist` (Sub-tasks) to track progress and control sub-issues.

## 1. Project Identity & Tech Stack
*   **Goal:** Create a high-fidelity clone of the YouTube Android app.
*   **Architecture:** **Clean Architecture** + **MVVM**.
    *   **UI:** **Jetpack Compose** (Material3). Activity/Fragment usage is minimized.
    *   **DI:** Hilt.
    *   **Async:** Coroutines + Flow.
    *   **Network:** Retrofit + OkHttp.
    *   **Image:** Coil.
*   **Language:** Kotlin (100%).

## 2. Spec-Driven Development (SpecDD)
Implementation flow strictly follows the **One-Shot TDD Workflow** defined in Section 4.

### 2.1. Rich Specification Structure
Every spec file in `doc/` must be self-contained and structure-complete:
*   **Overview:** What and Why.
*   **Detailed Requirements:** Functional & Non-functional.
*   **Technical Specs:**
    *   Data Models (DTO, Entity).
    *   API Endpoints.
    *   Logic Flows (Mermaid diagrams encouraged).
*   **UI/UX:** Screen composition, Components used.
*   **Implementation Checklist:**
    *   Break down the feature into atomic tasks.
    *   Format: `- [ ] Task Name`
    *   Used for creating sub-issues and tracking progress.

## 3. Code Modification Guidelines
To strictly follow **Constitution #2 and #3**:

*   **Context Awareness:** Before editing `Foo.kt`, you MUST read `Foo.kt` and its related files (tests, usage) to understand *how* it works.
*   **Tool Selection:**
    *   **New Files:** Use `write_file` freely to create **NEW** files (e.g., Skeleton code).
    *   **Existing Files:** PREFER `replace` to modify specific blocks. AVOID `write_file` on existing files unless rewriting (Refactoring).
*   **Dependency Management:** Check `libs.versions.toml` (Version Catalog) before adding new dependencies. Do not hardcode versions in `build.gradle.kts`.

## 4. One-Shot TDD Workflow
To prevent infinite repair loops, the agent must follow this strict linear process for implementation:

1.  **Skeleton:** Create interface/class structures based on the Spec. (Use `write_file` for new files).
2.  **Test (Red):** Write unit tests that verify the Spec requirements. These tests should ideally fail against the skeleton.
3.  **Implement (Green):** Write the actual implementation logic.
4.  **Verify (Run Once):** Execute the tests **EXACTLY ONCE**.
5.  **Stop & Report:**
    *   Commit the changes regardless of the test result.
    *   Report the test outcome (Pass/Fail) in the PR body.
    *   **Failure Analysis:** If tests fail, briefly analyze if it's an Implementation Bug or a Spec Flaw in the report.
    *   **DO NOT** attempt to fix the code autonomously if tests fail. Await human instruction.

*Tools:* JUnit4, Mockk, Turbine (for Flow).
*Scope:* Focus on Unit Tests for Domain Layer (UseCases) and Presentation Layer (ViewModels).
