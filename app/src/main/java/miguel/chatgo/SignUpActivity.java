package miguel.chatgo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameField, passwordField, emailField;
    private ParseUser parseUser = new ParseUser();
    private ProgressBar progressBar;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.bringToFront();
        emailField = (EditText) findViewById(R.id.emailField);

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "Lighthouse_PersonalUse.ttf");
        usernameField.setTypeface(font);
        passwordField.setTypeface(font);
        emailField.setTypeface(font);


    }


    public void terminar(View v){
        finish();
    }
    public void registrarse(View v) {
        if (checkeoCampos()) {
            parseUser.setUsername(usernameField.getText().toString());
            parseUser.setPassword(passwordField.getText().toString());
            parseUser.setEmail(emailField.getText().toString());

            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (e == null) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //ActivityCompat.finishAffinity(SignUpActivity.this);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        generarDialogo(e.getMessage()).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            generarDialogo("Introduce los campos").show();

        }
    }


    public boolean checkeoCampos() {
        return !usernameField.getText().toString().equals("") && !passwordField.getText().toString().equals("") && emailValidator(emailField.getText().toString());

    }


    private Dialog generarDialogo(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(error);
        builder.setTitle("Atención");
        builder.setCancelable(true);

        return builder.create();

    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}