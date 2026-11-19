
 CREATE OR REPLACE FUNCTION get_all_booking_manager(
	p_manager_user_id  integer,
	p_manager_group_id integer,
	p_end_time timestamp,
	p_status integer,
	p_room_ids integer[],
	p_start    timestamp
)
RETURNS SETOF booking
  LANGUAGE sql
  STABLE
  AS $$
SELECT b.*
FROM booking AS b
left JOIN room As r
on r.id = b.room_id
where
	(b.status  = p_status or p_status is null)
	AND (p_end_time IS NULL OR b.start_time < p_end_time )
        AND (p_start IS NULL OR b.end_time > p_start OR b.end_time IS NULL)
	and (b.created_by = p_manager_user_id or r.manager_group_id = p_manager_group_id)
	and (b.room_id = ANY(p_room_ids));
  $$;