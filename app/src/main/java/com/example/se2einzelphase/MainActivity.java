package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText input;
    TextView ergebnis;
    TextView txtAntwort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.editNumber);
        ergebnis = findViewById(R.id.txtErgebnis);
        txtAntwort = findViewById(R.id.txtAntwort);

        btn = findViewById(R.id.btnAbschicken);
        btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String matrN = input.getText().toString();
                        ergebnis.setText(toAscii(matrN));
                        Thread t = new Thread(new AsciiRun());
                        t.start();
                    }
                }
        );
    }

    public String toAscii(String number){
        char[] ascii = {'a','b','c','d','e','f','g','h','i','j'};
        String temp = "";

        for(int i=0; i < number.length(); i++){
            temp = temp + ascii[Character.getNumericValue(number.charAt(i))];
        }
        return temp;
    }

    public class AsciiRun implements Runnable{

        @Override
        public void run() {
            try {
                System.out.println("bla " );
                Socket clientSocket = new Socket("se2-isys.aau.at",53212);
                DataOutputStream outToServer = new DataOutputStream((clientSocket.getOutputStream()));
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes(input.getText().toString()+'\n');
                final String answer = inFromServer.readLine();
                System.out.println("bla " + answer);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtAntwort.setText(answer);
                    }
                });
                clientSocket.close();

            } catch (IOException e) {
                System.out.println("err");
                e.printStackTrace();
            }

        }
    }
}
