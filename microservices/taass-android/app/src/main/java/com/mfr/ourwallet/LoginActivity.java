package com.mfr.ourwallet;

import android.content.Intent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mfr.model.Request.Credentials;
import com.mfr.model.Request.GoogleUser;
import com.mfr.model.Response.BaseResponse;
import com.mfr.model.Response.Parsed.BodyJWT;
import com.mfr.model.Utils.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A manageLoginResponse screen that offers manageLoginResponse via emailEditText/passwordEditText.
 */
public class LoginActivity extends AppCompatActivity {


	// UI references.
	private EditText usernameEditText;
	private EditText passwordEditText;
	private View loginView;
	private Boolean login;

	private View progressView;
	private Button loginRegisterButton;


	private TextView registerLogin;
	private TextInputLayout inputEmail;
	private EditText emailEditText;

	private GoogleSignInClient googleSignInClient;
	private static final int RC_SIGN_IN = 22;
	private GoogleSignInAccount accountGoogle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AndroidNetworking.initialize(getApplicationContext());

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail().requestProfile().requestIdToken(getString(R.string.oauth))
				.build();
		googleSignInClient = GoogleSignIn.getClient(this, gso);

		usernameEditText = (AutoCompleteTextView) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
					if(login) {
						attemptLogin();
					}else{
						attemptRegistration();
					}
					return true;
				}
				return false;
			}
		});

		loginRegisterButton = (Button) findViewById(R.id.email_login_in_button);
		loginRegisterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(login) {
					attemptLogin();
				}else{
					attemptRegistration();
				}
			}
		});
		loginView = findViewById(R.id.login_form);
		progressView = findViewById(R.id.login_progress);
		registerLogin = (TextView)findViewById(R.id.textSignLogin);
		registerLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeRegisterLogin(!login);
			}
		});

		inputEmail = findViewById(R.id.inputEmail);
		emailEditText = findViewById(R.id.email);

		changeRegisterLogin(true);

		if(!Utils.getPassword(getApplicationContext()).equals("")){
			usernameEditText.setText(Utils.getUsername(getApplicationContext()));
			passwordEditText.setText(Utils.getPassword(getApplicationContext()));
		}

		findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				signIn();
			}
		});

	}

	/*UI*/
	private void changeRegisterLogin(boolean b) {
		login = b;
		if(login){
			loginRegisterButton.setText("Login");
			registerLogin.setText("Don’t have an account? Sign up");
			inputEmail.setVisibility(View.GONE);
		}else{
			loginRegisterButton.setText("Register");
			registerLogin.setText("Already have an account? Log in");
			inputEmail.setVisibility(View.VISIBLE);
		}
	}
	private void showProgress(boolean b) {
		if (b) {
			loginView.setVisibility(View.GONE);
			progressView.setVisibility(View.VISIBLE);
		} else{
			loginView.setVisibility(View.VISIBLE);
			progressView.setVisibility(View.GONE);
		}
	}


	/*Checks*/
	private boolean isUsernameValid(String username) {
		return true;
	}

	private boolean isEmailValid(String email) {
		return email.contains("@");
	}


	/*Login*/
	private void attemptLogin() {

		usernameEditText.setError(null);
		passwordEditText.setError(null);

		String username = this.usernameEditText.getText().toString();
		String password = this.passwordEditText.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(password)) {
			this.passwordEditText.setError(getString(R.string.error_invalid_password));
			focusView = this.passwordEditText;
			cancel = true;
		}

		if (TextUtils.isEmpty(username)) {
			this.usernameEditText.setError(getString(R.string.error_field_required));
			focusView = this.usernameEditText;
			cancel = true;
		} else if (!isUsernameValid(username)) {
			this.usernameEditText.setError(getString(R.string.error_invalid_username));
			focusView = this.usernameEditText;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			showProgress(true);
			userLoginTask(new Credentials(username,password));
		}
	}

	public void userLoginTask(Credentials user) {


		AndroidNetworking.post(Utils.IP+":8081/api/login")
					.addApplicationJsonBody(user)
					.build()
					.getAsString(new StringRequestListener() {
						@Override
						public void onResponse(String response) {
							manageLoginResponse(response, false);
						}

						@Override
						public void onError(ANError anError) {
							manageLoginResponse(anError.getErrorBody(), true);
						}
					});
		}

	private void manageLoginResponse(String response, boolean isError) {
		usernameEditText.setError(null);
		passwordEditText.setError(null);

		if(response!=null && response.length()>0) {
			Log.d("Risposta", response);
			Gson gson = new Gson();

			BaseResponse res = gson.fromJson(response, BaseResponse.class);
			Log.d("RispostaGson", res.toString());
			if (!isError) {
				BodyJWT jwt = gson.fromJson(res.getBody().toString(), BodyJWT.class);
				Utils.setToken(jwt.getJwt(), getApplicationContext());
				Utils.setUsername(usernameEditText.getText().toString(), getApplicationContext());
				Utils.setPassword(passwordEditText.getText().toString(), getApplicationContext());
				Log.d("RispostaJWT", jwt.getJwt());
				Intent intent = new Intent(this, AppActivity.class);
				startActivity(intent);
			} else {
				showProgress(false);
				usernameEditText.setError(getString(R.string.error_invalid_username));
				passwordEditText.setError(getString(R.string.error_invalid_password));
				loginView.requestFocus();

			}
		}else{
			showProgress(false);
			loginView.requestFocus();
		}
	}


	/*Registrazione*/
	private void attemptRegistration() {
		usernameEditText.setError(null);
		passwordEditText.setError(null);
		String username = usernameEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		String email = emailEditText.getText().toString();

		boolean cancel = false;
		View focusView = null;
		if (TextUtils.isEmpty(password)) {
			passwordEditText.setError(getString(R.string.error_invalid_password));
			focusView = passwordEditText;
			cancel = true;
		}
		if (TextUtils.isEmpty(username)) {
			usernameEditText.setError(getString(R.string.error_field_required));
			focusView = usernameEditText;
			cancel = true;
		} else if (!isUsernameValid(username)) {
			usernameEditText.setError(getString(R.string.error_invalid_username));
			focusView = usernameEditText;
			cancel = true;
		}

		if (TextUtils.isEmpty(email)) {
			emailEditText.setError(getString(R.string.error_email_required));
			focusView = emailEditText;
			focusView = emailEditText;
			cancel = true;
		} else if (!isEmailValid(email)) {
			emailEditText.setError(getString(R.string.error_invalid_email));
			focusView = emailEditText;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			showProgress(true);
			Credentials c = new Credentials();
			c.setEmail(email);
			c.setPassword(password);
			c.setUsername(username);
			userRegisterTask(c);
		}
	}

	public void userRegisterTask(Credentials user) {

		AndroidNetworking.post(Utils.IP+"/api/users")
				.addApplicationJsonBody(user)
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						manageRegisterResponse(response, false);
					}

					@Override
					public void onError(ANError anError) {
						manageRegisterResponse(anError.getErrorBody(), true);
					}
				});
	}

	private void manageRegisterResponse(String response, boolean isError) {
		Log.e("Risposta", response);
		usernameEditText.setError(null);
		passwordEditText.setError(null);

		if(response!=null && response.length()>0) {
			Log.d("Risposta", response);
			Gson gson = new Gson();

			BaseResponse res = gson.fromJson(response, BaseResponse.class);
			Log.d("RispostaGson", res.toString());
			if (!isError) {
				Toast.makeText(getBaseContext(),"SignUp complet",Toast.LENGTH_LONG).show();
				showProgress(false);
				changeRegisterLogin(true);
				loginView.requestFocus();
			} else {
				ArrayList<String> errors = res.getMeta().getErrors();

				if(errors.contains("usernameExists"))
					usernameEditText.setError(getString(R.string.error_username_taken));
				if(errors.contains("emailExists"))
					emailEditText.setError(getString(R.string.error_email_taken));

				showProgress(false);
				loginView.requestFocus();
			}
		}else{
			showProgress(false);
			loginView.requestFocus();
		}
	}


	/*Google LOGIN*/
	private void signIn() {
		Intent signInIntent = googleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

			handleSignInResult(task);
		}
	}

	private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
		try {
			accountGoogle = completedTask.getResult(ApiException.class);
			loginGoogle(false);
		} catch (ApiException e) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.w("Google login", "signInResult:failed code=" + e.getStatusCode());
		}
	}

	private void loginGoogle(boolean isOnStart) {

		GoogleUser u = new GoogleUser();
		u.setIdToken(accountGoogle.getIdToken());
		Gson gson = new Gson();
		Log.e("kml",gson.toJson(u));
		AndroidNetworking.post(Utils.IP+"/api/oAuthUsers")
				.addApplicationJsonBody(u)
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						Log.d("LoginGoogle","google OK");
						manageLoginResponse(response, false);
					}

					@Override
					public void onError(ANError anError) {
						Log.e("LoginGoogle",anError.getErrorBody());
					}
				});

	}



	@Override
	protected void onStart() {
		super.onStart();
		accountGoogle = GoogleSignIn.getLastSignedInAccount(this);
		if(accountGoogle!=null) {
			loginGoogle(true);
		}
	}
}

