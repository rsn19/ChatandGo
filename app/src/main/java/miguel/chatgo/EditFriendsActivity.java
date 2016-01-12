package miguel.chatgo;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EditFriendsActivity extends ListActivity {
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;

    List<ParseUser> mUsers;
    String [] usernames;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        progressBar = (ProgressBar) findViewById(R.id.progressBarFriendsActivity);
        progressBar.setVisibility(View.VISIBLE);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser= ParseUser.getCurrentUser();
        mFriendsRelation=mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    mUsers = objects;
                    usernames = new String[objects.size()];
                    copyUsernamesIntoStringArray(usernames);
                    progressBar.setVisibility(View.GONE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter);
                    addFriendCheckmarks();
                } else {
                    generateDialog(e.getMessage()).show();

                }
            }
        });

    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null){
                    //success
                    for(int i=0; i<mUsers.size(); i++){
                        ParseUser user = mUsers.get(i);
                        for (ParseUser friend : friends){
                            if (friend.getObjectId().equals(user.getObjectId())){
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    Log.e("ParseException", e.getMessage());
                }
            }
        });
    }




    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(getListView().isItemChecked(position)){
            mFriendsRelation.add(mUsers.get(position));
        }else{
            mFriendsRelation.remove(mUsers.get(position));
        }

        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    generateDialog(e.getMessage()).show();
                    Log.e("ParseException", e.getMessage());
                }
            }
        });

    }

    protected AlertDialog generateDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
        builder.setTitle(R.string.editFriendsErrorTitle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
            return dialog;
    }

    protected void copyUsernamesIntoStringArray(String [] usernames){
        int i =0 ;
        for (ParseUser user : mUsers){
            usernames[i]=user.getUsername();
            i++;
        }
    }
}
