import input.InputEvent;
import model.VendingMachine;

public class Main {

	public static void main(String[] args) {
		InputEvent[] INPUT_TRAJECTORY = {
			new InputEvent(0.4,0,'q'), new InputEvent(3,0,'d'),
			new InputEvent(3.2,0,'d'), new InputEvent(3.2,1,'n'), 
			new InputEvent(7,0,'q'), new InputEvent(9,0,'d'),
			new InputEvent(10,0,'q','q','q','q','q','n','d','n','d','q')
		};
		VendingMachine vm = new VendingMachine(INPUT_TRAJECTORY);
		vm.run();
	}

}
