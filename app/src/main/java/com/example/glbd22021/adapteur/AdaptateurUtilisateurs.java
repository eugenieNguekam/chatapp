package com.example.glbd22021.adapteur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glbd22021.databinding.ContaineurUtilisateurBinding;
import com.example.glbd22021.listeners.UtilisateurListener;
import com.example.glbd22021.modeles.Utilisateur;


import java.util.List;

public class AdaptateurUtilisateurs extends RecyclerView.Adapter<AdaptateurUtilisateurs.VueUtilisateur> {
    private final List<Utilisateur> utilisateurs;
    private final UtilisateurListener utilisateurListener;

    public AdaptateurUtilisateurs(List<Utilisateur> utilisateurs, UtilisateurListener utilisateurListener){
        this.utilisateurs = utilisateurs;
        this.utilisateurListener = utilisateurListener;
    }

    @NonNull
    @Override
    public VueUtilisateur onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ContaineurUtilisateurBinding containeurUtilisateurBinding = ContaineurUtilisateurBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VueUtilisateur(containeurUtilisateurBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VueUtilisateur holder, int position) {
        holder.donneeUtilisateur(utilisateurs.get(position));
    }


    @Override
    public int getItemCount(){
        return utilisateurs.size();
    }

class VueUtilisateur extends RecyclerView.ViewHolder{
    ContaineurUtilisateurBinding binding;
    VueUtilisateur(ContaineurUtilisateurBinding containeurUtilisateurBinding){
        super(containeurUtilisateurBinding.getRoot());
        binding = containeurUtilisateurBinding;
    }

   void donneeUtilisateur(Utilisateur utilisateur){
        binding.textNom.setText(utilisateur.nom);
        binding.textEmail.setText(utilisateur.email);
        binding.imageProfile.setImageBitmap(recupImageUtilisateur(utilisateur.image));
        binding.getRoot().setOnClickListener(v -> utilisateurListener.onUserClicked(utilisateur));
    }

}

    private Bitmap recupImageUtilisateur(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}