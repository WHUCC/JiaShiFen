package cn.vailing.chunqiu.jiashifen.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;

import android.os.Build;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import cn.vailing.chunqiu.jiashifen.R;

import cn.vailing.chunqiu.jiashifen.engine.LockDialogHelper;
import cn.vailing.chunqiu.jiashifen.engine.RecordManager;
import cn.vailing.chunqiu.jiashifen.engine.SimpleRxGalleryFinal;
import cn.vailing.chunqiu.jiashifen.fragment.AddRecordFragment;
import cn.vailing.chunqiu.jiashifen.fragment.ChooseClassFragment;
import cn.vailing.chunqiu.jiashifen.fragment.RecordBrowseFragment;
import cn.vailing.chunqiu.jiashifen.fragment.RecordInfoFragment;
import cn.vailing.chunqiu.jiashifen.util.CalenderUtil;
import cn.vailing.chunqiu.jiashifen.util.ContentValueUtil;
import cn.vailing.chunqiu.jiashifen.util.ImmerseUtil;
import cn.vailing.chunqiu.jiashifen.util.SpUtil;
import yalantis.com.sidemenu.model.SlideMenuItem;

import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;


import com.orm.SchemaGenerator;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarDb;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainActivity extends ActionBarActivity implements ViewAnimator.ViewAnimatorListener, SupportAnimator.AnimatorListener {

    private yalantis.com.sidemenu.util.ViewAnimator viewAnimator;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    public static int res = R.drawable.main_bk;
    private GridLayout gridLayout;
    public static Toolbar toolbar;
    public static int topId = R.drawable.main_top;
    public static String color = "#d23b20";
    public static TextView topTitle;
    private AddRecordFragment addRecordFragment;
    private ChooseClassFragment chooseClassFragment;
    private RecordBrowseFragment recordBrowseFragment;
    private Toast toast = null;
    private TextView textView = null;
    private long firstTime = 0;
    private float scale;
    private RecordInfoFragment recordInfoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPower();
        initData();
        setImmsere();
        initToast();
        initIconChooser();
        initUI();
        setActionBar();
        createMenuList();
        viewAnimator = new yalantis.com.sidemenu.util.ViewAnimator<>(this, list, chooseClassFragment, drawerLayout, this);
    }

    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,}, 1);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,}, 1);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            }
        }

    }

    private void initData() {
        LockDialogHelper.getInstance().init(this);
        SugarContext.init(getApplicationContext());
        SchemaGenerator schemaGenerator = new SchemaGenerator(this);
        schemaGenerator.createDatabase(new SugarDb(this).getDB());
        RecordManager.getInstance().init(getApplicationContext());
        scale = this.getResources().getDisplayMetrics().density;
    }

    private void initToast() {
        toast = new Toast(this);
        textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(18f);
        textView.setBackgroundResource(R.drawable.imagebutton_shape);
        textView.setTextColor(0xffd1d2d1);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);
    }

    private void initIconChooser() {


    }

    private void setImmsere() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
        }
        ImmerseUtil.setImmerse(this, color);
    }

    private void initUI() {
        if (TextUtils.isEmpty(SpUtil.getString(this, ContentValueUtil.LOCK, null)))
            LockDialogHelper.getInstance().createLockDialog();
//        boolean isNeedStudy = RecordManager.getInstance().isNeedStudy();
        chooseClassFragment = ChooseClassFragment.newInstance(R.drawable.main_bk);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, chooseClassFragment)
                .commit();
        addRecordFragment = AddRecordFragment.newInstance(this.res);
        recordBrowseFragment = recordBrowseFragment.newInstance(this.res);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        gridLayout = (GridLayout) findViewById(R.id.left_drawer);
        gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        topTitle = (TextView) findViewById(R.id.top_title);

    }


    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentValueUtil.CLOSE, R.drawable.bk_transplant);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentValueUtil.HOSTPAGE, R.drawable.host_page);
        list.add(menuItem);
        SlideMenuItem menuItem1 = new SlideMenuItem(ContentValueUtil.ADDRECORD, R.drawable.add_record);
        list.add(menuItem1);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentValueUtil.RECORDBROWSE, R.drawable.record_browse);
        list.add(menuItem2);
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                gridLayout.removeAllViews();
                gridLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && gridLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case ContentValueUtil.CLOSE:
                return screenShotable;
            case ContentValueUtil.HOSTPAGE:
                return replaceChooseClassFragment(screenShotable, position);
            case ContentValueUtil.ADDRECORD:
                return replaceAddRecordFragment(screenShotable, position);
            case ContentValueUtil.RECORDBROWSE:
                return replaceRecordBrowseFragment(screenShotable, position);
            default:
                return screenShotable;
        }
    }

    private ScreenShotable replaceRecordBrowseFragment(ScreenShotable screenShotable, int position) {
        if (res == R.drawable.browse_bk)
            return recordBrowseFragment;
        res = R.drawable.browse_bk;
        topId = R.drawable.browse_top;
        color = "#085959";
        topTitle.setText(R.string.record_browse);
        ImmerseUtil.setImmerse(this, color);
        toolbar.setBackgroundResource(topId);
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        recordBrowseFragment = recordBrowseFragment.newInstance(res);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, recordBrowseFragment).commit();
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, position, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(yalantis.com.sidemenu.util.ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
        animator.addListener(this);
        animator.start();

        return recordBrowseFragment;
    }

    private ScreenShotable replaceAddRecordFragment(ScreenShotable screenShotable, int position) {
        if (res == R.drawable.record_bk)
            return addRecordFragment;
        res = R.drawable.record_bk;
        topId = R.drawable.other_top;
        color = "#122c24";
        topTitle.setText(R.string.add_record);
        ImmerseUtil.setImmerse(this, color);
        toolbar.setBackgroundResource(topId);
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        addRecordFragment = AddRecordFragment.newInstance(res);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, addRecordFragment).commit();

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, position, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(yalantis.com.sidemenu.util.ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
        animator.addListener(this);
        animator.start();
        return addRecordFragment
                ;
    }

    private ScreenShotable replaceChooseClassFragment(ScreenShotable screenShotable, int position) {
        if (this.res == R.drawable.main_bk)
            return chooseClassFragment;
        return changeToHost(0, position);

    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (this.res != R.drawable.main_bk) {
            WindowManager wm1 = this.getWindowManager();
            int width = wm1.getDefaultDisplay().getWidth();
            int height = wm1.getDefaultDisplay().getHeight();
            changeToHost(width, height);

        } else {
            if (secondTime - firstTime > 2000) {

                textView.setText("再按一次退出程序");
                toast.show();
                firstTime = secondTime;
            } else {
                SugarContext.terminate();
                toast.cancel();
                super.onBackPressed();
            }
        }

    }

    private ChooseClassFragment changeToHost(int width, int height) {

        res = R.drawable.main_bk;
        topId = R.drawable.main_top;
        color = "#d23b20";
        topTitle.setText(R.string.host_page);
        ImmerseUtil.setImmerse(this, color);
        toolbar.setBackgroundResource(topId);
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        chooseClassFragment = ChooseClassFragment.newInstance(res);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, chooseClassFragment).commit();
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, width / 2, height, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(yalantis.com.sidemenu.util.ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
        animator.addListener(this);
        animator.start();
        return chooseClassFragment;
    }


    @Override
    public void addViewToContainer(View view) {
        view.setBackgroundResource(R.drawable.bk_b);
        GridLayout.LayoutParams paramsGl = new GridLayout.LayoutParams(
                new ViewGroup.LayoutParams((int) (60 * scale + 0.5f), (int) (60 * scale + 0.5f)));
        gridLayout.addView(view, paramsGl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleRxGalleryFinal.get().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAnimationStart() {
        toolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationEnd() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationCancel() {

    }

    @Override
    public void onAnimationRepeat() {

    }

}
