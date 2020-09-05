package com.svm.yaplacaklarlistesi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    LinearLayout icerik;
    int secilenId;
    List<liste> gelecekListemiz;
    veritabaniIslemleri veritabaniIslemleri;
    ImageView ekle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gelecekListemiz = new ArrayList<liste>();
        icerik = findViewById(R.id.icerik);
        ekle = findViewById(R.id.ekle);
        try {
            gelecekListemiz = (new HttpRequestListe().execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                @SuppressLint("InflateParams") final View alertPenceresi = layoutInflater.inflate(R.layout.alertdialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Yeni Oluştur")
                        .setView(alertPenceresi)
                        .setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editTextAlert = alertPenceresi.findViewById(R.id.editTextAlert);
                                liste liste = new liste();
                                liste.setDurum(0);
                                liste.setYapilacak(editTextAlert.getText().toString());
                                try {
                                    boolean durum = new HttpRequestEkle().execute(liste).get();
                                    if(durum){
                                        Toast.makeText(context,"Başarıyla Eklendi!",Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(getIntent());
                                    }else{
                                        Toast.makeText(context,"Hata!",Toast.LENGTH_LONG).show();
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("İptal", null)
                        .setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        if(gelecekListemiz != null) {
            for (int i = 0; i < gelecekListemiz.size(); i++) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                final LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.liste, null, false);
                icerik.addView(linearLayout);

                CardView cardViewListe = linearLayout.findViewById(R.id.cardViewListe);
                cardViewListe.setId(gelecekListemiz.get(i).getId());
                cardViewListe.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) context);
                cardViewListe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openContextMenu(v);
                    }
                });


                final TextView textView = linearLayout.findViewById(R.id.textView);
                textView.setId(gelecekListemiz.get(i).getId());
                textView.setText(gelecekListemiz.get(i).getYapilacak());

                final CheckBox checkBox = linearLayout.findViewById(R.id.checkbox);
                checkBox.setId(gelecekListemiz.get(i).getId());
                if (gelecekListemiz.get(i).getDurum() == 1) {
                    checkBox.setChecked(true);
                    textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                final int finalI = i;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            liste duzenlenenliste = new liste();
                            duzenlenenliste.setId(gelecekListemiz.get(finalI).getId());
                            duzenlenenliste.setDurum(1);
                            duzenlenenliste.setYapilacak(gelecekListemiz.get(finalI).getYapilacak());
                            try {
                                boolean durum = new HttpRequestDuzenle().execute(duzenlenenliste).get();
                                if (durum) {
                                    textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    buttonView.setChecked(true);
                                } else {
                                    textView.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                                    buttonView.setChecked(false);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            liste liste = new liste();
                            liste.setId(gelecekListemiz.get(finalI).getId());
                            liste.setDurum(0);
                            liste.setYapilacak(gelecekListemiz.get(finalI).getYapilacak());
                            try {
                                boolean durum = new HttpRequestDuzenle().execute(liste).get();
                                if (durum) {
                                    textView.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                                    buttonView.setChecked(false);
                                } else {
                                    textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    buttonView.setChecked(true);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }

    private  class HttpRequestEkle extends AsyncTask<liste,Void,Boolean>{
        @Override
        protected Boolean doInBackground(liste... listes) {
            veritabaniIslemleri veritabaniIslemleri = new veritabaniIslemleri();
            return veritabaniIslemleri.ekle(listes[0]);
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

    };

    private  class HttpRequestSil extends AsyncTask<Integer,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Integer... integers) {
            veritabaniIslemleri veritabaniIslemleri = new veritabaniIslemleri();
            return veritabaniIslemleri.sil(integers[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

    };

    private  class HttpRequestDuzenle extends AsyncTask<liste,Void,Boolean>{
        @Override
        protected Boolean doInBackground(liste... listes) {
            veritabaniIslemleri veritabaniIslemleri = new veritabaniIslemleri();
            return veritabaniIslemleri.duzenle(listes[0]);
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

    };

    private  class HttpRequestListe extends AsyncTask<List<liste>,List<liste>,List<liste>> {

        @Override
        protected List<liste> doInBackground(List<liste>... lists) {
            veritabaniIslemleri veritabaniIslemleri = new veritabaniIslemleri();
            return veritabaniIslemleri.liste();
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu, menu);
        secilenId = v.getId();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sil:
                try {
                    boolean durum = new HttpRequestSil().execute(secilenId).get();
                    if(durum){
                        Toast.makeText(context,"Silindi!",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

}
