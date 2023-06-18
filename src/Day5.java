import java.io.*;
import java.util.*;

public class Day5 {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new FileReader("input/day5.txt"));
        
        // stack up the layers with bottom layer on top of the stack
        Stack<String> layers = new Stack<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line.equals("")) break;
            layers.push(line);
        }
        
        // last layer contains the numbers
        String labels = layers.pop();
        int numStack = Character.getNumericValue(labels.charAt(labels.length() - 1));
        
        // store indices of each stack
        int[] label = new int[numStack + 1];
        
        // construct the stacks
        Stack<Character>[] stacks = new Stack[numStack + 1];
        
        for (int i = 0; i <= numStack; i++) {
            label[i] = labels.indexOf(i + "");
            stacks[i] = new Stack<>();
        }
        
        // move each layer to the stacks
        while (!layers.isEmpty()) {
            String layer = layers.pop();
            for (int i = 1; i <= numStack; i++) {
                int index = label[i];
                if (index < layer.length() && layer.charAt(index) != ' ') {
                    stacks[i].push(layer.charAt(index));
                }
            }
        }
        
        while (in.hasNextLine()) {
            int[] arr = new int[3];
            for (int i = 0; i < 3; i++) {
                in.next();
                arr[i] = in.nextInt();
            }
            int move = arr[0];
            int from = arr[1];
            int to = arr[2];
            Stack<Character> crateMover9001 = new Stack<>();
            for (int i = 0; i < move; i++) {
                // part 1
                // stacks[to].push(stacks[from].pop());
                
                // part 2
                crateMover9001.push(stacks[from].pop());
            }
            // part 2
            while (!crateMover9001.isEmpty()) stacks[to].push(crateMover9001.pop());
        }
        
        // StringBuilder res = new StringBuilder();
        // res.append(stack.peek());
        String res = "";
        for (Stack<Character> stack : stacks) {
            if (!stack.isEmpty()) res += stack.peek();
        }
        System.out.println(res);
    }
}
