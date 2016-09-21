package com.example.webprog26.support.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.handlers.ThumbsDownloader;
import com.example.webprog26.support.interfaces.ItemTouchHelperViewHolder;
import com.example.webprog26.support.interfaces.OnItemClickListener;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Photo;
import com.example.webprog26.support.providers.FamilyClientProvider;
import com.example.webprog26.support.utils.PictureUtils;
import com.example.webprog26.support.utils.SimpleCameraUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class FamiliesListAdapter extends RecyclerView.Adapter<FamiliesListAdapter.FamiliesViewHolder> implements OnItemClickListener{

    private static final String TAG = "FamiliesListAdapter";

    @Override
    public void onItemClick(FamilyClient client) {

    }

    @Override
    public void onItemClick(PersonClient client) {}

    public class FamiliesViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        private static final String TAG = "FamiliesViewHolder";

        @Override
        public void onItemSelected() {
            itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.TRANSPARENT);
        }

        private TextView mFamilyClienSecondName;
        private TextView mFamilyClientFirstName;
        private TextView mFamilyClienPatronymic;
        private TextView mFamilyClientAddress;
        private TextView mFamilyClientCategory;
        private TextView mFamilyDateAdded;
        private de.hdodenhof.circleimageview.CircleImageView mFamilyClientPhoto;


        public FamiliesViewHolder(View itemView) {
            super(itemView);
            
            mFamilyClienSecondName = (TextView) itemView.findViewById(R.id.clientSecondName);
            mFamilyClientFirstName = (TextView) itemView.findViewById(R.id.clientFirstName);
            mFamilyClienPatronymic = (TextView) itemView.findViewById(R.id.clientPatronymic);
            mFamilyClientAddress = (TextView) itemView.findViewById(R.id.clientAddr);
            mFamilyClientCategory = (TextView) itemView.findViewById(R.id.clientCategory);
            mFamilyDateAdded = (TextView) itemView.findViewById(R.id.dateAdded);
            mFamilyClientPhoto = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.clientPhoto);

        }
        public void bindClientImage(Bitmap bitmap){
            mFamilyClientPhoto.setImageBitmap(bitmap);
        }
        public void bind(final FamilyClient familyClient, final OnItemClickListener listener, ThumbsDownloader thumbsDownloader){

            mFamilyClienSecondName.setText(familyClient.getClientSecondName());
            mFamilyClientFirstName.setText(familyClient.getClientFirstName());
            mFamilyClienPatronymic.setText(familyClient.getClientPatronymic());
            mFamilyClientAddress.setText(familyClient.getClientAddress());
            mFamilyClientCategory.setText(familyClient.getClientCategory());
            Log.i(TAG, new SimpleDateFormat("yyyy-MM-dd").format(familyClient.getClientRegDate()));
            mFamilyDateAdded.setText(new SimpleDateFormat("yyyy-MM-dd").format(familyClient.getClientRegDate()));
            thumbsDownloader.queueThumbnail(FamiliesViewHolder.this, familyClient.getPhoto().getFileName());
            if(familyClient.getPhoto().getFileName() == null) {
                mFamilyClientPhoto.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.default_client_avatar));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(familyClient);
                }
            });
        }


    }

    private Activity mActivity;
    private Context mContext;
    private List<FamilyClient> mFamilyClientList;
    private OnItemClickListener mListener;
    private FamilyClientProvider mFamilyClientProvider;
    private ThumbsDownloader mThumbsDownloader;

    public FamiliesListAdapter(Activity activity, List<FamilyClient> familyClientList, OnItemClickListener listener, ThumbsDownloader thumbsDownloader) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mFamilyClientList = familyClientList;
        mListener = listener;
        mFamilyClientProvider = new FamilyClientProvider(mActivity);
        mThumbsDownloader = thumbsDownloader;
    }

    @Override
    public FamiliesListAdapter.FamiliesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item, parent, false);
        FamiliesViewHolder fvh = new FamiliesViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(FamiliesListAdapter.FamiliesViewHolder holder, int position) {
        holder.bind(mFamilyClientList.get(position), mListener, mThumbsDownloader);
    }

    @Override
    public int getItemCount() {
        return mFamilyClientList.size();
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
                final FamilyClient client = mFamilyClientList.get(adapterPosition);

                Snackbar snackbar = Snackbar.make(recyclerView, R.string.dismiss_swipe, Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel_question, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFamilyClientList.add(adapterPosition, client);
                                notifyItemInserted(adapterPosition);
                                recyclerView.scrollToPosition(adapterPosition);
                            }
                        })
                        .setActionTextColor(Color.RED);
                snackbar.show();
                mFamilyClientProvider.deleteFamilyClient(client.getClientId());
                mFamilyClientList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                String prefMode = preferences.getString(Constants.SWIPE_CONTROLS, "");
                if(prefMode.equals("true")){
                    return true;
                }
                return false;
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
                Collections.swap(mFamilyClientList, i, i + 1);
            }
        } else {
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(mFamilyClientList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void updateList(ArrayList<FamilyClient> data){
        Collections.reverse(data);
        mFamilyClientList = data;
        notifyDataSetChanged();
    }


}
