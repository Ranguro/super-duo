package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Randall on 09/10/2015.
 */
public class FootballScoreWidgetService extends IntentService {


    // these indices must match the projection
    private static final int INDEX_MATCH_TIME = 2;
    private static final int INDEX_HOME = 3;
    private static final int INDEX_AWAY = 4;
    private static final int INDEX_HOME_GOALS = 6;
    private static final int INDEX_AWAY_GOALS = 7;

    public FootballScoreWidgetService() {
        super("FootballScoreWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballScoreWidgetProvider.class));

        String todayDate[] = new String[]{Utilies.getTodayDate()};



        // Get today's score from the ContentProvider
        Cursor data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null,
                null, todayDate ,null);

        if (data == null){
            return;
        }
        if (!data.moveToFirst()){
            data.close();
            return;
        }

        // Extract the scores from the Cursor

        String homeTeamName = data.getString(INDEX_HOME);
        int homeTeamScore = data.getInt(INDEX_HOME_GOALS);
        String matchTime = data.getString(INDEX_MATCH_TIME);
        String awayTeamName = data.getString(INDEX_AWAY);
        int awayTeamScore = data.getInt(INDEX_AWAY_GOALS);


        // Perform this loop procedure for each Score widget
        for (int appWidgetId : appWidgetIds) {


            int layoutId = R.layout.football_score_simple_widget;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setTextViewText(R.id.widget_home_name,homeTeamName);
            views.setTextViewText(R.id.widget_away_name,awayTeamName);
            views.setTextViewText(R.id.widget_score_textview, Utilies.getScores(homeTeamScore, awayTeamScore));
            views.setTextViewText(R.id.widget_match_time_textview, matchTime);
            views.setImageViewResource(R.id.widget_home_crest, Utilies.getTeamCrestByTeamName(homeTeamName));
            views.setImageViewResource(R.id.widget_away_crest,Utilies.getTeamCrestByTeamName(awayTeamName));

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.scores_widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        data.close();
    }


    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        //return  getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
        return 1;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        //views.setContentDescription(R.id.widget_icon, description);
    }


}

