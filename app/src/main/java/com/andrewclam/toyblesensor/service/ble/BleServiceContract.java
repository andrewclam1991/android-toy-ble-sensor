package com.andrewclam.toyblesensor.service.ble;

import com.andrewclam.toyblesensor.service.BaseController;
import com.andrewclam.toyblesensor.service.BaseService;
import com.andrewclam.toyblesensor.view.BaseView;

public interface BleServiceContract {
    interface View extends BaseView{

    }

    interface Service extends BaseService<View> {

    }

    interface Controller extends BaseController<Service> {

    }
}
