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

import info.gogou.gogou.R;


/**
 * Created by grace on 15-11-24.
 */
public class RequirementMoreChoicePopWindow extends PopupWindow{
    private Button get_destination_btn, hign_score_btn, hign_fee_btn;
    private View mMenuView;

    public RequirementMoreChoicePopWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.requiremnt_more_choice_layout, null);


        get_destination_btn = (Button) mMenuView.findViewById(R.id.requiremnet_get_destination_button);
        hign_score_btn = (Button) mMenuView.findViewById(R.id.requirement_hign_score_button);
        hign_fee_btn = (Button) mMenuView.findViewById(R.id.requiremnt_hign_fee_button);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();

        //设置按钮监听
        get_destination_btn.setOnClickListener(itemsOnClick);
        hign_score_btn.setOnClickListener(itemsOnClick);
        hign_fee_btn.setOnClickListener(itemsOnClick);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 4 +50 );
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //刷新状态
        this.update();
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.imagePopupMenu).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 4, 15);
        } else {
            this.dismiss();
        }
    }
}
