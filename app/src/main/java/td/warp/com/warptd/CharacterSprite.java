package td.warp.com.warptd;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite{

    private Bitmap image;
    private int x, y;

    public CharacterSprite(Bitmap bmp, int x, int y) {

        image = bmp;

        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update()
    {
        ;
    }

}
