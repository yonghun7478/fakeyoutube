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
    json_match = re.search(r"```json\s*(.*?)\s*```", text, re.DOTALL)
    if json_match:
        return json_match.group(1)
    
    json_match = re.search(r"(\{.*\}|\[.*\])", text, re.DOTALL)
    if json_match:
        return json_match.group(1)
        
    return text

def fetch_spec_content(text):
    """Finds doc/*.md references in text and fetches their content."""
    spec_content = ""
    # Regex to find file paths like doc/something.md or just something.md if mentioned in doc context
    matches = re.findall(r'(doc/[\w\-\.]+\.md)', text)
    
    if not matches:
        return ""
        
    print(f"Found spec references: {matches}")
    
    for file_path in matches:
        try:
            # Fetch file content from the repo (main branch)
            file_content = repo.get_contents(file_path, ref="main")
            decoded_content = file_content.decoded_content.decode("utf-8")
            spec_content += f"\n\n--- Content of {file_path} ---\n{decoded_content}\n------------------------------\n"
        except Exception as e:
            print(f"Warning: Could not fetch spec file {file_path}: {e}")
            
    return spec_content

def get_gemini_response(prompt, system_instruction_text=None):
    """Helper to get response from Gemini using the new SDK."""
    
    if not system_instruction_text:
        try:
            with open("GEMINI.md", "r") as f:
                system_instruction_text = f.read()
        except FileNotFoundError:
            system_instruction_text = "You are a helpful AI software engineer."

    system_instruction_text += "\n\nIMPORTANT: All explanations must be in Korean. Output MUST be valid JSON. All newlines inside JSON strings MUST be escaped as \\n."

    # DEBUG LOG: Print the full prompt to see what's being sent
    print("\n[DEBUG] System Instruction:\n", system_instruction_text)
    print("\n[DEBUG] User Prompt:\n", prompt)

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
    4. The content MUST include a section "Implementation Checklist" with "- [ ] Task Name" items.
    5. The content of the specification MUST be in Korean.
    6. Return ONLY the JSON structure with NO extra text or markdown formatting outside the JSON.
    
    Expected JSON Format:
    {{
        "filename": "doc/...",
        "content": "..."
    }}
    
    IMPORTANT: The 'content' field must be a SINGLE LINE string with all newlines escaped (\\n).
    """
    
    response_text = get_gemini_response(prompt)
    
    try:
        json_str = extract_json(response_text)
        data = json.loads(json_str, strict=False)
        filename = data["filename"]
        content = data["content"]
        
        os.makedirs(os.path.dirname(filename), exist_ok=True)
        with open(filename, "w") as f:
            f.write(content)
            
        branch_name = f"spec/issue-{ISSUE_NUMBER}"
        run_command(f"git config --global user.email 'gemini-bot@example.com'")
        run_command(f"git config --global user.name 'Gemini Bot'")
        run_command(f"git checkout -b {branch_name}")
        run_command(f"git add {filename}")
        run_command(f"git commit -m 'Add spec for Issue #{ISSUE_NUMBER}'")
        run_command(f"git push origin {branch_name}")
        
        pr = repo.create_pull(
            title=f"Spec: {issue.title}",
            body=f"이슈 #{ISSUE_NUMBER}를 기반으로 생성된 명세서입니다.",
            head=branch_name,
            base="main"
        )
        issue.create_comment(f"명세서가 작성되었습니다! PR: {pr.html_url}\n파일: {filename}")
        
    except Exception as e:
        print(f"Raw Response: {response_text}")
        issue.create_comment(f"명세서 작성 중 오류 발생: {str(e)}")

def handle_plan():
    """Handles the /plan command."""
    print(f"Processing /plan for Issue #{ISSUE_NUMBER}")
    
    # Fetch referenced specs
    spec_context = fetch_spec_content(issue.body) + fetch_spec_content(COMMENT_BODY)

    prompt = f"""
    The user wants to break down the work into sub-issues.
    Issue Title: {issue.title}
    Issue Body: {issue.body}
    
     Referenced Specifications (Context):
    {spec_context}
    
    Task:
    1. Identify the specification file path (e.g., doc/foo.md) from the context.
    2. Break down the implementation into small, manageable sub-tasks.
    3. The Task Title and Body MUST be in Korean.
    4. CRITICAL: The 'body' of EACH sub-issue MUST start with "Related Spec: [path to spec file]" so the agent knows the context later.
    5. Return a JSON list of tasks.
    Format:
    [
        {{
            "title": "작업 제목 (Korean)",
            "body": "Related Spec: doc/foo.md\\n\\n상세 설명 (Korean)... 부모 이슈: #{ISSUE_NUMBER}"
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
    
    # Fetch referenced specs (Crucial Step!)
    spec_context = fetch_spec_content(issue.body) + fetch_spec_content(COMMENT_BODY)
    
    prompt = f"""
    The user wants to implement the feature described in this issue.
    Issue Title: {issue.title}
    Issue Body: {issue.body}
    Comment Instruction: {COMMENT_BODY}
    
    Referenced Specifications (Context):
    {spec_context}
    
    STRICT RULE: Follow SpecDD.
    1. Write Unit Test (src/test/...) that FAILS.
    2. Write Skeleton Code.
    3. Write Implementation.
    4. **Update the Spec File**: Find the relevant checklist item in the referenced spec file and change "- [ ]" to "- [x]".
    
    Return JSON with ALL files to create/modify (including the updated spec file):
    {{
        "files": [
            {{ "path": "app/src/main/...", "content": "..." }},
            {{ "path": "app/src/test/...", "content": "..." }},
            {{ "path": "doc/...", "content": "..." }}
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
        print(f"[DEBUG] Test Result:\n{test_result}")

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
