package crossfire;

public record Card(String name, Damage damage, int id){
    public static Card Street_Smarts(int id) {
        return new Card("Street_Smarts", Damage.RED, id);
    }
}
