package dm.example.androidclients;


import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveMessageFromServer implements Runnable {
    MainActivity parent;
    BufferedReader br;

    public static String REQUEST_MESSAGE = "Request_key";


    public void setProcessFlag(boolean processFlag) {
        this.processFlag = processFlag;
    }

    public boolean isProcessFlag() {
        return processFlag;
    }

    boolean processFlag = true;

    String line = "";

    public ReceiveMessageFromServer(MainActivity parent) {
        this.parent = parent;
        this.br = parent.getBr();
    }

    public void run() {
        while (this.processFlag == true) {
            try {
                line = this.br.readLine();

            switch (line.split(":")[0]) {
                case "Players":
                    String[] names = line.split(":")[1].trim().split(" ");
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Brisanje trenutnog sadrzaja Spinner-a
                            parent.getUsers().setAdapter(null);
                            // Popunjavanje Spinner-a sa novim podacima u vezi sa prisutnim korisnicima
                            // Prvo uzmi referencu na spiner iz glavne aktivnosti
                            Spinner spinner = parent.getUsers();
                            // Napravi ArrayAdapter na osnovu imena korisnika prepoznatih iz poruke servera
                            // i postavi taj adapter da bude adapter zeljenog spiner-a
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(parent, android.R.layout.simple_spinner_item, names);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            // Na kraju, azuriraj i TextView polje iz glavne aktivnosti tako sto
                            // ces dodati novi tekst na vec postojeci. U ovom tekstu ce stajati
                            // informacije o trenutno povezanim korisnicima u ChatRoom-u
                            //parent.setNewReceivedMessage("Novi clan se prikljucio ili je postojeci napustio sobu! Tretnutni clanovi su: " + line.split(": ")[1].toString());
                        }
                    });
                    break;
                case "Play game with":
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //stigao zahtev od playera1
                            String gameInvitation = "Play game with " + line.split(":")[1] + "?";
                            parent.tvQuestion.setEnabled(true);
                            parent.btnYes.setEnabled(true);
                            parent.btnNo.setEnabled(true);
                            parent.askQuestion(gameInvitation);
                        }
                    });
                    break;
                case "Not accepted by":
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String toastMessage1 = "Not accepted by " + line.split(":")[1] + ".";
                            Toast.makeText(parent, toastMessage1, Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                case "Accepted by":
                    this.processFlag = false;
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String toastMessage2 = "Accepted by " + line.split(":")[1] + ".";
                            Toast.makeText(parent, toastMessage2, Toast.LENGTH_LONG).show();
                            String message = parent.etIpAddress.getText().toString() + ";" + parent.etPort.getText().toString() + ":" + "Player1";
                            parent.challengeAccepted(message);
                        }
                    });
                    break;
                case "Set flag":
                    this.processFlag = false;
                    break;
                default:
                    Toast.makeText(parent, "Nesto drugo", Toast.LENGTH_LONG).show();
                    break;
            }

            } catch (IOException ex) {
                Toast.makeText(parent, "Ne mogu da primim poruku!", Toast.LENGTH_LONG).show();
            }



        }
    }
}
