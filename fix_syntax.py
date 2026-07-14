import os

files = [
    "src/main/java/com/app/backend/service/impl/RoomTypeServiceImpl.java",
    "src/main/java/com/app/backend/service/impl/HouseServiceImpl.java",
    "src/main/java/com/app/backend/service/impl/ManagerGroupServiceImpl.java",
    "src/main/java/com/app/backend/service/impl/DeviceCategoryServiceImpl.java",
    "src/main/java/com/app/backend/service/impl/DeviceTypeServiceImpl.java",
    "src/main/java/com/app/backend/service/impl/ManufacturerDeviceServiceImpl.java",
    "src/main/java/com/app/backend/service/impl/DeviceModelServiceImpl.java"
]

for filepath in files:
    if not os.path.exists(filepath):
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Fix the broken private final CacheService cacheService;
    import re
    # We want to find cases like:
    # private final CacheService
    # 
    #     private static final Duration HOUSE_CACHE_TTL = Duration.ofMinutes(15);
    #  cacheService;
    
    pattern = r'(private final CacheService)\s*\n+\s*(private static final Duration \w+_CACHE_TTL = Duration.ofMinutes\(15\);)\s*\n\s*(cacheService;)'
    
    content = re.sub(pattern, r'\1 \3\n\n    \2', content)
    
    # Check if DeviceModelServiceImpl is broken similarly
    pattern_2 = r'(private final DeviceModelRepository repo;\n    private final CacheService cacheService;\n    private final RedisService redisService;\n    private final DeviceTypeServiceImpl deviceTypeService;\n    private final ManufacturerDeviceServiceImpl manufacturerDeviceService)\n\n    private static final Duration DEVICE_MODEL_CACHE_TTL = Duration.ofMinutes\(15\);\n;'
    
    if "DEVICE_MODEL_CACHE_TTL" in content and "manufacturerDeviceService" in content:
         content = re.sub(r'(private final ManufacturerDeviceServiceImpl manufacturerDeviceService)\n+\s*(private static final Duration DEVICE_MODEL_CACHE_TTL = Duration.ofMinutes\(15\);)\s*\n\s*;',
                          r'\1;\n\n    \2', content)

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Fixed {filepath}")
