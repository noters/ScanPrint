package tools.com.scanprint.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.posapi.PosApi;
import android.text.TextUtils;
import android.widget.Toast;
import android.zyapi.PrintQueue;
import tools.com.scanprint.R;
import tools.com.scanprint.miniprinter.App;

import java.io.UnsupportedEncodingException;

public class PrintTextUtils {

    private static PosApi mPosApi = null;
    private static PrintQueue mPrintQueue = null;

    private static Context context;

    public static void init(Context context) {
        PrintTextUtils.context = context;
        mPosApi = App.getInstance().getPosApi();
        mPrintQueue = new PrintQueue(context, mPosApi);
        mPrintQueue.init();
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {

            @Override
            public void onGetState(int state) {
                switch(state){
                    case 0:

                        //有纸
                        Toast.makeText(PrintTextUtils.context, PrintTextUtils.context.getString(R.string.state_is_paper), Toast.LENGTH_SHORT).show();
                        break;

                    case 1:

                        //缺纸
                        Toast.makeText(PrintTextUtils.context, PrintTextUtils.context.getString(R.string.state_no_paper), Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onPrinterSetting(int state) {
                switch(state){
                    case 0:
                        Toast.makeText(PrintTextUtils.context, PrintTextUtils.context.getString(R.string.setting_is_paper), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(PrintTextUtils.context, PrintTextUtils.context.getString(R.string.setting_no_paper), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(PrintTextUtils.context, PrintTextUtils.context.getString(R.string.setting_check), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFinish() {
                //mPosApi.gpioControl((byte)0x23,2,0);
                Toast.makeText(PrintTextUtils.context, PrintTextUtils.context.getString(R.string.print_complete), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int state) {
                //mPosApi.gpioControl((byte)0x23,2,0);
                switch(state){

                    case PosApi.ERR_POS_PRINT_NO_PAPER:
                        //打印缺纸
                        showTip(PrintTextUtils.context.getString(R.string.print_no_paper));
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED:
                        //打印失败
                        showTip(PrintTextUtils.context.getString(R.string.print_failed));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW:
                        //电压过低
                        showTip(PrintTextUtils.context.getString(R.string.print_voltage_low));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH:
                        //电压过高
                        showTip(PrintTextUtils.context.getString(R.string.print_voltage_high));
                        break;
                }
                //Toast.makeText(PrintTextUtils.context, "打印失败  错误码:"+state, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void showTip(String msg){
        new AlertDialog.Builder(context).setTitle(context.getString(R.string.tips))
                .setMessage(msg)
                .setNegativeButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    /*
	 * 打印文字   size 1 --倍大小    2--2倍大小
	 */
    private static void addPrintTextWithSize(int size ,int concentration,  byte[] data){
        if(data == null) return ;
        //2倍字体大小
        byte[] _2x = new byte[]{0x1b,0x57,0x02};
        //1倍字体大小
        byte[] _1x = new byte[]{0x1b,0x57,0x01};
        byte[] mData = null;
        if(size == 1){
            mData = new byte[3+data.length];
            //1倍字体大小  默认
            System.arraycopy(_1x, 0, mData, 0, _1x.length);
            System.arraycopy(data, 0, mData, _1x.length, data.length);
            mPrintQueue.addText(concentration, mData);
        }else if(size == 2){
            mData = new byte[3+data.length];
            //1倍字体大小  默认
            System.arraycopy(_2x, 0, mData, 0, _2x.length);
            System.arraycopy(data, 0, mData, _2x.length, data.length);
            mPrintQueue.addText(concentration, mData);
        }

    }

    public static void printTextWithInput(int concentration, String content){
        try {
            //int  concentration = Integer.valueOf(etConcentration.getText().toString().trim());
            if(TextUtils.isEmpty(content)){
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(content);
            sb.append("\n");
            byte[] text = sb.toString().getBytes(context.getString(R.string.charset));

            addPrintTextWithSize(1, concentration, text);

            mPrintQueue.printStart();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
