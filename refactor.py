import os
import re

directory = r"d:\document\unknow\web\AIigenRB\src\main\java\com\app\backend\controller"

for filename in os.listdir(directory):
    if not filename.endswith(".java"):
        continue
    filepath = os.path.join(directory, filename)
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    print(f"Processing {filename}...")

    # Determine the EntityResponse and EntityRequest names
    request_match = re.search(r'@RequestBody\s+([a-zA-Z0-9]+Request)', content)
    request_type = request_match.group(1) if request_match else "Request"
    
    new_content = content
    
    # Replace create
    if filename != "ManagerGroupController.java":
        create_pattern = re.compile(r'@PostMapping\s+public\s+ResponseEntity<([a-zA-Z0-9]+Response)>\s+create\s*\(\s*@RequestBody\s+[a-zA-Z0-9]+Request\s+request\s*\)\s*\{\s*return\s+ResponseEntity\.ok\((.+?)\);\s*\}', re.DOTALL)
        def create_repl(m):
            t = m.group(1)
            inner = m.group(2)
            return f"""@PostMapping
    public ResponseEntity<DataResponse<{t}>> create(@RequestBody {request_type} request) {{
        DataResponse<{t}> response = DataResponse.<{t}>builder()
                .data({inner})
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}"""
        new_content = create_pattern.sub(create_repl, new_content)

    # Replace getById
    getById_pattern = re.compile(r'@GetMapping\("/\{id\}"\)\s+public\s+ResponseEntity<([a-zA-Z0-9]+Response)>\s+getById\s*\(\s*@PathVariable\s+Integer\s+id\s*\)\s*\{\s*([a-zA-Z0-9]+Response)\s+res\s*=\s*(.+?);\s*return\s+res\s*!=\s*null\s*\?\s*ResponseEntity\.ok\(res\)\s*:\s*ResponseEntity\.notFound\(\)\.build\(\);\s*\}', re.DOTALL)
    def getById_repl(m):
        t = m.group(1)
        res_type = m.group(2)
        inner = m.group(3)
        return f"""@GetMapping("/{{id}}")
    public ResponseEntity<DataResponse<{t}>> getById(@PathVariable Integer id) {{
        {res_type} res = {inner};
        if (res != null) {{
            DataResponse<{t}> response = DataResponse.<{t}>builder()
                    .data(res)
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }}
        return ResponseEntity.notFound().build();
    }}"""
    new_content = getById_pattern.sub(getById_repl, new_content)

    # Replace getAll
    getAll_pattern = re.compile(r'@GetMapping\s+public\s+ResponseEntity<List<([a-zA-Z0-9]+Response)>>\s+getAll\s*\(\s*\)\s*\{\s*return\s+ResponseEntity\.ok\((.+?)\);\s*\}', re.DOTALL)
    def getAll_repl(m):
        t = m.group(1)
        inner = m.group(2)
        return f"""@GetMapping
    public ResponseEntity<DataResponse<List<{t}>>> getAll() {{
        DataResponse<List<{t}>> response = DataResponse.<List<{t}>>builder()
                .data({inner})
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}"""
    new_content = getAll_pattern.sub(getAll_repl, new_content)

    # Replace update
    update_pattern = re.compile(r'@PutMapping\("/\{id\}"\)\s+public\s+ResponseEntity<([a-zA-Z0-9]+Response)>\s+update\s*\(\s*@PathVariable\s+Integer\s+id\s*,\s*@RequestBody\s+[a-zA-Z0-9]+Request\s+request\s*\)\s*\{\s*return\s+ResponseEntity\.ok\((.+?)\);\s*\}', re.DOTALL)
    def update_repl(m):
        t = m.group(1)
        inner = m.group(2)
        return f"""@PutMapping("/{{id}}")
    public ResponseEntity<DataResponse<{t}>> update(@PathVariable Integer id, @RequestBody {request_type} request) {{
        DataResponse<{t}> response = DataResponse.<{t}>builder()
                .data({inner})
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}"""
    new_content = update_pattern.sub(update_repl, new_content)

    # Replace delete
    delete_pattern = re.compile(r'@DeleteMapping\("/\{id\}"\)\s+public\s+ResponseEntity<Void>\s+delete\s*\(\s*@PathVariable\s+Integer\s+id\s*\)\s*\{\s*(.+?);\s*return\s+ResponseEntity\.(?:noContent|ok)\(\)\.build\(\);\s*\}', re.DOTALL)
    def delete_repl(m):
        inner = m.group(1)
        return f"""@DeleteMapping("/{{id}}")
    public ResponseEntity<DataResponse<Void>> delete(@PathVariable Integer id) {{
        {inner};
        DataResponse<Void> response = DataResponse.<Void>builder()
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}"""
    new_content = delete_pattern.sub(delete_repl, new_content)

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {filename}")
