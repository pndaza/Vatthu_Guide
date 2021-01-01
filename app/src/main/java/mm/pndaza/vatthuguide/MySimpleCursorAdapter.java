package mm.pndaza.vatthuguide;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Filterable;
import android.widget.TextView;

public class MySimpleCursorAdapter extends SimpleCursorAdapter  implements Filterable{

    Context context;

    public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        super.bindView(view, context, cursor);

        int position = cursor.getPosition();

        TextView tv = view.findViewById(R.id.tv_item);
        tv.setTextSize(17);
        if (MDetect.isUnicode() == true) {
            tv.setText(cursor.getString(cursor.getColumnIndex("item_uni")));
        } else {
            tv.setText(cursor.getString(cursor.getColumnIndex("item")));
        }
        tv.setTextColor(Color.rgb(51,25,0));

        if (position % 2 == 0) {
            tv.setBackgroundColor(Color.argb(40, 250, 170, 90));
        } else {
            tv.setBackgroundColor(Color.argb(40, 250, 224, 178));
        }

    }

    @Override
    public android.widget.Filter getFilter() {
        // TODO Auto-generated method stub
        return new android.widget.Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // TODO Auto-generated method stub
                final FilterResults fr = new FilterResults();
                Cursor c = null;
                if (constraint != null) {
                    c = getFilterQueryProvider().runQuery(constraint);
                }
                fr.values = c;
                return fr;
            }

            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                // TODO Auto-generated method stub

            }

        };
    }


}