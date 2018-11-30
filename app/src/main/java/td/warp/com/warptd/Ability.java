package td.warp.com.warptd;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Ability {

    public String name;
    public int x, y;
    public int ttl;
    public int lifetime = 0;
    public int strength;

    public Ability(String name, int x, int y, int strength, GameView parent)
    {
        this.ttl = ttl;
        this.name = name;
        this.x = x;
        this.y = y;
        this.strength = strength;

        switch (name)
        {
            case "Stasis":
                ttl = 30*5;
                break;
            case "Black Hole":
                ttl=30*5;
                break;
        }
    }

    public void update()
    {
        lifetime++;
    }

}

