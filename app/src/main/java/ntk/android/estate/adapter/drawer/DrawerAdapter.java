package ntk.android.estate.adapter.drawer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.activity.common.AuthWithSmsActivity;
import ntk.android.base.activity.common.IntroActivity;
import ntk.android.base.activity.common.NotificationsActivity;
import ntk.android.base.activity.poling.PolingActivity;
import ntk.android.base.activity.ticketing.FaqActivity;
import ntk.android.base.activity.ticketing.TicketListActivity;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.room.NotificationStorageService;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.base.view.dialog.SweetAlertDialog;
import ntk.android.estate.MyApplication;
import ntk.android.estate.R;
import ntk.android.estate.activity.AboutUsActivity;
import ntk.android.estate.activity.ArticleListActivity;
import ntk.android.estate.activity.EstateListActivity;
import ntk.android.estate.activity.FavoriteEstateListActivity;
import ntk.android.estate.activity.MyEstateActivity;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.activity.NewsListActivity;
import ntk.android.estate.activity.ProfileActivity;


public class DrawerAdapter extends BaseRecyclerAdapter<DrawerChildThemeDtoModel, RecyclerView.ViewHolder> {

    protected final static int ID_LOGOUT = 1092;
    protected final static int ID_INVITE = 1095;
    private final Context context;

    private final FlowingDrawer Drawer;

    public DrawerAdapter(Context context, List<DrawerChildThemeDtoModel> children, FlowingDrawer drawer) {
        super(children);
        this.context = context;
        this.Drawer = drawer;
        drawable = R.drawable.sweet_error_center_x;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View view = inflate(viewGroup, R.layout.drawer_theme_1_header);
            return new HeaderViewHolder(view);
        } else {
            View view = inflate(viewGroup, R.layout.drawer_theme_1_item);
            return new MenuViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mholder, final int position) {
        if (position == 0) {
            bindHeader((HeaderViewHolder) mholder, position);
        } else {
            bindMenu((MenuViewHolder) mholder, position);
        }
    }

    private void bindMenu(MenuViewHolder holder, int position) {

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
                    clickMyEstates();
                    break;
                case 1:
                    ClickFavoriteEstateList();
                    break;
                case 2:
                    ClickNews();
                    break;
                case 3:
                    ClickArticle();
                    break;
                case 4:
                    ClickContact();
                    break;
                case 5:
                    ClickPooling();
                    break;
                case 6:
                    ClickInboxNotification();
                    break;
                case 7:
                    ClickQuestion();
                    break;
                case 8:
                    ClickFeedBack();
                    break;
                case 9:
                    ClickAbout();
                    break;
                case 10:
                    ClickIntro();
                    break;
//                case 11:
//
//                    break;
//                case 12:
//
//                    break;
                case ID_INVITE:
                    ClickShare();
                    break;
                case ID_LOGOUT:
                    logout();
                    break;
            }
        });
    }


    private void bindHeader(HeaderViewHolder holder, int position) {
        Long userid = Preferences.with(MyApplication.getAppContext()).UserInfo().userId();
        if (userid > 0) {
            String name = " تلفن همراه : " + Preferences.with(context).UserInfo().mobile();
            holder.name.setText(name);
            holder.userId.setText("شناسه کاربری : " + userid);
            holder.loginBtn.setText("حساب کاربری");
            holder.loginBtn.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                //clear all activity that open before
                v.getContext().startActivity(i);
            });
        } else {
            holder.name.setText("کاربر مهمان");
            holder.userId.setText("فاقد شناسه کاربری");
            holder.loginBtn.setText("ورود");
            holder.loginBtn.setOnClickListener(v -> {
                Preferences.with(v.getContext()).appVariableInfo().set_registerNotInterested(false);
                Intent i = new Intent(v.getContext(), AuthWithSmsActivity.class);
                //clear all activity that open before
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(i);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        if (Preferences.with(context).appVariableInfo().isLogin() && Preferences.with(context).UserInfo().userId() > 0)
            context.startActivity(new Intent(context, MyEstateActivity.class));
        else {
            //show dialog to go to login page
            SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            dialog.setTitle("خطا در انجام عملیات");
            dialog.setContentText("برای دیدین لیست املاک حود نیاز است که به حساب خود وارد شوید. آیا مایلید به صفحه ی ورود هدایت شوید؟");
            dialog.setConfirmButton("بلی", d -> {
                Preferences.with(d.getContext()).appVariableInfo().set_registerNotInterested(false);
                Intent i = new Intent(d.getContext(), AuthWithSmsActivity.class);
                //clear all activity that open before
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                d.getContext().startActivity(i);
                d.dismiss();
            });
            dialog.setCancelButton("تمایل ندارم", SweetAlertDialog::dismiss);
            dialog.show();
        }
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

    private void ClickArticle() {
        context.startActivity(new Intent(context, ArticleListActivity.class));
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

    private void logout() {
        Preferences.with(context).appVariableInfo().set_registerNotInterested(false);
        Preferences.with(context).appVariableInfo().setIsLogin(false);
        Intent i = new Intent(context, AuthWithSmsActivity.class);
        //clear all activity that open before
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        TextView title;
        TextView badge;

        RelativeLayout Root;

        public MenuViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.ImgRecyclerDrawerChild);
            title = view.findViewById(R.id.lblRecyclerDrawerChild);
            badge = view.findViewById(R.id.lblBadgeRecyclerDrawerChild);
            Root = view.findViewById(R.id.RootContainerRecyclerDrawerChild);
            title.setTypeface(FontManager.T1_Typeface(context));
            badge.setTypeface(FontManager.T1_Typeface(context));
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;

        TextView name;
        TextView userId;
        ExtendedFloatingActionButton loginBtn;

        public HeaderViewHolder(View view) {
            super(view);
            userImage = view.findViewById(R.id.userImage);
            name = view.findViewById(R.id.usernameTv);
            userId = view.findViewById(R.id.userIdTv);
            loginBtn = view.findViewById(R.id.loginBtn);
            name.setTypeface(FontManager.T1_Typeface(context));
            userId.setTypeface(FontManager.T1_Typeface(context));
            loginBtn.setTypeface(FontManager.T1_Typeface(context));
        }
    }

    public static List<DrawerChildThemeDtoModel> createDrawerItems(boolean allowDirectShareApp, boolean isLogin) {

        ArrayList<DrawerChildThemeDtoModel> list = new ArrayList<>();
        int i = 0;
        list.add(new DrawerChildThemeDtoModel().setId(-1).setTitle("سربرگ").setDrawableIcon(R.drawable.add));
//        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("آخرین ملک های ثبت شده").setDrawableIcon(R.drawable.estate));
//        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("ثبت ملک جدید").setDrawableIcon(R.drawable.add));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("املاک من").setDrawableIcon(R.drawable.my_estate));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("لیست علاقه مندی").setDrawableIcon(R.drawable.favorites_folder));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("اخبار").setDrawableIcon(R.drawable.news2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("مقالات").setDrawableIcon(R.drawable.article_place_holder));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("پشتیبانی").setDrawableIcon(R.drawable.inbox));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("نظرسنجی").setDrawableIcon(R.drawable.polling2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("صندوق پیام").setDrawableIcon(R.drawable.notification2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("پرسش های متداول").setDrawableIcon(R.drawable.faq2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("بازخورد").setDrawableIcon(R.drawable.feedback2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("درباره ما").setDrawableIcon(R.drawable.about_us2));
        list.add(new DrawerChildThemeDtoModel().setId(i++).setTitle("راهنما").setDrawableIcon(R.drawable.intro2));
        if (allowDirectShareApp) {
            list.add(new DrawerChildThemeDtoModel().setId(ID_INVITE).setTitle("دعوت از دوستان").setDrawableIcon(R.drawable.invite2));
        }
        if (isLogin) {
            list.add(new DrawerChildThemeDtoModel().setId(ID_LOGOUT).setTitle("خروج").setDrawableIcon(R.drawable.exit));
        }
        return list;
    }
}
