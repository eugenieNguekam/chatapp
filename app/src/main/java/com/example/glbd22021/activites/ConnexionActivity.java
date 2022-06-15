package com.example.glbd22021.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.glbd22021.databinding.ActivityConnexionBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import utils.Constante;
import utils.PreferenceManager;

public class ConnexionActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private ActivityConnexionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }


    private void setListeners() {

        binding.textRegister.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        binding.buttonConnexion.setOnClickListener(v -> {
            if(verifierDetails()){
                connexion();
            }
        });
    }

    private void connexion(){
        chargement(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constante.CLE_COLLECTION_UTILISATEUR)
                .whereEqualTo(Constante.CLE_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constante.CLE_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task ->{
                   if (task.isSuccessful() && task.getResult() != null
                        && task.getResult().getDocuments().size() > 0){
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                       preferenceManager.putBoolean(Constante.CLE_EST_CONNECTE, true);
                       preferenceManager.putString(Constante.CLE_ID_UTILISATEUR, documentSnapshot.getId());
                       preferenceManager.putString(Constante.CLE_NOM, documentSnapshot.getString(Constante.CLE_NOM));
                       preferenceManager.putString(Constante.CLE_IMAGE, documentSnapshot.getString(Constante.CLE_IMAGE));
                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);
                   }else{
                       chargement(false);
                       afficherToast("Email et/ou Mot de passe incorrecte(s)");
                    }

                });
        }

        private void chargement(Boolean charge){
            if(charge){
                binding.buttonConnexion.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
            }else{
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.buttonConnexion.setVisibility(View.VISIBLE);
            }
    }

    private void afficherToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean verifierDetails(){
        if(binding.inputEmail.getText().toString().trim().isEmpty()){
            afficherToast("Entrer l'Email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            afficherToast("Entrer une adresse valide");
            return false;
        }else  if(binding.inputPassword.getText().toString().trim().isEmpty()){
            afficherToast("Entrer le mot de passe");
            return false;
        }else{
            return true;
        }
    }


}