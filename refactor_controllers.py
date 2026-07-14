import os

controller_dir = r"d:\document\unknow\web\AIigenRB\src\main\java\com\app\backend\controller"

for filename in os.listdir(controller_dir):
    if not filename.endswith("Controller.java"):
        continue
    
    name = filename.replace("Controller.java", "")
    service_name = name + "Service"
    
    controller_item_type = name + "Response"
    data_wrapper_single = "service.mapToResponse({expr})"
    data_wrapper_list = "{expr}.stream().map(service::mapToResponse).toList()"
        
    mapping_name = name.lower() + "s"
    
    controller_content = f"""package com.app.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import com.app.backend.dtos.request.*;
import com.app.backend.dtos.response.*;
import com.app.backend.service.{service_name};

@RestController
@RequestMapping("/api/v1/{mapping_name}")
@AllArgsConstructor
public class {name}Controller {{
    private final {service_name} service;

    @PostMapping
    public ResponseEntity<DataResponse<{controller_item_type}>> create(@RequestBody {name}Request request) {{
        DataResponse<{controller_item_type}> response = DataResponse.<{controller_item_type}>builder()
                .data({data_wrapper_single.format(expr="service.create(request)")})
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<DataResponse<{controller_item_type}>> getById(@PathVariable Integer id) {{
        var res = service.getById(id);
        if (res != null) {{
            DataResponse<{controller_item_type}> response = DataResponse.<{controller_item_type}>builder()
                    .data({data_wrapper_single.format(expr="res")})
                    .statusCode(StatusRes.SUCCESS)
                    .message("SUCCESS")
                    .build();
            return ResponseEntity.ok(response);
        }}
        return ResponseEntity.notFound().build();
    }}

    @GetMapping
    public ResponseEntity<DataResponse<List<{controller_item_type}>>> getAll() {{
        DataResponse<List<{controller_item_type}>> response = DataResponse.<List<{controller_item_type}>>builder()
                .data({data_wrapper_list.format(expr="service.getAll()")})
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}

    @PutMapping("/{{id}}")
    public ResponseEntity<DataResponse<{controller_item_type}>> update(@PathVariable Integer id, @RequestBody {name}Request request) {{
        DataResponse<{controller_item_type}> response = DataResponse.<{controller_item_type}>builder()
                .data({data_wrapper_single.format(expr="service.update(id, request)")})
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}

    @DeleteMapping("/{{id}}")
    public ResponseEntity<DataResponse<Void>> delete(@PathVariable Integer id) {{
        service.delete(id);
        DataResponse<Void> response = DataResponse.<Void>builder()
                .statusCode(StatusRes.SUCCESS)
                .message("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }}
}}
"""
    with open(os.path.join(controller_dir, filename), "w", encoding="utf-8") as f:
        f.write(controller_content)
        print(f"Generated {filename}")
