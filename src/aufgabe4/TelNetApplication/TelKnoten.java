package aufgabe4.TelNetApplication;

public class TelKnoten {
    public final int x;
    public final int y;

    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int hashCode() {
        Integer tx = x;
        Integer ty = y;
        return (tx.hashCode() + ty.hashCode()) * 23;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  TelKnoten) {
            if(obj == null) {
                return false;
            } else if (obj.getClass() != this.getClass()) {
                return false;
            }
            TelKnoten other = (TelKnoten) obj;
            if(this.x == other.x && this.y == other.y) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
