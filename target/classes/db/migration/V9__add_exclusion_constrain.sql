CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE booking
    ADD CONSTRAINT booking_no_room_overlap
    EXCLUDE USING gist (
        room_id WITH =,
        tsrange(start_time, actual_end_time, '[)') WITH &&
    )
    WHERE (status IN (2, 5, 7));