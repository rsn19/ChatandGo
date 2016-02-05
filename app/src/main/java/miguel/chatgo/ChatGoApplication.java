package miguel.chatgo;

import android.app.Application;

import com.parse.Parse;

import miguel.chatgo.Utils.utilSingleton;

/**
 * Created by Miguel on 12/19/2015.
 */
public class ChatGoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        utilSingleton utilSingleton = miguel.chatgo.Utils.utilSingleton.getInstance();


        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/

    }

}
