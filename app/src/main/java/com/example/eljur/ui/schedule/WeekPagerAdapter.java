package com.example.eljur.ui.schedule;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class WeekPagerAdapter extends FragmentStateAdapter
{

    public WeekPagerAdapter( @NonNull Fragment fragment )
    {
        super( fragment );
    }

    @NonNull
    @Override
    public Fragment createFragment( int position )
    {
        return WeekFragment.newInstance( position - 1000 );
    }

    @Override
    public int getItemCount()
    {
        return 2001;
    }

}
