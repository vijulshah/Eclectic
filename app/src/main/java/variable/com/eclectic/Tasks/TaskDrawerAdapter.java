package variable.com.eclectic.Tasks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import variable.com.eclectic.R;

class TaskDrawerAdapter extends RecyclerView.Adapter<TaskDrawerAdapter.MyViewHolder>
{
    private final Context context;
    private final ArrayList<TaskModel> stringArrayList;
    private View view;

    public TaskDrawerAdapter(Context context, ArrayList<TaskModel> stringArrayList)
    {
        this.context = context;
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public TaskDrawerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.task_drawer_folders,viewGroup,false);

        TaskDrawerAdapter.MyViewHolder myViewHolder = new TaskDrawerAdapter.MyViewHolder(view,context,stringArrayList);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int position)
    {
        final String folderName = stringArrayList.get(position).getFolderTitle();
        viewHolder.tv_newFolderName.setText(folderName);
        viewHolder.ib_taskFolders_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,viewHolder.ib_taskFolders_options);

                popupMenu.inflate(R.menu.item_task_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.item_delete:
                            {
                                DatabaseHandler db = new DatabaseHandler(context,folderName);
                                db.deleteFolderNameRecord(folderName);
                                notifyDataSetChanged();
                            }
                            break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        viewHolder.rl_newFolder_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, TaskFolder.class);
                intent.putExtra("FOLDER_NAME",folderName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_newFolderName;
        private final RelativeLayout rl_newFolder_drawer;
        private final ArrayList<TaskModel> stringArrayList;
        private final ImageButton ib_taskFolders_options;

        public MyViewHolder(@NonNull View itemView, Context context, ArrayList<TaskModel> stringArrayList)
        {
            super(itemView);
            this.stringArrayList = stringArrayList;
            tv_newFolderName = (TextView)itemView.findViewById(R.id.tv_newFolderName);
            rl_newFolder_drawer = (RelativeLayout)itemView.findViewById(R.id.rl_newFolder_drawer);
            ib_taskFolders_options = (ImageButton)itemView.findViewById(R.id.ib_taskFolders_options);
        }
    }
}
