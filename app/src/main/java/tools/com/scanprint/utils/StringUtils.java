package tools.com.scanprint.utils;

import android.content.Context;
import tools.com.scanprint.R;
import tools.com.scanprint.entrty.Product;

import java.util.List;

public class StringUtils {

    private static int splitLength = 4;
    private static String[] splitSeparate = {"@", ";"};

    public static String getFormatPrintText(Context context, List<Product> list, boolean isChinese) {
        StringBuilder sb = new StringBuilder();
        int index = 1;

        String printFormatTitle;
        String printFormatBodyStart;
        String printFormatBodyEnd;
        String printFormatFooterStart;
        String printFormatFooterEnd;
        if (isChinese) {
            printFormatTitle = context.getString(R.string.print_format_title);
            printFormatBodyStart = context.getString(R.string.print_format_body_start);
            printFormatBodyEnd = context.getString(R.string.print_format_body_end);
            printFormatFooterStart = context.getString(R.string.print_format_footer_start);
            printFormatFooterEnd = context.getString(R.string.print_format_footer_end);
        } else {
            printFormatTitle = context.getString(R.string.print_format_title_en);
            printFormatBodyStart = context.getString(R.string.print_format_body_start_en);
            printFormatBodyEnd = context.getString(R.string.print_format_body_end_en);
            printFormatFooterStart = context.getString(R.string.print_format_footer_start_en);
            printFormatFooterEnd = context.getString(R.string.print_format_footer_end_en);
        }


        sb.append("\n");
        sb.append(printFormatTitle);
        sb.append("\n");

        for (Product product : list) {

            sb.append(printFormatBodyStart);
            sb.append(index);
            sb.append(printFormatBodyEnd);
            sb.append("\n");

            sb.append(product.getProductCode());
            sb.append("     ");
            sb.append(product.getWidth());
            if (!product.getWidth().endsWith("\n")) {
                sb.append("\n");
            }

            // 可能为空
            if (product.getProductName() != null && !"".equals(product.getProductName())) {
                sb.append("(");
                sb.append(product.getProductName());
                sb.append(")");
                //sb.append("\n");
            }

            sb.append(product.getSpecifications());
            sb.append("\n");

            index ++;
        }

        sb.append(printFormatFooterStart);
        sb.append(list.size());
        sb.append(printFormatFooterEnd);
        sb.append("\n\n\n\n");
        return sb.toString();
    }

    // 按符号分割，可能是@符或;号
    public static String[] getStringSplit(String inputString) {
        String[] resultString = new String[]{};
        if (inputString != null && !"".equals(inputString)) {
            for (String separate : splitSeparate) {
                resultString = inputString.split(separate);
                if (resultString != null && resultString.length == splitLength) {
                    break;
                } else {
                    resultString = new String[]{};
                }
            }
        }
        return resultString;
    }
}
