package com.example.glbd22021.activites;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.glbd22021.databinding.ActivityRegisterBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import utils.Constante;
import utils.PreferenceManager;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private PreferenceManager preferenceManager;
    private String encodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners() {
        binding.textConnexion.setOnClickListener(v ->onBackPressed());
        binding.buttonRegister.setOnClickListener(v ->{
            if(verificationDonnees()){
                enregistrement();
            }
        });
        binding.layoutImage.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void afficherToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void enregistrement() {
        chargement(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> utilisateur = new HashMap<>();
        utilisateur.put(Constante.CLE_NOM, binding.inputName.getText().toString());
        utilisateur.put(Constante.CLE_EMAIL, binding.inputEmail.getText().toString());
        utilisateur.put(Constante.CLE_PASSWORD, binding.inputPassword.getText().toString());
        utilisateur.put(Constante.CLE_IMAGE,encodeImage);
        database.collection(Constante.CLE_COLLECTION_UTILISATEUR)
                .add(utilisateur)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constante.CLE_EST_CONNECTE, true);
                    preferenceManager.putString(Constante.CLE_ID_UTILISATEUR, documentReference.getId());
                    preferenceManager.putString(Constante.CLE_NOM, binding.inputName.getText().toString());
                    preferenceManager.putString(Constante.CLE_IMAGE, encodeImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    chargement(false);
                    afficherToast(exception.getMessage());
                });
    }
        private String encodeImage(Bitmap bitmap){
            int previewWidth = 150;
            int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
            Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }


    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                       Uri imageUri = result.getData().getData();
                       try{
                           InputStream inputStream = getContentResolver().openInputStream(imageUri);
                           Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                           binding.imageProfile.setImageBitmap(bitmap);
                           binding.textAddImage.setVisibility(View.GONE);
                           encodeImage= encodeImage(bitmap);

                       }catch (FileNotFoundException e){
                           e.printStackTrace();
                       }
                    }
                }
            }
    );

    private Boolean verificationDonnees(){
        if (encodeImage == null){
            afficherToast("Selectionnez une photo de profil");
            return false;
        }else if (binding.inputName.getText().toString().trim().isEmpty()) {
            afficherToast("Entrez votre nom");
            return false;
        }else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            afficherToast("Entrez votre email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            afficherToast("Entrez un email valide");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            afficherToast("Entrez votre mot de passe");
            return false;
        }else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            afficherToast("Confirmez votre mot de passe");
            return false;
        }else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            afficherToast("Le mot de passe et Confirmer le mot de passe doivent etre identique");
            return false;
        }else{
            return true;
        }
    }

        private void chargement(Boolean charge){
        if (charge){
            binding.buttonRegister.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonRegister.setVisibility(View.VISIBLE);
        }
        }


}