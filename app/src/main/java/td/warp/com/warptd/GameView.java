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
    private int blockHeight, blockWidth;

    public GameView(Context context, int height, int width) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        this.width = width;
        this.height = height;


        Bitmap yourBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_1);
        Bitmap resized = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_1), 50, 50, true);

        blockHeight = resized.getHeight();
        blockWidth = resized.getWidth();

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
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_1), 50, 50, true),
                            i*blockWidth, j*blockHeight, null);

                }
                else if (val == 1)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_2), 50, 50, true),
                            i*blockWidth, j*blockHeight, null);
                }
                else if (val == 2)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_3), 50, 50, true),
                            i*blockWidth, j*blockHeight, null);
                }
                else if (val == 3)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_4), 50, 50, true),
                            i*blockWidth, j*blockHeight, null);
                }
                else if (val == 4)
                {
                    temp.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ground_tile_5), 50, 50, true),
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

                if (val == 0)
                {
                    tempBoard[i][j] = 1;


                    Random rand2 = new Random();
                    int val2 = rand.nextInt(3);

                    if (val2 == 0)
                    {
                        buildingList.add(new BuildingSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.building_1), 50, 50, true),
                                i*blockWidth, j*blockHeight));
                    }
                    else if (val2 == 1)
                    {
                        buildingList.add(new BuildingSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.building_2), 50, 50, true),
                                i*blockWidth, j*blockHeight));
                    }
                    else if (val2 == 2)
                    {
                        buildingList.add(new BuildingSprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.building_3), 50, 50, true),
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



    public void update() {
        // ADD IN ACCELEROMTER CODER
        // PASS UPDATES TO BUILDING BASED ON ACCELEROMTER READ IF WARP IS ENABLED
        //
        //
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();

        buildingList = new ArrayList<BuildingSprite>();

        bg = generateBackground();
        gameBoard = generateStartingBoard();
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
        }
    }

}
