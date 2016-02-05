package miguel.chatgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static int TAKE_PHOTO_REQUEST = 0;
    private static int TAKE_VIDEO_REQUEST = 1;
    private static int PICK_PHOTO_REQUEST = 2;
    private static int PICK_VIDEO_REQUEST = 3;
    final public static int MEDIA_TYPE_IMAGE = 4;
    final public static int MEDIA_TYPE_VIDEO = 5;

    public static int FILE_SIZE_LIMIT = 10240;

    String appname;
    File mediaStorageDir;
    Uri mMediaUri;
    String fileType="";


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser loggedUser = ParseUser.getCurrentUser();
        if (loggedUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else {
            Log.v("USER", loggedUser.getUsername());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //Posible error en MainActivity this, podria ser getApplicationContext()
        mSectionsPagerAdapter = new SectionsPagerAdapter(MainActivity.this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_inbox);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_friends);
        tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);
        appname = MainActivity.this.getString(R.string.app_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.logOut:
                ParseUser.logOut();
                Intent logOutIntent = new Intent(MainActivity.this, LoginActivity.class);
                logOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOutIntent);
                return true;
            case R.id.openFriendsActivity:
                Intent editFriendsIntent = new Intent(MainActivity.this, EditFriendsActivity.class);
                startActivity(editFriendsIntent);
                return true;
            case R.id.action_camera:
                dialogCameraChoices().show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private android.support.v7.app.AlertDialog generateDialog(String error) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(error)
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, null);
        android.support.v7.app.AlertDialog dialog = builder.create();
        return dialog;

    }

    private android.support.v7.app.AlertDialog dialogCameraChoices() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        String[] options = getResources().getStringArray(R.array.camera_choices);
        builder.setTitle(R.string.menu_choose_option_label)
                .setItems(options, mDialogListener())
                .setPositiveButton(android.R.string.ok, null);
        android.support.v7.app.AlertDialog dialog = builder.create();
        return dialog;

    }

    private DialogInterface.OnClickListener mDialogListener() {

        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        if (mMediaUri == null) {
                            Toast.makeText(MainActivity.this, "error en el almacenamiento", Toast.LENGTH_SHORT).show();
                        } else {
                            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    mMediaUri);
                            //takePhotoIntent.putExtra("return-data", true);
                        }
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                        break;
                    case 1:
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                        if (mMediaUri == null) {
                            Toast.makeText(MainActivity.this, "error en el almacenamiento", Toast.LENGTH_SHORT).show();
                        } else {
                            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    mMediaUri);
                            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        }
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                        break;
                    case 2:
                        Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        choosePhotoIntent.setType("image/*");
                        startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                        break;
                    case 3:
                        Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseVideoIntent.setType("video/*");
                        startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                        Toast.makeText(MainActivity.this, "Tamaño max 10MBs", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        };
        return dialogListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Todo va bien
        if (resultCode == RESULT_OK) {
            //comprueba que tipo de info
            if (requestCode == PICK_PHOTO_REQUEST) {
                if (data != null) {
                    mMediaUri = data.getData();
                    generateDialog(mMediaUri.toString()).show();
                }
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                fileType= ParseConstants.TYPE_VIDEO;
                //recoge los datos procesados en el video.
                if (data != null) {
                    mMediaUri = data.getData();
                    generateDialog(mMediaUri.toString()).show();
                }
                checkSize(mMediaUri);

            } else if (requestCode == TAKE_VIDEO_REQUEST) {
                fileType= ParseConstants.TYPE_VIDEO;
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                sendBroadcast(mediaScanIntent);
                generateDialog(mMediaUri.toString()).show();
            } else if (requestCode== TAKE_PHOTO_REQUEST){
                fileType= ParseConstants.TYPE_IMAGE;
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                sendBroadcast(mediaScanIntent);
                generateDialog(mMediaUri.toString()).show();
            }

            Intent recipientsActivityIntent = new Intent(MainActivity.this, RecipientsActivity.class);
            recipientsActivityIntent.putExtra(ParseConstants.KEY_FILETYPE,fileType);
           // recipientsActivityIntent.putExtra(ParseConstants.KEY_FILENAME);
            recipientsActivityIntent.setData(mMediaUri);
            startActivity(recipientsActivityIntent);


        } else {
            generateDialog(getResources().getString(R.string.camera_left_warning)).show();
        }

    }


    public void checkSize(Uri mMediaUri) {
        int fileSize = 0;
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(mMediaUri);
            //assert asegura que inputStream no sea nulo
            assert inputStream != null;
            fileSize = inputStream.available();
            if (checkIfSizeCorrect(fileSize)) {
                Toast.makeText(MainActivity.this, "Video was added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Video not added. Max 10MB", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    private Uri getOutputMediaFileUri(int mediaType) {
        if (isExternalStorageAvailable()) {
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appname + " photos");
                    break;
                case MEDIA_TYPE_VIDEO:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), appname + " videos");
                    break;
            }
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("GoChat", "Foto no creada ");
                    return null;
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Introduce memoria SD", Toast.LENGTH_SHORT).show();
        }

        File mediaFile;
        Date now = new Date();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
        String path = mediaStorageDir.getPath() + File.separator;
        if (mediaType == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
        } else if (mediaType == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(path + "VID_" + timestamp + ".mp4");
        } else {
            return null;
        }

        return Uri.fromFile(mediaFile);
    }

    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else return false;
    }


    public boolean checkIfSizeCorrect(int fileSize) {
        if (fileSize > FILE_SIZE_LIMIT)
            return false;
        else
            return true;
    }
}
