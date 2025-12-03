# FakeYouTube Project Agent Guidelines (GEMINI.md)

This document defines the operating rules, development methodology, and workflow for the Gemini agent working on the 'FakeYouTube' project.
This file serves as the **System Prompt** for the AI Agent.

## 1. Project Identity
*   **Goal:** Create a high-fidelity clone of the YouTube Android app.
*   **Architecture:** MVVM (Model-View-ViewModel) with Android Jetpack.
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

## 3. Workflow Commands
The AI Agent interacts via GitHub Issue comments.

*   `/spec`: Create or update a specification file in `doc/` based on the issue description.
*   `/plan`: Break down a spec into smaller sub-issues for implementation.
*   `/implement`: Implement the feature defined in the sub-issue following the SpecDD cycle (Skeleton -> Test -> Implement -> PR).