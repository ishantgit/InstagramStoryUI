package com.stories.stories;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private List<StoryModel> storyModels = new ArrayList<>();
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.story_parent);
        addStories();
    }

    private void addStories(){
        for(int i=0;i<10;i++){
            StoryModel storyModel = new StoryModel();
            storyModel.setImageList(getRandomUrl());
            storyModels.add(storyModel);
        }
        addStoryToParent();
    }

    private List<String> getRandomUrl(){
        Random randomGenerator = new Random();
        List<String> photos = new ArrayList<>();
        for(int i=0;i<3;i++){
            int randomInt = randomGenerator.nextInt(100) + 1;
            String url = "https://tineye.com/images/widgets/mona.jpg";
            photos.add(url);
        }
        return photos;
    }

    private void addStoryToParent(){
        for(int i =0;i<storyModels.size();i++){
            View view = LayoutInflater.from(this).inflate(R.layout.story_item,null);
            ImageView imageView = view.findViewById(R.id.image_view);
            try{
                Picasso.get()
                        .load(storyModels.get(i).getImageList().get(0))
                        .centerCrop()
                        .fit()
                        .transform(new CircleTransform())
                        .into(imageView);
            }catch (Exception e){
                Log.i("error","error");
            }
            int finalI = i;
            imageView.setOnClickListener(v->{
                initialiseStories(finalI);
            });
            linearLayout.addView(view);
        }
    }

    private void initialiseStories(int position){
        StoryBottomFragment storyBottomFragment = new StoryBottomFragment();
        storyBottomFragment.setPosition(position);
        storyBottomFragment.setStoryModels(storyModels);
        try{
            storyBottomFragment.show(getSupportFragmentManager(),"");
        }catch (Exception e){}
    }
}
