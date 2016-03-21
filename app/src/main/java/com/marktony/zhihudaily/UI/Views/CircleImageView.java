package com.marktony.zhihudaily.UI.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class CircleImageView extends ImageView {

    //基本的三个构造函数
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public CircleImageView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    //自定义view实现过程中很重要的onDraw绘制图形的方法
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        //空值判断，必要的步骤，避免由于没有设置src导致的异常错误
        if (drawable == null){
            return;
        }

        //必要步骤，避免由于初始化之前导致的异常错误
        if (getWidth() == 0 || getHeight() == 0){
            return;
        }

        if (!(drawable instanceof BitmapDrawable)){
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();

        if (null == b){
            return;
        }

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888,true);

        int w = getWidth();

        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap,0,0,null);

    }

    /**
     * 初始化Bitmap对象的缩放裁剪过程
     * @param bitmap 初始bitmap对象
     * @param radius 圆形图片的直径大小
     * @return 返回一个圆形的缩放裁剪过后的bitmap对象
     */
    public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {

        Bitmap sbmp;

        //比较初始的bitmap宽高和给定的圆形的直径，判断是否需要进行裁剪
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius){
            sbmp = Bitmap.createScaledBitmap(bitmap,radius,radius,false);
        } else {
            sbmp = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,sbmp.getWidth(),sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,sbmp.getHeight() / 2 + 0.7f,sbmp.getWidth() / 2 + 0.1f,paint);

        //核心部分，设置两张图片的交互模式，在这里就是上面绘制的Circle和下面绘制的bitmap
        /**
         *系统提供的Xfermode子类有三个：
         * AvoidXfermode
         * 指定了一个颜色和容差，强制Paint避免在它上面绘图(或者只在它上面绘图)。
         * PixelXorXfermode
         * 当覆盖已有的颜色时，应用一个简单的像素XOR操作。
         * PorterDuffXfermode
         * 这是一个非常强大的转换模式，使用它，可以使用图像合成的16条Porter-Duff规则的任意一条来控制Paint如何与已有的Canvas图像进行交互。
         * 可以简单理解为，canvas原有的图片就是背景，即dst， 新画上去的图片就是前景，即src
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp,rect,rect,paint);

        return output;

    }
}
