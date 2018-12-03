package td.warp.com.warptd;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BuildingSprite {

    private Bitmap image;
    public int x, y;

    public BuildingSprite(Bitmap bmp, int x, int y) {

        image = bmp;

        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update()
    {
        ;
    }
}
