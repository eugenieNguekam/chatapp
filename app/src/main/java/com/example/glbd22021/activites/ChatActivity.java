package com.example.glbd22021.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.glbd22021.R;
import com.example.glbd22021.databinding.ActivityChatBinding;
import com.example.glbd22021.modeles.Utilisateur;

import utils.Constante;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Utilisateur receveur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        chargerReceveurDetails();
    }

    private void chargerReceveurDetails(){
        receveur = (Utilisateur) getIntent().getSerializableExtra(Constante.CLE_COLLECTION_UTILISATEUR);
        binding.textNom.setText(receveur.nom);
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
}