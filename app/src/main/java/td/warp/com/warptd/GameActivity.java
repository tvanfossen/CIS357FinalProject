package td.warp.com.warptd;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.LayoutInflater;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

public class GameActivity extends Activity {

    private Button ability1;
    private Button ability2;
    private Button ability3;
    private TabLayout tabLayout;
    private Context context;
    public ProgressBar warpBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        setContentView(R.layout.activity_game);

        LinearLayout layout = (LinearLayout)findViewById(R.id.gamelayout);

        final GameView gv = new GameView(this, height, width, (SensorManager) getSystemService(Context.SENSOR_SERVICE));

        layout.addView(gv);

        tabLayout = findViewById(R.id.tabs);
        ability1 = findViewById(R.id.ability1);
        ability2 = findViewById(R.id.ability2);
        ability3 = findViewById(R.id.ability3);
        warpBar = findViewById(R.id.warpBar);
        warpBar.setProgress(0);


        ability1.setText("Collect Bodies 1");
        ability2.setText("Return 1");
        ability3.setText("Black Hole");
        // Add cooldowns to abilities
        ability1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context, "Touch map to begin " + ability1.getText().toString() , Toast.LENGTH_SHORT).show();

                gv.thread.paused = true;
                gv.abilityPress = true;
                gv.abilityCalled = ability1.getText().toString();

                warpBar.setProgress(gv.bodies);

            }
        });

        ability2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context, "Touch map to begin " + ability2.getText().toString() , Toast.LENGTH_SHORT).show();

                gv.thread.paused = true;
                gv.abilityPress = true;
                gv.abilityCalled = ability2.getText().toString();
            }
        });

        ability3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context, "Touch map to begin " + ability3.getText().toString() , Toast.LENGTH_SHORT).show();

                gv.thread.paused = true;
                gv.abilityPress = true;
                gv.abilityCalled = ability3.getText().toString();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0)
                {
                    ability1.setText("Collect Bodies 1");
                    ability2.setText("Return 1");
                    ability3.setText("Black Hole");
                    // Add cooldowns to abilities
                    ability1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability1.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability1.getText().toString();

                            warpBar.setProgress(gv.bodies);

                        }
                    });

                    ability2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability2.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability2.getText().toString();
                        }
                    });

                    ability3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability3.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability3.getText().toString();
                        }
                    });
                }
                else if (position == 1)
                {
                    ability1.setText("Collect Bodies 2");
                    ability2.setText("Return 2");
                    ability3.setText("Warp Hammer");
                    // Add cooldowns to abilities
                    ability1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability1.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability1.getText().toString();

                            warpBar.setProgress(gv.bodies);

                        }
                    });

                    ability2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability2.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability2.getText().toString();
                        }
                    });

                    ability3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability3.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability3.getText().toString();
                        }
                    });
                }
                else if (position == 2)
                {
                    ability1.setText("Collect Bodies 3");
                    ability2.setText("Return 3");
                    ability3.setText("Gravity Shift");
                    // Add cooldowns to abilities
                    ability1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability1.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability1.getText().toString();

                            warpBar.setProgress(gv.bodies);

                        }
                    });

                    ability2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability2.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability2.getText().toString();
                        }
                    });

                    ability3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability3.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability3.getText().toString();
                        }
                    });
                }
                else if (position == 3)
                {
                    ability1.setText("Collect Bodies 4");
                    ability2.setText("Return 4");
                    ability3.setText("Stasis");
                    // Add cooldowns to abilities
                    ability1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability1.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability1.getText().toString();

                            warpBar.setProgress(gv.bodies);

                        }
                    });

                    ability2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability2.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability2.getText().toString();
                        }
                    });

                    ability3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability3.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability3.getText().toString();
                        }
                    });
                }
                else if (position == 4)
                {
                    ability1.setText("Warp Hammer");
                    ability2.setText("Black Hole");
                    ability3.setText("Gravity Shift");
                    // Add cooldowns to abilities
                    ability1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Lift phone, and then hammer down to start " + ability1.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability1.getText().toString();
                        }
                    });

                    ability2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability2.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability2.getText().toString();
                        }
                    });

                    ability3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context, "Touch map to begin " + ability3.getText().toString() , Toast.LENGTH_SHORT).show();

                            gv.thread.paused = true;
                            gv.abilityPress = true;
                            gv.abilityCalled = ability3.getText().toString();
                        }
                    });
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ability1.setText("Ability 1");
                ability2.setText("Ability 2");
                ability3.setText("Ability 3");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }
}
