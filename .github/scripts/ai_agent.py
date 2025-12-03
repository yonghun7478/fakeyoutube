import os
import json
import subprocess
import re
from google import genai
from google.genai import types
from github import Github

# --- Configuration ---
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
REPO_NAME = os.getenv("REPO_NAME")
ISSUE_NUMBER = int(os.getenv("ISSUE_NUMBER"))
COMMENT_BODY = os.getenv("COMMENT_BODY")
COMMENT_AUTHOR = os.getenv("COMMENT_AUTHOR")

# Initialize API Clients
client = genai.Client(api_key=GEMINI_API_KEY)
gh = Github(GITHUB_TOKEN)
repo = gh.get_repo(REPO_NAME)
issue = repo.get_issue(ISSUE_NUMBER)

# Constants
MODEL_ID = "gemini-3-pro-preview"

def run_command(command):
    """Runs a shell command and returns output."""
    try:
        result = subprocess.run(command, shell=True, check=True, capture_output=True, text=True)
        return result.stdout.strip()
    except subprocess.CalledProcessError as e:
        print(f"Error running command: {command}")
        print(e.stderr)
        return None

def extract_json(text):
    """Robustly extracts JSON from a string."""
    # Try to find JSON block within markdown code blocks
    json_match = re.search(r"```json\s*(.*?)\s*```", text, re.DOTALL)
    if json_match:
        return json_match.group(1)
    
    # Try to find a raw JSON object or array
    json_match = re.search(r"(\{.*\}|\[.*\])", text, re.DOTALL)
    if json_match:
        return json_match.group(1)
        
    return text # Return original if no pattern matched, hoping it's raw JSON

def get_gemini_response(prompt, system_instruction_text=None):
    """Helper to get response from Gemini using the new SDK."""
    
    # Load system instruction from GEMINI.md if not provided
    if not system_instruction_text:
        try:
            with open("GEMINI.md", "r") as f:
                system_instruction_text = f.read()
        except FileNotFoundError:
            system_instruction_text = "You are a helpful AI software engineer."

    # Force Korean language and JSON format
    system_instruction_text += "\n\nIMPORTANT: All explanations must be in Korean. Output MUST be valid JSON."

    config = types.GenerateContentConfig(
        system_instruction=system_instruction_text,
        response_modalities=["TEXT"]
    )

    response = client.models.generate_content(
        model=MODEL_ID,
        contents=prompt,
        config=config
    )
    return response.text

def handle_spec():
    """Handles the /spec command."""
    print(f"Processing /spec for Issue #{ISSUE_NUMBER}")
    
    prompt = f"""
    The user wants to create a specification for the following requirement.
    Issue Title: {issue.title}
    Issue Body: {issue.body}
    Comment Instruction: {COMMENT_BODY}
    
    Task:
    1. Create a Markdown specification file in the `doc/` directory.
    2. The filename should be descriptive (e.g., `doc/feature_name_spec.md`).
    3. Content must strictly follow the 'Spec Definition' rules in GEMINI.md.
    4. The content of the specification MUST be in Korean.
    5. Return ONLY the JSON structure:
    {{
        "filename": "doc/...",
        "content": "...markdown content in Korean..."
    }}
    """
    
    response_text = get_gemini_response(prompt)
    
    try:
        json_str = extract_json(response_text)
        data = json.loads(json_str)
        filename = data["filename"]
        content = data["content"]
        
        # Write file
        os.makedirs(os.path.dirname(filename), exist_ok=True)
        with open(filename, "w") as f:
            f.write(content)
            
        # Git Operations
        branch_name = f"spec/issue-{ISSUE_NUMBER}"
        run_command(f"git config --global user.email 'gemini-bot@example.com'")
        run_command(f"git config --global user.name 'Gemini Bot'")
        run_command(f"git checkout -b {branch_name}")
        run_command(f"git add {filename}")
        run_command(f"git commit -m 'Add spec for Issue #{ISSUE_NUMBER}'")
        run_command(f"git push origin {branch_name}")
        
        # Create PR
        pr = repo.create_pull(
            title=f"Spec: {issue.title}",
            body=f"이슈 #{ISSUE_NUMBER}를 기반으로 생성된 명세서입니다.",
            head=branch_name,
            base="main"
        )
        issue.create_comment(f"명세서가 작성되었습니다! PR: {pr.html_url}\n파일: {filename}")
        
    except Exception as e:
        print(f"Raw Response: {response_text}") # Log for debugging
        issue.create_comment(f"명세서 작성 중 오류 발생: {str(e)}")

def handle_plan():
    """Handles the /plan command."""
    print(f"Processing /plan for Issue #{ISSUE_NUMBER}")
    
    prompt = f"""
    The user wants to break down the work into sub-issues.
    Issue Title: {issue.title}
    Issue Body: {issue.body}
    
    Task:
    1. Identify the specification (if mentioned).
    2. Break down the implementation into small, manageable sub-tasks.
    3. The Task Title and Body MUST be in Korean.
    4. Return a JSON list of tasks.
    Format:
    [
        {{
            "title": "작업 제목 (Korean)",
            "body": "상세 설명 (Korean)... 부모 이슈: #{ISSUE_NUMBER}"
        }}
    ]
    """
    
    response_text = get_gemini_response(prompt)
    try:
        json_str = extract_json(response_text)
        tasks = json.loads(json_str)
        
        created_issues = []
        for task in tasks:
            new_issue = repo.create_issue(title=task["title"], body=task["body"])
            created_issues.append(f"#{new_issue.number}")
            
        issue.create_comment(f"서브 이슈들이 생성되었습니다: {', '.join(created_issues)}")
        
    except Exception as e:
        print(f"Raw Response: {response_text}")
        issue.create_comment(f"계획 수립 중 오류 발생: {str(e)}")

def handle_implement():
    """Handles the /implement command."""
    print(f"Processing /implement for Issue #{ISSUE_NUMBER}")
    
    prompt = f"""
    The user wants to implement the feature described in this issue.
    Issue Title: {issue.title}
    Issue Body: {issue.body}
    Comment Instruction: {COMMENT_BODY}
    
    STRICT RULE: Follow SpecDD.
    1. Write Unit Test (src/test/...) that FAILS.
    2. Write Skeleton Code.
    3. Write Implementation.
    
    Return JSON with ALL files to create/modify:
    {{
        "files": [
            {{ "path": "app/src/main/...", "content": "..." }},
            {{ "path": "app/src/test/...", "content": "..." }}
        ]
    }}
    """
    
    response_text = get_gemini_response(prompt)
    try:
        json_str = extract_json(response_text)
        data = json.loads(json_str)
        files = data["files"]
        
        branch_name = f"feat/issue-{ISSUE_NUMBER}"
        run_command(f"git config --global user.email 'gemini-bot@example.com'")
        run_command(f"git config --global user.name 'Gemini Bot'")
        run_command(f"git checkout -b {branch_name}")
        
        for file in files:
            os.makedirs(os.path.dirname(file["path"]), exist_ok=True)
            with open(file["path"], "w") as f:
                f.write(file["content"])
            run_command(f"git add {file['path']}")
            
        # Run Tests
        run_command("chmod +x gradlew")
        test_result = run_command("./gradlew testDebugUnitTest")
        
        commit_msg = "Implement feature with tests"
        test_status_kor = "실패"
        if test_result and "BUILD SUCCESSFUL" in test_result:
            commit_msg += " (Tests Passed)"
            test_status_kor = "성공"
        else:
            commit_msg += " (Tests Failed - Needs Review)"
            
        run_command(f"git commit -m '{commit_msg}'")
        run_command(f"git push origin {branch_name}")
        
        pr = repo.create_pull(
            title=f"Feat: {issue.title}",
            body=f"이슈 #{ISSUE_NUMBER}에 대한 구현입니다.\n\n테스트 결과: {test_status_kor}",
            head=branch_name,
            base="main"
        )
        issue.create_comment(f"구현 PR이 생성되었습니다: {pr.html_url}")
        
    except Exception as e:
        print(f"Raw Response: {response_text}")
        issue.create_comment(f"구현 중 오류 발생: {str(e)}")

def main():
    if "/spec" in COMMENT_BODY:
        handle_spec()
    elif "/plan" in COMMENT_BODY:
        handle_plan()
    elif "/implement" in COMMENT_BODY:
        handle_implement()
    else:
        print("No valid command found.")

if __name__ == "__main__":
    main()