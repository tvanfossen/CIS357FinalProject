package td.warp.com.warptd;

public class UserInfo {

    private String email;
    private int biomass, kills, warps, wins, gamesPlayed;


    public UserInfo(String email, int biomass, int kills, int warps, int wins, int gamesPlayed){


        this.email = email;
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.biomass = biomass;
        this.kills = kills;
        this.warps = warps;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBiomass() {
        return biomass;
    }

    public void setBiomass(int biomass) {
        this.biomass = biomass;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWarps() {
        return warps;
    }

    public void setWarps(int warps) {
        this.warps = warps;
    }
}
