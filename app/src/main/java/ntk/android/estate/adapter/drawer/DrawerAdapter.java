package ntk.android.estate.adapter.drawer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.activity.common.IntroActivity;
import ntk.android.base.activity.common.NotificationsActivity;
import ntk.android.base.activity.poling.PolingActivity;
import ntk.android.base.activity.ticketing.FaqActivity;
import ntk.android.base.activity.ticketing.TicketListActivity;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.room.NotificationStorageService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.AboutUsActivity;
import ntk.android.estate.activity.EstateListActivity;
import ntk.android.estate.activity.FavoriteEstateListActivity;
import ntk.android.estate.activity.MyEstateActivity;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.activity.NewsListActivity;


public class DrawerAdapter extends BaseRecyclerAdapter<DrawerChildThemeDtoModel, DrawerAdapter.ViewHolder> {


    private final Context context;

    private final FlowingDrawer Drawer;

    public DrawerAdapter(Context context, List<DrawerChildThemeDtoModel> children, FlowingDrawer drawer) {
        super(children);
        this.context = context;
        this.Drawer = drawer;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflate(viewGroup, R.layout.drawer_theme_1_item);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DrawerChildThemeDtoModel item = getItem(position);
        if (item.Icon != null)
            ImageLoader.getInstance().displayImage(item.Icon, holder.icon);
        else if (item.drawableIcon != 0)
            holder.icon.setImageResource(item.drawableIcon);
        if (item.Type == 1) {
            int n = new NotificationStorageService().getAllUnread(context).size();
            if (n != 0) {
                holder.badge.setText(String.valueOf(n));
                holder.badge.setVisibility(View.VISIBLE);
            }
        }
        holder.title.setText(item.Title);

        holder.Root.setOnClickListener(v -> {
            switch (item.Id) {
                case 0:
                    ClickEstateList();
                    break;
                case 1:
                    ClickNewEstate();
                    break;
                case 2:
                    ClickFavoriteEstateList();
                    break;
                case 3:
                    clickMyEstates();
                    break;
                case 4:
                    ClickNews();
                    break;
                case 5:
                    ClickContact();
                    break;
                case 6:
                    ClickPooling();
                    break;
                case 7:
                    ClickInboxNotification();
                    break;
                case 8:
                    ClickQuestion();
                    break;
                case 9:
                    ClickFeedBack();
                    break;
                case 10:
                    ClickShare();
                    break;
                case 11:
                    ClickAbout();
                    break;
                case 12:
                    ClickIntro();
                    break;
            }
        });
    }

    private void ClickEstateList() {
        context.startActivity(new Intent(context, EstateListActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickNewEstate() {
        NewEstateActivity.START_ACTIVITY(context);
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickFavoriteEstateList() {
        context.startActivity(new Intent(context, FavoriteEstateListActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void clickMyEstates() {
        context.startActivity(new Intent(context, MyEstateActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickNews() {
        context.startActivity(new Intent(context, NewsListActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickContact() {
        context.startActivity(new Intent(context, TicketListActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickPooling() {
        context.startActivity(new Intent(context, PolingActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickInboxNotification() {
        context.startActivity(new Intent(context, NotificationsActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickQuestion() {
        context.startActivity(new Intent(context, FaqActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickFeedBack() {
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
        ((AbstractMainActivity) context).onFeedbackClick();
    }


    private void ClickShare() {
        ((AbstractMainActivity) context).onInviteMethod();
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickAbout() {
        context.startActivity(new Intent(context, AboutUsActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }


    private void ClickIntro() {
        context.startActivity(new Intent(context, IntroActivity.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        TextView title;
        TextView badge;

        RelativeLayout Root;

        public ViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.ImgRecyclerDrawerChild);
            title = view.findViewById(R.id.lblRecyclerDrawerChild);
            badge = view.findViewById(R.id.lblBadgeRecyclerDrawerChild);
            Root = view.findViewById(R.id.RootContainerRecyclerDrawerChild);
            title.setTypeface(FontManager.T1_Typeface(context));
            badge.setTypeface(FontManager.T1_Typeface(context));
        }
    }

    public static List<DrawerChildThemeDtoModel> createDrawerItems() {
        ArrayList<DrawerChildThemeDtoModel> list = new ArrayList<>();
        int i = 0;
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("آخرین ملک های ثبت شده").setDrawableIcon(R.drawable.estate));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("ثبت ملک جدید").setDrawableIcon(R.drawable.add));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("لیست علاقه مندی").setDrawableIcon(R.drawable.favorites_folder));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("املاک من").setDrawableIcon(R.drawable.favorites_folder));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("اخبار").setDrawableIcon(R.drawable.news2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("پشتیبانی").setDrawableIcon(R.drawable.inbox));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("صندوق پیام دریافتی").setDrawableIcon(R.drawable.notification2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("نظرسنجی").setDrawableIcon(R.drawable.polling2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("پرسش های متداول").setDrawableIcon(R.drawable.faq2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("بازخورد").setDrawableIcon(R.drawable.feedback2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("دعوت از دوستان").setDrawableIcon(R.drawable.invite2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("درباره ما").setDrawableIcon(R.drawable.about_us2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("راهنما").setDrawableIcon(R.drawable.intro2));
        return list;
    }
}
