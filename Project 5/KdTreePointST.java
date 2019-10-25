import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Stack;

public class KdTreePointST<Value> implements PointST<Value> {
    private Node root;
    private int N;

    // 2d-tree (generalization of a BST in 2d) representation.
    private class Node {
        private Point2D p;   // the point
        private Value val;   // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to 
                             // this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Construct a node given the point, the associated value, and the 
        // axis-aligned rectangle corresponding to the node.
        Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // Construct an empty symbol table of points.
    public KdTreePointST() {
        root = null;
    }

    // Is the symbol table empty?
    public boolean isEmpty() { 
        return size() == 0;        
    }

    // Number of points in the symbol table.
    public int size() {
        return N;
    }
    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        RectHV rect = new RectHV(0, 0, 1, 1);
        if (isEmpty()) {
            root = new Node(p, val, rect);
            N++;
            return;
        }
       
        root = put(root, p, val, rect, true);
    }

    // Helper for put(Point2D p, Value val).
    private Node put(Node x, Point2D p, Value val, RectHV rect, boolean lr) {
        if (x == null) {
            N++;
            return new Node(p, val, rect);
        }
        RectHV recthv;
        double x1, x2, y1, y2;
        int cmp = compare(x.p, p, lr);
        if (cmp > 0) {
            if (x.lb == null) {
                 x1 = rect.xmin();
                 y1 = rect.ymin();
                 if (lr) {
                   x2 = x.p.x();
                   y2 = rect.ymax();
                   }
                else {
                   x2 = rect.xmax();
                   y2 = x.p.y();
                }
                recthv= new RectHV(x1, y1, x2, y2);
            }
            else {
                recthv = x.lb.rect;
            }
            x.lb = put(x.lb, p, val, recthv, !lr);
        } 
        if (cmp < 0) {
            if (x.rt == null) {
                if (lr) {
                    x1 = x.p.x();
                    y1 = rect.ymin();
                }
                else {
                    x1 = rect.xmin();
                    y1 = x.p.y();
                }
                x2 = rect.xmax();
                y2 = rect.ymax();
                recthv = new RectHV(x1, y1, x2, y2);
            }
            else {
                recthv = x.rt.rect;
            }
            x.rt = put(x.rt, p, val, recthv, !lr);
        }
        if(x.p.equals(p)) {
            x.val = val;
            return x;
        }
        return x;
    }
    // Value associated with point p.
    public Value get(Point2D p) {
        //lr ain't defined so what hell i'm na do
        if (p == null) {
            return null;
        }
        return get(root, p, true);
    }

    // Helper for get(Point2D p).
    private Value get(Node x, Point2D p, boolean lr) {
        if (x == null) { 
            return  null; 
        }
        int cmp = compare(x.p, p, lr);
        if(cmp > 0) { 
            return  get(x.lb , p, !lr); 
        }
        else if (cmp < 0) {
            return  get(x.rt , p, !lr); 
        }
        return x.val;
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        return get(p) != null;
    }
    //private helper for comparing x - axis
    private int compare(Point2D p1, Point2D p2, boolean lr) {
        if (lr) {
            if (p1.x() < p2.x()) {
            return -1;
            }
            if (p1.x() > p2.x()) {
            return +1;
            }
            else {
            return 0;
            }
        }
        else {
            if (p1.y() < p2.y()) {
                return -1;
            }
            if (p1.y() > p2.y()) {
                return +1;
            }
            else {
                return 0;
            }
        }  
    }

    // All points in the symbol table, in level order.
    public Iterable<Point2D> points() {
        Queue<Point2D> queue = new Queue<Point2D>();
        points(root, queue);
        return queue;
    }

    private void points(Node x, Queue<Point2D> q) {
        Queue<Node> n = new Queue<Node>();
        n.enqueue(root);
        while(!n.isEmpty()) {
            Node y2 = n.dequeue();
            q.enqueue(y2.p);
            if (y2.lb != null) {
                n.enqueue(y2.lb); 
            }
            if (y2.rt != null) {
                n.enqueue(y2.rt);
            }
        }
    }
    
    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, queue);
        //problem printing in out of order so don't forget to sort it in y order
        return queue;
    }

    // Helper for public range(RectHV rect).
    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        //needs some fixing
        //wow this area needs fixing
        if (x != null && rect.intersects(x.rect)) {
            if (rect.contains(x.p)) {
                q.enqueue(x.p);
            }
            range(x.rt, rect, q); 
            range(x.lb, rect, q);
        }
        
    }
        
    // A nearest neighbor to point p; null if the symbol table is empty.
   public Point2D nearest(Point2D p) {
        return nearest(root, p, root.p, Double.MAX_VALUE, true);
    }
    
    // Helper for public nearest(Point2D p).
   private Point2D nearest(Node x, Point2D p, Point2D nearest, 
                            double nearestDistance, boolean lr) {
        Point2D closest = nearest;
        nearestDistance = closest.distanceSquaredTo(p);
        if (x == null) {
            return closest;
        }
        if (!p.equals(x.p) && nearestDistance < x.rect.distanceSquaredTo(p)) { 
            return closest; 
        }
        int cmp = compare(x.p, p, lr);
        if (!p.equals(x.p) && x.p.distanceSquaredTo(p) < nearestDistance) {
            closest = x.p;
            nearestDistance = x.p.distanceSquaredTo(p);
        }
        if (cmp < 0) {
            closest = nearest(x.lb, p, closest, nearestDistance, !lr);
            closest = nearest(x.rt, p, closest, nearestDistance, !lr);
        }
        else {
            closest = nearest(x.rt, p, closest, nearestDistance, !lr);
            closest = nearest(x.lb, p, closest, nearestDistance, !lr);
        }
    return closest;
    }
    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        MaxPQ<Point2D> pq = new MaxPQ<Point2D>();  
        nearest(root, p, k, pq, true);
        
        MinPQ<Double> distance = new MinPQ();
        ST<Double, Point2D> st = new ST<Double, Point2D>();
        Stack<Point2D> que = new Stack<Point2D>();
        for (Point2D x: points()) {
            if (x.distanceSquaredTo(p) != 0) {
                st.put(x.distanceSquaredTo(p), x);
                distance.insert(x.distanceSquaredTo(p));
            }
        }
        for (Double y: st.keys()) {
            if (que.size() < k) {
                que.push(st.get(distance.delMin()));
            }
        }
        //very disaapointed for using for loop. dumbest choice in life
        return que;

    }

    // Helper for public nearest(Point2D p, int k).
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, 
                         boolean lr) {

        if (x == null) {
            return;
        }
        int cmp = compare(x.p, p, lr); 
        if (!p.equals(x.p)) {
            pq.insert(x.p);
        }

        if (pq.size() > k) {
            pq.delMax();
        }
        
        if (cmp < 0) {
            nearest(x.lb, p, k, pq, !lr);
            nearest(x.rt, p, k, pq, !lr);
        }
        else {
            nearest(x.rt, p, k, pq, !lr);
            nearest(x.lb, p, k, pq, !lr);
        }
    }
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        double rx1 = Double.parseDouble(args[2]);
        double rx2 = Double.parseDouble(args[3]);
        double ry1 = Double.parseDouble(args[4]);
        double ry2 = Double.parseDouble(args[5]);
        int k = Integer.parseInt(args[6]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(rx1, ry1, rx2, ry2);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.println("First " + k + " values:");
        i = 0;
        for (Point2D p : st.points()) {
            StdOut.println("  " + st.get(p));
            if (i++ == k) {
                break;
            }
        }
        StdOut.println("st.contains(" + query + ")? " + st.contains(query));
        StdOut.println("st.range(" + rect + "):");
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.println("st.nearest(" + query + ") = " + st.nearest(query));
        StdOut.println("st.nearest(" + query + ", " + k + "):");
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}