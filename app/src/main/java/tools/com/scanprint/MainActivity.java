package tools.com.scanprint;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import tools.com.scanprint.entrty.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private List<View> views = new ArrayList<>();
    private ViewPager viewPager;
    private LinearLayout actionBarScanLayout, actionBarPrintLayout, actionBarDeleteLayout, actionBarClearLayout;
    //private ImageView actionBarScanImage, actionBarPrintImage, actionBarDeleteImage, actionBarClearImage, currentImage;
    private TextView actionBarScanIcon, actionBarPrintIcon, actionBarDeleteIcon, actionBarClearIcon, currentIcon;
    private TextView actionBarScanText, actionBarPrintText, actionBarDeleteText, actionBarClearText, currentText;

    private TableAdapter tableAdapter;
    private int tableDeletePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        initData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.e(TAG, "menu id: " + id);

        if (id == R.id.action_about) {
            //showPopWinShare(findViewById(R.id.toolbar));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        actionBarScanLayout = (LinearLayout) findViewById(R.id.actionBarScanLayout);
        actionBarPrintLayout = (LinearLayout) findViewById(R.id.actionBarPrintLayout);
        actionBarDeleteLayout = (LinearLayout) findViewById(R.id.actionBarDeleteLayout);
        actionBarClearLayout = (LinearLayout) findViewById(R.id.actionBarClearLayout);

        actionBarScanLayout.setOnClickListener(this);
        actionBarPrintLayout.setOnClickListener(this);
        actionBarDeleteLayout.setOnClickListener(this);
        actionBarClearLayout.setOnClickListener(this);

        /*actionBarScanImage = (ImageView) findViewById(R.id.actionBarScanImage);
        actionBarPrintImage = (ImageView) findViewById(R.id.actionBarPrintImage);
        actionBarDeleteImage = (ImageView) findViewById(R.id.actionBarDeleteImage);
        actionBarClearImage = (ImageView) findViewById(R.id.actionBarClearImage);*/
        actionBarScanIcon = (TextView) findViewById(R.id.actionBarScanIcon);
        actionBarPrintIcon = (TextView) findViewById(R.id.actionBarPrintIcon);
        actionBarDeleteIcon = (TextView) findViewById(R.id.actionBarDeleteIcon);
        actionBarClearIcon = (TextView) findViewById(R.id.actionBarClearIcon);

        actionBarScanText = (TextView) findViewById(R.id.actionBarScanText);
        actionBarPrintText = (TextView) findViewById(R.id.actionBarPrintText);
        actionBarDeleteText = (TextView) findViewById(R.id.actionBarDeleteText);
        actionBarClearText = (TextView) findViewById(R.id.actionBarClearText);

        actionBarScanIcon.setTypeface(font);
        actionBarPrintIcon.setTypeface(font);
        actionBarDeleteIcon.setTypeface(font);
        actionBarClearIcon.setTypeface(font);

        //actionBarScanImage.setSelected(true);
        actionBarScanIcon.setSelected(true);
        actionBarScanText.setSelected(true);
        //currentImage = actionBarScanImage;
        currentIcon = actionBarScanIcon;
        currentText = actionBarScanText;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "navigation bar change page position: " + position);
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "navigation bar change page position: " + position);
                changeTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });*/

    }

    private void initData() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view_context_main = mInflater.inflate(R.layout.context_main, null);
        /*View tab02 = mInflater.inflate(R.layout.tab02, null);
        View tab03 = mInflater.inflate(R.layout.tab03, null);
        View tab04 = mInflater.inflate(R.layout.tab04, null);*/
        views.add(view_context_main);
        /*views.add(tab02);
        views.add(tab03);
        views.add(tab04);*/

        MyPagerAdapter adapter = new MyPagerAdapter(views);
        viewPager.setAdapter(adapter);

        ViewGroup tableTitle = (ViewGroup) view_context_main.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));

        ListView tableListView = (ListView) view_context_main.findViewById(R.id.table_list);
        List<Product> productList = new ArrayList<>();
        tableAdapter = new TableAdapter(this, productList);
        tableListView.setAdapter(tableAdapter);
        //添加点击事件
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e(TAG, "item click position: " + position);
                tableDeletePosition = position;
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.e(TAG, "navigation bar button id: " + id);
        changeTab(id);
    }

    private void changeTab(int id) {
        //currentImage.setSelected(false);
        currentIcon.setSelected(false);
        currentText.setSelected(false);
        switch (id) {
            case R.id.actionBarScanLayout:
                viewPager.setCurrentItem(0);
                tableAddRow();
            case 0:
                /*actionBarScanImage.setSelected(true);
                currentImage = actionBarScanImage;*/
                actionBarScanIcon.setSelected(true);
                currentIcon = actionBarScanIcon;
                actionBarScanText.setSelected(true);
                currentText = actionBarScanText;
                break;
            case R.id.actionBarPrintLayout:
                viewPager.setCurrentItem(0);
            case 1:
                /*actionBarPrintImage.setSelected(true);
                currentImage = actionBarPrintImage;*/
                actionBarPrintIcon.setSelected(true);
                currentIcon = actionBarPrintIcon;
                actionBarPrintText.setSelected(true);
                currentText = actionBarPrintText;
                break;
            case R.id.actionBarDeleteLayout:
                viewPager.setCurrentItem(0);
                tableAdapter.deleteRow(tableDeletePosition);
            case 2:
                /*actionBarDeleteImage.setSelected(true);
                currentImage = actionBarDeleteImage;*/
                actionBarDeleteIcon.setSelected(true);
                currentIcon = actionBarDeleteIcon;
                actionBarDeleteText.setSelected(true);
                currentText = actionBarDeleteText;
                break;
            case R.id.actionBarClearLayout:
                viewPager.setCurrentItem(0);
                tableAdapter.clearRow();
            case 3:
                /*actionBarClearImage.setSelected(true);
                currentImage = actionBarClearImage;*/
                actionBarClearIcon.setSelected(true);
                currentIcon = actionBarClearIcon;
                actionBarClearText.setSelected(true);
                currentText = actionBarClearText;
                break;
            default:
                break;
        }
    }

    private void tableAddRow() {
        Product product = new Product();
        long id = System.currentTimeMillis();
        String idString = String.valueOf(id);
        product.setProductCode("code" + idString.substring(idString.length() - 4));
        product.setProductName("name");
        product.setSpecifications("xxx");
        product.setWidth("10");

        tableAdapter.addRow(product);
    }

}
