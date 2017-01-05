
public class COMPParser {
	
	Token tok;
	String programID;
	TreeNode root, temp, arrays, procs, main;
	states currState;
	TreeNode.Node node;
	
	enum states{start, id}
	
	COMPParser(){
		tok = null;
		root = null;
		temp = null;
		currState = states.start;
	}
	
	public void parse(Token symbol){
		
		switch(currState){
			case start:
				if(root == null){
					if(symbol.getTokenID().equals("TPROG")){
						currState = states.id;
						root = new TreeNode(node.NPROG);
					}
				}
				break;
			case id:
				
				break;
		}
	}
	
}
