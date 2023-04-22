package com.example.yaencasa.Data.Network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.yaencasa.Activity_Home;
import com.example.yaencasa.Auxiliary.Constants;
import com.example.yaencasa.Data.ModelOrder;
import com.example.yaencasa.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOrdersService extends Service {
    private Handler handler;
    private Runnable runnable;
    private ArrayList<ModelOrder> al_orders;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPEditor;
    private int lastIdNotified;

    private Context context;

    public CheckOrdersService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


        //Preferences
        sharedPreferences = getSharedPreferences("YaEnCasa", MODE_PRIVATE);
        sharedPEditor = sharedPreferences.edit();
        lastIdNotified = sharedPreferences.getInt("lastIdNotified", -1);

        //Array
        al_orders = new ArrayList<>();


        //Threath
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startAction();
                handler.postDelayed(this, 60000);
            }
        };

    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runnable);
        return START_STICKY;
    }

    private void startAction() {
        RetrofitOrdersImpl retrofitImpl = new RetrofitOrdersImpl();

        Call<ArrayList<ModelOrder>> call = retrofitImpl.fetchOrders(Constants.PHP_TOKEN);
        call.enqueue(new Callback<ArrayList<ModelOrder>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelOrder>> call, Response<ArrayList<ModelOrder>> response) {
                al_orders = response.body();
                checkNews();
            }

            @Override
            public void onFailure(Call<ArrayList<ModelOrder>> call, Throwable t) {

            }
        });
    }

    private void checkNews() {
        if (al_orders != null && !al_orders.isEmpty()) {
            ModelOrder modelOrder = al_orders.get(al_orders.size() - 1);
            checkLastNotified(modelOrder.getId());
        }
    }

    private void checkLastNotified(int lastIdNetwork) {
        lastIdNotified = sharedPreferences.getInt("lastIdNotified", -1);
        if (lastIdNetwork > lastIdNotified) {
            sharedPEditor.putInt("lastIdNotified", lastIdNetwork);
            sharedPEditor.apply();
            notifyNews();
        }
    }

    private void notifyNews() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentTitle(getString(R.string.tiene_nuevos_pedidos))
                .setContentText(getString(R.string.toque_para_ir_a_la_aplicaci_n))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID1 = "1";
            String channelName1 = "channel1";

            NotificationChannel channel = new NotificationChannel(channelID1, channelName1, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.enableVibration(true);

            builder.setChannelId(channelID1);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_VIBRATE);
        }


        Intent intent = new Intent(context, Activity_Home.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }


    }

}