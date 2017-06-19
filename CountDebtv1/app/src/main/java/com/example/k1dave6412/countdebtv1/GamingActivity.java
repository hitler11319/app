package com.example.k1dave6412.countdebtv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.k1dave6412.countdebtv1.GameActivity.host_id;
import static com.example.k1dave6412.countdebtv1.GameActivity.players;

public class GamingActivity extends AppCompatActivity {

    Button returnButton, passButton, addButton, nextPlayerButton;
    ImageView nowPlayers;
    TextView playerMs, nowNumber;
    int order_id = 0;
    int direction = 1, nnum = 0, btn_count = 0, ans, pre_player ;
    boolean endGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        nowPlayers = (ImageView) findViewById(R.id.nowPlayer);
        playerMs = (TextView) findViewById(R.id.PlayerMsg);
        nowNumber = (TextView) findViewById(R.id.nowNum);
        returnButton = (Button) findViewById(R.id.ReturnBtn);
        passButton = (Button) findViewById(R.id.PassBtn);
        addButton = (Button) findViewById(R.id.addNumBtn);
        nextPlayerButton = (Button) findViewById(R.id.nextBtn);

        if(host_id == 0){pre_player = 3;}else{pre_player = host_id -1;}


        Bundle bundle = getIntent().getExtras();
        ans = bundle.getInt("ans");
        endGame = bundle.getBoolean("endgame");
        order_id = host_id;  //第一位猜的是莊家自己

        nowNumber.setText(String.valueOf(nnum));   //當前累積的數字
        playerMs.setText(getResources().getString(R.string.choseMsg, players[order_id].getName()));
        choose_player_pic(order_id);
        buttonTimesCheck();

        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nextPlayerButton.getVisibility() == View.INVISIBLE) {
                    nextPlayerButton.setVisibility(View.VISIBLE);
                }
                btn_count += 1;
                nnum += 1;
                nowNumber.setText(String.valueOf(nnum));

                if (nnum == ans) {
                    addButton.setVisibility(View.INVISIBLE);
                    passButton.setVisibility(View.INVISIBLE);
                    returnButton.setVisibility(View.INVISIBLE);
                    nextPlayerButton.setVisibility(View.INVISIBLE);

                    players[order_id].setScore(-1);
                    if(pre_player == host_id){players[pre_player].setScore(2);}else{players[pre_player].setScore(1);}
                    if(players[order_id].getScore() <0){endGame = true;}
                    host_id += 1;
                    btn_count = 0;

                    if(endGame) {
                        String toastMsg = "5回合到了！重新開始遊戲！\n  排名如下：\n" + sort_rank();
                        Toast.makeText(GamingActivity.this, toastMsg, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(GamingActivity.this, MainActivity.class);
                        startActivity(i);
                        GamingActivity.this.finish();
                    }
                    else{
                        String toastMsg = players[order_id].getName() + "，你輸了！"+ players[pre_player].getName() +"得分\n遊戲即將重新開始！";
                        Toast.makeText(GamingActivity.this, toastMsg, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(GamingActivity.this, GameActivity.class);
                        startActivity(i);
                        GamingActivity.this.finish();
                    }

                }
                if (btn_count == 3) {
                    addButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        nextPlayerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPlayer();
            }
        });

        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction *= -1;
                players[order_id].useReturn();
                nextPlayer();
            }
        });

        passButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                players[order_id].usePass();
                nextPlayer();
            }
        });
    }

    public void choose_player_pic(int id) {
        switch (id) {
            case 0:
                nowPlayers.setImageResource(R.drawable.playericon);
                break;
            case 1:
                nowPlayers.setImageResource(R.drawable.player2icon);
                break;
            case 2:
                nowPlayers.setImageResource(R.drawable.player3icon);
                break;
            case 3:
                nowPlayers.setImageResource(R.drawable.player4icon);
                break;
        }
    }

    public void buttonTimesCheck() {
        if (players[order_id].getReturnTimes() > 0) {
            returnButton.setVisibility(View.VISIBLE);
        } else {
            returnButton.setVisibility(View.INVISIBLE);
        }

        if (players[order_id].getPassTimes() > 0) {
            passButton.setVisibility(View.VISIBLE);
        } else {
            passButton.setVisibility(View.INVISIBLE);
        }
    }

    public void nextPlayer() {
        pre_player = order_id;
        order_id = (order_id + direction) % players.length;
        if (order_id < 0) {
            order_id += players.length;
        }
        buttonTimesCheck();

        playerMs.setText(getResources().getString(R.string.choseMsg, players[order_id].getName()));
        choose_player_pic(order_id);
        addButton.setVisibility(View.VISIBLE);
        btn_count = 0;
        nextPlayerButton.setVisibility(View.INVISIBLE);
    }


    public String sort_rank(){
        String retS = "";

        for(int i = 0; i <= players.length-2; i++) {
            for(int j = i+1; j <= players.length -1 ; j++){
                if(players[i].getScore() < players[j].getScore()){
                    Player t;
                    t = players[i];
                    players[i] = players[j];
                    players[j] = t;
                }
            }
        }

        for(int i = 0; i <= players.length -1 ; i++){
            retS = retS + "第" + (i+1) +"名是： " + players[i].getName() + "     分數為：   " +  Integer.toString(players[i].getScore()) +  "分\n";
        }

        return retS;
    }

}