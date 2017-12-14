package com.yida.ethereumsample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

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
        // 지갑 생성
        Button btn = findViewById(R.id.btn_run);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsync().execute();
            }
        });
    }

    class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                // Using Infura with web3j -> https://docs.web3j.io/infura.html
                // We start by creating a new web3j instance to connect to remote nodes on the network.
                String url = "https://rinkeby.infura.io/<your-token>";
                Web3j web3j = Web3jFactory.build(new HttpService(url));

                Log.i("test", "Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());

                String basePath = getFilesDir().getAbsolutePath();
                Log.i("test", "basePath : " + basePath);

                // create wallet file
                String privateKey = stringToHex("<your-private-key>");
                String password = "<your-password>";
                ECKeyPair keys = ECKeyPair.create(Hex.decode(privateKey));
                String walletAddress = generateWalletFile(password, keys, new File(getFilesDir(), ""), true);
                Log.i("test", "walletAddress : " + walletAddress);

                // We then need to load our Ethereum wallet file
                // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
                Credentials credentials = WalletUtils.loadCredentials(
                        password,
                        basePath + "/" + walletAddress);
                Log.i("test", "Credentials loaded");

                // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
//                Log.i("test", "Sending 1 Wei (" + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
//                TransactionReceipt transferReceipt = Transfer.sendFunds(
//                        web3j, credentials,
//                        "0x19e03255f667bdfd50a32722df860b1eeaf4d635",  // you can put any address here
//                        BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
//                        .send();
//                Log.i("test", "Transaction complete, view it at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash());
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // 문자열을 헥사 스트링으로 변환하는 메서드
        public String stringToHex(String s) {
            String result = "";

            for (int i = 0; i < s.length(); i++) {
                result += String.format("%02X ", (int) s.charAt(i));
            }

            return result;
        }

        /**
         * 지갑 만들기
         * @param password
         * @param ecKeyPair
         * @param destinationDirectory
         * @param useFullScrypt
         * @return
         * @throws CipherException
         * @throws IOException
         */
        public String generateWalletFile(
                String password, ECKeyPair ecKeyPair, File destinationDirectory, boolean useFullScrypt)
                throws CipherException, IOException {

            WalletFile walletFile;
            if (useFullScrypt) {
                walletFile = Wallet.createStandard(password, ecKeyPair);
            } else {
                walletFile = Wallet.createLight(password, ecKeyPair);
            }

            String fileName = getWalletFileName(walletFile);
            File destination = new File(destinationDirectory, fileName);

            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            objectMapper.writeValue(destination, walletFile);

            return fileName;
        }

        private String getWalletFileName(WalletFile walletFile) {
            return walletFile.getAddress();
        }
    }
}
