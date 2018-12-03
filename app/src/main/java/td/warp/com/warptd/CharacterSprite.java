package td.warp.com.warptd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class CharacterSprite{

    private Bitmap image;
    private int x, y;
    private int blockWidth, blockHeight;
    private int weightedBoard[][];
    private Queue<Pair<String, Integer>> pathQueue;
    private Pair<String, Integer> nextMove;
    private int updateCount;
    private int speed;
    public int health;
    private int width, height;
    private int place;

    public CharacterSprite(Bitmap bmp, int x, int y, int place, int blockWidth, int blockHeight) {

        image = bmp;

        this.x = x;
        this.y = y;
        this.place = place;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        speed = 30;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update(int board[][], boolean mapHasChanged, List<Ability> abilityList, int warpX, int warpY)
    {

    }

    private void pathfinder(int startX, int startY, int targetX, int targetY, int board[][], int mapWidth, int mapHeight)
    {
        updateCount = 0;
        weightedBoard = new int[mapWidth][mapHeight];

        for (int i = 0; i < mapWidth; i++)
        {
            for (int j = 0; j < mapHeight; j++)
            {
                weightedBoard[i][j] = -1;
            }
        }
        weightedBoard[startX][startY] = 0;

        dijkstra(startX, startY, board, mapWidth, mapHeight, 0);


        calculatePath(targetX, targetY, mapWidth, mapHeight);
    }

    private void calculatePath(int targetX, int targetY, int mapWidth, int mapHeight) {
        int curX = targetX;
        int curY = targetY;

        for (int i = weightedBoard[targetX][targetY] - 1; i >= 0; i--) {
            boolean temp = false;
            if (curX + 1 < mapWidth && !temp) {
                if (weightedBoard[curX + 1][curY] == i) {
                    pathQueue.add(new Pair<>("x", 1));

                    curX++;

                    temp = true;
                }

            }
            if (curX - 1 > 0 && !temp) {
                if (weightedBoard[curX - 1][curY] == i) {
                    pathQueue.add(new Pair<>("x", -1));

                    curX--;

                    temp = true;

                }
            }
            if (curY + 1< mapHeight && !temp) {
                if (weightedBoard[curX][curY + 1] == i) {
                    pathQueue.add(new Pair<>("y", 1));

                    curY++;

                    temp = true;

                }
            }
            if (curY > 0 && !temp) {
                if (weightedBoard[curX][curY - 1] == i) {
                    pathQueue.add(new Pair<>("y", -1));

                    curY--;

                    temp = true;

                }
            }
        }

        Stack<Pair<String, Integer>> s = new Stack<>();  //create a stack

        //while the queue is not empty
        while(!pathQueue.isEmpty())
        {  //add the elements of the queue onto a stack
            s.push(pathQueue.remove());
        }

        //while the stack is not empty
        while(!s.isEmpty())
        { //add the elements in the stack back to the queue
            pathQueue.add(s.pop());
        }
    }




    private void dijkstra(int startX, int startY, int board[][], int mapWidth, int mapHeight, int length)
    {
        if (startX + 1 < mapWidth)
        {
            if (board[startX+1][startY] != 1)
            {
                if (weightedBoard[startX+1][startY] > weightedBoard[startX][startY] + 1 || weightedBoard[startX+1][startY] == -1)
                {
                    weightedBoard[startX+1][startY] = weightedBoard[startX][startY] + 1;
                    if (length < mapWidth + mapHeight)
                    {
                        dijkstra(startX+1, startY, board, mapWidth, mapHeight, length+1);
                    }
                }
            }
        }
        if (startX - 1 >= 0)
        {
            if (board[startX-1][startY] != 1)
            {
                if (weightedBoard[startX-1][startY] > weightedBoard[startX][startY] + 1 || weightedBoard[startX-1][startY] == -1)
                {
                    weightedBoard[startX-1][startY] = weightedBoard[startX][startY] + 1;
                    if (length < mapWidth + mapHeight)
                    {
                        dijkstra(startX-1, startY, board, mapWidth, mapHeight, length+1);
                    }
                }
            }
        }
        if (startY + 1 < mapHeight)
        {
            if (board[startX][startY+1] != 1)
            {
                if (weightedBoard[startX][startY+1] > weightedBoard[startX][startY] + 1 || weightedBoard[startX][startY+1] == -1)
                {
                    weightedBoard[startX][startY+1] = weightedBoard[startX][startY] + 1;
                    if (length < mapWidth + mapHeight)
                    {
                        dijkstra(startX, startY+1, board, mapWidth, mapHeight, length+1);
                    }
                }
            }
        }
        if (startY - 1 >= 0)
        {
            if (board[startX][startY-1] != 1)
            {
                if (weightedBoard[startX][startY-1] > weightedBoard[startX][startY] + 1 || weightedBoard[startX][startY-1] == -1)
                {
                    weightedBoard[startX][startY-1] = weightedBoard[startX][startY] + 1;
                    if (length < mapWidth + mapHeight)
                    {
                        dijkstra(startX, startY-1, board, mapWidth, mapHeight, length+1);
                    }
                }
            }
        }
    }

}
