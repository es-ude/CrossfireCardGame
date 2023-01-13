package crossfire;

public record Card(String name, Damage damage, int id){

    public static Card Street_Smarts(int id) {
        return new Card(Prototype.STREET_SMARTS.name(), Prototype.STREET_SMARTS.damage(), id);
    }
    private record Prototype(String name, Damage damage) {
        private static final Prototype STREET_SMARTS = new Prototype("STREET_SMARTS", Damage.RED);
    }
}
