package mm.pndaza.vatthuguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;



public class FavDataAdapter extends RecyclerView.Adapter<FavDataAdapter.ViewHolder> {

    public Context context;
    public ArrayList<FavData> item_list;


    public FavDataAdapter(Context context,ArrayList<FavData> arrayList) {

        this.context = context;
        item_list = arrayList;
        MDetect.init(context);
    }

    @Override
    public FavDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavDataAdapter.ViewHolder holder, int position) {

        final int pos = position;
        String str_item = item_list.get(position).getItem();
        if (MDetect.isUnicode())
            str_item = Rabbit.zg2uni(str_item);
        holder.item_name.setText(str_item);
        holder.item_name.setTag(item_list.get(position));

        holder.chkSelected.setChecked(item_list.get(position).isSelected());
        holder.chkSelected.setTag(item_list.get(position));


        if (FavData.isCheckboxShow == true ) {
            holder.chkSelected.setVisibility(View.VISIBLE);
        } else {
            holder.chkSelected.setVisibility(View.INVISIBLE);
        }


        holder.btn_delete.setTag(item_list.get(position));


        holder.item_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                TextView textView = (TextView) v;
                FavData item = (FavData) textView.getTag();

                showDetail(item.getId());

            }
        });


        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                FavData item = (FavData) cb.getTag();

                item.setSelected(cb.isChecked());
                item_list.get(pos).setSelected(cb.isChecked());

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ImageButton button = (ImageButton) v;
                FavData item = (FavData) button.getTag();
                int _id = item.getId();
                deleteItemFromList(v, pos, _id);

            }
        });

    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }


    // confirmation dialog box to delete an unit
    private void deleteItemFromList(View v, final int position, final int _id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        //builder.setTitle("Dlete ");
        builder.setMessage("Delete Item ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                removeFromDB(context, _id);
                                item_list.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.show();

    }

    public void removeFromDB(Context context,final int _id){

        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM fav WHERE sid = ?", new Object[] {_id});
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_name;
        public ImageButton btn_delete;
        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            item_name = itemLayoutView.findViewById(R.id.txt_Name);
            btn_delete = itemLayoutView.findViewById(R.id.btn_delete_unit);
            chkSelected = itemLayoutView.findViewById(R.id.chk_selected);

        }
    }

    private void showDetail(int _id) {

        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from vatthuguide where _id = " + _id , null);

        cursor.moveToFirst();

        int id = cursor.getInt(0);
        String item = cursor.getString(1);
        String content = cursor.getString(3);
        String bref = cursor.getString(4);
        cursor.close();


        Intent result = new Intent(context.getApplicationContext(),Detail.class);
        result.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        result.putExtra("id",id);
        if (MDetect.isUnicode()) {
            result.putExtra("item", Rabbit.zg2uni(item));
            result.putExtra("content", Rabbit.zg2uni(content));
            result.putExtra("bref", Rabbit.zg2uni(bref));
        } else {
            result.putExtra("item", item);
            result.putExtra("content", content);
            result.putExtra("bref", bref);
        }
        context.startActivity(result);


    }

}