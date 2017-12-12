package com.yida.ethereumsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 뷰 초기화
     */
    private void initView() {

        Button btn = findViewById(R.id.btn_run);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //시작
                    run();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 시작
     */
    private void run() throws Exception {
        // Using Infura with web3j -> https://docs.web3j.io/infura.html
        // We start by creating a new web3j instance to connect to remote nodes on the network.
        Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/<your token>"));  // FIXME: Enter your Infura token here;
        Log.i("test", "Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());
    }
}
