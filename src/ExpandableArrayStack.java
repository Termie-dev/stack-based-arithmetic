
public class ExpandableArrayStack<T> {

	private T stack[];
	private int size;
	
	public ExpandableArrayStack() {
		
		this.stack = (T[]) new Object[1];
		this.size = 0;
		
	}
	
	public T push(T obj) {
		
		if (size == stack.length) {
			
			System.out.println("Array is full. Doubling size");
			resize(stack.length * 2);
			
		}
		
		stack[size++] = obj;
		return obj;
		
	}
	
	public void resize(int newSize) {
		
		T[] newStack = (T[]) new Object[newSize];
		
		System.arraycopy(stack, 0, newStack, 0, size);
		
		stack = newStack;
		
	}
			
	public T pop() {
		
        if (isEmpty()) {
        	
            System.out.println("Stack is empty");
            
        }
        
        T value = stack[--size]; // Check if this should be --size
        stack[size] = null;
        
        if (size < stack.length - 1) {
        	
            resize(stack.length - 1); // Decrease capacity by 1
            
        }

        return value;
        
    }
	
	public T top() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return stack[size - 1];
    }
	
	public boolean isEmpty() {
		
		return size == 0;
		
	}
			
	public int getSize() {
		
		return size;
		
	}
	
	public T get(int index) {
		
        if (index < 0 || index >= size) {
        	
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            
        }
        
        return stack[index];
    }
	
}
