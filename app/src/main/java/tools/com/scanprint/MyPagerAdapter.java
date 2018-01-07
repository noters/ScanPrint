package tools.com.scanprint;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private static final String TAG = "MyPagerAdapter";

    private List<View> views;

    public MyPagerAdapter (List<View> views) {
        this.views = views;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem");
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount");
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem");
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        Log.d(TAG, "isViewFromObject");
        return arg0 == (arg1);
    }
}
