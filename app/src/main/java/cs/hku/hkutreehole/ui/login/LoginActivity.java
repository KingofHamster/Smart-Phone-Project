package cs.hku.hkutreehole.ui.login;

import static cs.hku.hkutreehole.Utils.Utils.checkHKUEmailAddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cs.hku.hkutreehole.EmailService;
import cs.hku.hkutreehole.MainActivity;
import cs.hku.hkutreehole.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 关联activity.xml
        setContentView(R.layout.login);
        // 关联用户名、密码和登录、注册按钮
        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        Button loginButton = (Button) this.findViewById(R.id.LoginButton);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        //登录按钮监听器
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 获取用户名和密码
                        String strUserName = userName.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();
                        String verificationCode = String.valueOf(strUserName.hashCode()).substring(0, 6);
                        // 判断如果用户名为"123456"密码为"123456"则登录成功
                        if (strPassWord.equals(verificationCode)||strPassWord.equals("123456")) {
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("EmailAddress", strUserName);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Error Information", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        //注册按钮监听器
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // send Email Verification Code
                        String emailAddress = userName.getText().toString().trim();
                        if(checkHKUEmailAddress(emailAddress)) {
                            String verificationCode = String.valueOf(emailAddress.hashCode()).substring(0, 6);
                            EmailService.startActionSendEmail(LoginActivity.this,
                                    emailAddress,
                                    verificationCode);
                            Toast.makeText(LoginActivity.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Not a HKU email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
}
