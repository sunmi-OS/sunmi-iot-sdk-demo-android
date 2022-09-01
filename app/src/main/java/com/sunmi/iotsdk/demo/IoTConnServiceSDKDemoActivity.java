package com.sunmi.iotsdk.demo;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.EditText;

import com.sunmi.connectionservice.iot.DeviceConfigInfo;
import com.sunmi.connectionservice.iot.IActionListener;
import com.sunmi.connectionservice.iot.IConnectedListener;
import com.sunmi.connectionservice.iot.SunmiConnManager;

public class IoTConnServiceSDKDemoActivity extends BaseDemoActivity {
    private String demoDeviceId = "4da4d6b2762443ec96b8ad382331c9b4";
    private String demoDeviceSecret = "f4094dea6d3e7571aa8d5f792b9b36e41ad1e603394cc6b6bdc6f5dbf08f01eb";
    private String demoServerHttpGateway = "https://api.sunmi.com/v3/smlink/iot/device/getConfig";
    private IConnectedListener connectedListener = new IConnectedListener.Stub() {
        @Override
        public void onConnected() throws RemoteException {
            log("监听到：连接成功");
        }

        @Override
        public void onDisconnected(int errorCode, String errorReason) throws RemoteException {
            log("监听到：连接断开：" + errorCode + "," + errorReason);
        }
    };
    private EditText deviceIdEt;
    private EditText deviceSecretEt;
    private EditText serverHttpGatewayEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conn_service_demo);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    public void initView() {
        deviceIdEt = findViewById(R.id.et_device_id);
        deviceSecretEt = findViewById(R.id.et_device_secret);
        serverHttpGatewayEt = findViewById(R.id.et_server_http_gateway);
        deviceIdEt.setText(demoDeviceId);
        deviceSecretEt.setText(demoDeviceSecret);
        serverHttpGatewayEt.setText(demoServerHttpGateway);
        findViewById(R.id.btn_bind).setOnClickListener(v -> bind());
        findViewById(R.id.btn_isServiceConnected).setOnClickListener(v -> isServiceConnected());
        findViewById(R.id.btn_start).setOnClickListener(v -> start());
        findViewById(R.id.btn_stop).setOnClickListener(v -> stop());
        findViewById(R.id.btn_registerConnectedListener).setOnClickListener(v -> registerConnectedListener());
        findViewById(R.id.btn_unregisterConnectedListener).setOnClickListener(v -> unregisterConnectedListener());
        findViewById(R.id.btn_isConnected).setOnClickListener(v -> isConnected());
        findViewById(R.id.btn_getSDKVersion).setOnClickListener(v -> getSDKVersion());
    }

    /**
     * 绑定联接服务
     */
    private void bind() {
        SunmiConnManager.getInstance().bind(this, new IActionListener.Stub() {
            @Override
            public void onSuccess() throws RemoteException {
                log("服务绑定成功!");
            }

            @Override
            public void onError(int errorCode, String errorReason) throws RemoteException {
                log("服务绑定错误！错误码：" + errorCode + ",错误原因：" + errorReason);
            }
        });
    }

    private void isServiceConnected() {
        boolean isServiceConnected = SunmiConnManager.getInstance().isServiceConnected();
        log("服务绑定状态:" + isServiceConnected);
    }

    private void start() {
        String dId = deviceIdEt.getEditableText().toString();
        String dSecret = deviceSecretEt.getEditableText().toString();
        String serverHost = serverHttpGatewayEt.getEditableText().toString();
        DeviceConfigInfo deviceConfigInfo = new DeviceConfigInfo();
        deviceConfigInfo.deviceId = dId;
        deviceConfigInfo.deviceSecret = dSecret;
        deviceConfigInfo.serverHttpGateway = serverHost;
        SunmiConnManager.getInstance().start(deviceConfigInfo, new IActionListener.Stub() {
            @Override
            public void onSuccess() throws RemoteException {
                log("连接成功！");
            }

            @Override
            public void onError(int errorCode, String errorReason) throws RemoteException {
                log("连接异常：" + errorCode + "," + errorReason);
            }
        });
    }

    private void stop() {
        SunmiConnManager.getInstance().stop();
        log("断开连接！无法接收以及发送消息");
    }

    private void registerConnectedListener() {
        boolean result = SunmiConnManager.getInstance().registerConnectedListener(connectedListener);
        if (result) {
            log("注册连接状态监听成功");
        } else {
            log("注册连接状态监听异常，请检查服务绑定状态！isServiceConnected：" + SunmiConnManager.getInstance().isServiceConnected());
        }
    }


    private void unregisterConnectedListener() {
        SunmiConnManager.getInstance().unregisterConnectedListener(connectedListener);
        log("注销连接状态监听");
    }

    private void isConnected() {
        boolean isConnected = SunmiConnManager.getInstance().isConnected();
        if (isConnected) {
            log("当前已连接");
        } else {
            log("连接已断开");
        }
    }

    private void getSDKVersion() {
        String sdkVersion = SunmiConnManager.getInstance().getSDKVersion();
        log("SDK Version:" + sdkVersion);
    }
}
