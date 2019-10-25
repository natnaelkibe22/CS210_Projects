import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// An immutable WordNet data type.
public class WordNet {
    private RedBlackBST<String, SET<Integer>> st;
    private RedBlackBST<Integer, String> rst;
    private ShortestCommonAncestor sca;
    
    // Construct a WordNet object given the names of the input (synset and
    // hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        /*if (synsets == null || hypernyms == null) {
            throw NullPointerException();
        }*/
        st = new RedBlackBST<String, SET<Integer>>();
        rst = new RedBlackBST<Integer, String>();
        
        In syn = new In(synsets);
        In hyp = new In(hypernyms);
       
        String word;
        while (syn.hasNextLine()) {
            String line = syn.readLine();
            String[] all = line.split(",");
            String[] noun = all[1].split(" ");
            Integer id = Integer.parseInt(all[0]);
            for (int i = 0; i < noun.length; i++) {
                SET<Integer> set= new SET<Integer>();
                if (st.contains(noun[i])) {
                    set = st.get(noun[i]);
                }
                st.put(noun[i], set);
                st.get(noun[i]).add(id);
            }
            word = all[1];
            rst.put(id, word);
        }
        Digraph G = new Digraph(rst.size());
        while (hyp.hasNextLine()) {
            String line = hyp.readLine();
            String[] all = line.split(",");
            for (int i = 0; i < all.length; i++) {
                G.addEdge(Integer.parseInt(all[0]), Integer.parseInt(all[i]));
            }
        }
        sca = new ShortestCommonAncestor(G);
    }

    // All WordNet nouns.
    public Iterable<String> nouns() {
        SET<String> set = new SET<String>();
        for (String s: st.keys()) {
            set.add(s);
        }
        return set;
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (st.contains(word)) {
            return true;
        }
        return false;
    }
    // A synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
       if (!isNoun(noun1) || !isNoun(noun2)) {
        throw new IllegalArgumentException();
       }
       if (noun1 == null || noun2 == null) {
        throw new NullPointerException();
       }
       SET<Integer> a = st.get(noun1);
       SET<Integer> b = st.get(noun2);
       if (sca.ancestor(a, b) == -1) {
        return null;
       }
       else {
          return rst.get(sca.ancestor(a, b));
       }
    }

    // Distance between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
        throw new IllegalArgumentException();
       }
       if (noun1 == null || noun2 == null) {
        throw new NullPointerException();
       }
       SET<Integer> A = st.get(noun1);
       SET<Integer> B = st.get(noun2);
       return sca.length(A, B);
    }
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];        
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.println("# of nouns = " + nouns);
        StdOut.println("isNoun(" + word1 + ") = " + wordnet.isNoun(word1));
        StdOut.println("isNoun(" + word2 + ") = " + wordnet.isNoun(word2));
        StdOut.println("isNoun(" + (word1 + " " + word2) + ") = "
                       + wordnet.isNoun(word1 + " " + word2));
        StdOut.println("sca(" + word1 + ", " + word2 + ") = "
                     + wordnet.sca(word1, word2));
        StdOut.println("distance(" + word1 + ", " + word2 + ") = "
                       + wordnet.distance(word1, word2));
    }
}