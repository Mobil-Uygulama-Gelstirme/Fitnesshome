package com.example.fitnesshome;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Video_activity extends AppCompatActivity {

    String video_path;
    TextView  textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String move_name = getIntent().getStringExtra("move_name");

        VideoView video_player = findViewById(R.id.video_player);



        switch(move_name)
        {
            case "Over and Back":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo1;
                break;
            case "Cobra Pose":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo2;
                break;
            case "Stand and Reach":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo3;
                break;
            case "Wall Slides w Chin Nod":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo4;
                break;
            case "Quadruped Thoracic Rotations":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo5;
                break;
            case "Kneeling Hip Flexor Stretch":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo6;
                break;
            case "Pigeon Stretch":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo7;
                break;
            case "Glute Bridges":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.posturvideo8;
                break;


            case "Towel Stretch":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.mobilityvideo1;
                break;
            case "Thoraric Extensions":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.mobilityvideo2;
                break;
            case "Cat Cows":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.mobilityvideo3;
                break;
            case "90/90 Drill":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.mobilityvideo4;
                break;
            case "Bench Ankle Mobilization":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.mobilityvideo5;
                break;
            case "Weighted Goblet Squat":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.mobilityvideo6;
                break;


            case "Leg Raise":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo1;
                break;
            case "Reach Troughs":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo2;
                break;
            case "Reverse Crunches":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo3;
                break;
            case "Dead bugs":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo4;
                break;
            case "Hollow Body":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo5;
                break;
            case "Plank":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo6;
                break;
            case "Rest":
                video_path = "android.resource://" + getPackageName() +"/"+R.raw.corevideo7;
                break;


            default:
                // code block
        }


        Uri uri = Uri.parse(video_path);
        video_player.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        video_player.setMediaController(mediaController);
        mediaController.setAnchorView(video_player);


        textView=findViewById(R.id.textView);
        textView.setText(move_name);




    }

}