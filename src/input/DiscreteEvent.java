package input;

public class DiscreteEvent implements Comparable<DiscreteEvent> {
	public final double REAL_TIME;
	public int DISCRETE_TIME;
	
	protected DiscreteEvent(double rt, int dt) {
		REAL_TIME = rt;
		DISCRETE_TIME = dt;
	}

	@Override
	public int compareTo(DiscreteEvent o) {
		final int REAL_TIME_DIFF = (int)((REAL_TIME - o.REAL_TIME) * 100);	
		return REAL_TIME_DIFF != 0 ? REAL_TIME_DIFF : DISCRETE_TIME - o.DISCRETE_TIME;
	}
	
	public String toString() {
		return "["+getClass().getSimpleName()+"] ("+REAL_TIME+","+DISCRETE_TIME+")";
	}
	
}
