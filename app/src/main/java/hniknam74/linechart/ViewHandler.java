package hniknam74.linechart;

import android.content.res.Resources;
import android.util.TypedValue;

public class ViewHandler
{

    public static int dpToPixel(float dipValue)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, Resources.getSystem().getDisplayMetrics());
    }

}
