package ateam.tickettoride.View.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import ateam.tickettoride.Presenter.LoginRegisterPresenter;
import ateam.tickettoride.R;
import ateam.tickettoride.View.IView;


public class LoginRegisterFragment extends Fragment implements IView {

        private static final String TAG = "LoginRegisterFragment";
        private static final String LOGIN_FAILED = "LOGIN FAILED: ";
        private static final String LOGIN_SUCCESSFUL = "LOGIN SUCCEEDED.";

        private String mUsername;
        private String mPassword;
        private String mIPAddress;
        private EditText mIPAddressField;
        private EditText mUsernameField;
        private EditText mPasswordField;
        private Button mLoginButton;
        private Button mRegisterButton;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);

            createUserNameListener(v);
            createPasswordListener(v);
            createIPAddressListener(v);

            checkForButtonRestrictions(v);

            mLoginButton = (Button) v.findViewById(R.id.login_button);
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginRegisterPresenter.getInstance().setActivity(getActivity());
                    LoginRegisterPresenter.getInstance().login(mIPAddress, mUsername, mPassword);
                }
            });

            final View view = v;
            mRegisterButton = (Button) v.findViewById(R.id.register_button);
            mRegisterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vew) {
                    LoginRegisterPresenter.getInstance().setActivity(getActivity());
                    LoginRegisterPresenter.getInstance().register(mIPAddress, mUsername, mPassword);
                }
            });

            return v;
        }

    /**
     * Checks if the necessary fields are filled to enable sign in and register
     * buttons to be active
     * @param v the current view
     */
    private void checkForButtonRestrictions(View v){
        checkForEmptyLoginValues(v);
    }

    /**
     * Create a listener for the EditText box and updates the mUsername string
     * @param v the view associated with the listener
     */
    private void createUserNameListener(View v){
            mUsernameField = (EditText) v.findViewById(R.id.username_input);
            final View view = v;
            mUsernameField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mUsername = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkForButtonRestrictions(view);
                }
            });
        }

    /**
     * Create a listener for the EditText box and updates the mPassword string
     * @param v the view associated with the listener
     */
        private void createPasswordListener(View v){
            mPasswordField = (EditText) v.findViewById(R.id.password_input);
            final View view = v;
            mPasswordField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mPassword = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkForButtonRestrictions(view);
                }
            });
        }

    private void createIPAddressListener(View v){
        mIPAddressField = (EditText) v.findViewById(R.id.ip_address_input);
        final View view = v;
        mIPAddressField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIPAddress = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkForButtonRestrictions(view);
            }
        });
    }


        /**
         * Checks that each field that is needed for sign in is filled and enables the sign in button
         * @param v the current view
         */
        private void checkForEmptyLoginValues(View v){
            Button loginButton = (Button) v.findViewById(R.id.login_button);
            Button registerButton = (Button) v.findViewById(R.id.register_button);

            String s1 = mUsernameField.getText().toString();
            String s2 = mPasswordField.getText().toString();
            String s3 = mIPAddressField.getText().toString();

            if(s1.equals("") || s2.equals("") || s3.equals("")){
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
            } else {
                loginButton.setEnabled(true);
                registerButton.setEnabled(true);
            }
        }


    @Override
    public void update() {

    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
