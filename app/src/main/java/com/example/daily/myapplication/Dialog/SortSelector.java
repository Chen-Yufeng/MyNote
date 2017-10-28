package com.example.daily.myapplication.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.daily.myapplication.R;

public class SortSelector extends Dialog {
    //在构造方法里预加载我们的样式，这样就不用每次创建都指定样式了
    //查一下这种用法
    public SortSelector(Context context) {
        this(context, R.style.SortSelectorDialog);
    }

    public SortSelector(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预先设置Dialog的一些属性
        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.x = 0;
        p.y = 0;
        p.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dialogWindow.setAttributes(p);
    }


}