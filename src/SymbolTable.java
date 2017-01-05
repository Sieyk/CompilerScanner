import java.util.LinkedList;

public class SymbolTable {

	LinkedList<Token> ST;
	
	SymbolTable(){
		ST = new LinkedList<Token>();
	}
	
	public int lookup(Token symbol){
		if (ST.size() > 0){
			for(int i = 0; i < ST.size(); i++)
				if(ST.get(i).getTokenID().equals(symbol.getTokenID()) && ST.get(i).getContents().equals(symbol.getContents()))
					return i;
			return -1;
		}
		else
			return -1;
	}
	
	public void insert(Token symbol){
		ST.add(symbol);
	}
	
	public Token getSymbol(int in){
		return ST.get(in);
	}
	
}
