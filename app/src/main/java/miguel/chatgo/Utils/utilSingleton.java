package miguel.chatgo.Utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.parse.ParseUser;

import java.util.List;

import miguel.chatgo.R;

/**
 * Created by Miguel on 1/20/2016.
 */
public class utilSingleton {
    private static utilSingleton UtilSingleton;

    private utilSingleton(){}

    public static utilSingleton getInstance(){
        if(UtilSingleton ==null)
            UtilSingleton =new utilSingleton();

        return UtilSingleton;
    }

    public void getDescription(){
        Log.v("Singleton creado", "singleton creado");
    }

    public void copyUsernamesIntoStringArray(String [] usernames, List<ParseUser> mUsers){
        int i =0 ;
        for (ParseUser user : mUsers){
            usernames[i]=user.getUsername();
            i++;
        }
    }
    public AlertDialog generateDialog(String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.editFriendsErrorTitle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
