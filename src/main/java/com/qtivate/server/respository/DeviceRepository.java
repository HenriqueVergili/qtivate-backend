package com.qtivate.server.respository;

import com.qtivate.server.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DeviceRepository extends MongoRepository<Device, String> {
    @Query("{mac: '?0'}")
    Device findDeviceByMacAddress(String mac);
}
