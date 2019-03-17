package com.ksh.motsa.abc;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ksh.motsa.R;

public class SuccessActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private StoreFragment storeFragment;
    private FavoriteFragment favoriteFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_success);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        storeFragment = new StoreFragment();
        favoriteFragment = new FavoriteFragment();
        profileFragment = new ProfileFragment();


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //Intent homeIntent = new Intent(SuccessActivity.this, )
                        return true;
                    case R.id.nav_category:
                        setFrament(categoryFragment);
                        return true;
                    case R.id.nav_store:
                        setFrament(storeFragment);
                        return true;
                    case R.id.nav_favorite:
                        setFrament(favoriteFragment);
                        return true;
                    case R.id.nav_profile:
                        setFrament(profileFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
//        Button logout = findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private void setFrament(Fragment frament) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.main_frame, frament);
        fragmentTransaction.commit();
    }


}
