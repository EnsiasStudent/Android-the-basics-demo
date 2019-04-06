package com.example.adnane.tp4;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adnane.tp4.bd.MyDatabase;
import com.example.adnane.tp4.bd.Etablissement;

import java.util.List;

public class Mylist extends AppCompatActivity {
    public static final int LOAD_DATA = 2;
    private final static String ETAB_NAME = "NAME";
    private final static String ETAB_LABEL = "LABEL";
    private final static String ETAB_IMG = "IMG";
    static MyDatabase mydatabase;
    RecyclerView rv;
    MyAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Universities");
        setContentView(R.layout.activity_recycleview);
        mydatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "user_bd")
                .allowMainThreadQueries().build();
        List<Etablissement> etablissements = Mylist.mydatabase.mydao2().getEtablissements();
        ad = new MyAdapter(this, etablissements);
        rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(ad);

        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        rv.addItemDecoration(new RecyclerDecoration(sidePadding, topPadding));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Toast.makeText(this, "Supprimer un etablissement", Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Mylist.this);
                alertDialog.setTitle("Etablissement par Label");

                final EditText input = new EditText(Mylist.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.mipmap.keynote);

                alertDialog.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String validate = input.getText().toString();
                        if(!validate.matches("")){
                            Etablissement test = Mylist.mydatabase.mydao2().getEtablissement(validate);
                            if(test == null){
                                Toast.makeText(Mylist.this, "Etablissement non existant", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Mylist.mydatabase.mydao2().deleteEtablissement(test);
                                dialog.cancel();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        }
                    }});

                alertDialog.setNegativeButton("Modifier",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String validate = input.getText().toString();
                                if(!validate.matches("")){
                                    Etablissement test = Mylist.mydatabase.mydao2().getEtablissement(validate);
                                    if(test == null){
                                        Toast.makeText(Mylist.this, "Etablissement non existant", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Intent intent = new Intent(getBaseContext(), Create.class);
                                        intent.putExtra(ETAB_NAME, test.getName());
                                        intent.putExtra(ETAB_LABEL, test.getLabel());
                                        intent.putExtra(ETAB_IMG, test.getImage());
                                        startActivity(intent);
                                    }
                                }
                                // dialog.cancel();
                            }
                        });

                alertDialog.show();
                return true;

            case R.id.action_add:
                Intent intent = new Intent(this, Create.class);
                startActivity(intent);
                return true;

            case R.id.action_propos:
                // Toast.makeText(this, "à propos", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
