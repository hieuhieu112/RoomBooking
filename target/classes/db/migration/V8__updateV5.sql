CREATE OR REPLACE FUNCTION list_device_quantity_available_by_time(
     	p_start    timestamp,
	p_device_id integer[],
      	p_end_time timestamp,
                                p_booking_id_exception integer
  )
RETURNS TABLE (
    device_id          integer,
    device_name        varchar,
	tracking_type	   varchar,
    available_quantity bigint
)
  LANGUAGE sql
  STABLE
  AS $$
SELECT
        d.id,
        d.name,
		d.tracking_type,
	d.quantity - COALESCE(SUM(borrowed.quantity), 0) AS available_quantity
FROM device AS d
LEFT JOIN (
	SELECT
            de.device_id,
            de.quantity
        FROM device_borrow_detail AS de
	JOIN booking AS b
            ON b.id = de.booking_id
	WHERE b.status NOT IN (1, 3, 4)
          	AND (p_end_time IS NULL OR b.start_time < p_end_time)
          	AND (p_start IS NULL OR b.end_time > p_start OR b.end_time IS NULL)
          	AND b.id IS DISTINCT FROM p_booking_id_exception
	) AS borrowed
	ON borrowed.device_id = d.id
WHERE p_device_id IS NULL OR d.id = ANY(p_device_id) and d.status = 'ACTIVE'
GROUP BY d.id, d.name, d.quantity
ORDER BY d.id;
$$;