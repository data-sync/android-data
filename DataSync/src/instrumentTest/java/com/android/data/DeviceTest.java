package com.android.data;

import android.test.suitebuilder.annotation.MediumTest;

public class DeviceTest extends BaseServiceTestCase {

    private Repository<Device> deviceRepository;
    private DataStore dataStore;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataStore = bindDataService();
        deviceRepository = new Repository<Device>(Device.class, dataStore);
    }

    @MediumTest
    public void testRegistersADevice() {
        Device device = deviceRepository.getAll().get(0);
        assertNotNull(device.getId());
    }

    @MediumTest
    public void testRegisterOnlyOnce() {
        Device.register(dataStore);

        Device device = deviceRepository.getAll().get(0);
        assertNotNull(device.getId());
    }
}
