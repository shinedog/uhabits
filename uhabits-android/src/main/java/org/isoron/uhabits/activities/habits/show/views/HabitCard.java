/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.activities.habits.show.views;

import android.content.*;
import android.support.annotation.*;
import android.util.*;
import android.widget.*;

import org.isoron.androidbase.utils.*;
import org.isoron.uhabits.*;
import org.isoron.uhabits.core.models.*;
import org.isoron.uhabits.core.models.memory.*;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.isoron.androidbase.utils.InterfaceUtils.dpToPixels;

public abstract class HabitCard extends LinearLayout
    implements ModelObservable.Listener
{
    @NonNull
    private Habit habit;

    public HabitCard(Context context)
    {
        super(context);
        init();
    }

    public HabitCard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    @NonNull
    public Habit getHabit()
    {
        return habit;
    }

    public void setHabit(@NonNull Habit habit)
    {
        detachFrom(this.habit);
        attachTo(habit);

        this.habit = habit;
    }

    @Override
    public void onModelChange()
    {
        post(() -> refreshData());
    }

    @Override
    protected void onAttachedToWindow()
    {
        if (isInEditMode()) return;

        super.onAttachedToWindow();
        refreshData();
        attachTo(habit);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        detachFrom(habit);
        super.onDetachedFromWindow();
    }

    protected abstract void refreshData();

    private void attachTo(Habit habit)
    {
        habit.getObservable().addListener(this);
        habit.getRepetitions().getObservable().addListener(this);
    }

    private void detachFrom(Habit habit)
    {
        habit.getRepetitions().getObservable().removeListener(this);
        habit.getObservable().removeListener(this);
    }

    private void init()
    {
        int p16 = (int) dpToPixels(getContext(), 16);
        int p4 = (int) dpToPixels(getContext(), 4);
        setPadding(p16, p16, p4, p16);

        StyledResources sres = new StyledResources(getContext());
        setBackgroundColor(sres.getColor(R.attr.cardBackgroundColor));
        if(SDK_INT >= LOLLIPOP) {
            setElevation(dpToPixels(getContext(), 1));
        }

        if (!isInEditMode()) habit = new MemoryModelFactory().buildHabit();
    }
}
