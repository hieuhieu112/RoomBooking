 CREATE OR REPLACE FUNCTION total_device_quantity_using_by_time(
     	p_start    timestamp,
	p_device_id  integer,
      	p_end_time timestamp
  )
RETURNS bigint
  LANGUAGE sql
  STABLE
  AS $$
SELECT COALESCE(SUM(de.quantity), 0)
FROM device_borrow_detail AS de
JOIN booking AS B
	ON b.id = de.booking_id
WHERE
	b.status NOT IN (1,3,4)
	AND de.device_id = p_device_id
	AND (p_end_time IS NULL OR b.start_time < p_end_time )
        AND (p_start IS NULL OR b.end_time > p_start OR b.end_time IS NULL);
  $$;