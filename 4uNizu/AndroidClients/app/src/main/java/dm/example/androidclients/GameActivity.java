package dm.example.androidclients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    public static String REQUEST_MESSAGE = "Request_key";
    public static String RESPONSE_MESSAGE = "Response_key";


    private Socket socket;

    private BufferedReader br;
    private PrintWriter pw;

    private boolean processFlag;

    public String getUserOnMove() {
        return userOnMove;
    }

    public void setUserOnMove(String userOnMove) {
        this.userOnMove = userOnMove;
    }

    private String userOnMove = "Player1";

    public void setMove(boolean move) {
        this.move = move;
    }

    private boolean move = true;

    private int numOfMoves = 0;

    private LinearLayout llmain;

    int numOfRows = 6;
    int numOfCol = 7;

    String firstPlayer;
    Intent intent;

    boolean isGameFinished = false;
    HashMap<String, ImageView> circles;

    public BufferedReader getBr() {
        return br;
    }

    EditText etPlay;
    Button btnYes;
    Button btnNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        etPlay= (EditText) findViewById(R.id.etPlay);
        etPlay.setEnabled(false);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setEnabled(false);
        btnNo = (Button) findViewById(R.id.btnNo);
        btnNo.setEnabled(false);

        intent = getIntent();
        String message = (String) intent.getStringExtra(REQUEST_MESSAGE);
        String data = message.split(":")[0];
        String ip = data.split(";")[0];
        int port = Integer.parseInt(data.split(";")[1]);
        GameActivity.this.userOnMove = message.split(":")[1];

        if(GameActivity.this.userOnMove.equals("Player1")){
            GameActivity.this.move = true;
        }else{
            GameActivity.this.move = false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Singleton socketSingleton = Singleton.getInstance(ip, port);
                    GameActivity.this.br = socketSingleton.getBr();
                    GameActivity.this.pw = socketSingleton.getPw();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();

        GameActivity.this.circles = new HashMap<String, ImageView>();
        GameActivity.this.llmain = findViewById(R.id.gameLayout);
        for (int col = 1; col <= numOfCol; col++) {
            LinearLayout llrow = new LinearLayout(this);
            llrow.setOrientation(LinearLayout.VERTICAL);
            llrow.setTag(col);

            for (int row = 1; row <= numOfRows; row++){
                ImageView iv = new ImageView(this);
                iv.setTag("gray");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 200);
                layoutParams.weight = 1;
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.drawable.gray_circle);
                GameActivity.this.circles.put(row + "," + col, iv);
                llrow.addView(iv);
            }

            llrow.setOnClickListener( v -> {
                if (isGameFinished) {
                    return;
                }else {
                    if (GameActivity.this.move) {
                        String tag = v.getTag().toString();
                        GameActivity.this.sendMessage("Move done:" + GameActivity.this.userOnMove + "," + tag);
                        int colPosition = Integer.parseInt(tag);
                        GameActivity.this.changePhoto(colPosition, GameActivity.this.userOnMove);

                        if (GameActivity.this.gameFinished()) {
                            GameActivity.this.sendMessage("Winner:" + GameActivity.this.userOnMove);

                        }

                        GameActivity.this.move = false;
                    } else {
                        Toast.makeText(GameActivity.this, "It is not your turn to play.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            GameActivity.this.llmain.addView(llrow);
        }

        new Thread(new ReceiveMovesFromServer(GameActivity.this)).start();

        this.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("Yes from:" + GameActivity.this.userOnMove );
                GameActivity.this.btnYes.setEnabled(false);
                GameActivity.this.btnNo.setEnabled(false);
                GameActivity.this.etPlay.setText("");
                GameActivity.this.etPlay.setEnabled(false);
            }
        });

        this.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("No from:" + GameActivity.this.userOnMove );
            }
        });

    }

    public void changePhoto(int colPosition, String player){
        for(int i = GameActivity.this.numOfRows; i > 0; i--){
            String colour = GameActivity.this.circles.get(i + "," + colPosition).getTag().toString();
            if(colour.equals("gray")){
                if(player.equals("Player1")){
                    GameActivity.this.circles.get(i + "," + colPosition).setImageResource(R.drawable.red_circle);
                    GameActivity.this.circles.get(i + "," + colPosition).setTag(player);
                    break;
                }
                else {
                    GameActivity.this.circles.get(i + "," + colPosition).setImageResource(R.drawable.blue_circle);
                    GameActivity.this.circles.get(i + "," + colPosition).setTag(player);
                    break;
                }
            }
        }
    }

    public void resetGame(){
//        GameActivity.this.TotalNumOfMoves = 42;
        GameActivity.this.btnYes.setEnabled(false);
        GameActivity.this.btnNo.setEnabled(false);
        GameActivity.this.etPlay.setText("");
        GameActivity.this.etPlay.setEnabled(false);
        for(int i = 1; i <= GameActivity.this.numOfRows; i++) {
            for (int j = 1; j <= GameActivity.this.numOfCol; j++) {
                GameActivity.this.circles.get(i + "," + j).setImageResource(R.drawable.gray_circle);
                GameActivity.this.circles.get(i + "," + j).setTag("gray");
            }
        }

    }

    public void endOfGame(){
        GameActivity.this.intent.putExtra(RESPONSE_MESSAGE, String.valueOf(true));
        setResult(RESULT_OK, intent);
        finish();
    }

    public boolean gameFinished() {
        int sameColour = 0;

        //horizontalni pravac
        for (int row = 1; row <= GameActivity.this.numOfRows; row++) {
            for (int col = 1; col <= GameActivity.this.numOfCol; col++) {
                if (GameActivity.this.circles.get(row + "," + col).getTag().toString().equals(GameActivity.this.userOnMove))
                    sameColour += 1;
                else
                    sameColour = 0;


                if (sameColour == 4)
                    return true;

            }
            sameColour = 0;
        }

        //vertikalni pravac
        for (int col = 1; col <= GameActivity.this.numOfCol; col++) {
            for (int row = 1; row <= GameActivity.this.numOfRows; row++) {
                if (GameActivity.this.circles.get(row + "," + col).getTag().toString().equals(GameActivity.this.userOnMove))
                    sameColour += 1;
                else
                    sameColour = 0;

                if (sameColour == 4)
                    return true;

            }
            sameColour = 0;
        }

        //dijagonalno sa leva na desno
        for(int row = 1; row <= 4; row++) {
            if (GameActivity.this.circles.get(row + "," + row).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 1; row <= 4; row++) {
            if (GameActivity.this.circles.get(row + "," + (row + 1)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 1; row <= 4; row++) {
            if (GameActivity.this.circles.get(row + "," + (row + 2)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 1; row <= 4; row++) {
            if (GameActivity.this.circles.get(row + "," + (row + 3)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 2; row <= 5; row++) {
            if (GameActivity.this.circles.get(row + "," + (row - 1)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 2; row <= 5; row++) {
            if (GameActivity.this.circles.get(row + "," + row).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 2; row <= 5; row++) {
            if (GameActivity.this.circles.get(row + "," + (row + 1)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 2; row <= 5; row++) {
            if (GameActivity.this.circles.get(row + "," + (row + 2)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 3; row <= 6; row++) {
            if (GameActivity.this.circles.get(row + "," + (row - 2)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 3; row <= 6; row++) {
            if (GameActivity.this.circles.get(row + "," + (row - 1)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 3; row <= 6; row++) {
            if (GameActivity.this.circles.get(row + "," + row).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        for(int row = 3; row <= 6; row++) {
            if (GameActivity.this.circles.get(row + "," + (row + 1)).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
        }

        //dijagonalno sa desna na levo
        int col1 = 1;
        for(int row = 6; row >= 3; row--){
            if (GameActivity.this.circles.get(row + "," + col1).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col1 += 1;
        }

        int col2 = 2;
        for(int row = 6; row >= 3; row--){
            if (GameActivity.this.circles.get(row + "," + col2).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col2 += 1;
        }

        int col3 = 3;
        for(int row = 6; row >= 3; row--){
            if (GameActivity.this.circles.get(row + "," + col3).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col3 += 1;
        }

        int col4 = 4;
        for(int row = 6; row >= 3; row--){
            if (GameActivity.this.circles.get(row + "," + col4).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col4 += 1;
        }

        int col5 = 1;
        for(int row = 5; row >= 2; row--){
            if (GameActivity.this.circles.get(row + "," + col5).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col5 += 1;
        }

        int col6 = 2;
        for(int row = 5; row >= 2; row--){
            if (GameActivity.this.circles.get(row + "," + col6).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col6 += 1;
        }

        int col7 = 3;
        for(int row = 5; row >= 2; row--){
            if (GameActivity.this.circles.get(row + "," + col7).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col7 += 1;
        }

        int col8 = 4;
        for(int row = 5; row >= 2; row--){
            if (GameActivity.this.circles.get(row + "," + col8).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col8 += 1;
        }

        int col9 = 1;
        for(int row = 4; row >= 1; row--){
            if (GameActivity.this.circles.get(row + "," + col9).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col9 += 1;
        }

        int col10 = 2;
        for(int row = 4; row >= 1; row--){
            if (GameActivity.this.circles.get(row + "," + col10).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col10 += 1;
        }

        int col11 = 3;
        for(int row = 4; row >= 1; row--){
            if (GameActivity.this.circles.get(row + "," + col11).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col11 += 1;
        }

        int col12 = 4;
        for(int row = 4; row >= 1; row--){
            if (GameActivity.this.circles.get(row + "," + col12).getTag().toString().equals(GameActivity.this.userOnMove))
                sameColour += 1;
            else
                sameColour = 0;
            if(sameColour == 4)
                return true;
            col12 += 1;
        }
        return false;
    }


    public void sendMessage(String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (GameActivity.this.pw != null){
                    GameActivity.this.pw.println(message);
                    GameActivity.this.pw.flush();
                }
            }
        }).start();
    }

    public void printMessage(String message){
        this.etPlay.setText("");
        this.etPlay.append(message);
    }


}


