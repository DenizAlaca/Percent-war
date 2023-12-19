package com.vris.yuzdesavasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class MailSender extends AppCompatActivity {
    public SharedPreferences userlogin;
    public String logdogrulanma;
    public String password;
    public String maildogrulama;
    public String maildurum;
    public String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sender);
        userlogin=getSharedPreferences("userlog", Context.MODE_PRIVATE);
        logdogrulanma =userlogin.getString("ulog","");
        maildogrulama=userlogin.getString("uname","");
        password=userlogin.getString("pass","");
        maildurum=userlogin.getString("mail","");

            content="Merhaba aramıza hoş geldin. Bir sonraki girişinde kullanman için gereken bilgilerin; \n \nkullanıcı adı: "+maildogrulama.toString()+"\n \nşifre: "+password.toString()+"\n \nSoru ve sorunlar için support@percentwar.com adresine mail atabilirsiniz.";

            sendMail();
            Intent don=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(don);

    }
    private void sendMail()
    {
      //  Toast.makeText(getApplicationContext(), logdogrulanma, Toast.LENGTH_SHORT).show();
        String message="VRIS Hesap Bilgilerin";
        String mail=userlogin.getString("ulog","");
        JavaMailAPI javaMailApi=new JavaMailAPI(this,mail,message,content);
        javaMailApi.execute();
    }
}