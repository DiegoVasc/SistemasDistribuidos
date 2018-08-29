package tacio.sistemas_distribuidos.appsd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {



    SensorManager sensorManager;
    Sensor sensor;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";


    TextView textView;//MOSTAR DADOS NA TELA
    //MediaRecorder microfone;
    //private static double conversao = 0.0;
    //static final private double EMA_FILTER = 0.6;

    private Button bIP;
    private RadioButton b1, b2, b3;
    private EditText ip;
    private RadioGroup radio;
    int porta = 0;
    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bIP = (Button) findViewById(R.id.conectar_ip);//BOTAO PARA CONECTAR IP
        b1 = (RadioButton) findViewById(R.id.rbnt1);//PORTA 500
        b2 = (RadioButton) findViewById(R.id.rbnt2);//PORTA 5001
        b3 = (RadioButton) findViewById(R.id.rbnt3);//PORTA 5002
        ip = (EditText) findViewById(R.id.IP_servidor);//ONDE SERA DIGITADO IP DO SERVIDOR
        radio = (RadioGroup) findViewById(R.id.radioGroup);// LISTA DE RADIOBUTTON

        textView = (TextView) findViewById(R.id.textView2);//MOSTRA O VALOR DO SENSOR NA TELA;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    android.Manifest.permission.WAKE_LOCK, Manifest.permission.RECORD_AUDIO
            }, 0);
        }


        //SELECIONA POTA
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radio = (RadioButton) findViewById(i);

                if (radio.getText().toString().contains("9002")) {
                    porta = 9002;
                } else if (radio.getText().toString().contains("9003")) {
                    porta = 9003;
                } else if (radio.getText().toString().contains("9004")) {
                    porta = 9004;
                }
            }
        });

        bIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IP = ip.getText().toString();
                //Toast.makeText(MainActivity.this, "IP Salvo: "+IP, Toast.LENGTH_SHORT).show();

                //INCIANDO CLIENTE

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {


                                try {

                                    //Adicionando o IP configurável aqui
                                    //Toast.makeText(MainActivity.this, "IP/PORTA"+ip+"  "+porta, Toast.LENGTH_SHORT).show();
                                    Socket s = new Socket(IP, porta);//conecta a ip e porta

                                    OutputStream out = s.getOutputStream();

                                    PrintWriter output = new PrintWriter(out);

                                    output.println(textView.getText().toString());
                                    output.flush();

                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Erro: " + e, Toast.LENGTH_SHORT).show();
                                }

                            }

                        });
                        thread.start();


                    }
                });


            }
        });
    }



    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY){

            textView.setText(" " + event.values[0]);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {



    }


}
