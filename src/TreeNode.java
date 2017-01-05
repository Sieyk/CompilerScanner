public class TreeNode {

	private static int count = 0;
	private static int index = 0;
	private Node nodeValue;
	private int idx;
	private TreeNode left,middle,right;
	private String name;
	//private String type; //String was originally StRec
	private Token tok;

	public enum Node {
		NUNDEF, NPROG, NARRYL, NPROCL, NMAIN, NPROC, NPLIST, NSIMPAR, NARRPAR,
		NDLIST, NSIMDEC, NARRDEC, NARRVAR, NSIMVAR, NSLIST,
		NLOOP, NEXIT, NIFT, NIFTE, NINPUT, NPRINT, NPRLN, NCALL, NELIST,
		NASGN, NPLEQ, NMNEQ, NSTEQ, NDVEQ, NADD, NSUB, NMUL, NDIV, NIDIV,
		NNOT, NAND, NOR, NXOR, NEQL, NNEQ, NGTR, NLESS, NGEQ, NLEQ,
		NILIT, NFLIT, NIDLST, NVLIST, NPRLIST, NLEN, NSTRG
	};
	
	public TreeNode () {
		nodeValue = Node.NUNDEF;
		index++;
		idx = index;
		left = null;
		middle = null;
		right = null;
		name = null;
		//type = null;
	}

	public TreeNode (Node value) {
		this();
		nodeValue = value;
	}

	public TreeNode (Node value, String st) {
		this(value);
		name = st;
	}

	public TreeNode (Node value, TreeNode l, TreeNode r) {
		this(value);
		left = l;
		right = r;
	}

	public TreeNode (Node value, TreeNode l, TreeNode m, TreeNode r) {
		this(value,l,r);
		middle = m;
	}

	public Node getValue() { return nodeValue; }

	public TreeNode getLeft() { return left; }

	public TreeNode getMiddle() { return middle; }

	public TreeNode getRight() { return right; }

	public String getName() { return name; }

	//public String getType() { return type; }
	
	public Token getToken() { return tok; }

	public void setValue(Node value) { nodeValue = value; }

	public void setLeft(TreeNode l) { left = l; }

	public void setMiddle(TreeNode m) { middle = m; }

	public void setRight(TreeNode r) { right = r; }

	public void setName(String st) { name = st; }

	//public void setType(String st) { type = st; }
	
	public void setTokenContents(String in) { tok.setContents(in); }

	public static void resetIndex() {
		index = 0;
	}
}