package tools.com.scanprint;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.posapi.PosApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import tools.com.scanprint.entrty.Product;
import tools.com.scanprint.miniprinter.App;
import tools.com.scanprint.utils.PrintTextUtils;
import tools.com.scanprint.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
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
    private int tableDeletePosition = -1;

    private EditText editText;

    private PosApi mApi = null;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    private static boolean isChinese = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        initData();
        initPrinter();
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

        editText = (EditText) findViewById(R.id.text_input_info);

        //showSoftInputFromWindow(this, editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = editText.getText().toString();
                if (inputText.contains("\n") || inputText.contains("\r\n")) {
                    Log.e(TAG, "editText length: " + count);
                    String formatInputText = inputText.replace("\n", "");
                    tableAddRowCall(formatInputText);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Log.i(TAG, "输入文本之前的状态");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.i(TAG, "输入文字后的状态");
            }
        });

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
        tableTitle.setBackgroundColor(Color.rgb(240, 255, 255));

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

    private void initPrinter() {
        //1  单片机上电
        try {
            FileWriter localFileWriterOn = new FileWriter(new File("/proc/gpiocontrol/set_sam"));
            localFileWriterOn.write("1");
            localFileWriterOn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2 接口初始化
        mApi = App.getInstance().getPosApi();
        //设置初始化回调
        mApi.setOnComEventListener(mCommEventListener);
        //使用扩展方式初始化接口
        mApi.initDeviceEx("/dev/ttyMT2");
        // 初始化打印类
        PrintTextUtils.init(this);
    }

    PosApi.OnCommEventListener mCommEventListener = new PosApi.OnCommEventListener() {

        @Override
        public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
            switch(cmdFlag){
                case PosApi.POS_INIT:
                    if(state==PosApi.COMM_STATUS_SUCCESS){
                        Toast.makeText(getApplicationContext(), getString(R.string.pos_init_success), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), getString(R.string.pos_init_fail), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };

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
                tablePrintRow();
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
                tableDeleteRow();
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
                tableClearRow();
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
        /*new IntentIntegrator(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setCaptureActivity(ScanActivity.class)
                .setPrompt("请对准二维码")// 设置提示语
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                .initiateScan();// 初始化扫码
        */
        Log.e(TAG, "print language isChinese: " + isChinese);

        if (isChinese) {
            isChinese = false;
            actionBarScanText.setText(getString(R.string.action_bar_scan_name_en));
        } else {
            isChinese = true;
            actionBarScanText.setText(getString(R.string.action_bar_scan_name));
        }

    }

    private void tableAddRowCall(String result) {
        // 按@符号分割
        String[] item = result.split("@");
        Log.e(TAG, "addRowCall item length: " + item.length);
        if (item.length == 4) {
            Product product = new Product();
            product.setProductCode(item[0]);
            product.setProductName(item[1]);
            product.setSpecifications(item[2]);
            product.setWidth(item[3]);

            // 排重
            boolean flag = false;
            List<Product> list = tableAdapter.getList();
            for (Product cacheProduct : list) {
                if (product.getProductCode().equals(cacheProduct.getProductCode())) {
                    if (product.getProductName().equals(cacheProduct.getProductName()) &&
                            product.getSpecifications().equals(cacheProduct.getSpecifications()) &&
                            product.getWidth().equals(cacheProduct.getWidth())) {
                        flag = true;
                    }
                }
            }
            if (!flag) {
                tableAdapter.setSelectItem(tableDeletePosition);
                tableAdapter.addRow(product);
                // 添加完了，重新设置，防止删除
                tableDeletePosition = -1;
            } else {
                String message = getString(R.string.table_scan_already);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            // 清空输入框
            editText.setText("");
            // 设置焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }
    }

    private void tablePrintRow() {
        List<Product> list = tableAdapter.getList();
        Log.e(TAG, "printRow list size: " + list.size());
        if (list.size() > 0) {
            String content = StringUtils.getFormatPrintText(this, list, isChinese);
            PrintTextUtils.printTextWithInput(60, content);
        } else {
            String message = getString(R.string.table_print_row);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void tableDeleteRow() {
        // 大于-1时才删除，防止连续点删除按钮
        if (tableDeletePosition > -1) {
            tableAdapter.deleteRow(tableDeletePosition);
            tableDeletePosition = -1;
        } else {
            String message = getString(R.string.table_delete_row);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void tableClearRow() {
        // 创建退出对话框
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isExit.setTitle(getString(R.string.clear_alert_title));
        // 设置对话框消息
        isExit.setMessage(getString(R.string.clear_alert_message));
        // 添加选择按钮并注册监听
        isExit.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.clear_alert_confirm), listenerClear);
        isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.clear_alert_cancel), listenerClear);
        // 显示对话框
        isExit.show();
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listenerClear = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    tableDeletePosition = -1;
                    tableAdapter.clearRow();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String resultContents = result.getContents();
            if(resultContents == null) {
                Toast.makeText(this, getString(R.string.scan_cancel), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.scan_success) + resultContents, Toast.LENGTH_LONG).show();

                tableAddRowCall(resultContents);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁接口
        if(mApi!=null){
            mApi.closeDev();
        }
        try {
            FileWriter localFileWriterOn = new FileWriter(new File("/proc/gpiocontrol/set_sam"));
            localFileWriterOn.write("0");
            localFileWriterOn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        /*editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), getString(R.string.exit_message),
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            //finish();
            //System.exit(0);
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle(getString(R.string.exit_alert_title));
            // 设置对话框消息
            isExit.setMessage(getString(R.string.exit_alert_message));
            // 添加选择按钮并注册监听
            isExit.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.exit_alert_confirm), listener);
            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.exit_alert_cancel), listener);
            // 显示对话框
            isExit.show();
        }
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

}
