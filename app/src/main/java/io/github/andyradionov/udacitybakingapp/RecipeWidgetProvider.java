package io.github.andyradionov.udacitybakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import io.github.andyradionov.udacitybakingapp.data.model.Recipe;
import io.github.andyradionov.udacitybakingapp.data.utils.RecipesLoader;
import io.github.andyradionov.udacitybakingapp.data.utils.WidgetPreferenceHelper;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String PREV_RECIPE_ACTION = "prev_recipe_action";
    private static final String NEXT_RECIPE_ACTION = "next_recipe_action";
    private static final String BOOT_COMPLETE_ACTION = "android.intent.action.BOOT_COMPLETED";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        views.setTextViewText(R.id.tv_widget_title, recipe.getName());
        views.setTextViewText(R.id.tv_widget_ingredients, recipe.getIngredientsString());

        views.setOnClickPendingIntent(R.id.btn_widget_prev,
                getPendingSelfIntent(context, PREV_RECIPE_ACTION));
        views.setOnClickPendingIntent(R.id.btn_widget_next,
                getPendingSelfIntent(context, NEXT_RECIPE_ACTION));

        Intent appIntent = new Intent(context, BakingActivity.class);
        appIntent.putExtra(BakingActivity.RECIPE_EXTRA, recipe);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_widget_ingredients, appPendingIntent);
        views.setOnClickPendingIntent(R.id.tv_widget_title, appPendingIntent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        int recipeId = WidgetPreferenceHelper.loadRecipeId(context);
        Recipe[] recipes = RecipesLoader.loadFromJsonFile(context);
        Recipe recipe = RecipesLoader.loadRecipeById(recipes, recipeId);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Timber.d("onReceive" + intent.getAction());
        int recipeId = WidgetPreferenceHelper.loadRecipeId(context);
        Recipe[] recipes = RecipesLoader.loadFromJsonFile(context);
        int minRecipeId = 1;
        int maxRecipeId = recipes[recipes.length - 1].getId();

        if (PREV_RECIPE_ACTION.equals(intent.getAction())) {
            recipeId = --recipeId < minRecipeId ? maxRecipeId : recipeId;
            WidgetPreferenceHelper.updateRecipeId(context, recipeId);
        } else if (NEXT_RECIPE_ACTION.equals(intent.getAction())) {
            recipeId = ++recipeId > maxRecipeId ? minRecipeId : recipeId;
            WidgetPreferenceHelper.updateRecipeId(context, recipeId);
        }

        Recipe recipe = RecipesLoader.loadRecipeById(recipes, recipeId);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = widgetManager.getAppWidgetIds(
                new ComponentName(context, RecipeWidgetProvider.class));

        for (int widgetId : widgetIds) {
            updateAppWidget(context, widgetManager, recipe, widgetId);
        }
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, RecipeWidgetProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
