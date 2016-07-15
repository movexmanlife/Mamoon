package widget;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by GaoYL on 2015/7/29.
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                pxValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * px 2 sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, int pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                pxValue, context.getResources().getDisplayMetrics());
    }

    /**
     * dip 2 px 升级版
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px_ex(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px 2 dip 升级版
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip_ex(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * sp 2 px 升级版
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px_ex(Context context, float spValue) {
        return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * px 2 sp 升级版
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp_ex(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
