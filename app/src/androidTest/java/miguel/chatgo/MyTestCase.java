package miguel.chatgo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MyTestCase extends ActivityInstrumentationTestCase2<LoginActivity> {

	private Button boton;
	private EditText usernameField, passwordField;
	private LoginActivity actividad;
	private static final String PASSWORD = "caca";
	private static final String LOGIN = "Pepito";

	public MyTestCase() {
//		super("com.example.calc", MainActivity.class);
		super(LoginActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		actividad = getActivity();
		usernameField = (EditText) actividad.findViewById(R.id.usernameField);
		passwordField = (EditText) actividad.findViewById(R.id.passwordField);
		boton = (Button) actividad.findViewById(R.id.actionButton);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLogin() {
		//on value 1 entry
		TouchUtils.tapView(this, usernameField);
		getInstrumentation().sendStringSync(LOGIN);
		// now on value2 entry
		TouchUtils.tapView(this, passwordField);
		getInstrumentation().sendStringSync(PASSWORD);
		// now on Add button
		TouchUtils.clickView(this, boton);

		if(ParseUser.getCurrentUser()!=null)
			ParseUser.logOut();

		ParseUser.logInInBackground(usernameField.getText().toString(), passwordField.getText().toString(), new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				assertTrue("Add result should be...", e == null);
			}
		});
	}
}
