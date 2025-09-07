package com.example.lacdpapel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lacdpapel.Fragments.FavoriteActorsFRAG;
import com.example.lacdpapel.R;
import com.example.lacdpapel.Fragments.glavnayaFRAG;
import com.example.lacdpapel.Fragments.storeFRAG;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class glavnaya extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glavnaya_activity);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        bottomNav = findViewById(R.id.nav_view);

        bottomNav.setSelectedItemId(R.id.navigotion_home);
        if (bottomNav.getSelectedItemId() == R.id.navigotion_home) setFragment(new glavnayaFRAG());



        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_item_third)
                {
                    FirebaseAuth.getInstance().signOut();

                    saveLoginStatus(false);

                    Intent intent = new Intent(glavnaya.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if(id == R.id.menu_item_second)
                {
                    Intent intent = new Intent(glavnaya.this, LookLater.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.menu_item_first)
                {
                    Intent intent = new Intent(glavnaya.this, AllOrders.class);
                    intent.putExtra("USER_ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    startActivity(intent);
                    finish();
                }
                return true;

            }
        });



        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigotion_home) setFragment(new glavnayaFRAG());
                else if (id == R.id.navigation_store) setFragment(new storeFRAG());
                else if (id == R.id.navigation_my) setFragment(new FavoriteActorsFRAG());
                return true;
            }
        });
    }
    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment, null)
                .commit();
    }
    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

}
