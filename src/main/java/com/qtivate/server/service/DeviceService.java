package com.qtivate.server.service;

import com.mongodb.MongoCursorNotFoundException;
import com.qtivate.server.exceptions.BlockedDeviceException;
import com.qtivate.server.model.Device;
import com.qtivate.server.respository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Calendar;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public int addDevice(String mac) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 2);
            Device device = new Device(mac, calendar.getTime());
            deviceRepository.insert(device);
            return 0;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
    }

    public int addDevice(String mac, int minutes) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, minutes);
            Device device = new Device(mac, calendar.getTime());
            deviceRepository.insert(device);
            return 0;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
    }

    public int updateLimitInDevice(String mac) {
        try {
            Device device = deviceRepository.findDeviceByMacAddress(mac);
            if (device == null) throw new Exception("Dispostivo não encontrado");
            device.changeLimit(2);
            return 0;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
    }

    public int updateLimitInDevice(String mac, int minutes) {
        try {
            Device device = deviceRepository.findDeviceByMacAddress(mac);
            if (device == null) throw new Exception("Dispostivo não encontrado");
            device.changeLimit(minutes);
            return 0;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
    }

    public int verifyMac(String mac) throws Exception {
        try {
            Device device = deviceRepository.findDeviceByMacAddress(mac);
            if (device == null) throw new FileNotFoundException("Dispositivo não encontrado");
            long diff = Calendar.getInstance().getTime().getTime() - device.getLimit().getTime();
            if (diff <= 0) throw new BlockedDeviceException("Dispositivo bloqueado");
            device.changeLimit(2);
            return 0;
        } catch (FileNotFoundException error) {
            System.err.println(error.getMessage());
            return 1;
        } catch (BlockedDeviceException error) {
            System.err.println(error.getMessage());
            return 2;
        } catch (Exception error) {
            throw new Exception(error.getMessage());
        }
    }

    public int verifyMac(String mac, int minutes) {
        try {
            Device device = deviceRepository.findDeviceByMacAddress(mac);
            if (device == null) throw new FileNotFoundException("Dispositivo não encontrado");
            long diff = Calendar.getInstance().getTime().getTime() - device.getLimit().getTime();
            if (diff <= 0) throw new Exception("Dispositivo bloqueado");
            device.changeLimit(minutes);
            return 0;
        } catch (FileNotFoundException error) {
            System.err.println(error.getMessage());
            return 1;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 2;
        }
    }
}
