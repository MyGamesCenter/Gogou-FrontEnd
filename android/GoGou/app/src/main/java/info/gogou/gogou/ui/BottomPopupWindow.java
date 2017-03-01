package info.gogou.gogou.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;

/**
 * Created by lxu on 2016-04-17.
 */
public class BottomPopupWindow extends PopupWindow {

    public BottomPopupWindow(Activity context, int resource, final int menuLayoutId, int[] menuIds, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View menuView = inflater.inflate(resource, null);

        List<Button> buttons = new ArrayList<Button>();
        for (int i = 0; i < menuIds.length; i++) {
            Button button = (Button) menuView.findViewById(menuIds[i]);
            buttons.add(button);
        }

        for (Button button : buttons)
        {
            button.setOnClickListener(itemsOnClick);
        }

        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        //ColorDrawable dw = new ColorDrawable(0000000000);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.colorPrimary));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        menuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = menuView.findViewById(menuLayoutId).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
