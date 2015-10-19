package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Randall on 18/10/2015.
 */

/**
 * RemoteViewsService controlling the data being shown in the scrollable Football scores detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballScoreDetailWidgetRemoteViewsService extends RemoteViewsService {

    public final String LOG_TAG = FootballScoreDetailWidgetRemoteViewsService.class.getSimpleName();



    // these indices must match the projection
    private static final int INDEX_MATCH_ID = 0;
    private static final int INDEX_MATCH_TIME = 2;
    private static final int INDEX_HOME = 3;
    private static final int INDEX_AWAY = 4;
    private static final int INDEX_HOME_GOALS = 6;
    private static final int INDEX_AWAY_GOALS = 7;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

                String todayDate[] = new String[]{Utilies.getTodayDate()};

                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                // Get today's score from the ContentProvider
                data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null,
                        null, todayDate, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_match_detail_list_item);
                // Extract the scores from the Cursor

                String homeTeamName = data.getString(INDEX_HOME);
                int homeTeamScore = data.getInt(INDEX_HOME_GOALS);
                String matchTime = data.getString(INDEX_MATCH_TIME);
                String awayTeamName = data.getString(INDEX_AWAY);
                int awayTeamScore = data.getInt(INDEX_AWAY_GOALS);


                // Add the data to the RemoteViews
                views.setTextViewText(R.id.widget_detail_home_name,homeTeamName);
                views.setTextViewText(R.id.widget_detail_away_name,awayTeamName);
                views.setTextViewText(R.id.widget_detail_score, Utilies.getScores(homeTeamScore, awayTeamScore));
                views.setTextViewText(R.id.widget_detail_match_time, matchTime);
                views.setImageViewResource(R.id.widget_detail_home_crest, Utilies.getTeamCrestByTeamName(homeTeamName));
                views.setImageViewResource(R.id.widget_detail_home_crest, Utilies.getTeamCrestByTeamName(awayTeamName));

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_match_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_MATCH_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}


