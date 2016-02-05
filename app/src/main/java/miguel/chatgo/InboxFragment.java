package miguel.chatgo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import miguel.chatgo.Utils.MessageAdapter;
import miguel.chatgo.Utils.utilSingleton;

/**
 * Created by Miguel on 1/8/2016.
 */
public class InboxFragment extends ListFragment {
    private List<ParseObject> mMessages;
    private TextView noMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inboxfragment, container, false);
        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarListView);
        progressBar.setVisibility(View.GONE);
        noMessages = (TextView) rootView.findViewById(R.id.emptyLabel);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENTSID, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                //progressBar.setVisibility
                if (e == null) {
                    mMessages = objects;
                    String[] usernames = new String[objects.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDERNAME);
                        i++;
                    }
                    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            usernames);*/
                    MessageAdapter adapter = new MessageAdapter(getListView().getContext(), 0, mMessages);
                    setListAdapter(adapter);

                    if (mMessages.size() != 0) {
                        noMessages.setVisibility(View.INVISIBLE);
                    }

                } else {
                    utilSingleton.getInstance().generateDialog(e.getMessage(), getView().getContext());
                }
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILETYPE);

        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENTSID);


        //Operación al recibir el mensaje
        if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
            Intent intentTypeImage = new Intent(getActivity(), ViewImageActivity.class);
            intentTypeImage.setData(fileUri);
            startActivity(intentTypeImage);
        } else {
            Intent intentTypeVideo = new Intent(Intent.ACTION_VIEW, fileUri);
            intentTypeVideo.setDataAndType(fileUri, "video/*");
            startActivity(intentTypeVideo);

        }

        if (ids.size() > 1) {

            utilSingleton.getInstance().generateDialog("ids!>1", getActivity().getApplicationContext());
            ids.remove(ParseUser.getCurrentUser().getObjectId());
            ArrayList<String> idsToRemove = new ArrayList<>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
            message.removeAll(ParseConstants.KEY_RECIPIENTSID, idsToRemove);
            message.saveInBackground();

        } else {
            message.deleteInBackground();
        }
    }

}
