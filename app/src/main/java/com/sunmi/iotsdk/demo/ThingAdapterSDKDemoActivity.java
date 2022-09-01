package com.sunmi.iotsdk.demo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.sunmi.connectionservice.iot.SunmiConnManager;
import com.sunmi.thing.bus.AdapterInfo;
import com.sunmi.thing.bus.ThingAction;
import com.sunmi.thing.bus.ThingServiceData;
import com.sunmi.thingadapterlib.ThingAdapterSDK;
import com.sunmi.thingadapterlib.callback.BusCallBack;
import com.sunmi.thingadapterlib.callback.InitCallBack;

import org.json.JSONObject;

public class ThingAdapterSDKDemoActivity extends BaseDemoActivity {
    private static String SERVICE_ID = "thermal_printer";
    private static String SERVICE_TYPE = "printer";

    private BusCallBack callBack = new BusCallBack() {
        @Override
        public boolean executeAdapter(String actionType, String action, String data) {
            //收到云端指令
            log("executeAdapter : actionType:" + actionType + "action:" + action + "data:" + data);
            switch (actionType) {
                case ThingAction.ACTION_TYPE_COMMAND:
                    //execute command
                    return true;
                case ThingAction.ACTION_TYPE_COMMANDS:
                    return true;
                case ThingAction.ACTION_TYPE_PROPERTY:
                    //property get/set
                    if (ThingAction.ACTION_GET.equals(action)) {
                        //property get
                    } else if (ThingAction.ACTION_SET.equals(action)) {
                        //property set
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_adapter_demo);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapterInfo adapterInfo = new AdapterInfo(SERVICE_ID, SERVICE_TYPE);
                ThingAdapterSDK.setInitCallBack(new InitCallBack() {
                    @Override
                    public void connect() {
                        log("connect:");
                        ThingAdapterSDK.registerAbility(adapterInfo, callBack);
                    }

                    @Override
                    public void disconnect() {
                        log("disconnect");
                        ThingAdapterSDK.unRegisterAbility(adapterInfo);
                    }
                });
                boolean bindResult = ThingAdapterSDK.init(ThingAdapterSDKDemoActivity.this);
                log("bindResult:" + bindResult);
            }
        });

        findViewById(R.id.btn_event_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThingServiceData thingServiceData = new ThingServiceData();
                thingServiceData.serviceId = SERVICE_ID;
                try {
                    JSONObject eventDataObj = new JSONObject();
                    JSONObject statusObj = new JSONObject();
                    statusObj.put("code", "000000");
                    statusObj.put("msg", "主动事件上报");
                    eventDataObj.put("status_printer", statusObj);
                    thingServiceData.data = eventDataObj;
                    ThingAdapterSDK.eventReport(thingServiceData);
                    log("事件上报");
                    if (!SunmiConnManager.getInstance().isConnected()) {
                        log("当前未连接，上报事件无法上报至云端");
                    }
                }catch (Exception e){
                }
            }
        });
    }
}
