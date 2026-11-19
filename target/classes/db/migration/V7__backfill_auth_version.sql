ALTER TABLE users
    ADD COLUMN IF NOT EXISTS auth_version INTEGER;

-- Chỉ điền những bản ghi chưa có version
UPDATE users
SET auth_version = 0
WHERE auth_version IS NULL;

-- Giá trị mặc định cho bản ghi mới
ALTER TABLE users
    ALTER COLUMN auth_version SET DEFAULT 0;

-- Không cho phép lưu NULL
ALTER TABLE users
    ALTER COLUMN auth_version SET NOT NULL;