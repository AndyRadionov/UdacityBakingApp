package io.github.andyradionov.udacitybakingapp.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.List;

import io.github.andyradionov.udacitybakingapp.R;
import io.github.andyradionov.udacitybakingapp.app.App;
import io.github.andyradionov.udacitybakingapp.data.model.Recipe;
import io.github.andyradionov.udacitybakingapp.databinding.ActivityMainBinding;
import io.github.andyradionov.udacitybakingapp.ui.base.BaseDrawerActivity;
import io.github.andyradionov.udacitybakingapp.ui.steps.BakingActivity;
import timber.log.Timber;

public class MainActivity extends BaseDrawerActivity
        implements RecipesListAdapter.OnRecipeItemClickListener {

    private ActivityMainBinding mBinding;
    private RecipesListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate()");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAdapter = new RecipesListAdapter(this, this);
        mBinding.rvRecipesContainer.setAdapter(mAdapter);

        mBinding.rvRecipesContainer.setLayoutManager(getLayoutManager());
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        super.showRecipes(recipes);
        mBinding.pbRecipesLoading.setVisibility(View.INVISIBLE);
        mBinding.rvRecipesContainer.setVisibility(View.VISIBLE);
        mAdapter.updateData(recipes);
    }

    @Override
    public void onRecipeItemClick(Recipe recipe) {
        Timber.d("onRecipeItemClick(): %s", recipe);

        Intent startBakingActivity = new Intent(this, BakingActivity.class);
        startBakingActivity.putExtra(BakingActivity.RECIPE_EXTRA, recipe);
        startActivity(startBakingActivity);
    }

    @NonNull
    private RecyclerView.LayoutManager getLayoutManager() {
        Timber.d("getLayoutManager()");

        RecyclerView.LayoutManager layoutManager;
        if (isTablet()) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }
}
