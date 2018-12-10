package td.warp.com.warptd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener
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
    public int maxZombies;
    private int warpX, warpY;
    private boolean mapHasChanged = false;
    public String abilityCalled = "";
    public float gravX, gravY, gravZ;
    public boolean abilityPress = false;
    public Context context;
    public ProgressBar warpBar;
    public int bodies = 0;
    private SensorManager sensorManager;
    private boolean hammerTrigger;


    public GameView(Context context, int height, int width, SensorManager sensorManager) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);
        this.sensorManager = sensorManager;
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        gravX = event.values[0];
        gravY = event.values[1];
        gravZ = event.values[2];

//        System.out.println("SPOTCHECK: " + gravX + " : " + gravY);


        if (abilityCalled.equals("Warp Hammer") && abilityPress)
        {
            if (gravZ < 0) //Lift
            {
                hammerTrigger = true;
            }
            if (hammerTrigger && gravZ > 9.8 ) //and slam
            {
                int maxGravZ = (int)gravZ;
                hammerTrigger = false;

                Random rand = new Random();
                int rx = rand.nextInt(width/blockWidth)*blockWidth;
                int ry = rand.nextInt(height/blockHeight)*blockHeight;

                abilityList.add(new Ability(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.warp_hammer), 2*blockWidth, 2*blockHeight, true),
                        abilityCalled, rx, ry, (int)maxGravZ * 15, 3, this));

                abilityPress = false;
                thread.paused = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
        List<BuildingSprite> removalList = new ArrayList<>();
        for (BuildingSprite building : buildingList)
        {
            if (building.x/blockWidth + 1 == warpX + 1 || building.x/blockWidth - 1 == warpX - 1 ||
                building.y/blockHeight + 1 == warpY + 1 || building.y/blockHeight - 1 == warpY - 1)
            {
                removalList.add(building);
            }
        }
        buildingList.removeAll(removalList);

        warpX = rand.nextInt((this.width/blockWidth)-4)+2;
        warpY = rand.nextInt((this.height/blockHeight)-4)+2;
        warpEngine = new WarpEngineSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.warpengine), blockWidth, blockHeight, true),
                warpX*blockWidth, warpY*blockHeight);

        gameBoard[warpX][warpY] = 0;
    }

    public void generateCharacters()
    {

        gameBoard[warpX + 1][warpY] = 0;
        gameBoard[warpX - 1][warpY] = 0;
        gameBoard[warpX][warpY + 1] = 0;
        gameBoard[warpX][warpY - 1] = 0;

        characterList.add(new CharacterSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.character_1), blockWidth, blockHeight, true),
                (warpX + 1)*blockWidth, (warpY)*blockHeight,1, blockWidth, blockHeight));
        characterList.add(new CharacterSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.character_1), blockWidth, blockHeight, true),
                (warpX - 1)*blockWidth, (warpY)*blockHeight, 2, blockWidth, blockHeight));
        characterList.add(new CharacterSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.character_1), blockWidth, blockHeight, true),
                (warpX)*blockWidth, (warpY + 1)*blockHeight,3, blockWidth, blockHeight));
        characterList.add(new CharacterSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.character_1), blockWidth, blockHeight, true),
                (warpX)*blockWidth, (warpY - 1)*blockHeight, 4, blockWidth, blockHeight));
    }


    public void update() {

//
//        for (CharacterSprite character : characterList)
//        {
//            character.update(gameBoard,mapHasChanged, abilityList, warpX, warpY);
//        }

        if (abilityList.isEmpty())
        {
            for (ZombieSprite zombie : zombieList)
            {
                if (zombie.health < 0)
                {
                    zombie.image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.splatter_1),
                            blockWidth, blockHeight, true);
                }
                if (zombie.update(gameBoard, warpX, warpY, width/blockWidth, height/blockHeight, mapHasChanged) == -1)
                {
                    zombieList.remove(zombie);
                }
                generateZombies();
            }
        }
        else
        {
            for (Ability ability: abilityList)
            {
                ability.update();
                if (ability.lifetime > ability.ttl)
                {
                    abilityList.remove(ability);
                }
                else
                {

                    for (BuildingSprite building : buildingList)
                    {
                        if (ability.name.equals("Black Hole"))
                        {
                            if (building.x < ability.x + blockWidth*ability.range && building.x > ability.x - blockWidth*ability.range &&
                                    building.y < ability.y + blockHeight*ability.range && building.y > ability.y - blockHeight*ability.range)
                            {
                                Random rand = new Random();
                                int newX = rand.nextInt(ability.range*2) - ability.range + 1;
                                int newY = rand.nextInt(ability.range*2) - ability.range + 1;

                                gameBoard[building.x/blockWidth][building.y/blockHeight] = 0;
                                gameBoard[building.x/blockWidth + newX][building.y/blockHeight + newY] = 1;


                                building.x = (ability.x/blockWidth+newX)*blockWidth;
                                building.y = (ability.y/blockHeight+newY)*blockHeight;

                                mapHasChanged = true;
                            }
                        }

                        if (ability.name.equals("Gravity Shift"))
                        {
                            if (building.x < ability.x + blockWidth*ability.range && building.x > ability.x - blockWidth*ability.range &&
                                    building.y < ability.y + blockHeight*ability.range && building.y > ability.y - blockHeight*ability.range)
                            {


                                gameBoard[building.x/blockWidth][building.y/blockHeight] = 0;
                                gameBoard[building.x/blockWidth + (int)gravX/3][building.y/blockHeight + (int)gravY/3] = 1;

                                building.x = (building.x/blockWidth + (int)gravX/3) *blockWidth;
                                building.y = (building.y/blockHeight + (int)gravY/3) *blockHeight;

                                mapHasChanged = true;
                            }
                        }

                        if (ability.name.equals("Warp Hammer"))
                        {
                            if (building.x < ability.x + blockWidth*ability.range && building.x > ability.x - blockWidth*ability.range &&
                                    building.y < ability.y + blockHeight*ability.range && building.y > ability.y - blockHeight*ability.range)
                            {
                                buildingList.remove(building);
                                mapHasChanged = true;
                            }
                        }


                    }

                    for (ZombieSprite zombie : zombieList)
                    {
                        if (ability.name.equals("Stasis"))
                        {
                            if (zombie.x < ability.x + blockWidth*ability.range && zombie.x > ability.x - blockWidth*ability.range &&
                                    zombie.y < ability.y + blockHeight*ability.range && zombie.y > ability.y - blockHeight*ability.range)
                            {
                                zombie.x += (ability.x/blockWidth - zombie.x/blockWidth)*blockWidth/ability.ttl/30;
                                zombie.y += (ability.y/blockHeight - zombie.y/blockHeight)*blockHeight/ability.ttl/30;
                                zombie.health -= 1 *ability.strength;
                            }
                        }
                        if (ability.name.equals("Black Hole"))
                        {
                            if (zombie.x < ability.x + blockWidth*ability.range && zombie.x > ability.x - blockWidth*ability.range &&
                                    zombie.y < ability.y + blockHeight*ability.range && zombie.y > ability.y - blockHeight*ability.range)
                            {
                                Random rand = new Random();
                                int newX = rand.nextInt(ability.range*2) - ability.range + 1;
                                int newY = rand.nextInt(ability.range*2) - ability.range + 1;

                                gameBoard[zombie.x/blockWidth][zombie.y/blockHeight] = 0;
                                gameBoard[zombie.x/blockWidth + newX][zombie.y/blockHeight + newY] = 1;
                                zombie.x = (ability.x/blockWidth+newX)*blockWidth;
                                zombie.y = (ability.y/blockHeight+newY)*blockHeight;
                                zombie.health -= 50*ability.strength;
                                mapHasChanged = true;
                            }
                        }

                        if (ability.name.equals("Warp Hammer"))
                        {
                            if (zombie.x < ability.x + blockWidth*ability.range && zombie.x > ability.x - blockWidth*ability.range &&
                                    zombie.y < ability.y + blockHeight*ability.range && zombie.y > ability.y - blockHeight*ability.range)
                            {

                                zombie.health -= ability.strength;
                                mapHasChanged = true;
                            }
                        }

                        if (ability.name.equals("Gravity Shift"))
                        {
                            if (zombie.x < ability.x + blockWidth*ability.range && zombie.x > ability.x - blockWidth*ability.range &&
                                    zombie.y < ability.y + blockHeight*ability.range && zombie.y > ability.y - blockHeight*ability.range)
                            {

                                zombie.health -= ability.strength;
                                zombie.x = (zombie.x/blockWidth + (int)gravX/3) *blockWidth;
                                zombie.y = (zombie.y/blockHeight + (int)gravY/3) *blockHeight;

                                mapHasChanged = true;
                            }
                        }
                        if (ability.name.contains("Collect Bodies"))
                        {
                            if (zombie.x < ability.x + blockWidth*ability.range && zombie.x > ability.x - blockWidth*ability.range &&
                                    zombie.y < ability.y + blockHeight*ability.range && zombie.y > ability.y - blockHeight*ability.range)
                            {
                                if (zombie.health < 0)
                                {
                                    zombieList.remove(zombie);
                                    bodies++;
                                    generateZombies();
                                }
                            }
                        }
                        if (zombie.health < 0)
                        {
                            zombie.image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.splatter_1),
                                    blockWidth, blockHeight, true);
                        }
                        if (zombie.update(gameBoard, warpX, warpY, width/blockWidth, height/blockHeight, mapHasChanged) == -1)
                        {
                            zombieList.remove(zombie);
                        }
                        generateZombies();
                    }
                }
            }
        }

        mapHasChanged = false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN && abilityPress) {
            if (abilityCalled.equals("Black Hole"))
            {
                abilityList.add(new Ability(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.black_hole_ability), 2*blockWidth, 2*blockHeight, true),
                        abilityCalled, (int)evt.getX(), (int)evt.getY(), 2, 2, this));
            }
            else if (abilityCalled.equals("Stasis"))
            {
                abilityList.add(new Ability(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.stasis), 2*blockWidth, 2*blockHeight, true),
                        abilityCalled, (int)evt.getX(), (int)evt.getY(), 2, 2, this));
            }
            else if (abilityCalled.contains("Collect Bodies"))
            {
                abilityList.add(new Ability(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.collect_bodies), 2*blockWidth, 2*blockHeight, true),
                        abilityCalled, (int)evt.getX(), (int)evt.getY(), 2, 2,  this));
            }
            else if (abilityCalled.equals("Gravity Shift"))
            {
                abilityList.add(new Ability(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.stasis), 6*blockWidth, 6*blockHeight, true),
                        abilityCalled, (int)evt.getX(), (int)evt.getY(), 2, 6, this));
            }


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

        warpBar = findViewById(R.id.warpBar);

        maxZombies = 20;

        bg = generateBackground();
        gameBoard = generateStartingBoard();
        generateWarpEngine();
        generateZombies();
        generateCharacters();
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

            for (Ability ability : abilityList)
            {
                ability.draw(canvas);
            }

            for (ZombieSprite i : zombieList)
            {
                i.draw(canvas);
            }

            for (CharacterSprite character : characterList)
            {
                character.draw(canvas);
            }

            warpEngine.draw(canvas);
        }
    }

}
