package dm.example.androidclients;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText etIpAddress;
    EditText etPort;
    Button btnConnect;
    EditText etUsername;
    Button btnRegister;
    TextView tvUsername;

    TextView tvPlayer;
    Spinner spinnerUsers;
    Button btnOK;
    TextView tvQuestion;
    Button btnYes;
    Button btnNo;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    public BufferedReader getBr() {
        return br;
    }

    ReceiveMessageFromServer Receiver;

    public static String REQUEST_MESSAGE = "Request_key";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvQuestion.setEnabled(false);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setEnabled(false);
        btnNo = (Button) findViewById(R.id.btnNo);
        btnNo.setEnabled(false);
        tvPlayer = (TextView) findViewById(R.id.tvPlayer);
        tvPlayer.setEnabled(false);
        spinnerUsers = (Spinner) findViewById(R.id.spinner);
        spinnerUsers.setEnabled(false);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setEnabled(false);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etUsername.setEnabled(false);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setEnabled(false);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setEnabled(false);
        etIpAddress = (EditText) findViewById(R.id.etIpAddress);
        etPort = (EditText) findViewById(R.id.etPort);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = etIpAddress.getText().toString();
                int port = Integer.parseInt(etPort.getText().toString());

                Log.d("IP", address);
                Log.d("Port", String.format("value = %d", port));

                if(address.equals("10.0.2.2")  && String.format("%d", port).equals("6001")){
                    Log.d("CONNECTION", "Connecting to the server...");
                    connectToServer(address, port);

                    MainActivity.this.btnRegister.setEnabled(true);
                    MainActivity.this.etUsername.setEnabled(true);
                    MainActivity.this.tvUsername.setEnabled(true);

                    MainActivity.this.etPort.setEnabled(false);
                    MainActivity.this.etIpAddress.setEnabled(false);
                    MainActivity.this.btnConnect.setEnabled(false);

                }else{
                    Log.d("WRONG", "Wrong IP Address or Port.");
                    Toast.makeText(MainActivity.this, "Wrong IP Address or Port.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.this.etUsername.getText().toString().equals("")){
                    sendMessage(MainActivity.this.etUsername.getText().toString());
                    MainActivity.this.Receiver = new ReceiveMessageFromServer(MainActivity.this);
                    new Thread(MainActivity.this.Receiver).start();

                    MainActivity.this.etUsername.setEnabled(false);
                    MainActivity.this.tvUsername.setEnabled(false);
                    MainActivity.this.btnRegister.setEnabled(false);

                    MainActivity.this.tvPlayer.setEnabled(true);
                    MainActivity.this.spinnerUsers.setEnabled(true);
                    MainActivity.this.btnOK.setEnabled(true);
                }
                else {
                    MainActivity.this.tvPlayer.setEnabled(false);
                    MainActivity.this.spinnerUsers.setEnabled(false);
                    MainActivity.this.btnOK.setEnabled(false);
                }

            }
        });

        this.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.this.etUsername.getText().toString().equals(MainActivity.this.spinnerUsers.getSelectedItem().toString())){
                    String players = "Players:" + MainActivity.this.etUsername.getText().toString() + "," + MainActivity.this.spinnerUsers.getSelectedItem().toString();
                    sendMessage(players);
                }
                else{
                    Toast.makeText(MainActivity.this, "You can not play with yourself.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.btnNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MainActivity.this.tvQuestion.setText("");
                MainActivity.this.tvQuestion.setEnabled(false);
                MainActivity.this.btnYes.setEnabled(false);
                MainActivity.this.btnNo.setEnabled(false);
                sendMessage("NO:");
            }
        });

        this.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.tvQuestion.setText("");
                MainActivity.this.tvQuestion.setEnabled(false);
                MainActivity.this.btnYes.setEnabled(false);
                MainActivity.this.btnNo.setEnabled(false);
                String message = MainActivity.this.etIpAddress.getText().toString() + ";" + MainActivity.this.etPort.getText().toString() + ":" + "Player2";
                sendMessage("YES:");
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(REQUEST_MESSAGE, message);
                activity2Launcher.launch(intent);
            }
        });

    }

    public void connectToServer(String address, int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Singleton socketSingleton = Singleton.getInstance(address, port);
                    MainActivity.this.br = socketSingleton.getBr();
                    MainActivity.this.pw = socketSingleton.getPw();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.this.pw != null){
                    MainActivity.this.pw.println(message);
                }
            }
        }).start();
    }

    public void askQuestion(String gameInvitation){
        this.tvQuestion.setText("");
        this.tvQuestion.append(gameInvitation);
    }

    public Spinner getUsers() {
        return this.spinnerUsers;
    }


    public void challengeAccepted(String msg){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra(REQUEST_MESSAGE, msg);
//        startActivity(intent);
        activity2Launcher.launch(intent);
    }


    ActivityResultLauncher<Intent> activity2Launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        String response;
                        response = data.getStringExtra(GameActivity.RESPONSE_MESSAGE);
                        MainActivity.this.Receiver = new ReceiveMessageFromServer(MainActivity.this);
                        new Thread(MainActivity.this.Receiver).start();
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}