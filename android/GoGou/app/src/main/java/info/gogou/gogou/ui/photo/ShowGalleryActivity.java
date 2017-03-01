package info.gogou.gogou.ui.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import junit.framework.Assert;

import java.io.File;
import java.util.ArrayList;

import info.gogou.gogou.R;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


public class ShowGalleryActivity extends RoboActionBarActivity {
    public static final String TAG = "ShowGallery";
    public static final String EXTRA_NAME = "images";

    private GalleryPagerAdapter image_adapter;
    private ArrayList<String> images = null;

    @InjectView(R.id.pager)
    private ViewPager pager;
    @InjectView(R.id.thumbnails)
    private LinearLayout thumbnails;
    @InjectView(R.id.btn_close)
    private ImageButton closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        images = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_NAME);
        Log.d(TAG, "image size" + images.size());


        Assert.assertNotNull(images);

        image_adapter = new GalleryPagerAdapter(this);
        pager.setAdapter(image_adapter);
        pager.setOffscreenPageLimit(6); // how many images to load into memory

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Close clicked");
//                for(int i=0; i< images.size(); i++){
//                    delFile(images.get(0));
//                }
                finish();
            }
        });
    }

    //删除文件
    public static void delFile(String path){
        File file = new File(path);
        if(file.isFile()){
            file.delete();
        }
        file.exists();
    }

    class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = thumbnails.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    pager.getLayoutParams()).bottomMargin - (borderSize*2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);

            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Thumbnail clicked");

                    // Set the pager position when thumbnail clicked
                    pager.setCurrentItem(position);
                }
            });
            thumbnails.addView(thumbView);

            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image);

            // Asynchronously load the image and set the thumbnail and pager view
            Glide.with(_context)
                    .load(images.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImage(ImageSource.bitmap(bitmap));
                            thumbView.setImageBitmap(bitmap);
                        }
                    });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
