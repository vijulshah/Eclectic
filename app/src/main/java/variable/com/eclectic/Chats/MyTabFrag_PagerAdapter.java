package variable.com.eclectic.Chats;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

class MyTabFrag_PagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public MyTabFrag_PagerAdapter(@NonNull FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
            {
                ChatTab chatTab = new ChatTab();
                return chatTab;
            }

            case 1:
            {
                StatusTab statusTab = new StatusTab();
                return statusTab;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
