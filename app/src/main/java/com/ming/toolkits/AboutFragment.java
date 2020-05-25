package com.ming.toolkits;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

//import org.jetbrains.annotations.Nullable;

import androidx.annotation.Nullable;

import miui.app.AlertDialog;

public class AboutFragment extends PreferenceFragment implements OnPreferenceChangeListener {
    long fristTime=0;
    long[] mHits = new long[3];//改变数组长度即可改变点击次数
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setTheme(miui.R.style.Theme_Light_Settings);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainabout);
        additem();

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
if(preference.getKey().equals("group"))
{
    joinQQGroup("AnmJU_yXXu1JYvIzOFlwe9M2GRjpdw8a");
}

if(preference.getKey().equals("pay"))
{
    if (fristTime == 0) {
        fristTime = System.currentTimeMillis();//获取第一次点击的时间
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                    fristTime = 0;//为双击做准备
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    } else {
        long secondTime = System.currentTimeMillis();//获取第二次点击的时间
        if (secondTime - fristTime < 500) {
            Toast.makeText(getContext(),"我被双击了。。。",Toast.LENGTH_SHORT).show();
            fristTime = 0;//为下次双击做准备
        }
    }

}
        if(preference.getKey().equals("help")) {

            new AlertDialog.Builder(getContext())
                    .setTitle("温馨提示")
                    .setView(R.layout.teamlogo)
                    .setCancelable(true)
                    .setMessage("欢迎您使用 水木皆Ming 官改作品！\n" +
                                    "使用此软件时请保持Root权限，否则无法打开!\n" +
                                    "请勿将包传给未上车的小伙伴，否则后果自负！"
                            )
                    .show();
            // TODO: 2020/4/15
            //来源于谷歌源码
            // arraycopy 拷贝数组
            /*  参数解读如下：
             *  src the source array to copy the content.   拷贝哪个数组
             *	srcPos the starting index of the content in src.  从原数组中的哪里开始拷贝
             *	dst the destination array to copy the data into.  拷贝到哪个数组
             *	dstPos the starting index for the copied content in dst.  从目标数组的那个位置开始去写
             *	length the number of elements to be copied.  拷贝的长度
             */
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //获取离开机的时间
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            //单击时间的间隔，以500毫秒为临界值
            if (mHits[0] >= (SystemClock.uptimeMillis() - 500))
            {
                Settings.System.putInt(getContext().getContentResolver(),"impulse_drive_mode",Settings.System.getInt(getContext().getContentResolver(),"impulse_drive_mode",0)==0?1:0);
                int i = Settings.System.getInt(getContext().getContentResolver(),"impulse_drive_mode",0);
                if(i==1)
                {
                    Intent intent = new Intent("com.miui.app.ExtraStatusBarManager.action_enter_drive_mode");
                    getContext().sendBroadcast(intent);
                }
                else
                {
                    Intent intent = new Intent("com.miui.app.ExtraStatusBarManager.action_leave_drive_mode");
                    getContext().sendBroadcast(intent);
                }
                Toast.makeText(getContext(),"我被三击了。。。",Toast.LENGTH_SHORT).show();
                //一个三击（双击或多击事件完成），
                //把数组置为空并重写初始化，为下一次三击（双击或多击）做准备
                mHits = null;
                mHits = new long[3];
            }
        }
if(preference.getKey().equals("shuimujieming"))
{
    Intent intent = new Intent();
    intent.setData(Uri.parse("http://www.coolapk.com/u/1144501"));
    intent.setAction(Intent.ACTION_VIEW);
    this.startActivity(intent); //启动浏览器
}

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void additem(){
        findPreference("relative").setTitle("水木皆Ming");
        findPreference("ming").setTitle("团队成员");

        PreferenceScreen pay = (PreferenceScreen) findPreference("pay");

        findPreference("group").setTitle("加QQ群");
        findPreference("help").setTitle("使用提示");
        findPreference("device_name").setSummary(Build.MODEL);
        findPreference("device_type").setSummary(Build.DEVICE);

        //Build.DEVICE设备型号cepheus
        //Build.DISPLAY是BuildID，QKQ1.190825.002 test-keys.....
        //Build.BRAND品牌Xiaomi
        //Build.FINGERPRINT
        //Build.MODEL是MI 9
        //Build.VERSION.SDK安卓版本字符串类型
        //Build.VERSION.SDK——INT，整数类型

    }

    /****************
     *
     * 发起添加群流程。群号：Rom by 水木皆Ming(153805724) 的 key 为： ZwZjw-M9tjisU-hxUGLynaamA48IlgAn
     * 调用 joinQQGroup(ZwZjw-M9tjisU-hxUGLynaamA48IlgAn) 即可发起手Q客户端申请加群 Rom by 水木皆Ming(153805724)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
