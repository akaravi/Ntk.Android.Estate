package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.activity.BaseActivity;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.estate.R;
import ntk.android.estate.adapter.drawer.DrawerAdapter;
import ntk.android.estate.fragment.MainActivity2Fragment;

public class MainActivity2 extends BaseMainActivity {
    ArrayList<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        List<DrawerChildThemeDtoModel> menus = DrawerAdapter.createDrawerItems();
        RecyclerView drawerRecycler = findViewById(R.id.RecyclerDrawer);
        DrawerAdapter adapter = new DrawerAdapter(this, menus, findViewById(R.id.floaingDrawer));
        drawerRecycler.setAdapter(adapter);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        drawerRecycler.setHasFixedSize(true);
        fragments.add(MainActivity2Fragment.newInstance((row1), R.color.tab_red_inactive));
        fragments.add(MainActivity2Fragment.newInstance((row2), R.color.tab_blue_inactive));
        fragments.add(MainActivity2Fragment.newInstance((row3), R.color.tab_blue_grey_inactive));
        fragments.add(MainActivity2Fragment.newInstance((row4), R.color.tab_green_inactive));
        ViewPager2 pager = findViewById(R.id.view_pager);
        pager.setAdapter(new MainPagerAdapter(this));
        BubbleNavigationConstraintView topMenu = findViewById(R.id.floating_top_bar_navigation);
        topMenu.setNavigationChangeListener((view, position) -> {
            if (position>fragments.size() )
                ((FlowingDrawer) findViewById(R.id.floaingDrawer)).openMenu(true);
            else
            pager.setCurrentItem(position, true);
        });
    }

    private class MainPagerAdapter extends FragmentStateAdapter {

        public MainPagerAdapter(@NonNull BaseActivity act) {
            super(act);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}
