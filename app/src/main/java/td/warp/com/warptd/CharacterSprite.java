package td.warp.com.warptd;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {

    private Bitmap image;
    private int x, y;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;

        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 100, 100, null);
    }

    public void update()
    {
        y++;
    }

}
