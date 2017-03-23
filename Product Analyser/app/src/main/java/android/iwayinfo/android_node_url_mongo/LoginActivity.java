package android.iwayinfo.android_node_url_mongo;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IWAY on 09-03-2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name,password, email;
    Button login;
    TextView register;
    private String api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        name=(EditText)findViewById(R.id.et_name);
        email=(EditText)findViewById(R.id.et_email);
        password=(EditText)findViewById(R.id.et_password);
        login=(Button)findViewById(R.id.btn_login);
        register=(TextView)findViewById(R.id.tv_register);


        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }
    class GetDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return getData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //set data response to textView
            //mResult.setText(result);

            Log.d("login",result.toString()+" "+ result.length());
            if(result.length()>5){
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
            }

            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String getData(String urlPath) throws IOException {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader =null;

            try {
                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }

            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }

            return result.toString();
        }
    }

    @Override
    public void onClick(View v) {

        switch ((v.getId())){
            case R.id.btn_login:
                //make GET request
                String e1=email.getText().toString();
                String p1=password.getText().toString();
                api= "http://192.168.43.43:1000/api/login?username="+e1+"&password="+p1;
                Log.d("url",api);
                new GetDataTask().execute(api);
                break;
            case R.id.tv_register:
                Intent intent=new Intent(LoginActivity.this,register.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
