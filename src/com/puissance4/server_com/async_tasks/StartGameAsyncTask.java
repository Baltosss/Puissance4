package com.puissance4.server_com.async_tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.Puissance4.R;
import com.puissance4.configuration.GameConfiguration;
import com.puissance4.model.Party;
import com.puissance4.server_com.network_handlers.NetworkComm;
import com.puissance4.server_com.network_handlers.NetworkPlayer;
import com.puissance4.view.activities.GameActivity;
import com.puissance4.view.activities.MainActivity;

/**
 * Created by fred on 12/01/15.
 */
public class StartGameAsyncTask extends AsyncTask<Void, Void, Integer> {
    private Activity context;
    private NetworkPlayer opponent;

    public StartGameAsyncTask(Activity context, NetworkPlayer opponent) {
        this.context = context;
        this.opponent = opponent;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return NetworkComm.getInstance().proposeGame(opponent.getName(), GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH);
    }

    @Override
    protected void onPostExecute(Integer result) {
        String[] players = new String[2];
        switch (result) {
            case 0:
                //CREATE PARTY WITH SELF AS FIRST PLAYER
                Intent gameIntent = new Intent(context, GameActivity.class);
                players[0] = GameConfiguration.USERNAME;
                players[1] = opponent.getName();
                gameIntent.putExtra("party", new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                context.startActivity(gameIntent);
                break;
            case 1:
                //CREATE PARTY WITH SELF AS SECOND PLAYER
                Intent gameIntent1 = new Intent(context, GameActivity.class);
                players[1] = GameConfiguration.USERNAME;
                players[0] = opponent.getName();
                gameIntent1.putExtra("party", new Party(players, GameConfiguration.GRID_HEIGHT, GameConfiguration.GRID_WIDTH));
                context.startActivity(gameIntent1);
                break;
            case 2:
                //RETURN TO MAIN ACTIVITY
                Toast.makeText(context, R.string.gameRefused, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                break;
            case 3:
                //RETURN TO MAIN ACTIVITY
                Toast.makeText(context, R.string.proposeGameError, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(context, MainActivity.class);
                context.startActivity(intent1);
            default:
                //RETURN TO MAIN ACTIVITY
                Toast.makeText(context, R.string.proposeGameUnhandledError, Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(context, MainActivity.class);
                context.startActivity(intent2);
        }
    }
}