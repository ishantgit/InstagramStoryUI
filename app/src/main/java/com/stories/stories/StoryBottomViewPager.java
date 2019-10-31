package com.stories.stories;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stories.stories.storyProgress.StoriesProgressView;

import java.util.HashMap;
import java.util.List;

public class StoryBottomViewPager extends PagerAdapter {

    private List<StoryModel> stories;
    private Context context;
    private HashMap<Integer, StoriesProgressView> storiesProgressViews;

    public interface StoryBottomViewPagerInterface{
        void goLeft(int goTo);
        void goRight(int goTo);
        void removeProfile(int position);
    }

    private StoryBottomViewPagerInterface storyBottomViewPagerInterface;

    public void setStoryBottomViewPagerInterface(StoryBottomViewPagerInterface storyBottomViewPagerInterface) {
        this.storyBottomViewPagerInterface = storyBottomViewPagerInterface;
    }

    public StoryBottomViewPager(Context context, List<StoryModel> stories) {
        this.stories = stories;
        this.context = context;
        this.storiesProgressViews = new HashMap<>();
    }


    @Override
    public int getCount() {
        return stories.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    public void resumeProgress(int position){
        StoriesProgressView storiesProgressView = storiesProgressViews.get(position);
        if(storiesProgressView != null) {
            if(storiesProgressView.hasStarted())
                storiesProgressView.resume();
            else
                storiesProgressView.startStories();
        }
    }

    public void pauseProgress(int position){
        StoriesProgressView storiesProgressView = storiesProgressViews.get(position);
        if(storiesProgressView != null)
            storiesProgressView.pause();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_profile_story, container, false);
        final StoriesProgressView storiesProgressView = view.findViewById(R.id.stories);
        ImageView imageFirst = view.findViewById(R.id.image_first);
        ImageView imageSecond = view.findViewById(R.id.image_second);
        ImageView imageThird = view.findViewById(R.id.image_third);
        ImageView imageFourth = view.findViewById(R.id.image_fourth);
        View leftImage = view.findViewById(R.id.left_image);
        View rightImage = view.findViewById(R.id.right_image);
        final int[] imagePosition = {0};
        imageFirst.setVisibility(View.GONE);
        imageSecond.setVisibility(View.GONE);
        imageThird.setVisibility(View.GONE);
        imageFourth.setVisibility(View.GONE);
        final List<String> photos = stories.get(position).getImageList();
        if(photos != null && photos.size() > 0){
            for(int i=0;i<photos.size();i++){
                ImageView imageView = view.findViewById(getId(i));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.height = AppApplication.getScreen_height();
                params.weight = AppApplication.getScreen_width();
                imageView.setLayoutParams(params);
                setImage(photos.get(i),imageView);
            }
            storiesProgressView.setStoriesCount(photos.size());
            storiesProgressView.setStoryDuration(3000L);
            imageFirst.setVisibility(View.VISIBLE);
        }
        storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                if(imagePosition[0] + 1 <  stories.get(position).getImageList().size() && imagePosition[0] + 1 >= 0){
                    view.findViewById(getId(imagePosition[0])).setVisibility(View.GONE);
                    imagePosition[0] = imagePosition[0] + 1;
                    view.findViewById(getId(imagePosition[0])).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPrev() {
                if(imagePosition[0] - 1 < stories.get(position).getImageList().size() && imagePosition[0]-1>=0){
                    view.findViewById(getId(imagePosition[0])).setVisibility(View.GONE);
                    imagePosition[0] = imagePosition[0] - 1;
                    view.findViewById(getId(imagePosition[0])).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onComplete() {
                if(storyBottomViewPagerInterface != null && !storiesProgressView.isCompleted())
                    storyBottomViewPagerInterface.goRight(position+1);
            }
        });
        storiesProgressViews.put(position,storiesProgressView);
        final boolean[] isLongPressed = {false};
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isLongPressed[0] = true;
            }
        };
        rightImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start
                        storiesProgressView.pause();
                        handler.postDelayed(runnable,ViewConfiguration.getLongPressTimeout());
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        handler.removeCallbacks(runnable);
                        storiesProgressView.resume();
                        if(isLongPressed[0]){
//                            storiesProgressView.resume();
                        }else{
                            if(imagePosition[0] + 1 <  stories.get(position).getImageList().size() && imagePosition[0] + 1 >= 0){
                                storiesProgressView.skip();
                            }else if(storyBottomViewPagerInterface != null)
                                storyBottomViewPagerInterface.goRight(position+1);
                        }
                        isLongPressed[0] = false;
                        return true;
                }
                return false;
            }
        });
        leftImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start
                        storiesProgressView.pause();
                        handler.postDelayed(runnable,ViewConfiguration.getLongPressTimeout());
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        handler.removeCallbacks(runnable);
                        storiesProgressView.resume();
                        if(isLongPressed[0]){
//                            storiesProgressView.resume();
                        }else{
                            if(imagePosition[0] - 1 < stories.get(position).getImageList().size() && imagePosition[0]-1>=0){
                                storiesProgressView.reverse();
                            }else if(storyBottomViewPagerInterface != null)
                                storyBottomViewPagerInterface.goLeft(position-1);
                        }
                        isLongPressed[0] = false;
                        return true;
                }
                return false;
            }
        });
        container.addView(view);
        return view;
    }


    private int getId(int position){
        switch (position){
            case 0:return R.id.image_first;
            case 1:return R.id.image_second;
            case 2:return R.id.image_third;
            case 3:return R.id.image_fourth;
        }
        return R.id.image_first;
    }

    private void setImage(String image,ImageView imageView){
        try{
            Picasso.get()
                    .load(image)
                    .centerCrop()
                    .resize(AppApplication.getScreen_width(),AppApplication.getScreen_height())
                    .into(imageView);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View)object;
        StoriesProgressView storiesProgressView = view.findViewById(R.id.stories);
        storiesProgressView.destroy();
        storiesProgressViews.remove(position);
        container.removeView((View) object);
    }
}
