package com.example.lucas.pi3;


        import android.app.Activity;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.media.MediaPlayer;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.Objects;
        import java.util.UUID;


/**
 * Created by:
 *
 * Alejandro Alcalde (elbauldelprogramador.com)
 * Cristina Heredia
 *
 * on 2/9/16.
 *
 * This file is part of MovementSound
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * Constants for sensors
     */
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    private static final int conectionRequest = 2;
    private static final int MESSAGE_READ = 3;

    Handler mHandler;

    StringBuilder dadosRecebidos = new StringBuilder();

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSocket=null;
    UUID meuUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ConnectedThread connectedThread;

    //Button frente;
    //Button re;
    boolean conectado=false;

    /**
     * The sounds to play when a pattern is detected
     */

    /**
     * Sensors
     */
    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private Sensor mSensorGyr;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    /**
     * UI
     */
    private TextView mGyrox;
    private TextView mGyroy;
    private TextView mGyroz;
    private TextView mAccx;
    private TextView mAccy;
    private TextView mAccz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the sensors to use
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Instanciate the sound to use

       // mGyrox = (TextView) findViewById(R.id.gyro_x);
       // mGyroy = (TextView) findViewById(R.id.gyro_y);
       // mGyroz = (TextView) findViewById(R.id.gyro_z);
        mAccx = (TextView) findViewById(R.id.accele_x);
        mAccy = (TextView) findViewById(R.id.accele_y);
        mAccz = (TextView) findViewById(R.id.accele_z);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //frente = findViewById(R.id.frente);
        //re = findViewById(R.id.re);

        // Verifica se há suporte a bluetooth
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "Não há suporte a Bluetooth", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Finalizando", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Gerencia o recebimento de dados do bluetooth
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == MESSAGE_READ){
                    String recebidos = (String) msg.obj;
                    dadosRecebidos.append(recebidos);           // acumula dados recebidos
                    int fimInformacao = dadosRecebidos.indexOf("}");

                    //Log.d("Recebidos parcial",recebidos);

                    if(fimInformacao > 0){

                        String dadosCompletos = dadosRecebidos.substring(0,fimInformacao);
                        Log.d("Recebidos",dadosCompletos);
                        dadosRecebidos = new StringBuilder();       // reinicia o acumulador de dados
                    }
                }

            }
        };



        // Envia uma string via bluetooth por meio de click em botão
//        re.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(getApplicationContext(), "solta",   Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//
//        frente.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(getApplicationContext(), "solta",   Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorGyr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mAccx.setText(R.string.act_main_no_acuracy);
                mAccy.setText(R.string.act_main_no_acuracy);
                mAccz.setText(R.string.act_main_no_acuracy);
//            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                mGyrox.setText(R.string.act_main_no_acuracy);
//                mGyroy.setText(R.string.act_main_no_acuracy);
//                mGyroz.setText(R.string.act_main_no_acuracy);
            }
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccx.setText("x = " + Float.toString(event.values[0]));
            mAccy.setText("y = " + Float.toString(event.values[1]));
            mAccz.setText("z = " + Float.toString(event.values[2]));
            if(conectado){
                byte y;
                byte z;

                char dados[]={(char)(event.values[1]*10)};//,(byte)(event.values[1]*10+127),(byte)(event.values[2]*10+127)};
                String x = new String(dados);
               // connectedThread.write(";");
                y =(byte)(event.values[1]*12.7+127);
                if(event.values[1]>9.5){
                    y=(byte)255;
                }else if(event.values[1]<-9.5){
                    y=(byte)0;
                }

                z =(byte)(event.values[2]*12.7+127);
                if(event.values[2]>9.5){
                    z=(byte)255;
                }else if(event.values[2]<-9.5){
                    z=(byte)0;
                }


                byte dado[]=new byte[4];
                dado[0]=y;
                dado[1]=z;
                dado[2]='\r';
                dado[3]='\n';

                connectedThread.write(dado);
                //byte x = (byte)(event.values[1]*10+127);
              //  connectedThread.write("Juca");
                //Log.d("dado enviado", y+";"+z+"\r\n");
                Log.d("dado yz","Y : "+((int)y)+" Z : "+((int)z));
//                Log.d("byte y",Byte.toString(dados[1]));
  //              Log.d("byte z",Byte.toString(dados[2]));
            }

            detectShake(event);
//        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            mGyrox.setText("x = " + Float.toString(event.values[0]));
//            mGyroy.setText("y = " + Float.toString(event.values[1]));
//            mGyroz.setText("z = " + Float.toString(event.values[2]));
//            detectRotation(event);
        }



    }

    // References:
    //  - http://jasonmcreynolds.com/?p=388
    //  - http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125

    /**
     * Detect a shake based on the ACCELEROMETER sensor
     *
     * @param event
     */
    private void detectShake(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            // Change background color if gForce exceeds threshold;
            // otherwise, reset the color
            if (gForce > SHAKE_THRESHOLD) {
            }
        }
    }

    /**
     * Detect a rotation in on the GYROSCOPE sensor
     *
     * @param event
     */
    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Opções da ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.primeiroBotao:
                if(conectado){
                    //desconectar
                    try{
                        meuSocket.close();
                        conectado = false;
                        Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();
                    }catch (IOException erro){
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
                    }
                }else{
                    //conectar
                    Intent abrelista = new Intent(MainActivity.this,ListaDispositivos.class);
                    startActivityForResult(abrelista,conectionRequest);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Checa o resultado da janela de conexão
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        switch (requestCode){
            case conectionRequest:
                if (resultCode == Activity.RESULT_OK){
                    String MAC = Objects.requireNonNull(data.getExtras()).getString(ListaDispositivos.ENDERECO_MAC);
                    //Toast.makeText(this, "MAC final" + MAC, Toast.LENGTH_LONG).show();
                    meuDevice = mBluetoothAdapter.getRemoteDevice(MAC);

                    try{
                        meuSocket = meuDevice.createInsecureRfcommSocketToServiceRecord(meuUUID);
                        meuSocket.connect();
                        conectado = true;

                        connectedThread = new ConnectedThread(meuSocket);
                        connectedThread.start();

                        Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show();
                    }catch(IOException erro){
                        conectado = false;
                        Toast.makeText(this, "Erro na conexão"+erro, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Falha ao obter MAC", Toast.LENGTH_SHORT).show();
                }
        }
    }


    // Gerenciamento da conexão bluetooth
    private class ConnectedThread extends Thread {
        // private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;  // buffer store for the stream
            buffer = new byte[1024];
            int bytes; // bytes returned from read()

            try {
                mmInStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Keep listening to the InputStream until an exception occurs
            while (conectado) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    String dadosBt = new String(buffer,0,bytes);
                    Log.d("recebido","y="+(int)buffer[0]+"z="+(int)buffer[1]+"[2]="+(int)buffer[2]+"[3]="+(int)buffer[3]);

                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, dadosBt).sendToTarget();

                } catch (IOException e) {

                    conectado=false;
                }
            }

//            mNotificationManager.cancelAll();
            //stopForeground(true);
            Log.d("Finalizando thread src",""+true);
        }

        // connectedThread.write(string);   Para enviar dados
        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] msgBuffer) {

            //byte[] msgBuffer = dadosEnviar.getBytes();
            try {
                mmOutStream.write(msgBuffer);
                sleep(20);
            } catch (IOException ignored) { } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }


}