package com.example.adnane.tp4;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import android.content.Context;
import com.example.adnane.tp4.bd.Etablissement;


public class MyAdapter extends RecyclerView.Adapter {
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";
    private final static String ETAB_LABEL = "LABEL";
    private List<Etablissement> Etablissements;
    Context context;

    // We are actually storing our pictures in the database in String format so we need to decode
    // Before usage
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    // for getting the data from Activity "Mylist"
    public MyAdapter(Context context, List Etab) {
        this.context = context;
        this.Etablissements = Etab;
    }


    //inflate the layout item xml and pass it to View Holder
    //associer notre adapter à notre vu
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_deux, parent, false);
        return new MyViewHolder(view);

    }

    //
    @Override
    //set the data in the view’s by way of ViewHolder.
    //affecte les données aux widgets de la vue
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Etablissement Etab=  Etablissements.get(position);

        ((MyViewHolder)    holder).name.setText(Etab.getName());
        ((MyViewHolder)    holder).label.setText(Etab.getLabel());
        ((MyViewHolder)    holder).img.setImageBitmap(decodeBase64(Etab.getImage()));
        ((MyViewHolder)    holder).display(Etab);

    }

    @Override
    public int getItemCount() {
        return Etablissements.size();
    }


    //get the reference of item view's
    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView label;
        TextView name;
        ImageView img;
        private Etablissement currentEtab;

        public MyViewHolder(final View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            name = itemView.findViewById(R.id.name);
            img=  itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra(ETAB_LABEL, currentEtab.getLabel());
                    intent.putExtra(LATITUDE,currentEtab.getLatitude());
                    intent.putExtra(LONGITUDE,currentEtab.getLongitude());
                    context.startActivity(intent);
                    /*
                    * new AlertDialog.Builder(itemView.getContext())
                    * .setTitle(currentEtab.getLabel())
                            .show(); */
                }
            });

        }

        public void display(Etablissement Etab) {
            currentEtab = Etab;
            name.setText(Etab.getName());
            label.setText(Etab.getLabel());
            img.setImageBitmap(decodeBase64(Etab.getImage()));
        }


    }



}





































































