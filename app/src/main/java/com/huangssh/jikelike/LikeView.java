package com.huangssh.jikelike;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 说明：仿即刻点赞效果
 *
 * @作者 huangssh
 * @创建时间 2017/10/16 17:17
 * @版本
 * @------修改记录-------
 * @修改人
 * @版本
 * @修改内容
 */

public class LikeView extends LinearLayout {

    // 是否点赞
    private boolean isLike = false;

    // 左边的点赞icon
    private ImageView ivLike;
    // 点赞动画
    private ObjectAnimator mLikeAnimator;

    // 闪耀
    private ImageView ivShining;
    // 闪耀动画
    private ObjectAnimator mShiningAnimator;

    private Paint mPaint0 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTextSize;
    private int mTextPadding;
    // 文字移动距离
    private int translationY;
    // 至少的文字的移动位置
    private int mMoveY;
    // 文字动画
    private ObjectAnimator mTextAnimator;
    // 是否第一次显示
    private boolean isFirst = true;

    public int newNum;

    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }

    public LikeView(Context context) {
        super(context);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode())
            return;

        // ViewGroup默认情况下onDraw就不会被调用，这里设置成false
        setWillNotDraw(false);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_like, this);

        ivLike = findViewById(R.id.iv_like);
        ivShining = findViewById(R.id.iv_shining);

        initListener();

        mTextSize = DensityUtil.dip2px(getContext(), 16);
        mTextPadding = DensityUtil.dip2px(getContext(), 25);
        mMoveY = DensityUtil.dip2px(getContext(), 18);

        mPaint0.setTextSize(mTextSize);
        mPaint0.setColor(Color.BLACK);
        mPaint0.setAntiAlias(true);
        mPaint1.setTextSize(mTextSize);
        mPaint1.setColor(Color.BLACK);
        mPaint1.setAntiAlias(true);
        mPaint2.setTextSize(mTextSize);
        mPaint2.setColor(Color.BLACK);
        mPaint2.setAntiAlias(true);


        // 文字y位移
        mTextAnimator = ObjectAnimator.ofInt(this, "translationY", 0, mMoveY);
        mTextAnimator.setDuration(300);
    }

    // 变动位数，不变动位数
    private int digits, noDigits;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int num1 = newNum;
        int num2 = num1 + 1;
        String text1 = String.valueOf(num1);
        String text2 = String.valueOf(num2);
        if (text1.length() != text2.length()){
            text1 += " ";
        }

        double paddingLeft = 120;
        float baseLine;
        centerY = getHeight() / 2;
        // 计算基准线
        baseLine = centerY + -(mPaint1.getFontMetrics().top + mPaint1.getFontMetrics().bottom) / 2 ;

        // 计算变动位数
        for (int i = 0; i < text2.length(); i++) {
            String n1 = text1.substring(i, i + 1);
            String n2 = text2.substring(i, i + 1);
            if (!n1.equals(n2) && isFirst){
                digits = text2.length() - i;
                break;
            }
        }

        noDigits = text2.length() - digits;

        for (int i = 0; i < text2.length(); i++) {
            String n1 = text1.substring(i, i + 1);
            String n2 = text2.substring(i, i + 1);
            // 第2个数字开始文字x坐标递增
            if (i > 0){
                paddingLeft += mPaint1.measureText("0");
            }

            // 交替出现的数字透明度渐变
            int alpha = (int) ((1 - 1.0 * translationY / mMoveY) * 255);
            mPaint1.setAlpha(alpha);
            mPaint2.setAlpha(255 - alpha);


            // 加1之后同位不相等则启动动画置换
            if (!n1.equals(n2)){
                // x位移以半个身位往中心点聚拢
                float spacing = (1.0f * translationY / mMoveY) * (mPaint1.measureText("0") / 2f * ((digits - (1 + (digits - 1) * 0.5f)) - (i - noDigits)));
                // 点赞动画
                if (isLike()){
//                    float spacing = (1.0f * translationY / mMoveY) * (mPaint1.measureText("0"));
                    canvas.drawText(n1, (float) paddingLeft + spacing, baseLine - translationY, mPaint1);
                    canvas.drawText(n2, (float) paddingLeft, baseLine + mMoveY - translationY, mPaint2);

//                    // 变动位数为偶数
//                    if (digits % 2 == 0){
//                        if (text2.length() - i > digits / 2){
//                            canvas.drawText(n1, (float) paddingLeft + spacing, baseLine - translationY, mPaint1);
//                        }else{
//                            canvas.drawText(n1, (float) paddingLeft - spacing, baseLine - translationY, mPaint1);
//                        }
//                    // 变动位数为奇数
//                    }else{
//                        // 变动数为1或变动位为中间的数，x方向不做偏移
//                        if (digits == 1 || i == (digits + 1) / 2){
//                            canvas.drawText(n1, (float) paddingLeft, baseLine - translationY, mPaint1);
//                        }else{
//                            if (text2.length() - i > digits / 2f){
//                                canvas.drawText(n1, (float) paddingLeft + spacing, baseLine - translationY, mPaint1);
//                            }else{
//                                canvas.drawText(n1, (float) paddingLeft - spacing, baseLine - translationY, mPaint1);
//                            }
//                        }
//                    }
//                    canvas.drawText(n2, (float) paddingLeft, baseLine + mMoveY - translationY, mPaint2);
                }else{
                    // 首次不做动画
                    if (isFirst){
                        canvas.drawText(n1, (float) paddingLeft, baseLine, mPaint0);
                    // 取消赞动画
                    }else{

                        canvas.drawText(n2, (float) paddingLeft - spacing, baseLine + translationY, mPaint1);
                        canvas.drawText(n1, (float) paddingLeft, baseLine - mMoveY + translationY, mPaint2);
//                        // 变动位数为偶数
//                        if (digits % 2 == 0){
//                            if (text2.length() - i > digits / 2){
//                                canvas.drawText(n2, (float) paddingLeft - spacing, baseLine + translationY, mPaint1);
//                            }else{
//                                canvas.drawText(n2, (float) paddingLeft + spacing, baseLine + translationY, mPaint1);
//                            }
//                            // 变动位数为奇数
//                        }else{
//                            if (digits == 1 || i == (digits + 1) / 2){
//                                canvas.drawText(n1, (float) paddingLeft, baseLine + translationY, mPaint1);
//                            }else{
//                                if (text2.length() - i > digits / 2f){
//                                    canvas.drawText(n2, (float) paddingLeft - spacing, baseLine + translationY, mPaint1);
//                                }else{
//                                    canvas.drawText(n2, (float) paddingLeft + spacing, baseLine + translationY, mPaint1);
//                                }
//                            }
//                        }

//                        canvas.drawText(n1, (float) paddingLeft, baseLine - mMoveY + translationY , mPaint2);
                    }
                }
            // 其他位不用变动
            }else{
                canvas.drawText(n1, (float) paddingLeft, baseLine, mPaint0);
            }

            if (i == text2.length() - 1){
                isFirst = false;
            }
        }
    }

    private int centerX;
    private int centerY;

    private void initListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setLike(!isLike());
            }
        });
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        if (like){
            likeAnimStart();
        }else{
            unLikeAnimStart();
        }
        mTextAnimator.start();
        isLike = like;
    }

    private void likeAnimStart() {
        // 大拇指
        ivLike.setBackgroundResource(R.drawable.ic_messages_like_selected);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",1.0f, 0.4f, 1.2f, 1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",1.0f, 0.4f, 1.2f, 1.0f);

        mLikeAnimator = ObjectAnimator.ofPropertyValuesHolder(ivLike, scaleX, scaleY);


        // 点赞动画时候进行闪耀
        ivShining.setVisibility(View.VISIBLE);
        PropertyValuesHolder scaleXShining = PropertyValuesHolder.ofFloat("scaleX",0.1f, 0.2f, 1.0f);
        PropertyValuesHolder scaleYShining = PropertyValuesHolder.ofFloat("scaleY",0.1f, 0.2f, 1.0f);

        mShiningAnimator = ObjectAnimator.ofPropertyValuesHolder(ivShining, scaleXShining, scaleYShining);

        mLikeAnimator.start();
        mShiningAnimator.start();
    }

    private void unLikeAnimStart() {
        // 大拇指
        ivLike.setBackgroundResource(R.drawable.ic_messages_like_unselected);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",1.0f, 0.4f, 1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",1.0f, 0.4f, 1.0f);

        mLikeAnimator = ObjectAnimator.ofPropertyValuesHolder(ivLike, scaleX, scaleY);


        // 点赞动画时候进行闪耀
        ivShining.setVisibility(View.INVISIBLE);
        mLikeAnimator.start();
    }

    public void setTranslationY(int translationY) {
        this.translationY = translationY;
        invalidate();
    }

}
