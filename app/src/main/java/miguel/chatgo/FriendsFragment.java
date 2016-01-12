package miguel.chatgo;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Miguel on 1/8/2016.
 */
public class FriendsFragment extends ListFragment{
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;
    List<ParseUser> mUsers;
    String [] usernames;
    ProgressBar progressBar;
    TextView addFriends_Textview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friendsfragment, container, false);
        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        progressBar= (ProgressBar) rootView.findViewById(R.id.progressBarListView);
        addFriends_Textview = (TextView) rootView.findViewById(R.id.noFriendsLabel);
        progressBar.setVisibility(View.GONE);
        return rootView;
    }

    /*
    * No es carga de trabajo redundante para la aplicación? que tenga que recargar hasta 3 veces la misma lista. ( carga de usuarios en el menu de amigos
    * carga de usuarios marcados cuando volvemos a entrar en la activity de amigos
    * volver a llamar al backend para rellenar una lista que podría estar en local.
    * */
    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser= ParseUser.getCurrentUser();
        mFriendsRelation=mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();

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
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            usernames);
                    setListAdapter(adapter);
                    if(mUsers.size()!=0)
                        addFriends_Textview.setVisibility(View.INVISIBLE);
                } else {
                    generateDialog(e.getMessage()).show();

                }
            }
        });



    }

    protected void copyUsernamesIntoStringArray(String [] usernames){
        int i =0 ;
        for (ParseUser user : mUsers){
            usernames[i]=user.getUsername();
            i++;
        }
    }
    protected AlertDialog generateDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
        builder.setTitle(R.string.editFriendsErrorTitle)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
