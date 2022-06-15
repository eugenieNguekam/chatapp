package com.example.glbd22021.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.glbd22021.adapteur.AdaptateurUtilisateurs;
import com.example.glbd22021.databinding.ActivityUtilisateurBinding;
import com.example.glbd22021.listeners.UtilisateurListener;
import com.example.glbd22021.modeles.Utilisateur;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import utils.Constante;
import utils.PreferenceManager;

public class UtilisateurActivity extends AppCompatActivity implements UtilisateurListener {
    private ActivityUtilisateurBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityUtilisateurBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setlisteners();
        recupUtilisateurs();
    }

    private void setlisteners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void recupUtilisateurs(){
        chargement(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constante.CLE_COLLECTION_UTILISATEUR)
        .get()
        .addOnCompleteListener(task -> {
            chargement(false);
            String currentUserId = preferenceManager.getString(Constante.CLE_ID_UTILISATEUR);
            if(task.isSuccessful() && task.getResult() != null){
                List<Utilisateur> utilisateurs = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                    if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.nom = queryDocumentSnapshot.getString(Constante.CLE_NOM);
                    utilisateur.email = queryDocumentSnapshot.getString(Constante.CLE_EMAIL);
                    utilisateur.image = queryDocumentSnapshot.getString(Constante.CLE_IMAGE);
                    utilisateur.token = queryDocumentSnapshot.getString(Constante.CLE_FCM_TOKEN);
                    utilisateurs.add(utilisateur);
                }
                if(utilisateurs.size()> 0){
                    AdaptateurUtilisateurs adaptateurUtilisateurs = new AdaptateurUtilisateurs(utilisateurs, this);
                    binding.utilisateurRecycleVue.setAdapter(adaptateurUtilisateurs);
                    binding.utilisateurRecycleVue.setVisibility(View.VISIBLE);
                }else{
                    afficherErreur();
                }
            }else{
                afficherErreur();
            }
        });

    }

    private void afficherErreur(){
        binding.textErrorMessage.setText(String.format("%S","Aucun utilisteur"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void chargement(Boolean charge){
        if(charge){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onUserClicked(Utilisateur utilisateur){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constante.CLE_COLLECTION_UTILISATEUR, utilisateur);
        startActivity(intent);
        finish();
    }
}
