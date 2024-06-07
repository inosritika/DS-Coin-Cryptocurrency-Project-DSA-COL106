import Includes.*;
import java.util.*;

public class BlockChain{
	public static final String start_string = "LabModule5";
	public Block firstblock;
	public Block lastblock;

	public List<Pair<String, String>> path_to_root(MerkleTree mtree,int doc_idx) {
        ArrayList<Pair<String, String>> pathToRoot = new ArrayList<>();
        TreeNode curr = mtree.rootnode;
        int n = mtree.numstudents;
        int k = doc_idx;
        boolean initial = true;
        while (n >= 2) {
            if (initial) {

                pathToRoot.add(0, new Pair<String, String>(curr.val, null));
                initial = false;
            }
            pathToRoot.add(0, new Pair<String, String>(curr.left.val, curr.right.val));
            if ((n / 2) < k) {
                curr = curr.right;
                n = n / 2;
                k = k - n;
            } else {
                curr = curr.left;
                n = n / 2;
            }
        }
        return pathToRoot;
    }
	public String InsertBlock(List<Pair<String,Integer>> Documents, int inputyear){
		/*
			Implement Code here
		*/
		Block b = new Block();
		MerkleTree m = new MerkleTree();
		CRF obj = new CRF(64);
		b.year = inputyear;
		b.mtree = m;
		b.value = m.Build(Documents)+"_"+m.int_to_str(m.rootnode.maxleafval);
		if(firstblock==null){
			b.previous = null;
			b.next = null;
			b.dgst = obj.Fn(BlockChain.start_string + "#" + b.value);
			firstblock=lastblock = b;
		}else{
			b.previous = lastblock;
			lastblock.next = b;
			b.next = null;
			b.dgst = obj.Fn(b.previous.dgst + "#" + b.value);
			lastblock = b;
		}
		return this.lastblock.dgst;
	}

	public Pair<List<Pair<String,String>>, List<Pair<String,String>>> ProofofScore(int student_id, int year) {
		// Implement Code here
		Block a = firstblock;
		while(a.next!=null){
			if(a.year == year){
				break;
			}
			a=a.next;
		}
		//Path to root
		ArrayList<Pair<String,String>> l1;
		l1 = (ArrayList<Pair<String, String>>) path_to_root(a.mtree,student_id+1);
		//Path to lastblock
		ArrayList<Pair<String,String>> l2 = new ArrayList<>();
		while(a!=lastblock){
			l2.add(0, new Pair<>(a.value, a.dgst));
			a=a.next;
		}
		l2.add(0, new Pair<>(a.value, a.dgst));
		return new Pair<>(l1, l2);
	}
}
