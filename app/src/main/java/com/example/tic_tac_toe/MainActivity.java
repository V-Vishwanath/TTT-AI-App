package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    char player = 'X', computer = 'O';
    char board[] = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    int move = 0;

    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chooseMarker(View view)
    {
        ImageView img = (ImageView) view;
        String marker = img.getTag().toString();

        if(marker.equals("markerX")) { player = 'X'; computer = 'O'; }
        else { player = 'O'; computer = 'X'; }

        ConstraintLayout open = findViewById(R.id.markerScreen);
        ConstraintLayout game = findViewById(R.id.gamewindow);

        open.setVisibility(View.INVISIBLE);
        game.setVisibility(View.VISIBLE);

        if(computer == 'X')
        {
            AIPlays();
            move++;
        }
    }

    public void placeMarker(View view)
    {
        ImageView img = (ImageView) view;

        int cell = Integer.parseInt(img.getTag().toString());
        cell--;

        if (!gameOver && board[cell] == ' ')
        {
            board[cell] = player;

            if(player == 'X') img.setImageResource(R.drawable.x);
            else img.setImageResource(R.drawable.o);

            img.setTranslationY(-1000);
            img.animate().translationYBy(1000).rotation(360 * 10).setDuration(500);

            if(win() == player)
            {
                gameOver = true;
                String message = "You win!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                Button reset = findViewById(R.id.playAgain);
                TextView display = findViewById(R.id.winner);

                display.setText(message);

                display.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
            }

            else if(move == 8)
            {
                Button reset = findViewById(R.id.playAgain);
                TextView display = findViewById(R.id.winner);

                display.setText("That is a tie!");

                display.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
            }

            move++;

            if(!gameOver)
            {
                AIPlays();

                if(win() == computer)
                {
                    gameOver = true;
                    String message = "Computer won!";
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                    Button reset = findViewById(R.id.playAgain);
                    TextView display = findViewById(R.id.winner);

                    display.setText(message);

                    display.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.VISIBLE);
                }

                else if(move == 8)
                {
                    Button reset = findViewById(R.id.playAgain);
                    TextView display = findViewById(R.id.winner);

                    display.setText("That is a tie!");

                    display.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.VISIBLE);
                }

                move++;
            }
        }

        else if (!gameOver) Toast.makeText(this, "That cell is occupied!", Toast.LENGTH_SHORT).show();
    }

    public char win()
    {
        if(board[0] != ' ' && board[0] == board[1] && board[1] == board[2]) return board[0];
        if(board[3] != ' ' && board[3] == board[4] && board[4] == board[5]) return board[3];
        if(board[6] != ' ' && board[6] == board[7] && board[7] == board[8]) return board[6];

        if(board[0] != ' ' && board[0] == board[3] && board[3] == board[6]) return board[0];
        if(board[1] != ' ' && board[1] == board[4] && board[4] == board[7]) return board[1];
        if(board[2] != ' ' && board[2] == board[5] && board[5] == board[8]) return board[2];

        if(board[0] != ' ' && board[0] == board[4] && board[4] == board[8]) return board[0];
        if(board[2] != ' ' && board[2] == board[4] && board[4] == board[6]) return board[2];

        return 'n';
    }

    public void resetGame(View view)
    {
        Button reset = (Button) findViewById(R.id.playAgain);
        TextView winner = (TextView) findViewById(R.id.winner);

        reset.setVisibility(View.INVISIBLE);
        winner.setVisibility(View.INVISIBLE);

        ImageView i1 = findViewById(R.id.slot1);
        ImageView i2 = findViewById(R.id.slot2);
        ImageView i3 = findViewById(R.id.slot3);
        ImageView i4 = findViewById(R.id.slot4);
        ImageView i5 = findViewById(R.id.slot5);
        ImageView i6 = findViewById(R.id.slot6);
        ImageView i7 = findViewById(R.id.slot7);
        ImageView i8 = findViewById(R.id.slot8);
        ImageView i9 = findViewById(R.id.slot9);

        i1.setImageDrawable(null);
        i2.setImageDrawable(null);
        i3.setImageDrawable(null);
        i4.setImageDrawable(null);
        i5.setImageDrawable(null);
        i6.setImageDrawable(null);
        i7.setImageDrawable(null);
        i8.setImageDrawable(null);
        i9.setImageDrawable(null);

        for(int i = 0; i < 9; i++) board[i] = ' ';
        gameOver = false;
        move = 0;

        ConstraintLayout game = findViewById(R.id.gamewindow);
        game.setVisibility(View.INVISIBLE);

        ConstraintLayout open = findViewById(R.id.markerScreen);
        open.setVisibility(View.VISIBLE);
    }

    public boolean movesAvailable()
    {
        for(int i = 0; i < 9; i++) if(board[i] == ' ') return true;
        return false;
    }

    public int max(int a, int b) { return a > b ? a : b; }
    public int min(int a, int b) { return a < b ? a : b; }

    public int minMax(char p)
    {
        char w = win();
        int s = w == player ? -10 : (w == computer ? 10 : 0);

        if(s == 10 || s == -10) return s;
        if(!movesAvailable()) return 0;

        int best;
        if(p == player)
        {
            best = 1000;
            for(int i = 0; i < 9; i++)
                if(board[i] == ' ')
                {
                    board[i] = player;
                    best = min(best, minMax(computer));
                    board[i] = ' ';
                }
        }
        else
        {
            best = -1000;
            for(int i = 0; i < 9; i++)
                if(board[i] == ' ')
                {
                    board[i] = computer;
                    best = max(best, minMax(player));
                    board[i] = ' ';
                }
        }

        return best;
    }

    public int bestMove()
    {
        if(movesAvailable())
        {
            int best = -1000, cellVal, cell = 0;
            for (int i = 0; i < 9; i++)
                if (board[i] == ' ') {
                    board[i] = computer;
                    cellVal = minMax(player);
                    board[i] = ' ';

                    if (cellVal > best) {
                        best = cellVal;
                        cell = i;
                    }
                }

            return cell;
        }

        return -1;
    }

    public void AIPlays()
    {
        int cell = bestMove();
        if(cell == -1) return;

        board[cell] = computer;
        cell++;

        ImageView img;

        if(cell == 1) img = findViewById(R.id.slot1);
        else if(cell == 2) img = findViewById(R.id.slot2);
        else if(cell == 3) img = findViewById(R.id.slot3);
        else if(cell == 4) img = findViewById(R.id.slot4);
        else if(cell == 5) img = findViewById(R.id.slot5);
        else if(cell == 6) img = findViewById(R.id.slot6);
        else if(cell == 7) img = findViewById(R.id.slot7);
        else if(cell == 8) img = findViewById(R.id.slot8);
        else img = findViewById(R.id.slot9);

        if(computer == 'X') img.setImageResource(R.drawable.x);
        else img.setImageResource(R.drawable.o);

        img.setTranslationY(-1000);
        img.animate().translationYBy(1000).rotation(360*10).setDuration(500);
    }
}
