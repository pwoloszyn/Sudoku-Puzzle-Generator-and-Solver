/**
 * This class defines a Pair object, the function of which
 * is self explanatory: it's a pair of objects.
 * @author Piotr Woloszyn
 *
 * @param <V1>
 * @param <V2>
 */
public class Pair<V1,V2> {
	private V1 val_one;
	private V2 val_two;
	
	public Pair(V1 val_one, V2 val_two) {
		this.val_one = val_one;
		this.val_two = val_two;
	}
	
	public void setValOne(V1 val_one) {
		this.val_one = val_one;
	}
	
	public void setValTwo(V2 val_two) {
		this.val_two = val_two;
	}
	
	public V1 getValOne() {
		return val_one;
	}
	
	public V2 getValTwo() {
		return val_two;
	}
}
