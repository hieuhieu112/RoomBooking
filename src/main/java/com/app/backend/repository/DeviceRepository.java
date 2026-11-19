package com.app.backend.repository;

import com.app.backend.dtos.internal.DeviceQuantityAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.backend.entity.Device;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Boolean existsBySerial(String serial);

    @Query(
            value = "SELECT * from list_device_quantity_available_by_time( " +
                    "CAST(:p_start AS timestamp)" +
                    ",:p_device_id" +
                    ",CAST(:p_end_time AS timestamp))",
            nativeQuery = true
    )
    List<DeviceQuantityAvailable> findAllQuantityAvailable(
            @Param("p_start") String p_start,
            @Param("p_device_id") List<Integer> p_device_id,
            @Param("p_end_time") String p_end_time
    );
}
