package com.dev.android.sit_chat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dev.android.sit_chat.Fragments.CallsFragment;
import com.dev.android.sit_chat.Fragments.ChatsFragment;
import com.dev.android.sit_chat.Fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {


    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:  return new ChatsFragment();

            case 1:  return new StatusFragment();

            case 2:  return new CallsFragment();

            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        if(position==0){
            title="CHATS";
        }
        else if(position==1){
            title="STATUS";
        }else{
            title="CALLS";
        }
        return title;
    }

}
