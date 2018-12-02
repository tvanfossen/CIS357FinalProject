package td.warp.com.warptd;

import android.widget.ActionMenuView;

public class Achievement {

    private String name, description;

    public Achievement(String name, String description){
        this.name = name;
        this. description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
