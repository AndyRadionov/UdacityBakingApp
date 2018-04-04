package io.github.andyradionov.udacitybakingapp.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.github.andyradionov.udacitybakingapp.data.model.Recipe;

/**
 * @author Andrey Radionov
 */

public class BakingViewModel extends ViewModel {
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Integer> mStepNumber;

    public MutableLiveData<Recipe> getRecipe() {
        if (mRecipe == null) {
            mRecipe = new MutableLiveData<Recipe>() {
                @Override
                public void setValue(Recipe value) {
                    super.setValue(value);
                    getStepNumber().setValue(0);
                }
            };
        }
        return mRecipe;
    }

    public MutableLiveData<Integer> getStepNumber() {
        if (mStepNumber == null) {
            mStepNumber = new MutableLiveData<>();
        }
        return mStepNumber;
    }

    public void decStepNumber() {
        int stepNumber = getStepNumber().getValue();
        getStepNumber().setValue(--stepNumber);
    }

    public void incStepNumber() {
        int stepNumber = getStepNumber().getValue();
        getStepNumber().setValue(++stepNumber);
    }
}
