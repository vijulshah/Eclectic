package variable.com.eclectic.Tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import variable.com.eclectic.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class TaskArrayAdapter extends RecyclerView.Adapter<TaskArrayAdapter.MyViewHolder> {

    private final String folderName;
    private ArrayList<TaskModel> dataModelArrayList;
    private final ArrayList<String> taskIdArrayList;
    private final Context context;
    private View view;

    public TaskArrayAdapter(ArrayList<TaskModel> dataModelArrayList, ArrayList<String> taskIdArrayList, Context context, String folderName)
    {
        this.dataModelArrayList = dataModelArrayList;
        this.taskIdArrayList = taskIdArrayList;
        this.folderName =folderName;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.task_card,viewGroup,false);

        TaskArrayAdapter.MyViewHolder myViewHolder = new TaskArrayAdapter.MyViewHolder(view,context,dataModelArrayList);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskArrayAdapter.MyViewHolder viewHolder, final int position)
    {
        final String taskId = dataModelArrayList.get(position).getID();
        final String taskTitle = dataModelArrayList.get(position).getTaskTitle();
        final String taskNotes = dataModelArrayList.get(position).getTaskNotes();
        final String createDate = dataModelArrayList.get(position).getTaskCreateDate();
        final String createTime = dataModelArrayList.get(position).getTaskCreateTime();
        final String updateDate = dataModelArrayList.get(position).getTaskUpdateDate();
        final String updateTime = dataModelArrayList.get(position).getTaskUpdateTime();

        viewHolder.tv_taskCard_title.setText(taskTitle);
        viewHolder.tv_taskCard_notes.setText(taskNotes);
        viewHolder.tv_card_createdDate.setText("created : "+createDate);
        if(createDate.matches(updateDate))
            viewHolder.tv_card_updateDate.setText("updated : today");
        else
            viewHolder.tv_card_updateDate.setText("updated : "+updateDate);

        viewHolder.ib_taskCard_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(context,viewHolder.ib_taskCard_options);

                popupMenu.inflate(R.menu.item_task_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.item_delete:
                            {
                                DatabaseHandler db = new DatabaseHandler(context,folderName);
                                db.deleteRecord(taskId);
                                dataModelArrayList.remove(position);
                                notifyItemRemoved(position);
                            }
                            break;

                            case R.id.item_edit:
                            {
                                Intent intent = new Intent(context,OpenTaskToEdit.class);
                                intent.putExtra("SavedTaskId",taskId);
                                intent.putExtra("FOLDER_NAME",folderName);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            break;

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.item_task_show,null);
                WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                final PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                TextView tv_popupWind_taskTitle = popupView.findViewById(R.id.tv_popupWind_taskTitle);
                tv_popupWind_taskTitle.setText(taskTitle);

                TextView tv_popupWind_taskDesc = popupView.findViewById(R.id.tv_popupWind_taskDesc);
                tv_popupWind_taskDesc.setText(taskNotes);

                final ImageButton btn_popupWind_taskOptions = popupView.findViewById(R.id.btn_popupWind_taskOptions);
                btn_popupWind_taskOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        PopupMenu popupMenu = new PopupMenu(context,popupView.findViewById(R.id.btn_popupWind_taskOptions));

                        popupMenu.inflate(R.menu.item_task_menu);

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId())
                                {
                                    case R.id.item_delete:
                                    {
                                        DatabaseHandler db = new DatabaseHandler(context,folderName);
                                        db.deleteRecord(taskId);
                                        notifyDataSetChanged();
                                        popupWindow.dismiss();
                                    }
                                    break;

                                    case R.id.item_edit:
                                    {
                                        Intent intent = new Intent(context,OpenTaskToEdit.class);
                                        intent.putExtra("SavedTaskId",taskId);
                                        intent.putExtra("FOLDER_NAME",folderName);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        popupWindow.dismiss();
                                    }
                                    break;

                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });

                ImageButton ib_popupWind_taskBack = popupView.findViewById(R.id.ib_popupWind_taskBack);
                ib_popupWind_taskBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView tv_card_updateDate;
        private final TextView tv_card_createdDate;
        private ArrayList<TaskModel> dataModelArrayList;
        private TextView tv_taskCard_title;
        private ImageButton ib_taskCard_options;
        private TextView tv_taskCard_notes;

        public MyViewHolder(View itemView, Context context, ArrayList<TaskModel> dataModelArrayList)
        {
            super(itemView);

            this.dataModelArrayList = dataModelArrayList;
            tv_taskCard_title = (TextView)itemView.findViewById(R.id.tv_taskCard_title);
            ib_taskCard_options = (ImageButton)itemView.findViewById(R.id.ib_taskCard_options);
            tv_taskCard_notes = (TextView)itemView.findViewById(R.id.tv_taskCard_notes);
            tv_card_createdDate = (TextView)itemView.findViewById(R.id.tv_card_createdDate);
            tv_card_updateDate = (TextView)itemView.findViewById(R.id.tv_card_updateDate);
        }
    }
}
