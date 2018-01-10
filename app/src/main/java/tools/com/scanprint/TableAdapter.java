package tools.com.scanprint;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import tools.com.scanprint.entrty.Product;

public class TableAdapter extends BaseAdapter {

    private static final String TAG = "TableAdapter";

    private List<Product> list;
    private LayoutInflater inflater;
    private int selectItem = -1;

    public TableAdapter(Context context, List<Product> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "selectItem: " + selectItem + " position: " + position + " convertView: " + convertView);

        Product product = (Product) this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.tableTitleId = (TextView) convertView.findViewById(R.id.table_title_id);
            viewHolder.tableTitleProductCode = (TextView) convertView.findViewById(R.id.table_title_product_code);
            viewHolder.tableTitleProductName = (TextView) convertView.findViewById(R.id.table_title_product_name);
            viewHolder.tableTitleSpecifications = (TextView) convertView.findViewById(R.id.table_title_specifications);
            viewHolder.tableTitleWidth = (TextView) convertView.findViewById(R.id.table_title_width);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tableTitleId.setText(String.valueOf(product.getId()));
        viewHolder.tableTitleId.setTextSize(13);
        viewHolder.tableTitleProductCode.setText(product.getProductCode());
        viewHolder.tableTitleProductCode.setTextSize(13);
        viewHolder.tableTitleProductName.setText(product.getProductName());
        viewHolder.tableTitleProductName.setTextSize(13);
        viewHolder.tableTitleSpecifications.setText(product.getSpecifications());
        viewHolder.tableTitleSpecifications.setTextSize(13);
        viewHolder.tableTitleWidth.setText(product.getWidth());
        viewHolder.tableTitleWidth.setTextSize(13);

        /*if (selectItem == position) {
            System.out.println("--------------" + parent);
            Drawable drawable = inflater.getContext().getResources().getDrawable(R.drawable.list_view_bg);
            ((ListView) parent).setSelector(drawable);
            ((ListView) parent).setSelection(selectItem);
            ((LinearLayout) convertView).setSelected(true);
            ((LinearLayout) convertView).setPressed(true);
            //((ListView) parent).setSelected(true);
            //convertView.setSelected(true);
            //viewHolder.tableTitleId.setBackgroundResource(R.drawable.list_view_bg);
            *//*viewHolder.tableTitleProductCode.setActivated(true);
            viewHolder.tableTitleProductName.setFocusable(true);
            viewHolder.tableTitleSpecifications.setSelected(true);
            viewHolder.tableTitleWidth.setSelected(true);*//*
            //viewHolder.portalLinearLayout.setBackgroundColor(Color.GRAY);
            //convertView.setBackgroundResource(R.drawable.list_view_bg);
        }*/

        return convertView;
    }

    public void addRow(Product product) {
        // 添加时，先设置序号
        product.setId(list.size() + 1);
        list.add(product);

        notifyDataSetChanged();
    }

    public void deleteRow(int position) {
        list.remove(position);
        // 删除后，重新调整序号
        for (int i = 0; i < list.size(); i ++) {
            Product product = list.get(i);
            product.setId(i + 1);
        }
        notifyDataSetChanged();
    }

    public void clearRow() {
        list.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView tableTitleId;
        public TextView tableTitleProductCode;
        public TextView tableTitleProductName;
        public TextView tableTitleSpecifications;
        public TextView tableTitleWidth;
    }

}
