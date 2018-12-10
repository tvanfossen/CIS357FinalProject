package td.warp.com.warptd;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FireLog";
    public Button gameButton;
    public Button signOutButton;
    public TextView headerText;
    private TextView txtDMGCost;
    private TextView txtRangeCost;
    private TextView txtDMG;
    private TextView txtRange;
    private Button btnRange;
    private Button btnDMG;
    private FirebaseAuth mAuth;
    private RecyclerView listStats;
    private RecyclerView listAchievements;
    private FirebaseFirestore mFirestore;
    int mBioMass = 0;

    private int rangeCost = 0;
    private int dmgCost = 0;

    private UserInfo mUser;
    private Tower mTower;
    private List<UserInfo> userInfo;
    private List<Achievement> achievements;

    private UserStatsAdapter userStatsAdapter;
    private AchievementsAdapter achievementsAdapter;

    private int RESULT_WIN = 2;
    private int GAME_CODE = 1;
    private int RESULT_LOSS = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        txtDMG = (TextView) findViewById(R.id.txtStrength);
        txtDMGCost = (TextView) findViewById(R.id.txtDMGCost);
        txtRange = (TextView) findViewById(R.id.txtRange);
        txtRangeCost = (TextView) findViewById(R.id.txtRangeCost);
        btnRange = (Button) findViewById(R.id.btnRange);
        btnDMG = (Button) findViewById(R.id.btnDMG);


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

        btnDMG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserInfo user = userInfo.get(0);
                if(mBioMass > dmgCost) {
                    user.setBiomass(mBioMass - dmgCost);
                    mTower.strength++;
                }
                updateUserInfo();
            }
        });


        btnRange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserInfo user = userInfo.get(0);
                if(mBioMass > rangeCost) {
                    user.setBiomass(mBioMass - rangeCost);
                    mTower.range++;
                }
                updateUserInfo();
            }
        });



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

                mBioMass = biomass;

                mTower = new Tower(range, strength);
                mUser = new UserInfo(email,biomass,kills,warps,wins,gamesPlayed);
                userInfo.add(mUser);

                populateAchievements();
                populateUpgrades();

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
            if (name != "" && name!= null)
                headerText.setText("Welcome " + name);
            else if (email != null)
                headerText.setText("Welcome " + email);
            else
                headerText.setText(("Welcome!"));


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
                startActivityForResult(game, GAME_CODE);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GAME_CODE) {
            if (resultCode == RESULT_WIN) {
                int biomass = 0;
                int bodies = 0;
                data.getIntExtra("biomass", biomass);
                data.getIntExtra("bodies", bodies);

            }
            else if(resultCode == RESULT_LOSS)
            {

            }
        }
    }


    private void purchaseDamage() {
        UserInfo user = userInfo.get(0);
        if(mBioMass > dmgCost) {
            user.setBiomass(mBioMass - dmgCost);
            mTower.strength++;
        }
//        updateUserInfo();
        return;
    }

    private void populateAchievements(){
        UserInfo user = userInfo.get(0);
        achievements.clear();

        int kills = user.getKills();
        int wins = user.getWins();
        int gamesPlayed = user.getGamesPlayed();


        for(int i = 0; i < kills; i++){
            int level = i / 100;

            if (i % 100 == 0 && i > 0){
                String name = "Zombie Slayer " + level;
                String description = "Killed " + i + " zombies";
                Achievement a = new Achievement(name, description);
                achievements.add(a);
            }


        }
    }

    private void populateUpgrades() {
        rangeCost = mTower.range * 5;
        dmgCost = mTower.strength * 5;


        txtDMG.setText("DMG: " + mTower.strength);
        txtRange.setText("Range: " + mTower.range);

        txtDMGCost.setText("Cost: " + dmgCost);
        txtRangeCost.setText("Cost: " + rangeCost);
    }

    private void updateUserInfo(){
        UserInfo user = userInfo.get(0);
        DocumentReference userRef = mFirestore.collection("users").document(mAuth.getUid());

        // updating biomass
        userRef
                .update("biomass", Integer.toString(user.getBiomass()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        userRef
                .update("strength", Integer.toString(mTower.strength))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        userRef
                .update("range", Integer.toString(mTower.range))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        //updating games played
//        userRef
//                .update("gamesPlayed", "" + user.getGamesPlayed())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully updated!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error updating document", e);
//                    }
//                });
//
//        //updating kills
//        userRef
//                .update("kills", "" + user.getKills())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully updated!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error updating document", e);
//                    }
//                });
    }
}
