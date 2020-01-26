package cn.wbnull.androiddemo.activity;

import android.os.Bundle;

import butterknife.OnClick;
import cn.wbnull.androiddemo.R;
import cn.wbnull.androiddemo.anno.ActivityLayoutInject;
import cn.wbnull.androiddemo.tool.CommonTools;

/**
 * 菜单界面
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
@ActivityLayoutInject(R.layout.activity_menu)
public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.menuBtnUpdate)
    public void onClickCheckUpdate() {
        CommonTools.startNewActivity(UpdateActivity.class);
    }
}
