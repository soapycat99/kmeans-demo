package robots;

import java.util.Arrays;
import java.util.LinkedList;

public class RobotDelivery{
	
	
	//room volumes
	static final int V1 = 40;
	static final int V2 = 60;
	
	// array of boxes with its volume
	static int [] boxes = {0,10,25,17,9,34,21,30,23,15,27,20};
	


	
	public static void main(String[]args) {
		
		//Init list of boxes that fit into each room and its label( or id). 
		//List tempRoom and tempLabel is to keep track the local extrama(or maximum) of boxes fitted the room. 
		LinkedList<Integer> room_1 = new LinkedList<Integer>();
		LinkedList<Integer> tempRoom_1 = new LinkedList<Integer>();
		LinkedList<Integer> label_1 = new LinkedList<Integer>();
		LinkedList<Integer> tempLabel_1 = new LinkedList<Integer>();
		
		LinkedList<Integer> room_2 = new LinkedList<Integer>();
		LinkedList<Integer> tempRoom_2 = new LinkedList<Integer>();
		LinkedList<Integer> label_2 = new LinkedList<Integer>();
		LinkedList<Integer> tempLabel_2 = new LinkedList<Integer>();
		
		System.out.println("Room 1 volume = " + V1 + " m2\nRoom 2 volume = " + V2 + " m2\n");
	
		//Display boxes that need to be filled in the room
		System.out.println("\tBoxes await to be filled:\n");
		for (int i = 1; i < boxes.length; i++) {
			if( i % 2 == 1)
				System.out.print("*Box " + i + ": " + boxes[i] + " lb\t| ");
			else
				System.out.println("*Box " + i + ": " + boxes[i] +" lb");
		}
		
		//split boxes into 2 sub array with ratio respective with their volumes
		double ratio = (double) V1/(V1+V2);
		int length_1; // length of 1st sub array of boxes
		length_1 = (int) (ratio * (boxes.length - 1)); // length = ratio * (total amount of boxes - 1), the number "1" is the first element "0" of the boxes array that need to be subtracted.
 		
		//the very first element of an array always is zero, so that the recursive will start from position 1, easier to keep track with box order
		//init pack1
		int [] pack_1 = Arrays.copyOfRange(boxes, 0, length_1+1); // {0,10,25,17,9}
		
		//init pack 2 
		int [] pack_2 = new int[boxes.length - length_1];
		pack_2[0] = 0;
		for (int i =1; i < pack_2.length; i++) { //0,34,21,30,23,15,27,20}
			pack_2[i] = boxes[length_1 + i];
		}
		
		//init agents , assuming it run concurreboxes.lengthtly 
		RobotDelivery robot_1 = new RobotDelivery();
		robot_1.backTrackingSearch(V1, pack_1, room_1, label_1, tempRoom_1, tempLabel_1); //implementing backTracking for agent 1 

		RobotDelivery robot_2 = new RobotDelivery();
		robot_2.backTrackingSearch(V2, pack_2, room_2, label_2, tempRoom_2, tempLabel_2); //implementing backTracking for agent 2 
		
		//Display Result
		System.out.print("\n\t--------------------\n\tImplementing backtracking\n\t(.....................)\n\nRoom 1: ");
		printResult(room_1, label_1, pack_1.length, true);
		System.out.println("\n   Total boxes filled: " + room_1.size());
		System.out.println("   Total volume filled: " + robot_1.getVolume(room_1) +" lb");
		
		System.out.print("\nRoom 2: ");
		printResult(room_2,label_2, pack_2.length, false);
		System.out.println("\n   Total boxes filled: " + room_2.size());
		System.out.println("   Total volume filled: " + robot_2.getVolume(room_2) + " lb");
		
		//Display boxes leftover which didn't fit in the room
		int [] leftover  = new int[boxes.length - room_1.size() - room_2.size() - 1 ]; 
		int [] leftoverLabel  = new int[leftover.length];
		
		robot_1.collectLeftOver(room_1,room_2, length_1, leftover, leftoverLabel);
		System.out.print("\t-----------------\nBoxes leftover:  ");
		for(int i = 0; i < leftover.length; i++) {
			System.out.print("Box " + leftoverLabel[i] + ": " + leftover[i] + " lb| ");
		}
		
		
		
	}
	
	/**  
	 * Collect all the boxes leftover. 
	 * @param room1 ,room2: Lists of boxes already filled into the rooms
	 * @param length_1: Size of 1st initial pack of boxes
	 * @param leftover, leftoverLabel: Arrays of leftover boxes and its label(or ID)
	 */
	public void collectLeftOver(LinkedList<Integer> room_1, LinkedList<Integer> room_2, int length_1, int [] leftover, int [] leftoverLabel) {

		int count = -1;
		
		for(int i = 1; i < length_1 + 1; i++) {
			boolean filled = true;
			for (int j = 0; j < room_1.size(); j++) {
				if (boxes[i] == room_1.get(j)) {
					filled = false;
					break;
				}
			}	
			if(filled) {
				count++;
				leftoverLabel[count] = i;
				leftover[count] = boxes[i];
			}			
		}
		
		for(int i = length_1+1; i < boxes.length; i++) {
			boolean filled = true;
			for (int j = 0; j < room_2.size(); j++) {
				if (boxes[i] == room_2.get(j)) {
					filled = false;
					break;
				}
			}	
			if(filled) {
				count++;
				leftoverLabel[count] = i;
				leftover[count] = boxes[i];
			}			
		}
	}
	
	/**  
	 * Print the list of boxes filled in the  respective room and its label. 
	 * @param room, label: List of boxes and its label
	 * @param pack_size: Size of the initial 1st pack of boxes before delivered into room
	 * @param first_robot: identify whether it's the first robot agent 
	 */
	private static void printResult(LinkedList<Integer> room, LinkedList<Integer> label, int pack_size, boolean first_robot) {
		if (first_robot) {
			for(int i = room.size() - 1; i >= 0; i--) {
				System.out.print("Box " + label.get(i) + ": " + room.get(i) + " lb" + " | ");				
			}
		}
			
		else {		
			int length = boxes.length - pack_size;
			for(int i = room.size() - 1; i >= 0; i--) {
				System.out.print("Box " + (label.get(i) + length) + ": " + room.get(i) + " lb" + " | ");
			}
		}					
	}

	/**  
	 * Recall backTracking implementation, afterward assign the maximum state that already has been stored into the room list
	 * @param room, label: List of boxes and its label
	 * @param V: capacity of the room
	 * @param pack: divided pack of boxes 
	 */
	public void backTrackingSearch(int V, int[] pack,LinkedList<Integer> room, LinkedList<Integer> label,
			LinkedList<Integer> tempRoom, LinkedList<Integer> tempLabel) {
		recursiveBacktracking(V, pack, 1, -1, room, label, tempRoom, tempLabel);

		copyList(room, tempRoom);
		copyList(label, tempLabel);

	}
	
	/**  
	 * Implementing recursive Backtracking
	 * @param V: capacity of the room
	 * @param pack: divided pack of boxes 
	 * @param order: initial state number of recursion
	 * @param room, label, tempRoom, tempLabel: List of boxes and its label, and temporary list to keep track the maxima. 
	 * @param max: maximum number of boxes filled 
	 */

	private void recursiveBacktracking(int V, int [] pack, int order, int max, LinkedList<Integer> room, LinkedList<Integer> label,
			LinkedList<Integer> tempRoom, LinkedList<Integer> tempLabel) {
		
		//Recursive break condition
		if (order == pack.length)
			return;
		
		//Branching 
		for(int i = order ;i < pack.length; i++) {
						
			//check promsing
			//if the total remaining boxes not be filled in the room yet + total boxes already filled in the room of that successor branch < boxes filled maximum,  then implement pruning   
			if (pack.length + room.size() - i < tempRoom.size()) {
				break;
			}
			
			//Push the box into the room
			room.push(pack[i]);
			label.push(i);
			
			//volume taken so far in the room
			int volume = getVolume(room);
			
			//check if the amount of boxes >= maximum
			// if it > maximum , or either it equals to maximum but has smaller volume than maximum, then the current state become maximum
			if((room.size() > tempRoom.size() 
				|| (room.size() == tempRoom.size() && getVolume(room) < getVolume(tempRoom))) 
				&& volume <= V) {
				
				copyList(tempRoom, room);
				copyList(tempLabel,label);
			}
			
			//check constraint 
			if (volume < V) {
				recursiveBacktracking(V,pack, i+1, tempRoom.size(), room, label, tempRoom, tempLabel);
			}
			
			//backtrack boxes from the room
			room.pop();
			label.pop();
			
		}
	}
	//swapping two LinkedList
	private void copyList(LinkedList<Integer> list1,LinkedList<Integer> list2) {
		list1.clear();
		list1.addAll(list2);
	}

	//Access current volume of the room
	private int getVolume(LinkedList<Integer> Room) {
		int weight = 0;
		for (int i = 0; i < Room.size(); i++) {
			weight += Room.get(i);
		}
		return weight;
	}

	
}
