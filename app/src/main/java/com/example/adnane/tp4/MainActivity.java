package com.example.adnane.tp4;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnane.tp4.bd.MyDatabase;
import com.example.adnane.tp4.bd.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    static User user = new User();
    static MyDatabase mydatabase;
    private CardView connect;
    private EditText login;
    private EditText pass;
    private TextView newUser;

    public void myClick(View v){
        user.setLogin(login.getText().toString());
        user.setPass(pass.getText().toString());
        if(!(login.getText().toString()).matches("")){
            List<User> usr = MainActivity.mydatabase.mydao().getUser(login.getText().toString());
            switch(v.getId()){
                case R.id.newuser:
                    if (usr.size() != 0) {
                        Toast.makeText(getApplicationContext(), "Impossible de créer le compte: login existant",
                                Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        MainActivity.mydatabase.mydao().adduser(user);
                        Toast.makeText(getApplicationContext(), "Parfait le compte est créé",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                case R.id.card:
                    if (usr.size() != 0) {
                        if (usr.get(0).getPass().contentEquals(pass.getText().toString()) ) {
                            Intent intent = new Intent(this, Mylist.class);
                            startActivity(intent);
                            break;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Mot de passe incorrect",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Compte non existant",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }}}}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "user_bd")
                .allowMainThreadQueries().build();

        connect = (CardView) this.findViewById(R.id.card);
        login = (EditText) this.findViewById(R.id.login);
        pass = (EditText) this.findViewById(R.id.pass);
        newUser = (TextView) this.findViewById(R.id.newuser);

        connect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                myClick(v);
            }
        });
        newUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myClick(v);
            }
        });
    }
}
