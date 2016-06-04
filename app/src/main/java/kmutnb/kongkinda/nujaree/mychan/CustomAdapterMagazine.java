package kmutnb.kongkinda.nujaree.mychan;

/**
 * Created by vutchai14 on 7/1/2559.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapterMagazine extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Post> mPosts;
    private ViewHolder mViewHolder;

    private Bitmap mBitmap;
    private Post mPost;
    private Activity mActivity;

    public CustomAdapterMagazine(Activity activity, List<Post> posts) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mPosts = posts;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_magazine, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.post_thumbnail);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mPost = mPosts.get(position);

        if (mPost.get_thumbnail() != null) {

            Glide.with(mActivity)
                    .load(mPost.get_thumbnail())
                            //  .placeholder(R.mipmap.ic_wrench_grey600_36dp)
                            //  .error(R.mipmap.ic_wrench_grey600_36dp)
                            //.transform(new CircleTransform(mActivity))
                    .into(mViewHolder.thumbnail);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView thumbnail;
    }

}
