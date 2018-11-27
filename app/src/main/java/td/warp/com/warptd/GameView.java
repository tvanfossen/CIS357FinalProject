package td.warp.com.warptd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private int height, width;
    private Bitmap bg;
    private int gameBoard [][];
    private List<BuildingSprite> buildingList;
    private List<CharacterSprite> characterList;
    private List<ZombieSprite> zombieList;
    private WarpEngineSprite warpEngine;
    private int blockHeight, blockWidth;
    private int maxZombies;
    private int warpX, warpY;
    private boolean mapHasChanged = false;


    public GameView(Context context, int height, int width) {
        super(context);

        getHolder().addCallback(this);

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
        // ADD IN ACCELEROMTER CODER
        // PASS UPDATES TO BUILDING BASED ON ACCELEROMTER READ IF WARP IS ENABLED
        //
        //

        for (ZombieSprite zombie : zombieList)
        {
            if (zombie.update(gameBoard, warpX, warpY, width/blockWidth, height/blockHeight, mapHasChanged) == -1)
            {
                zombieList.remove(zombie);
            }
        }
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

        maxZombies = 150;

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
