package com.example.andrej.alarmandroidclient;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IServerDelegate {

    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private RecyclerView outputRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OutputListAdapter outputListAdapter;
    private Button button;
    private Thread serverThread;
    private SocketServer socketServer;
    private MediaPlayer alarmMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputRecyclerView = (RecyclerView)findViewById(R.id.outputRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        outputRecyclerView.setLayoutManager(layoutManager);

        outputListAdapter = new OutputListAdapter();
        outputRecyclerView.setAdapter(outputListAdapter);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        try {
            socketServer = new SocketServer(43542, this);
            serverThread = new Thread(socketServer);
            serverThread.start();

        } catch (Exception ex) {
            addMessage(ex.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socketServer != null) {
            try {
                socketServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setAlarmActivated(false);
    }

    private void addMessage(String message) {
        int verticalOffset = outputRecyclerView.computeVerticalScrollOffset();

        String timeText = timeFormat.format(new Date());

        outputListAdapter.add(0, timeText + ": " + message);

        int messageCount = outputListAdapter.getItemCount();

        if (messageCount > 200) {
            outputListAdapter.removeItemAtIndex(messageCount - 1);
        }

        if (verticalOffset == 0) {
            outputRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onClick(View v) {
        //addMessage("ngdaogn daiognas nasg iodangidas");


    }

    @Override
    public void serverGeneralException(Exception ex) {
        final Exception exc = ex;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addMessage(exc.toString());
            }
        });
    }

    @Override
    public void serverNewClientMessage(String message) {
        final String msg = message.trim();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addMessage(msg);
                setAlarmActivated(msg.equals("sirenOn:1"));
            }
        });
    }

    private void setAlarmActivated(Boolean activated) {
        if (activated) {
            if (alarmMediaPlayer == null) {
                alarmMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tornado_siren);
                alarmMediaPlayer.start();
            }
        } else {
            if (alarmMediaPlayer != null) {
                alarmMediaPlayer.stop();
                alarmMediaPlayer.release();
                alarmMediaPlayer = null;
            }
        }

    }
}
