package miguel.chatgo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final TextView tv = (TextView) findViewById(R.id.welcomeTextview);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lighthouse_PersonalUse.ttf");
        tv.setTypeface(font);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                       iv.setImageDrawable(getResources().getDrawable(R.drawable.imagensplashicono));
                    }
                }, 500);//Tiempo desde Gris hasta Amarillo

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        tv.setVisibility(View.VISIBLE);
                    }
                }, 1000); //Tiempo Amarillo hasta Welcome

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 1000); //Tiempo Welcome hasta MainActivity


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
