package miguel.chatgo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class ViewImageActivity extends AppCompatActivity {
    public static final int DELAY = 10000;
    private ImageView photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        photoFile= (ImageView) findViewById(R.id.photoFile);
        Uri photoFileUri= getIntent().getData();
        Picasso.with(this).load(
                photoFileUri.toString())
                .into(photoFile);



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, DELAY);
    }
}
