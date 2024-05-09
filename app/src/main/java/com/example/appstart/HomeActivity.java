package com.example.appstart;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appstart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail;
    private FirebaseFirestore mFirestore;
    private Button buttonLogout;
    private Button buttonRedirect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
// Find the existing button by its ID
        Button buttonRedirect = findViewById(R.id.buttonRedirect);

        // Set an OnClickListener for the button
        buttonRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the other activity
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonLogout = findViewById(R.id.buttonLogout);
        mFirestore = FirebaseFirestore.getInstance();

// Set onClickListener for logout button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Logout the user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login screen
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finish current activity
            }
        });



        // Get current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Get user's document from Firestore
            mFirestore.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String username = document.getString("username");
                                String email = document.getString("email");

                                textViewUsername.setText("Username: " + username);
                                textViewEmail.setText("Email: " + email);
                            } else {
                                // Handle the case where the document does not exist
                                // This could happen if the user's data has not been properly initialized
                                textViewUsername.setText("Username: N/A");
                                textViewEmail.setText("Email: N/A");
                            }
                        } else {
                            // Handle the error
                            // This could happen due to network issues or permission errors
                            textViewUsername.setText("Error retrieving data");
                            textViewEmail.setText("Error retrieving data");
                        }
                    });
        } else {
            // Handle the case where no user is signed in
            // You can redirect the user to the login screen or perform other actions
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
