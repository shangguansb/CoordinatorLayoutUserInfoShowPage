package example.jamase.userinfo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by jamase on 2016-03-18.
 */


public class BlurredLayout extends FrameLayout {

    public BlurredLayout(Context context) {
        this(context, null, 0);
    }

    public BlurredLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurredLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            mRenderScript = null;
        } else {
            mRenderScript = RenderScript.create(context);
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @TargetApi(21)
    @SuppressWarnings("unused")
    public BlurredLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (isInEditMode()) {
            mRenderScript = null;
        } else {
            mRenderScript = RenderScript.create(context);
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private static final Paint BITMAP_PAINT = new Paint();
    private static final String BLUR_REGION_TAG = "blur region";
    private static final String TAG = "BlurredLayout";

    static {
        BITMAP_PAINT.setAntiAlias(false);
        BITMAP_PAINT.setFilterBitmap(false);
        BITMAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    private final RenderScript mRenderScript;

    private Rect[] mBlurRects;
    private float[] mBlurRadii;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isInEditMode()) {
            super.dispatchDraw(canvas);
            return;
        }

        if (mBlurRects == null)
            findBlurRects();

        boolean externalCanvas = getBitmap(canvas);

        super.dispatchDraw(mCanvas);

        if (!externalCanvas) { // draw on own canvas :|
            canvas.drawBitmap(mBitmap, 0, 0, BITMAP_PAINT);
            Log.e(TAG, "Overdraw :'(");
        }

        for (int i = 0; i < mBlurRects.length; i++) {
            blurRect(mBlurRects[i], mBlurRadii[i], canvas);
        }

        if (!externalCanvas)
            mBitmap.recycle();
    }

    private boolean getBitmap(Canvas canvas) {
        boolean external = true;
        mBitmap = null;
        try {
            Field internalCanvas = Canvas.class.getDeclaredField("mBitmap");
            internalCanvas.setAccessible(true);
            mBitmap = (Bitmap) internalCanvas.get(canvas);
            mCanvas = canvas;
        } catch (Exception e) {
            Log.e(TAG, "getBitmap: reflection Exception", e);
        }
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            external = false;
            Log.e(TAG, "getBitmap: got empty bitmap from canvas. Created new.");
        }
        return external;
    }

    private void blurRect(Rect rect, float radius, Canvas canvas) {
        Bitmap blurred = Bitmap.createBitmap(mBitmap, rect.left, rect.top,
                rect.right - rect.left, rect.bottom - rect.top);

        Allocation input = Allocation.createFromBitmap(mRenderScript, blurred);
        Allocation output = Allocation.createTyped(mRenderScript, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output); // FIXME: the most expensive command
        output.copyTo(blurred);

        canvas.drawBitmap(blurred, rect.left, rect.top, BITMAP_PAINT);
        blurred.recycle();
    }

    public void setBlurRadius(int id, float value) {
        if (mBlurRadii == null)
            mBlurRadii = new float[id + 1];
        else if (mBlurRadii.length <= id) {
            expandRadii(id + 1);
        }
        mBlurRadii[id] = value;
        checkBlurRadii();
    }

    private void findBlurRects() {
        List<Rect> blurRects = new LinkedList<>();
        int cnt = getChildCount();
        for (int i = 0; i < cnt; i++) {
            View v = getChildAt(i);
            if (v.getTag() instanceof String &&
                    BLUR_REGION_TAG.equalsIgnoreCase((String) v.getTag())) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                blurRects.add(rect);
            }
        }

        if (blurRects.size() == 0) {
            blurRects.add(new Rect(getLeft(), getTop(), getRight(), getBottom()));
        }

        mBlurRects = new Rect[blurRects.size()];
        blurRects.toArray(mBlurRects);

        checkBlurRadii();
    }

    private void checkBlurRadii() {
        if (mBlurRadii == null)
            mBlurRadii = new float[mBlurRects.length];
        else if (mBlurRects != null && mBlurRadii.length < mBlurRects.length)
            expandRadii(mBlurRects.length);

        for (int i = 0; i < mBlurRadii.length; i++) {
            if (mBlurRadii[i] <= 0)
                mBlurRadii[i] = 10;
            else if (mBlurRadii[i] > 25)
                mBlurRadii[i] = 25;
        }
    }

    private void expandRadii(int size) {
        float[] oldRadii = mBlurRadii;
        mBlurRadii = new float[size];
        int fillFrom = 0;
        if (oldRadii != null) {
            System.arraycopy(oldRadii, 0, mBlurRadii, 0, oldRadii.length);
            fillFrom = oldRadii.length;
        }
        for (; fillFrom < mBlurRadii.length; fillFrom++) {
            mBlurRadii[fillFrom] = 16;
        }
    }
}