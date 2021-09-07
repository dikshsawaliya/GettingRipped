package com.northcap.gettingripped;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingOnClickListener;
import com.huawei.agconnect.appmessaging.model.AppMessage;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.aaid.entity.AAIDResult;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.northcap.gettingripped.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GettingRippedAccount";
    Button button1, button2;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        inAppMessage();
        getAAID();

        findViewById(R.id.login);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams();
                AccountAuthService service = AccountAuthManager.getService(MainActivity.this, authParams);
                startActivityForResult(service.getSignInIntent(), 8888);


            }
        });








        android.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        button1 = findViewById(R.id.startcoreworkout);
        button2 = findViewById(R.id.startweightlifting);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SecondActivity2.class);
                startActivity(intent);
            }
        });

    }

    public void inAppMessage()
    {
        //AGConnectAppMessaging.getInstance().setFetchMessageEnable(true);
        AGConnectAppMessaging appMessaging = AGConnectAppMessaging.getInstance();
        //
        appMessaging.setFetchMessageEnable(true);
        appMessaging.setDisplayEnable(true);
        appMessaging.setForceFetch();
        ClickListener listener = new ClickListener();
        appMessaging.addOnClickListener(listener);
    }

    public class ClickListener implements AGConnectAppMessagingOnClickListener {
        @Override
        public void onMessageClick(AppMessage appMessage) {
            // Obtain the content of the tapped message.
        }
    }

    public void getAAID(){
        Task<AAIDResult> idResult = HmsInstanceId.getInstance(getApplicationContext()).getAAID();
        idResult.addOnSuccessListener(new OnSuccessListener<AAIDResult>() {
            @Override
            public void onSuccess(AAIDResult aaidResult) {
                // Called when the AAID is obtained.
                String aaid = aaidResult.getId();
                Log.d("TAG", "getAAID successfully, aaid is " + aaid );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception myException) {
                // Called when the AAID fails to be obtained.
                Log.d("TAG", "getAAID failed, catch exceptio : " + myException);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Process the authorization result to obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (((Task<?>) authAccountTask).isSuccessful()) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "serverAuthCode:" + authAccount.getAuthorizationCode());
            } else {
                // The sign-in failed.
                Log.e(TAG, "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }


    public void coreworkout(View view) {

        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
        startActivity(intent);
    }

    public void weightlifting(View view) {

        Intent intent = new Intent(MainActivity.this,SecondActivity2.class);
        startActivity(intent);

    }

    public void food(View view) {

        Intent intent = new Intent(MainActivity.this, FoodActivity.class);
        startActivity(intent);

    }

}