package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Randall on 06/10/2015.
 */
public class FootballScoreWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = FootballScoreWidgetProvider.class.getSimpleName() ;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, FootballScoreWidgetService.class));
    }
}
