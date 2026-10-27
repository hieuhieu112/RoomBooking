 CREATE OR REPLACE FUNCTION available_rooms(
      p_start    timestamp,
      p_end_time timestamp
  )
  RETURNS SETOF room
  LANGUAGE sql
  STABLE
  AS $$
      SELECT r.*
      FROM room AS r
      WHERE r.is_deleted = false
        AND p_start < p_end_time
        AND NOT EXISTS (
            SELECT 1
            FROM booking AS b
            WHERE b.room_id = r.id
              AND b.is_deleted = false
              AND (b.status <3 or b.status >4)
              AND b.start_time < p_end_time
              AND b.actual_end_time > p_start
        );
  $$;