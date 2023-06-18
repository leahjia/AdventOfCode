import java.io.*;
import java.util.*;


public class Day7 {
    
    // Tree structure storing all directories & files
    private static class Tree {
        TreeNode root;
        
        private Tree() {
            this.root = new TreeNode("root");
        }
    }
    
    // Node storing a single directory or file
    private static class TreeNode {
        int val;
        String name;
        Map<String, TreeNode> branch;
        
        // name is only for debugging purposes
        private TreeNode(String name) {
            this.name = name;
            this.val = 0;
            this.branch = new HashMap<>();
        }
    }
    
    // part 1 - list of small-enough directories
    static List<TreeNode> directories = new ArrayList<>();
    
    // part 2 target storage needed
    static int target;
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader("input/day7.txt"));
        in.nextLine();
        
        // Build tree through all commands
        Tree tree = new Tree();
        tree.root.val = traverse(in, tree.root);
        
        // part 1
        int count = 0;
        for (TreeNode dir : directories) count += dir.val;
        System.out.println("Part 1: " + count);
        
        // part 2
        target = 30000000 - (70000000 - tree.root.val);
        System.out.println("Part 2: " + dfs(tree.root, tree.root.val));
    }
    
    // part 1 - construct tree & find directories
    private static int traverse(Scanner in, TreeNode root) {
        while (in.hasNext()) {
            String str = in.nextLine();
            String[] line = str.split(" ");
            if (str.startsWith("$ ls")) continue;
            
            if (str.startsWith("$ cd")) {
                String dir = line[2];
                // back up a directory
                if (dir.equals("..")) return root.val;
                
                // create new directory
                root.branch.put(dir, new TreeNode(dir));
                int newVal = traverse(in, root.branch.get(dir));
                root.val += newVal;
                
                // add to directory list for part 1
                if (newVal <= 100000) {
                    if (newVal != 0) directories.add(root.branch.get(dir));
                }
            } else if (!str.startsWith("dir")) {
                // e.g. `123 random.log`
                // create new file & add size to sum
                int fileSize = Integer.parseInt(line[0]);
                String fileName = line[1];
                TreeNode newNode = new TreeNode(fileName);
                newNode.val = fileSize;
                root.branch.put(fileName, newNode);
                root.val += fileSize;
            }
        }
        
        return root.val; // ~1297683
    }
    
    // part 2 - run search
    private static int dfs(TreeNode root, int curr) {
        // not a directory or not large enough
        if (root.branch.isEmpty() || root.val < target) return Integer.MAX_VALUE;
        // update current minimum
        curr = Math.min(curr, root.val);
        // run dfs on each branch
        for (TreeNode node : root.branch.values()) {
            curr = Math.min(curr, dfs(node, curr));
        }
        return curr; // ~5756764
    }
}