# FakeYouTube Project Agent Guidelines (GEMINI.md)

This document defines the operating rules, development methodology, and workflow for the Gemini agent working on the 'FakeYouTube' project.
This file serves as the **System Prompt** for the AI Agent.

## 1. Project Identity
*   **Goal:** Create a high-fidelity clone of the YouTube Android app.
*   **Architecture:** **Clean Architecture** + **MVVM** (Model-View-ViewModel).
    *   **Presentation Layer:** UI (Compose/XML), ViewModels.
    *   **Domain Layer:** UseCases, Repository Interfaces, Entities. (Pure Kotlin, no Android dependencies).
    *   **Data Layer:** Repository Implementations, Data Sources (API/DB), DTOs.
*   **Language:** Kotlin.
*   **Key Principle:** Respect existing code patterns and directory structures. Maintain a high standard of code quality.

## 2. Development Methodology: Spec-Driven Development (SpecDD)
All feature implementations must strictly follow this cycle:

1.  **Impact Analysis:** Before writing any code, analyze the existing codebase to understand dependencies.
2.  **Spec Definition:** All features must be defined in the `doc/` directory first.
    *   Specs must include detailed requirements, logic flows, and **code snippets**.
3.  **Skeleton Code:** Write the minimum necessary code (classes, method signatures) to allow tests to compile.
4.  **Test First (Red):** Write Unit Tests (`src/test`) that fail initially.
    *   **Focus:** Local Unit Tests only (`testDebugUnitTest`). Do NOT run `androidTest`.
5.  **Implementation (Green):** Implement the logic to make the tests pass.

## 3. Code Modification Guidelines (CRITICAL)
To prevent data loss and regression, adhere to these rules when modifying code:

*   **NO BLIND WRITES:** Never modify a file without reading it first (`read_file`). You must understand the existing context (imports, coding style, SDK versions, variable names) before making changes.
*   **PREFER `replace`:** Use the `replace` tool for modifying existing files. This ensures that only the targeted lines are changed, leaving the rest of the file (context) intact.
*   **SAFE `write_file`:** Use `write_file` ONLY for:
    *   Creating NEW files.
    *   Completely rewriting a file (Refactoring).
    *   *Constraint:* If you must use `write_file` on an existing file, you are responsible for **manually preserving** all existing logic, imports, and configurations that are not part of the change.
*   **STRICT MIMICRY:** When adding code, strictly mimic the existing project style (e.g., if `libs.versions.toml` is used, do not hardcode dependencies).

## 4. Workflow Commands
The AI Agent interacts via GitHub Issue comments.

*   `/spec`: Create or update a specification file in `doc/` based on the issue description.
*   `/plan`: Break down a spec into smaller sub-issues for implementation.
*   `/implement`: Implement the feature defined in the sub-issue following the SpecDD cycle (Skeleton -> Test -> Implement -> PR).

## 5. Technology Standards & Modern Practices
*   **Latest APIs:** Always prioritize the latest stable Android and Kotlin APIs.
    *   *Strictly Avoid:* Deprecated libraries (e.g., `AsyncTask`, `kotlin-android-extensions`, legacy support libraries).
    *   *Prefer:* Jetpack libraries (ViewModel, LiveData/Flow), Coroutines, Hilt (for DI), and modern dependency injection.
*   **Code Freshness:** Act as an engineer who is up-to-date with the 2024/2025 Android ecosystem. If a library has a known newer version (e.g., `google-genai` vs `google-generativeai`), ALWAYS use the newer one.
*   **Architectural Integrity:**
    *   Enforce strict separation between layers (Presentation -> Domain -> Data).
    *   Domain layer must remain pure Kotlin.
    *   Use dependency injection (Hilt recommended) to manage dependencies.
