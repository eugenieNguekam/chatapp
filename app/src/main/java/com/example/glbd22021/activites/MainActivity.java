package com.example.glbd22021.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.example.glbd22021.R;
import com.example.glbd22021.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import utils.Constante;
import utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        detailsUtilisateur();
        recupToken();
        setListeners();
    }

    private void setListeners(){
        binding.imageDeconnexion.setOnClickListener(v -> deconnexion());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UtilisateurActivity.class)));
    }

    private void detailsUtilisateur(){
        binding.texteNom.setText(preferenceManager.getString(Constante.CLE_NOM));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constante.CLE_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void afficherToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void recupToken(){
       // FirebaseMessaging.getInstance().getToken.addOnSuccesListener(this::majToken);
    }

    private void majToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constante.CLE_COLLECTION_UTILISATEUR).document(
                        preferenceManager.getString(Constante.CLE_ID_UTILISATEUR)
                );
        documentReference.update(Constante.CLE_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> afficherToast("Token mis a jour"))
                .addOnFailureListener(e -> afficherToast("Mis a jour du token impossible"));
    }

    private void deconnexion(){
        afficherToast("Deconnexion...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference=
                database.collection(Constante.CLE_COLLECTION_UTILISATEUR).document(
                        preferenceManager.getString(Constante.CLE_ID_UTILISATEUR)
                );
        HashMap< String, Object> maj = new HashMap<>();
        maj.put(Constante.CLE_FCM_TOKEN, FieldValue.delete());
        documentReference.update(maj)
                .addOnSuccessListener(unused ->{
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), ConnexionActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->afficherToast("Impossile de se deconnecter"));
    }
}