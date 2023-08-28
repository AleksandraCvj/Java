package dm.example.androidclients;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ReceiveMovesFromServer implements Runnable {

    GameActivity parent;
    BufferedReader br;

    boolean processFlag = false;


    public ReceiveMovesFromServer(GameActivity parent){
        this.parent = parent;
        this.br = parent.getBr();
    }

    @Override
    public void run() {
        while(this.processFlag == false) {
            String line = "";
            try {
                line = this.br.readLine();
            } catch (IOException ex) {
                Log.d("exception", "ex");
            }
            String msg = line.split(":")[0];
            String afterComma = line.split(":")[1];
            switch (msg) {
                case "Change photo":
                    if(parent.getUserOnMove().equals("Player1"))
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parent.changePhoto(Integer.parseInt(afterComma.split(",")[1]),"Player2");
                                parent.setMove(true);
                            }
                        });
                    else
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parent.changePhoto(Integer.parseInt(afterComma.split(",")[1]),"Player1");
                                parent.setMove(true);
                            }
                        });
                    break;
                case "Game done":
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            String winner = afterComma;
                            parent.btnYes.setEnabled(true);
                            parent.btnNo.setEnabled(true);
                            parent.etPlay.setEnabled(true);
                            parent.isGameFinished = true;
                            parent.printMessage("Winner is " + winner + ". Play again?");
                        }
                    });
                    break;
                case "Play again":
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            parent.isGameFinished = false;
                            parent.resetGame();
                            if(afterComma.equals("1")){
                                parent.setMove(true);
                            }

                        }
                    });
                    break;
                case "Back to main menu":
                    this.processFlag = true;
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            parent.endOfGame();
                        }
                    });
                    break;
                default:
                    Log.d("PORUKA STIGLA", "DRUGACIJI FORMAT");
                    break;
            }


        }
    }
}
