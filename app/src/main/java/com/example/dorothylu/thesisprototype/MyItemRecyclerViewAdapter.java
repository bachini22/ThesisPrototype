package com.example.dorothylu.thesisprototype;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.FeedItem;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private StorageReference storageRef;
    private final ArrayList<FeedItem> mValues;
    private final Feed.OnListFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;
    private static final String TAG = "Feed";
    private Context context;

    LinearLayoutManager layout;

    public MyItemRecyclerViewAdapter(ArrayList<FeedItem> items, Feed.OnListFragmentInteractionListener listener, Context con, LinearLayoutManager layout) {
        context = con;
        mValues = items;
        mListener = listener;
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://thesis-cb2dc.appspot.com");
        this.layout = layout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_item, parent, false);

        return new ViewHolder(view);
    }

    public void instanciateFirebase(){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("posts").addChildEventListener(childEventListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getFName() + " " + mValues.get(position).getLName());
        holder.mTimestamp.setText(mValues.get(position).getTimeStamp());
        holder.mMsg.setText(mValues.get(position).getMessage());
        if(mValues.get(position).getImge() != null)
            Picasso.with(context)
                    .load(mValues.get(position).getImge())
                    .placeholder( R.drawable.progress_animation)
                    .into(holder.mImage);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        if(mValues == null)
            return 0;
        else
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mTimestamp;
        public final TextView mMsg;
        public final ImageView mImage;
        public FeedItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.name);
            mTimestamp = (TextView) view.findViewById(R.id.timestamp);
            mMsg = (TextView) view.findViewById(R.id.txtStatusMsg);
            mImage = (ImageView) view.findViewById(R.id.feedImage1);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMsg.getText() + "'";
        }
    }

    public void loadPicture(final String userId, String postId, final FeedItem item) throws IOException {
        storageRef.child(userId).child(postId).child("1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri
                Uri url = uri;
                item.setImge(url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        getFullName(userId, item);

    }

    public void getFullName(String id, final FeedItem temp){
        mDatabase.child("Users").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                        temp.setFName(newPost.get("fName").toString());
                        temp.setLName(newPost.get("lName").toString());
                        System.out.println(newPost.get("fName").toString());
                        mValues.add(0, temp);
                        notifyItemInserted(0);
                        layout.scrollToPosition(0);

                        System.out.println("mValues size is " + mValues.size());
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }



    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            FeedItem temp = new FeedItem();

            Map<String, Object> newPost2 = (Map<String, Object>) dataSnapshot.getValue();
            Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
            temp.setId(dataSnapshot.getKey());
            temp.setMessage(newPost2.get("text").toString());
            temp.setTimeStamp(newPost2.get("timestamp").toString());
            temp.setCategory((newPost2.get("category").toString()));
            try {
                System.out.println("flag is " + newPost2.get("hasImg").toString());
                if(Integer.parseInt(newPost2.get("hasImg").toString()) == 1) {
                    System.out.println("loading image");
                    loadPicture(newPost2.get("userID").toString(), temp.getId(), temp);

                }
                else getFullName(newPost2.get("userID").toString(), temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // A new comment has been added, add it to the displayed list
            System.out.println("here");
            System.out.println(newPost2.get("text").toString());

            // ...
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
            Comment newComment = dataSnapshot.getValue(Comment.class);
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
            Comment movedComment = dataSnapshot.getValue(Comment.class);
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            //Toast.makeText(MyItemRecyclerViewAdapter.this, "Failed to load comments.",
              //      Toast.LENGTH_SHORT).show();
        }
    };
}
