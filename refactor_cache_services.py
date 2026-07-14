import os
import re

files_info = [
    {
        "file": "src/main/java/com/app/backend/service/impl/RoomTypeServiceImpl.java",
        "entity": "RoomType",
        "entityVar": "roomType",
        "keyPrefix": "roomType",
        "reqClass": "RoomTypeRequest",
        "exceptionCode": "ErrorCode.ROOM_TYPE_NOT_FOUND"
    },
    {
        "file": "src/main/java/com/app/backend/service/impl/HouseServiceImpl.java",
        "entity": "House",
        "entityVar": "house",
        "keyPrefix": "house",
        "reqClass": "HouseRequest",
        "exceptionCode": "ErrorCode.HOUSE_NOT_FOUND"
    },
    {
        "file": "src/main/java/com/app/backend/service/impl/ManagerGroupServiceImpl.java",
        "entity": "ManagerGroup",
        "entityVar": "managerGroup",
        "keyPrefix": "managerGroup",
        "reqClass": "ManagerGroupRequest",
        "exceptionCode": "ErrorCode.MANAGER_GROUP_NOT_FOUND",
        "hasException": False
    },
    {
        "file": "src/main/java/com/app/backend/service/impl/DeviceCategoryServiceImpl.java",
        "entity": "DeviceCategory",
        "entityVar": "deviceCategory",
        "keyPrefix": "deviceCategory",
        "reqClass": "DeviceCategoryRequest",
        "exceptionCode": "ErrorCode.DEVICE_CATEGORY_NOT_FOUND"
    },
    {
        "file": "src/main/java/com/app/backend/service/impl/DeviceTypeServiceImpl.java",
        "entity": "DeviceType",
        "entityVar": "deviceType",
        "keyPrefix": "deviceType",
        "reqClass": "DeviceTypeRequest",
        "exceptionCode": "ErrorCode.DEVICE_TYPE_NOT_FOUND"
    },
    {
        "file": "src/main/java/com/app/backend/service/impl/ManufacturerDeviceServiceImpl.java",
        "entity": "ManufacturerDevice",
        "entityVar": "manufacturerDevice",
        "keyPrefix": "manufacturer", 
        "reqClass": "ManufacturerDeviceRequest",
        "exceptionCode": "ErrorCode.MANUFACTOR_DEVICE_NOT_FOUND"
    },
    {
        "file": "src/main/java/com/app/backend/service/impl/DeviceModelServiceImpl.java",
        "entity": "DeviceModel",
        "entityVar": "deviceModel",
        "keyPrefix": "deviceModel",
        "reqClass": "DeviceModelRequest",
        "exceptionCode": "ErrorCode.DEVICE_MODEL_NOT_FOUND"
    }
]

for info in files_info:
    filepath = info['file']
    if not os.path.exists(filepath):
        print(f"Skipping {filepath}, not found")
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Add imports
    if "import com.app.backend.constant.RedisKey;" not in content:
        content = content.replace("import org.springframework.stereotype.Service;",
"""import com.app.backend.constant.RedisKey;
import com.app.backend.service.CacheService;
import com.app.backend.service.RedisService;
import java.time.Duration;
import java.util.Arrays;
import org.springframework.stereotype.Service;""")

    if "ErrorCode.MANAGER_GROUP_NOT_FOUND" in info['exceptionCode'] and "import com.app.backend.exception.ErrorCode" not in content:
        content = content.replace("import com.app.backend.dtos.request.*;",
"""import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.dtos.request.*;""")
        content = content.replace("//repo.findById(id).map(this::mapToResponse).orElse(null);", "")
        content = content.replace("repo.findById(id).orElseThrow();", f"repo.findById(id).orElseThrow(() -> new CommonException({info['exceptionCode']}));")

    # Check if already has cacheService injected
    if "private final CacheService cacheService;" not in content:
        content = re.sub(r'(private final \w+Repository repo;)', r'\1\n    private final CacheService cacheService;\n    private final RedisService redisService;', content)
        if "private final CacheService" not in content:
            print("Failed to inject cacheService into", filepath)

    cache_ttl = f"{info['entity'].upper()}_CACHE_TTL"
    if cache_ttl not in content:
        content = re.sub(r'(public class \w+ServiceImpl implements \w+Service \{[\s\S]*?private final \w+Service\w*(?:;|;?))',
                         fr'\1\n\n    private static final Duration {cache_ttl} = Duration.ofMinutes(15);\n', content)

    # Replace create
    if "evictTopicCache(null);" not in content:
        content = re.sub(r'(entity = repo\.save\(entity\);\n\s*return \(?entity\)?;)',
                         r'entity = repo.save(entity);\n        evictTopicCache(null);\n        return entity;', content)
                         
    # Special handle for manager group getById if it was still original
    if info['entity'] == 'ManagerGroup' and "repo.findById(id).orElseThrow()" in content:
        content = content.replace("repo.findById(id).orElseThrow()", f"repo.findById(id).orElseThrow(() -> new CommonException({info['exceptionCode']}))")

    # Replace getById
    if "cacheService.getOrLoad" not in content:
        get_by_id_pattern = r'(@Override\s*public ' + info['entity'] + r' getById\(Integer id\) \{.*?)(return [^;]+;)(\s*\})'
        
        replacement = fr'''@Override
    public {info['entity']} getById(Integer id) {{
        return cacheService.getOrLoad(
                RedisKey.{info['keyPrefix']}ById(id),
                {info['entity']}.class,
                {cache_ttl},
                () -> repo.findById(id).orElseThrow(() -> new CommonException({info['exceptionCode']}))
        );
    }}'''
        new_content = re.sub(get_by_id_pattern, replacement, content, flags=re.DOTALL)
        if new_content == content:
             print("Warning: regex getById didn't match for", filepath)
        else:
             content = new_content

    # Replace getAll
    if "Arrays.asList" not in content or "cacheService.getOrLoad(" not in content:
        get_all_pattern = r'(@Override\s*public List<' + info['entity'] + r'> getAll\(\) \{)(.*?)(\})'
        replacement = fr'''@Override
    public List<{info['entity']}> getAll() {{
        {info['entity']}[] items = cacheService.getOrLoad(
                RedisKey.{info['keyPrefix']}All(),
                {info['entity']}[].class,
                {cache_ttl},
                () -> repo.findAll().toArray(new {info['entity']}[0])
        );
        return Arrays.asList(items);
    }}'''
        new_content = re.sub(get_all_pattern, replacement, content, flags=re.DOTALL)
        if new_content == content:
             print("Warning: regex getAll didn't match for", filepath)
        else:
             content = new_content
        
    # Replace update
    if "evictTopicCache(entity.getId());" not in content:
        content = re.sub(r'entity = repo\.save\(entity\);\n\s*return \(?entity\)?;(?!.*?evictTopicCache\(entity\.getId\(\)\);)',
                         r'entity = repo.save(entity);\n        evictTopicCache(entity.getId());\n        return entity;', content)

    # Add evictTopicCache and modify delete
    if "void evictTopicCache" not in content:
        evict_method = fr'''
    private void evictTopicCache(Integer topicId) {{
        try {{
            redisService.delete(RedisKey.{info['keyPrefix']}All());
            if (topicId != null) {{
                redisService.delete(RedisKey.{info['keyPrefix']}ById(topicId));
            }}
        }} catch (Exception ignored) {{
        }}
    }}
'''
        content = re.sub(r'(@Override\s*public void delete\(Integer id\) \{\s*)(repo\.deleteById\(id\);\s*\})',
                         evict_method + r'\n    \1evictTopicCache(id);\n        \2', content)

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
        print(f"Updated {filepath}")
