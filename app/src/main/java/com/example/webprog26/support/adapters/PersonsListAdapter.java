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

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.handlers.ThumbsDownloader;
import com.example.webprog26.support.interfaces.ItemTouchHelperViewHolder;
import com.example.webprog26.support.interfaces.OnItemClickListener;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Photo;
import com.example.webprog26.support.providers.PersonClientProvider;
import com.example.webprog26.support.utils.PictureUtils;
import com.example.webprog26.support.utils.SimpleCameraUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class PersonsListAdapter extends RecyclerView.Adapter<PersonsListAdapter.PersonsViewHolder> implements OnItemClickListener {

    @Override
    public void onItemClick(PersonClient client) {}

    @Override
    public void onItemClick(FamilyClient client) {}

    public class PersonsViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

        private static final String TAG = "PersonsViewHolder";

        @Override
        public void onItemSelected() {
            itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.findViewById(R.id.wrapper).setBackgroundColor(Color.TRANSPARENT);
        }

        private TextView mPersonClienSecondName;
        private TextView mPersonClientFirstName;
        private TextView mPersonClienPatronymic;
        private TextView mPersonClientAddress;
        private TextView mPersonClientCategory;
        private TextView mPersonDateAdded;
        private de.hdodenhof.circleimageview.CircleImageView mPersonClientPhoto;


        public PersonsViewHolder(View itemView) {
            super(itemView);

            mPersonClienSecondName = (TextView) itemView.findViewById(R.id.clientSecondName);
            mPersonClientFirstName = (TextView) itemView.findViewById(R.id.clientFirstName);
            mPersonClienPatronymic = (TextView) itemView.findViewById(R.id.clientPatronymic);
            mPersonClientAddress = (TextView) itemView.findViewById(R.id.clientAddr);
            mPersonClientCategory = (TextView) itemView.findViewById(R.id.clientCategory);
            mPersonDateAdded = (TextView) itemView.findViewById(R.id.dateAdded);
            mPersonClientPhoto = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.clientPhoto);
        }

        public void bindClientImage(Bitmap bitmap){
            mPersonClientPhoto.setImageBitmap(bitmap);
        }

        public void bind(final PersonClient personClient, final OnItemClickListener listener, ThumbsDownloader thumbsDownloader){
            mPersonClienSecondName.setText(personClient.getClientSecondName());
            mPersonClientFirstName.setText(personClient.getClientFirstName());
            mPersonClienPatronymic.setText(personClient.getClientPatronymic());
            mPersonClientAddress.setText(personClient.getClientAddress());
            mPersonClientCategory.setText(personClient.getClientCategory());
            mPersonDateAdded.setText(new SimpleDateFormat("yyyy-MM-dd").format(personClient.getClientRegDate()));
            thumbsDownloader.queueThumbnail(PersonsViewHolder.this, personClient.getPhoto().getFileName());
            if(personClient.getPhoto().getFileName() == null){
                mPersonClientPhoto.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.default_client_avatar));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(personClient);
                }
            });
        }
    }

    private Activity mActivity;
    private Context mContext;
    private List<PersonClient> mPersonClientList;
    private OnItemClickListener mListener;
    private PersonClientProvider mPersonClientProvider;
    private ThumbsDownloader mThumbsDownloader;

    public PersonsListAdapter(Activity activity, List<PersonClient> personClientList, OnItemClickListener listener, ThumbsDownloader thumbsDownloader) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mPersonClientList = personClientList;
        mListener = listener;
        mPersonClientProvider = new PersonClientProvider(mActivity);
        mThumbsDownloader = thumbsDownloader;
    }

    @Override
    public PersonsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item, parent, false);
        PersonsViewHolder pvh = new PersonsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonsViewHolder holder, int position) {
        holder.bind(mPersonClientList.get(position), mListener, mThumbsDownloader);
    }

    @Override
    public int getItemCount() {
        return mPersonClientList.size();
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
                final PersonClient client = mPersonClientList.get(adapterPosition);

                Snackbar snackbar = Snackbar.make(recyclerView, R.string.dismiss_swipe, Snackbar.LENGTH_LONG)
                        .setAction(R.string.cancel_question, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPersonClientList.add(adapterPosition, client);
                                notifyItemInserted(adapterPosition);
                                recyclerView.scrollToPosition(adapterPosition);
                            }
                        })
                        .setActionTextColor(Color.RED);
                snackbar.show();
                mPersonClientProvider.deletePersonClientId(client.getClientId());
                mPersonClientList.remove(adapterPosition);
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
                Collections.swap(mPersonClientList, i, i + 1);
            }
        } else {
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(mPersonClientList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void updateList(ArrayList<PersonClient> data){
        Collections.reverse(data);
        mPersonClientList = data;
        notifyDataSetChanged();
    }
}
