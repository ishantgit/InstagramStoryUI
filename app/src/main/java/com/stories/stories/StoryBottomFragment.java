package com.stories.stories;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.stories.stories.storyBottomViewPager.BottomSheetUtils;
import com.stories.stories.storyBottomViewPager.CubeTransformer;
import com.stories.stories.storyBottomViewPager.ViewPagerBottomSheetDialogFragment;

import java.util.List;

public class StoryBottomFragment extends ViewPagerBottomSheetDialogFragment {

    ViewPager viewPager;
    private List<StoryModel> storyModels;
    private int currentPosition = 0;

    public void setPosition(int position) {
        this.currentPosition = position;
    }

    private StoryBottomViewPager storyBottomViewPager;

    public void setStoryModels(List<StoryModel> userModels) {
        this.storyModels = userModels;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View view = View.inflate(getContext(), R.layout.bottom_story_fragment, null);
        viewPager = view.findViewById(R.id.view_pager);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = AppApplication.getScreen_height();
        viewPager.setLayoutParams(layoutParams);
        initialise();
        dialog.setContentView(view);
    }


    private void initialise(){
        storyBottomViewPager = new StoryBottomViewPager(getActivity(),storyModels);
        storyBottomViewPager.setStoryBottomViewPagerInterface(new StoryBottomViewPager.StoryBottomViewPagerInterface() {
            @Override
            public void goLeft(int goTo) {
                int currentItem = viewPager.getCurrentItem();
                if(currentItem - 1 >= 0 && currentItem - 1 < storyBottomViewPager.getCount() && currentItem - 1 == goTo)
                    viewPager.setCurrentItem(currentItem-1);
            }

            @Override
            public void goRight(int goTo) {
                int currentItem = viewPager.getCurrentItem();
                if(currentItem + 1 >= 0 && currentItem + 1 < storyBottomViewPager.getCount() && currentItem + 1 == goTo)
                    viewPager.setCurrentItem(currentItem+1);
            }

            @Override
            public void removeProfile(int position) {
                storyModels.remove(position);
                storyBottomViewPager.notifyDataSetChanged();
                new Handler().postDelayed(()-> storyBottomViewPager.resumeProgress(position),100);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(currentPosition != position){
                    storyBottomViewPager.pauseProgress(currentPosition);
                    storyBottomViewPager.resumeProgress(position);
                    currentPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(storyBottomViewPager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageTransformer(false,new CubeTransformer());
        BottomSheetUtils.setupViewPager(viewPager);
        new Handler().postDelayed(()->{
            storyBottomViewPager.resumeProgress(0);
        },500);
    }
}

