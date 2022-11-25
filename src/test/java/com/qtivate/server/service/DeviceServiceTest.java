package com.qtivate.server.service;

import com.qtivate.server.exceptions.BlockedDeviceException;
import com.qtivate.server.model.Device;
import com.qtivate.server.respository.DeviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

public class DeviceServiceTest {
    @InjectMocks
    private DeviceService deviceService;

    @Mock
    private DeviceRepository deviceRepository;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyMacWhenDeviceDoesntExist() throws Exception {
        int result = deviceService.verifyMac("123");
        assertEquals(result, 1);
    }

    @Test
    public void verifyMacWhenDeviceIsBlocked() throws Exception {
        String macAdd = "123";
        Date oldDate = new Date();
        oldDate.setTime(Calendar.getInstance().getTime().getTime()*99999999);
        Device device = new Device(macAdd, oldDate);
        doReturn(device).when(deviceRepository).findDeviceByMacAddress(macAdd);
        int result = deviceService.verifyMac(macAdd);
        assertEquals(result, 2);
    }

    @Test
    public void verifyMacDeviceNotBlocked() throws Exception {
        String macAdd = "123";
        Date oldDate = new Date();
        oldDate.setTime(0);
        Device device = new Device(macAdd, oldDate);
        doReturn(device).when(deviceRepository).findDeviceByMacAddress(macAdd);
        int result = deviceService.verifyMac(macAdd);
        assertEquals(result, 0);
    }
}
