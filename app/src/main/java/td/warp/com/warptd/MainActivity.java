package td.warp.com.warptd;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FireLog";
    public Button gameButton;
    public Button signOutButton;
    public TextView headerText;
    private FirebaseAuth mAuth;
    private RecyclerView listStats;
    private RecyclerView listAchievements;
    private FirebaseFirestore mFirestore;

    private UserInfo mUser;
    private Tower mTower;
    private List<UserInfo> userInfo;
    private List<Achievement> achievements;

    private UserStatsAdapter userStatsAdapter;
    private AchievementsAdapter achievementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        /* Adding Firestore and recycler view stuff */
        userInfo = new ArrayList<>();
        achievements = new ArrayList<>();
        userStatsAdapter = new UserStatsAdapter(userInfo);
        achievementsAdapter = new AchievementsAdapter(achievements);

        listStats = (RecyclerView) findViewById(R.id.listStats);
        listStats.setHasFixedSize(true);
        listStats.setLayoutManager(new LinearLayoutManager(this));
        listStats.setAdapter(userStatsAdapter);

        listAchievements = (RecyclerView) findViewById(R.id.listAchievements);
        listAchievements.setHasFixedSize(true);
        listAchievements.setLayoutManager(new LinearLayoutManager(this));
        listAchievements.setAdapter(achievementsAdapter);



        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("users").document(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    Log.d(TAG, "Error: Failed to load user data.");
                }

//
                String email = documentSnapshot.getString("email");
                int kills = Integer.parseInt(documentSnapshot.getString( "kills"));
                int biomass = Integer.parseInt(documentSnapshot.getString("biomass"));
                int warps = Integer.parseInt(documentSnapshot.getString("warps"));
                int strength = Integer.parseInt(documentSnapshot.getString("strength"));
                int range = Integer.parseInt(documentSnapshot.getString("range"));
                int wins = Integer.parseInt(documentSnapshot.getString("wins"));
                int gamesPlayed = Integer.parseInt(documentSnapshot.getString("gamesPlayed"));


                mTower = new Tower(range, strength);
                mUser = new UserInfo(email,biomass,kills,warps,wins,gamesPlayed);
                userInfo.add(mUser);

                populateAchievements();

                userStatsAdapter.notifyDataSetChanged();
                achievementsAdapter.notifyDataSetChanged();




            }
        });

        /* End adding Firestore */

        gameButton = findViewById(R.id.gameButton);
        headerText = findViewById(R.id.txtWelcome);
        signOutButton = findViewById(R.id.btnSignOut);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            if (name != null)
                headerText.setText("Welcome " + name);
            else
                headerText.setText("Welcome " + email);


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }


        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                finish();

            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent game = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(game);

            }
        });
    }

    private void populateAchievements(){
        UserInfo user = userInfo.get(0);

        int kills = user.getKills();
        int wins = user.getWins();
        int gamesPlayed = user.getGamesPlayed();


        for(int i = 0; i < kills; i++){
            int level = i / 100;

            if (i % 100 == 0){
                String name = "Zombie Slayer " + level;
                String description = "Killed " + i + " zombies";
                Achievement a = new Achievement(name, description);
                achievements.add(a);
            }


        }
    }
}
