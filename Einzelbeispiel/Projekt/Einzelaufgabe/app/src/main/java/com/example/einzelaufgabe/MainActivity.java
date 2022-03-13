package com.example.einzelaufgabe;


import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    public Socket socket = null;
    protected PrintWriter out;
    protected BufferedReader in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//Verbindung
        try {
            socket = new Socket("se2-isys.aau.at", 53212);
        }catch(Exception e) {
            e.printStackTrace();
        }

        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        Button btn = findViewById(R.id.btnA);
        Button btnb = findViewById(R.id.btnberechne);

//BTN Abschicken
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable t = TCP();
                t.run();
            }
        });

//BTN Quersumme
        btnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txt =findViewById(R.id.editTextID);
                TextView txtmsg = findViewById(R.id.txtServer);

                int zahl = Integer.parseInt(txt.getText().toString());

                int[] array = new int[8];
                for (int i = 0; i< array.length;i++){
                    array[7-i]=zahl%10;
                    zahl = zahl/10;
                }

                int sum = 0;
                for (int i = 0; i< array.length;i++){
                    if(i%2==0){
                        sum+= array[array.length-1-i];
                    }else{
                        sum-= array[array.length-1-i];
                    }

                }

                txtmsg.setText(sum+"");
            }
        });

    }
//Runnable Methode für Button Abschicken
    public Runnable TCP(){
        Runnable t = new Runnable() {
            @Override
            public void run() {

                EditText txt =findViewById(R.id.editTextID);
                Button btn = findViewById(R.id.btnA);
                TextView txtmsg = findViewById(R.id.txtServer);

                out.println(txt.getText().toString());

                String response = null;
                try {
                    response = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                txtmsg.setText(response);
            }
        };

        return t;
    }


}