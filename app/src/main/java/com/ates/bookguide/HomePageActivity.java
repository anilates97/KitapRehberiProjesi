package com.ates.bookguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView name, email;
    ImageView profileImage;
    CardView getBooks,addBooks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        getBooks = findViewById(R.id.getBooks);
        addBooks = findViewById(R.id.addBooks);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this,GetAllBooksActivity.class);
                intent.putExtra("name",String.valueOf(user.getDisplayName()));
                intent.putExtra("email",String.valueOf(user.getEmail()));
                intent.putExtra("photo",String.valueOf(user.getPhotoUrl()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        addBooks.setOnClickListener(this);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Intent dataIntent = getIntent();
        String nameDw = dataIntent.getStringExtra("name");
        String emailDw = dataIntent.getStringExtra("email");
        String photoDw = dataIntent.getStringExtra("photo");

        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Subscribe Suceessful", "Successful");
                if(nameDw != null || emailDw != null || photoDw != null)
                {
                    name.setText(nameDw);
                    email.setText(emailDw);
                    Picasso.get().load(photoDw).into(profileImage);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Onfailure", "ONFAILED");
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//       getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle.isDrawerIndicatorEnabled();

        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        profileImage = headerView.findViewById(R.id.profileImage);





        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
           name.setText(account.getDisplayName());
           email.setText(account.getEmail());
           Picasso.get().load(account.getPhotoUrl()).transform(new CircleTransform()).into(profileImage);

        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homee : {
                        Log.i("HOME", "HOME IS CLICKED");
                        drawerLayout.closeDrawers();
                        break;
                    }


                    case R.id.log_out : {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;

                    }
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addBooks){
            Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.getBooks) {
            Intent intent = new Intent(getApplicationContext(), GetAllBooksActivity.class);
            startActivity(intent);
        }

    }
}
