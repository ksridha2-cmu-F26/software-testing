
public class MyPStack {

	private int maxSize = 10;
	private int[] stackArray;
	private int top;
    private IDataBase db;
    private String id; // unique ID of the stack in the DB
    private static int nextId = 1; // "index" to generate ID of a newly created stack;
                                   // shared among all stack objects

	public MyPStack() {
		stackArray = new int[maxSize];
		top = -1;
	}

	public MyPStack(IDataBase stackDB) { // constructor dependency injection
        db = stackDB; // same DB is shared among all stack objects
		stackArray = new int[maxSize];
		top = -1;
        id = String.valueOf(nextId);
        nextId++; // increment "index" for the next stack
	}
    
    public String getId() {
        return id;
        
    }
	public void push(int j) throws OverflowException {
		if (isFull()) throw new OverflowException(); 
		stackArray[++top] = j;
		if (db != null) {
			if (top == 0) {
				db.create(id, j);
			} else {
				db.update(id, j);
			}
		}
	}
    

	public int pop() throws InvalidOperationException {
		if (isEmpty()) throw new InvalidOperationException(); 
		int value = stackArray[top--];
		if (db != null) {
			if (top == -1) {
				db.delete(id);
			} else {
				db.update(id, stackArray[top]);
			}
		}
		return value;
	}

	public int peek() throws InvalidOperationException {
		if (isEmpty()) throw new InvalidOperationException(); 
		return stackArray[top];
	}

	public void reset() {
		top = -1;
		if (db != null) {
			stackArray[++top] = db.read(id);
		}
	}

	public int size() {
		return top + 1;
	}

	public boolean isEmpty() {
		return (top == -1);
	}
	
	public boolean isFull() {
	      return (top == maxSize - 1);
	}
	
	public int maxSize() { // added for visibility to test overflow
		return maxSize;
	}

}
