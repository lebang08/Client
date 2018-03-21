package com.lebang.phone_box;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lebang.protobuf.PhoneBoxCallProto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端，连接WIFI
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LeBangDebug";

    private static final String SERVER_URL = "192.168.43.1";
    private static final int SERVER_SOCKET_PORT = 7666;

    private DataInputStream mInput;
    private DataOutputStream mOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhoneBoxCallProto.PhoneBoxCall.Builder builder = PhoneBoxCallProto.PhoneBoxCall.newBuilder();
        builder.setCall("calling");
        PhoneBoxCallProto.PhoneBoxCall call = builder.build();

        Log.e(TAG."phone call = " + call.);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "class begin");

        requestConnect();
    }

    /**
     * 连接WIFI，请求连接
     */
    private void requestConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket mDatagramSocket = new DatagramSocket();
                    InetAddress local = InetAddress.getByName(SERVER_URL);
                    String message = "hello car life";
                    int msg_length = message.length();
                    byte[] messageByte = message.getBytes();
//                    Log.e(TAG, "send msg = " + Arrays.toString(messageByte));
                    DatagramPacket mDatagramPacket = new DatagramPacket(messageByte, msg_length, local, 7996);

                    mDatagramSocket.send(mDatagramPacket);
                    mDatagramSocket.close();
//                    Log.e(TAG, "send broadcast packet, ip = " + local.getHostAddress());

                    startAllConnectSocket();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startAllConnectSocket() {
        ServerSocket mServerSocket;

        Socket mSocketCommand = null;

        try {
            mServerSocket = new ServerSocket(SERVER_SOCKET_PORT);
            mSocketCommand = mServerSocket.accept();

            mInput = new DataInputStream(mSocketCommand.getInputStream());
            mOutput = new DataOutputStream(mSocketCommand.getOutputStream());

            mOutput.writeUTF("PX3: hello Server , I am Client");
            Log.e(TAG, "PX3 :" + mInput.readUTF());

        } catch (Exception ex) {
            Log.d(TAG, "start ConnectSocket fail");
            ex.printStackTrace();
        }

    }


}
