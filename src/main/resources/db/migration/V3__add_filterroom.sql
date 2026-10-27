 CREATE OR REPLACE FUNCTION booking_filter(
      p_start    timestamp,
	  p_status	 integer,
	  p_room_id  integer,
      p_end_time timestamp
  )
  RETURNS SETOF booking
  LANGUAGE sql
  STABLE
  AS $$
      SELECT b.*
      FROM booking AS b
      WHERE b.is_deleted = false
        AND (p_start is null or p_end_time is null or p_start < p_end_time)
		and (b.status = p_status or p_status is null)
		and (b.room_id = p_room_id or p_room_id is null)
		AND (p_end_time IS NULL OR b.start_time < p_end_time )
        AND (p_start IS NULL OR b.end_time > p_start OR b.end_time IS NULL);
  $$;