package com.example.webprog26.support.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.webprog26.support.R;
import com.example.webprog26.support.interfaces.ItemTouchHelperViewHolder;
import com.example.webprog26.support.interfaces.OnItemClickListener;
import com.example.webprog26.support.interfaces.OnReportClickListener;
import com.example.webprog26.support.models.FamilyClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by webprog26 on 11.04.2016.
 */
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {

    private static final String TAG = "ReportsAdapter";

    public class ReportsViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        private static final String TAG = "ReportsViewHolder";

        @Override
        public void onItemSelected() {
            itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.TRANSPARENT);
        }

        private AppCompatTextView mReportTitle;
        private de.hdodenhof.circleimageview.CircleImageView mAddToListIcon;
        private boolean isSelected = false;

        public ReportsViewHolder(View itemView) {
            super(itemView);

            mReportTitle = (AppCompatTextView) itemView.findViewById(R.id.reportTitle);
            mAddToListIcon = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.addToListIcon);
        }

        public void bind(final File file, final OnReportClickListener listener){
            mReportTitle.setText(file.getName().substring(0, file.getName().lastIndexOf('.')));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReportItemClick(file);
                    if(isSelected == false){
                        mAddToListIcon.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_remove_black_24dp));
                        itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.LTGRAY);
                        isSelected = true;
                    } else {
                        mAddToListIcon.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_add_black_24dp));
                        itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.TRANSPARENT);
                        isSelected = false;
                    }
                }
            });
        }


    }

    private Activity mActivity;
    private List<File> mFilesTitles;
    private OnReportClickListener mListener;

    public ReportsAdapter(Activity mActivity, List<File> filesTitles, OnReportClickListener listener) {
        this.mActivity = mActivity;
        this.mFilesTitles = filesTitles;
        this.mListener = listener;
        updateList((ArrayList<File>)filesTitles);
    }

    @Override
    public ReportsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        ReportsViewHolder rvh = new ReportsViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(ReportsViewHolder holder, int position) {
        holder.bind(mFilesTitles.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mFilesTitles.size();
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if(viewHolder.getItemViewType() != target.getItemViewType()){
                    return false;
                }
                onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                final File reportFile = new File(mFilesTitles.get(adapterPosition).getPath());

                Snackbar snackbar = Snackbar.make(recyclerView, R.string.dismiss_swipe, Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel_question, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFilesTitles.add(adapterPosition, reportFile);
                                notifyItemInserted(adapterPosition);
                                recyclerView.scrollToPosition(adapterPosition);
                            }
                        })
                        .setActionTextColor(Color.RED);
                snackbar.show();
                boolean deleted = reportFile.delete();
                if(deleted){
                    mFilesTitles.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                    if(viewHolder instanceof ItemTouchHelperViewHolder){
                        ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                        itemTouchHelperViewHolder.onItemSelected();

                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(1.0F);
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                    itemTouchHelperViewHolder.onItemClear();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){
                Collections.swap(mFilesTitles, i, i + 1);
            }
        } else {
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(mFilesTitles, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void updateList(ArrayList<File> data){
        Collections.reverse(data);
        mFilesTitles = data;
        notifyDataSetChanged();
    }
}
