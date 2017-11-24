package input;

public class InputEvent extends DiscreteEvent {
	public final Character INPUT;
	
	public InputEvent(double realTime, int discreteTime, Character input) {
		super(realTime, discreteTime);
		INPUT = input;
	}
	
	public String toString() {
		return super.toString() + ", input: " + INPUT;
	}
}
