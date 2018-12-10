package td.warp.com.warptd;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Ability {

    public String name;
    public int x, y;
    public int ttl;
    public int lifetime = 0;
    public int strength;
    public Bitmap image;
    public int range;

    public Ability(Bitmap bmp, String name, int x, int y, int strength, int range, GameView parent)
    {
        this.ttl = ttl;
        this.name = name;
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.image = bmp;
        this.range = range;

        switch (name)
        {
            case "Stasis":
                ttl = 30*5;
                break;
            case "Black Hole":
                ttl=1*1;
                break;
            case "Gravity Shift":
                ttl=30*15;
            default:
                ttl=30*1;
                break;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }


    public void update()
    {
        lifetime++;
    }

}

