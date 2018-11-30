package td.warp.com.warptd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.TabLayout;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    public MainThread thread;
    private int height, width;
    private Bitmap bg;
    private int gameBoard [][];
    private List<BuildingSprite> buildingList;
    private List<CharacterSprite> characterList;
    private List<ZombieSprite> zombieList;
    public List<Ability> abilityList;
    private WarpEngineSprite warpEngine;
    private int blockHeight, blockWidth;
    private int maxZombies;
    private int warpX, warpY;
    private boolean mapHasChanged = false;
    public String abilityCalled;
    public float gravX, gravY, gravZ;
    public boolean abilityPress = false;
    public Context context;


    public GameView(Context context, int height, int width, SensorManager sensorManager) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                gravX = event.values[0];
                gravY = event.values[1];
                gravZ = event.values[2];

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }


    public Bitmap generateBackground()
    { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bg);
        for (int i = 0; i < width/blockWidth; i++)
        {
            for (int j = 0; j < height/blockHeight; j++)
            {
                Random rand = new Random();
                int val = rand.nextInt(5);

                if (val == 0)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_1), blockWidth, blockHeight, true),
                            i*blockWidth, j*blockHeight, null);

                }
                else if (val == 1)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_2), blockWidth, blockHeight, true),
                            i*blockWidth, j*blockHeight, null);
                }
                else if (val == 2)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_3), blockWidth, blockHeight, true),
                            i*blockWidth, j*blockHeight, null);
                }
                else if (val == 3)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_4), blockWidth, blockHeight, true),
                            i*blockWidth, j*blockHeight, null);
                }
                else if (val == 4)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_5), blockWidth, blockHeight, true),
                            i*blockWidth, j*blockHeight, null);
                }

            }
        }

        return bg;

    }

    public int[][] generateStartingBoard()
    {
        int tempBoard[][] = new int[width/blockWidth][height/blockHeight];
        Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bg);

        for (int i = 0; i < width/blockWidth; i++)
        {
            for (int j = 0; j < height/blockHeight; j++)
            {
                Random rand = new Random();
                int val = rand.nextInt(10);

                if (val == 0 && tempBoard[i][j] == 0)
                {
                    tempBoard[i][j] = 1;


                    Random rand2 = new Random();
                    int val2 = rand.nextInt(3);

                    if (val2 == 0)
                    {
                        buildingList.add(new BuildingSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.building_2), blockWidth, blockHeight, true),
                                i*blockWidth, j*blockHeight));
                    }
                    else if (val2 == 1)
                    {
                        buildingList.add(new BuildingSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.building_2), blockWidth, blockHeight, true),
                                i*blockWidth, j*blockHeight));
                    }
                    else if (val2 == 2)
                    {
                        buildingList.add(new BuildingSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.building_3), blockWidth, blockHeight, true),
                                i*blockWidth, j*blockHeight));
                    }
                }
                else
                {
                    tempBoard[i][j] = 0;
                }


            }
        }



        return tempBoard;
    }

    public void generateZombies()
    {
        Random rand = new Random();

        while (zombieList.size() < maxZombies)
        {
            int x = rand.nextInt(this.width/blockWidth);
            int y = rand.nextInt(this.height/blockHeight);


            if (gameBoard[x][y] == 0) {
                zombieList.add(new ZombieSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.zombie_1),
                        blockWidth, blockHeight, true), x*blockWidth, y*blockHeight, blockWidth, blockHeight, gameBoard, height, width, warpX, warpY));
                gameBoard[x][y] = 1;
            }
        }
    }

    public void generateWarpEngine()
    {
        Random rand = new Random();

        warpX = rand.nextInt(this.width/blockWidth);
        warpY = rand.nextInt(this.height/blockHeight);
        warpEngine = new WarpEngineSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.warpengine), blockWidth, blockHeight, true),
                warpX*blockWidth, warpY*blockHeight);

        gameBoard[warpX][warpY] = 0;
    }


    public void update() {
        for (ZombieSprite zombie : zombieList)
        {
            if (zombie.health < 0)
            {
                zombie.image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.splatter_1),
                        blockWidth, blockHeight, true);
                continue;
            }
            if (zombie.update(gameBoard, warpX, warpY, width/blockWidth, height/blockHeight, mapHasChanged) == -1)
            {
                zombieList.remove(zombie);
                continue;
            }
            generateZombies();

        }

        System.out.println("SPOTCHECK: " + gravX + " : " + gravY);

        for (Ability ability: abilityList)
        {
            ability.update();
            if (ability.lifetime > ability.ttl)
            {
                abilityList.remove(ability);
                continue;
            }
            else
            {

                for (ZombieSprite zombie : zombieList)
                {
                    if (ability.name.equals("Stasis"))
                    {
                        if (zombie.x < ability.x + blockWidth*ability.strength && zombie.x > ability.x - blockWidth*ability.strength &&
                                zombie.y < ability.y + blockHeight*ability.strength && zombie.y > ability.y - blockHeight*ability.strength)
                        {
                            zombie.x += (ability.x/blockWidth - zombie.x/blockWidth)*blockWidth/ability.ttl/30;
                            zombie.y += (ability.y/blockHeight - zombie.y/blockHeight)*blockHeight/ability.ttl/30;
                            zombie.update(gameBoard, warpX, warpY, width/blockWidth, height/blockHeight, true);
                            zombie.health -= 1;
                        }
                    }
                    if (ability.name.equals("Black Hole"))
                    {
                        if (zombie.x < ability.x + blockWidth*ability.strength && zombie.x > ability.x - blockWidth*ability.strength &&
                                zombie.y < ability.y + blockHeight*ability.strength && zombie.y > ability.y - blockHeight*ability.strength)
                        {
                            zombie.x += (ability.x/blockWidth - zombie.x/blockWidth)*blockWidth;
                            zombie.y += (ability.y/blockHeight - zombie.y/blockHeight)*blockHeight;
                            zombie.update(gameBoard, warpX, warpY, width/blockWidth, height/blockHeight, true);
                            zombie.health -= 1;
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN && abilityPress) {
            System.out.println("SPOTCHECK: " + evt.getX());
            abilityList.add(new Ability(abilityCalled, (int)evt.getX(), (int)evt.getY(), 3, this));
            abilityPress = false;
            thread.paused = false;
            return true;
        }

        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();

        this.width = getWidth();
        this.height = getHeight()-400;

        blockHeight = this.width/20;
        blockWidth = this.width/20;

        buildingList = new ArrayList<BuildingSprite>();
        characterList = new ArrayList<CharacterSprite>();
        zombieList = new ArrayList<ZombieSprite>();
        abilityList = new ArrayList<Ability>();

        maxZombies = 50;

        bg = generateBackground();
        gameBoard = generateStartingBoard();
        generateWarpEngine();
        generateZombies();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawBitmap(bg,0,0,null);

            for (BuildingSprite i : buildingList)
            {
                i.draw(canvas);
            }

            for (ZombieSprite i : zombieList)
            {
                i.draw(canvas);
            }

            warpEngine.draw(canvas);
        }
    }

}
