package model;

import input.DiscreteEvent;
import input.InputEvent;
import input.TimeAdvanceEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class VendingMachine {
	private static final int COFFEE_PRICE = 100;
	
	private final Queue<DiscreteEvent> EVENT_QUEUE;
	private final Queue<DiscreteEvent> CURRENT_EVENTS;
	
	//time variables
	private double realTime;
	private int discreteTime;
	private int timeAdvance = Integer.MAX_VALUE;
	private double elapsedTime;
	
	//coins
	private int nickels = 0, dimes = 0, quarters = 0;
	private int value;
	
	public VendingMachine(InputEvent... trajectory) {
		EVENT_QUEUE = new PriorityQueue<>(Arrays.asList(trajectory));
		EVENT_QUEUE.add(generateTimeAdvanceEvent());
		
		CURRENT_EVENTS = new PriorityQueue<>();
	}
	
	public void run() {
		while(!(CURRENT_EVENTS.size() <= 1 && eventQueueHasNoInput() && timeAdvance == Integer.MAX_VALUE)) {
			log("state at " + getTimeString() + " - " + this);
			log("event queue size at " + getTimeString() + " - " + EVENT_QUEUE.size());
			log("current events queue size at " + getTimeString() + " - " + CURRENT_EVENTS.size());
			
			handleFunctionCalls();
		
			CURRENT_EVENTS.clear();
			prepareNextEvents();
			elapsedTime = CURRENT_EVENTS.peek().REAL_TIME - realTime;
			realTime = CURRENT_EVENTS.peek().REAL_TIME;
			System.out.println("");
		}
	}
	
	private void prepareNextEvents() {
		if(!EVENT_QUEUE.isEmpty()) {
			do {
				DiscreteEvent event = EVENT_QUEUE.poll();
				log("Adding event from event queue to current events: " + event);
				CURRENT_EVENTS.add(event);
			}while(!EVENT_QUEUE.isEmpty() && EVENT_QUEUE.peek().REAL_TIME <= CURRENT_EVENTS.peek().REAL_TIME);
		}
	}
	
	private void handleFunctionCalls() {
		final boolean TIME_ADVANCE_NEEDED = currentEventsContainsTimeAdvance();
		if(value >= COFFEE_PRICE || TIME_ADVANCE_NEEDED) {
			handleLambda();
		}
		
		if(CURRENT_EVENTS.size() >= 2 && TIME_ADVANCE_NEEDED) {
			deltaConf(getInputBag());
		}
		else if(CURRENT_EVENTS.size() >= 1 && !TIME_ADVANCE_NEEDED) {
			deltaExt(getInputBag());
		}
		else if(TIME_ADVANCE_NEEDED) {
			deltaInt();
		}
	}
	
	private void deltaExt(List<Character> input) {
		log("deltaExt at " + getTimeString());
		for(Character c : input) {
			switch(c) {
				case 'n':
					nickels++;
					value += 5;
				break;
				case 'd':
					dimes++;
					value += 10;
				break;
				case 'q':
					quarters++;
					value += 25;
				break;
			}
		}
		scheduleNewTimeAdvance();
	}
	
	private void deltaInt() {
		log("deltaInt at " + getTimeString());
		value %= COFFEE_PRICE;
		List<String> change = generateChange();
		for(String s : change) {
			String[] parts = s.split(" x ");
			if(parts.length != 2)
				continue;
			
			int amt = Integer.parseInt(parts[0]);
			switch(parts[1]) {
				case "n":
					nickels -= amt;
					value -= amt * 5;
				break;
				case "d":
					dimes -= amt;
					value -= amt * 10;
				break;
				case "q":
					quarters -= amt;
					value -= amt * 25;
				break;
			}
		}
		scheduleNewTimeAdvance();
	}
	
	private void deltaConf(List<Character> input) {
		deltaInt();
		deltaExt(input);
	}
	
	private List<String> lambda() {
		List<String> output = new ArrayList<>();
		if(value >= COFFEE_PRICE) {
			output.add(value / COFFEE_PRICE + " x coffee");
		}
		
		output.addAll(generateChange());
		return output;
	}
	
	private void handleLambda() {
		log("output at " + getTimeString() + " - ");
		List<String> output = lambda();
		output.stream().forEach(o -> System.out.println("\t\t\t"+o));
	}
	
	public List<String> generateChange() {
		List<String> change = new ArrayList<>();
		int changeNeeded = value % 100;
		String[] coins = {"n","d","q"};
		int[] stock = {nickels, dimes, quarters};
		int[] vals = {5,10,25};
		
		for(int i = 0; i < coins.length; i++) {
			if(changeNeeded < vals[i] || stock[i] == 0)
				continue;
			int numCoinsToUse = changeNeeded / vals[i];
			if(numCoinsToUse > stock[i])
				numCoinsToUse = stock[i];
			
			change.add(numCoinsToUse+" x "+coins[i]);
			changeNeeded -= numCoinsToUse * vals[i];
		}
		
		return change;
	}
	
	private List<Character> getInputBag() {
		final List<Character> input = new ArrayList<>();
		CURRENT_EVENTS.stream()
			.filter(e -> e instanceof InputEvent)
			.map(e -> (InputEvent)e)
			.forEach(e -> input.addAll(Arrays.asList(e.INPUT)));		
		
		return input;
	}
	
	private boolean currentEventsContainsTimeAdvance() {
		return CURRENT_EVENTS.stream().anyMatch(e -> e instanceof TimeAdvanceEvent);
	}
	
	private boolean eventQueueHasNoInput() {
		return EVENT_QUEUE.stream().noneMatch(d -> !(d instanceof TimeAdvanceEvent));
	}
	
	private void scheduleNewTimeAdvance() {
		EVENT_QUEUE.removeIf(event -> event instanceof TimeAdvanceEvent);
		DiscreteEvent newTimeAdvance = generateTimeAdvanceEvent();
		EVENT_QUEUE.add(newTimeAdvance);
		log("new time advance has been scheduled: " + newTimeAdvance);
	}
	
	private TimeAdvanceEvent generateTimeAdvanceEvent() {
		TimeAdvanceEvent event = value > 0 ? new TimeAdvanceEvent(realTime + 2, 0) : new TimeAdvanceEvent(Integer.MAX_VALUE, 0);
		timeAdvance = value > 0 ? 2 : Integer.MAX_VALUE;
		return event;
	}
	
	private String getTimeString() {
		return "("+realTime+","+discreteTime+")";
	}
	
	private void log(String str) {
		System.out.println("[Vending Machine]: " + str);
	}
	
	public String toString() {
		return "ta: " + timeAdvance + ", et: " + elapsedTime + ", n: " + nickels 
				+ ", d: " + dimes + ", q: " + quarters + ", v: " + value;
	}
	
}
