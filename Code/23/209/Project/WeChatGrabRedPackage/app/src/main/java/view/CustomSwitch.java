package view;

import android.content.Context;
import android.os.Build;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

/**
 * 自定义开关
 */
public class CustomSwitch extends SwitchPreference {

    /**
     * 用默认样式选项构造新的开关
     */
    public CustomSwitch(Context context) {
        super(context, null);
    }

    /**
     * 构造一个新的开关与给定的样式选项
     * @param context 上下文
     * @param attrs  与默认不同的样式属性
     */
    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造一个新的开关与给定的样式选项
     * @param context   上下文
     * @param attrs   与默认不同的样式属性
     * @param defStyle  定义默认样式选项的主题属性
     */
    public CustomSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //清理监听器之前调用开关
            ViewGroup viewGroup = (ViewGroup) view;
            clearListenerInViewGroup(viewGroup);
        }
        super.onBindView(view);
    }

    /**
     *
     * 在指定视图组的开关中清除监听器
     * @param viewGroup  需要清除监听器的视图组
     *
     */
    private void clearListenerInViewGroup(ViewGroup viewGroup) {
        if (null == viewGroup) {
            return;
        }

        int count = viewGroup.getChildCount();
        for (int n = 0; n < count; ++n) {
            View childView = viewGroup.getChildAt(n);
            if (childView instanceof Switch) {
                final Switch switchView = (Switch) childView;
                switchView.setOnCheckedChangeListener(null);
                return;
            } else if (childView instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) childView;
                clearListenerInViewGroup(childGroup);
            }
        }
    }

}