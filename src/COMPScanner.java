import java.io.*;

/**
 * Created by Matthew Muller on 20/08/15.
 */
public class COMPScanner {

    final String [] keywords = {"program", "end", "arrays", "procedure", "var", "val", "loop", "exit", "when",
                                "if", "then", "else", "elsif", "call", "with", "input", "print", "not", "and",
                                "or", "xor", "div", "length"};
    enum states {S, operator, zero, intLit, flLit, identOrKey, blockComment, comment, string, undef}
    private String holder;
    private String outputLine;
    private int currLine;
    private int currCol;
    private boolean nextIgnore;
    private boolean currIgnore;
    private boolean undfTokenDelimited;
    private boolean lexErrMsgFallback;
    states currState;
    boolean escape;
    boolean floatValidator;
    int tokenID;
    int i;
    boolean dontRead;
    Reader read;

    COMPScanner(String fileIn){
        holder = "";
        currLine = 1;
        currCol = 0;
        outputLine = "";
        dontRead = false;
        tokenID = 5;
        undfTokenDelimited = false;
        lexErrMsgFallback = false;
        currIgnore = false;
        nextIgnore = false;
        floatValidator = false;
        currState = states.S;
        escape = false;
        i = -1;
        try {
            File f = new File(fileIn);
            FileInputStream fis = new FileInputStream(f);
            read = new InputStreamReader(fis);
        }catch(FileNotFoundException o){}
    }

    public Token scan(){
        try {
            //System.out.println("scanning");
            holder = "";
            escape = false;
            floatValidator = false;
            //tokenID = 0;
            currIgnore = nextIgnore;
            nextIgnore = false;
            
            /*if (holder.equals(";") || holder.equals("[") || holder.equals("]") || holder.equals("\"")
                    || holder.equals("(") || holder.equals(")")) //If a delimiter delimited, this allows instant tokenization of it
            {escape = true;}*/
            
            while ((!escape || undfTokenDelimited) && ((dontRead) || (i = read.read()) != -1)) {
                //char input = (char) i;
                //System.out.print(input);
            	if (!dontRead)
            		currCol++;
            	dontRead= false;
            	if (undfTokenDelimited)
            	{
            		System.out.println();
            		System.out.print("TUNDF " + holder);
            		System.out.println();
            		currIgnore = true;
            		//nextIgnore = true;
            		holder = "";
            	}
            	undfTokenDelimited = false;
            	lexErrMsgFallback = false;
                switch (currState) {
                    case S:
                        if (i==48){ //i == 0
                        	holder+=(char)i;
                        	currState = states.zero;
                        }
                        else if(i>48&&i<58){ //i is an int other than 0
                        	holder+=(char)i;
                        	tokenID = 2;
                        	currState = states.intLit;
                        }
                        else if(Character.isLetter((char)i)){ //i is any letter
                        	holder+=(char)i;
                        	tokenID = 1;
                        	currState = states.identOrKey;
                        }
                        else if ((char)i == '.' || (char)i == ',' || (char)i == ';')
                        {
                        	holder+=(char)i;
                        	escape = true;
                        }
                        else if ((char)i == '/')
                        {
                        	currCol++;
                        	holder+=(char)i;
                        	if ((char)(i = read.read()) == '/')
                        	{
                        		holder = "";
                        		currState = states.comment;
                        	}
                        	else if((char)i == '=')
                        	{
                        		holder+=(char)i;
                        		escape = true;
                        	}
                        	else
                        	{
                        		dontRead = true;
                        		escape = true;
                        	}
                        }
                        else if ((char)i == '\"')
                        {
                        	currState = states.string;
                        }
                        else if ((char)i == '+')
                        {
                        	holder+=(char)i;
                        	currCol++;
                        	if ((char)(i = read.read()) == '=')
                        	{
                        		holder+="=";
                        		escape=true;
                        	}
                        	else if (currCol == 2 && (char)i == '/')
                        	{
                        		currCol++;
                        		holder+=(char)i;
                        		if ((char)(i = read.read()) == '+')
                            	{
                        			holder+=(char)i;
                        			if(currCol == 3)
                        			{
                        				holder="";
                        				currState = states.blockComment;
                        			}
                        			else
                        			{
                        				lexErr("Block comments must start at the beginning of a line.");
                        				tokenID = 0;
                        				dontRead = true;
                        				escape = true;
                        			}
                            	}
                        		else
                        		{
                        			lexErr("Unrecognised token \'+/\'");
                        			tokenID = 0;
                        			dontRead = true;
                        			escape = true;
                        		}
                        	}
                        	else
                        	{
                        		dontRead = true;
                        		escape = true;
                        	}
                        }
                        else if ((char)i == '*' || (char)i == '+' || (char)i == '-' ||
                        		(char)i == '<' || (char)i == '>')
                        {
                        	holder+=(char)i;
                        	currState = states.operator;
                        }
                        
                        else if((char)i == '!' || (char)i == '=')
                        {
                        	holder+=(char)i;
                        	currCol++;
                        	if ((char)i == '!')
                        	{
	                        	if ((char)(i = read.read()) == '=')
	                        	{
	                        		holder+=(char)i;
	                        		escape=true;
	                        	}
	                        	else
	                        	{
	                        		dontRead = true;
	                        		lexErr("NOT '!' operator requires '=' to follow it to be a valid symbol");
	                        		escape = true;
	                        	}
                        	}
                        	else if((char)i == '=')
                        	{
                        		if((char)(i = read.read()) == '=')
                        		{
                        			holder+=(char)i;
                        			escape = true;
                        		}
                        		else if ((char)i == ' ' || (char)i == '\n' || (char)i == '\t'|| (char)i == '\r')
                            	{
                            		newLineSpace(i);
                            		currState = states.S;
                            		escape = true;
                            	}
                        		else
                        		{
                        			dontRead = true;
                        			escape = true;
                        		}
                        	}
                        }
                        else if ((char)i == ';' || (char)i == '[' || (char)i == ']'
                                || (char)i == '(' || (char)i == ')') { //Delimiters
                            holder += (char)i;
                            escape = true;
                        }
                        else if ((char)i == ' ' || (char)i == '\n' || (char)i == '\t' || (char)i == '\r'){	//Delimiters and line/column counting
                        	newLineSpace(i);
                        }
                        else
                        {
                        	holder+=(char)i;
                        	tokenID = 0;
                        	escape = true;
                        	lexErr("Unknown Character.");
                        }
                        break;
                        
                    case comment:
                    	if ((char)i != '\n')
                    	{
                    		
                    	}
                    	else
                    	{
                    		currState = states.S;
                    		currLine++;
                    		currCol = 0;
                    	}
                    	break;
                    	
                    case string:
                    	tokenID = 4;
                    	if ((char)i != '\"' && (char)i != '\n' && (char)i != '\r')
                    	{
                    		holder+=(char)i;
                    	}
                    	else if ((char)i == '\n' || (char)i == '\r')
                    	{
                    		tokenID = 0;
                    		currState = states.S;
                    		lexErr("Strings may not be delimited by new lines.");
                    		escape = true;
                    	}
                    	else
                    	{
                    		currState = states.S;
                    		escape = true;
                    	}
                    	break;
                    	
                    case identOrKey:
                    	tokenID = 1;
                    	if (Character.isLetter((char)i) || (i>=48&&i<58))
                    	{
                    		holder+=(char)i;
                    	}
                    	else if((char)i == ' ' || (char)i == '\n' || (char)i == '\t' || (char)i == '\r')
                    	{
                    		newLineSpace(i);
                    		escape = true;
                    		currState = states.S;
                    	}
                    	else
                    	{
                    		dontRead = true;
                    		escape = true;
                    		currState = states.S;
                    	}
                    	break;
                    	
                    case blockComment:
                    	if ((char)i == '\n')
                    	{
                    		currCol = 0;
                    		currLine ++;
                    	}
                    	if ((char)i == '+')
                    	{
                    		currCol++;
                    		if ((char)(i = read.read()) == '/')
                        	{
                    			currCol++;
                    			if ((char)(i = read.read()) == '+' && currCol == 3)
                            	{
                            		currState = states.S;
                            	}
                        	}
                    	}
                    	break;
                    	
                    case operator:
                    	if ((char)i == '=')
                    	{
                    		holder+=(char)i;
                    		currState = states.S;
                    		escape = true;
                    	}
                    	else if ((char)i == ' ' || (char)i == '\n' || (char)i == '\t' || (char)i == '\r')
                    	{
                    		newLineSpace(i);
                    		currState = states.S;
                    		escape = true;
                    	}
                    	else
                    	{
                    		dontRead = true;
                    		currState = states.S;
                    		escape = true;
                    	}
                        break;
                        
                    case zero:
                    	tokenID = 2;
                    	if ((char)i == '.'){
                    		holder+=(char)i;
                    		tokenID = 3;
                    		currState = states.flLit;
                    	}
                    	else if (i>=48&&i<58)
                    	{
                    		dontRead = true;
                    		escape = true;
                    		tokenID = 0;
                    		lexErr("Integers containing more than one digit cannot start with 0");
                    		currState = states.S;
                    	}
                    	else if ((char)i == ' ' || (char)i == '\n' || (char)i == '\t' || (char)i == '\r')
                    	{
                    		tokenID = 2;
                    		newLineSpace(i);
                    		currState = states.S;
                    		escape = true;
                    	}
                    	else if (Character.isLetter((char)i))
                    	{
                    		currState = states.undef;
                    		holder+=(char)i;
                    	}
                    	else
                    	{
                    		dontRead = true;
                    		escape = true;
                    		currState = states.S;
                    	}
                    	
                    	break;
                    	
                    case intLit:
                    	tokenID = 2;
                    	if (i>=48&&i<58) //i is an int
                    	{
                    		holder+=(char)i;
                    	}
                    	else if ((char)i == '.')
                    	{
                    		holder+=(char)i;
                    		tokenID = 3;
                    		currState = states.flLit;
                    	}
                    	else if((char)i == ' ' || (char)i == '\n' || (char)i == '\t' || (char)i == '\r')
                    	{
                    		newLineSpace(i);
                    		currState = states.S;
                    		escape = true;
                    	}
                    	else if (Character.isLetter((char)i))
                    	{
                    		currState = states.undef;
                    		holder+=(char)i;
                    	}
                    	else
                    	{
                    		currState = states.S;
                    		escape = true;
                    		dontRead = true;
                    	}
                    	break;
                    	
                    case undef:
                    	tokenID = 0;
                    	if (!Character.isLetter((char)i) && !(i>=48&&i<58) && !((char)i == '.'))
                    	{
                    		lexErr("Undefined Token");
                    		dontRead = true;
                    		//escape = true;
                    		undfTokenDelimited = true;
                    		currState = states.S;
                    	}
                    	else
                    	{
                    		lexErrMsgFallback = true;
                    		holder += (char)i;
                    	}
                    	break;
                    	
                    case flLit:
                    	tokenID = 3;
                    	if (i>=48&&i<58) //i is an int
                    	{
                    		holder+=(char)i;
                    		floatValidator = true;
                    	}
                    	else if((char)i == '.')
                    	{
                    		lexErr("Floats can not contain more than one dot '.'");
                    		dontRead = true;
                    		escape = true;
                    	}
                    	else if (Character.isLetter((char)i))
                    	{
                    		currState = states.undef;
                    		holder += (char)i;
                    	}
                    	else if (!(i>=48&&i<58) && !floatValidator)
                    	{
                    		lexErr("Unexpected end of float.");
                    		tokenID = 0;
                    		dontRead = true;
                    		escape = true;
                    	}
                    	else if ((char)i == ' ' || (char)i == '\n' || (char)i == '\t' || (char)i == '\r')
                    	{
                    		newLineSpace(i);
                    		currState = states.S;
                    		escape = true;
                    	}
                    	else
                    	{
                    		currState = states.S;
                    		escape = true;
                    		dontRead = true;
                    	}
                    	break;
                }
            }
        }catch(Throwable e){ 
        	//System.out.println("End of file reached."); 
        }
        
        if (lexErrMsgFallback)
        {
        	lexErr("Undefined Token");
        	System.out.println();
        	lexErrMsgFallback = false;
        }
        return new Token(holder, tokenID);
    }

    public void printToken(Token tokIn){
    	if (!tokIn.getTokenID().equals("TUNDF") 
    		&& !tokIn.getTokenID().equals("TIDNT")
    		&& !tokIn.getTokenID().equals("TILIT")
    		&& !tokIn.getTokenID().equals("TFLIT")
    		&& !tokIn.getTokenID().equals("TSTRG"))
    	{
    		if(outputLine.length() <= 60)
    		{
    			System.out.print(tokIn.getTokenID()+" ");
    			outputLine += tokIn.getTokenID()+" ";
    		}
    		else
    		{
    			System.out.println();
    			outputLine = "";
    			System.out.print(tokIn.getTokenID()+" ");
    			outputLine += tokIn.getTokenID()+" ";
    		}
    	}
    	else if(tokIn.getTokenID().equals("TUNDF"))
    	{
    		if (!currIgnore)
    			System.out.println();
    		nextIgnore = true;
    		outputLine = "";
    		System.out.print(tokIn.getTokenID()+" "+tokIn.getContents());
    		System.out.println();
    	}
    	else if(tokIn.getTokenID().equals("TUNDF"))
    	{
    		if(outputLine.length() <= 60)
    		{
    			System.out.print(tokIn.getTokenID()+" \""+tokIn.getContents()+"\" ");
    			outputLine += tokIn.getTokenID()+" \""+tokIn.getContents()+"\" ";
    		}
    		else
    		{
    			System.out.println();
    			outputLine = "";
    			System.out.print(tokIn.getTokenID()+" \""+tokIn.getContents()+"\" ");
    			outputLine += tokIn.getTokenID()+" \""+tokIn.getContents()+"\" ";
    		}
    	}
    	else if (tokIn.getTokenID().equals("TSTRG"))
    	{
    		if(outputLine.length() <= 60)
    		{
    			System.out.print(tokIn.getTokenID()+" \""+tokIn.getContents()+"\" " + addSpaces((holder.length()+2) % 6));
    			outputLine += tokIn.getTokenID()+" \""+tokIn.getContents()+"\" " + addSpaces((holder.length()+2) % 6);
    		}
    		else
    		{
    			System.out.println();
    			outputLine = "";
    			System.out.print(tokIn.getTokenID()+" \""+tokIn.getContents()+"\" " + addSpaces((holder.length()+2) % 6));
    			outputLine += tokIn.getTokenID()+" \""+tokIn.getContents()+"\" " + addSpaces((holder.length()+2) % 6);
    		}
    	}
    	else
    	{
    		if(outputLine.length() <= 60)
    		{
    			System.out.print(tokIn.getTokenID()+" "+tokIn.getContents()+" " + addSpaces(holder.length() % 6));
    			outputLine += tokIn.getTokenID()+" "+tokIn.getContents()+" " + addSpaces(holder.length() % 6);
    		}
    		else
    		{
    			System.out.println();
    			outputLine = "";
    			System.out.print(tokIn.getTokenID()+" "+tokIn.getContents()+" " + addSpaces(holder.length() % 6));
    			outputLine += tokIn.getTokenID()+" "+tokIn.getContents()+" " + addSpaces(holder.length() % 6);
    		}
    	}
    }
    
    private void newLineSpace(int i)
    {
    	if ((char)i == '\n')
    	{
    		currLine++;
    		currCol=0;
    	}
    }
    
    private void lexErr(String errorMsg)
    {
    	if (!currIgnore)
    		System.out.println();
    	System.out.print("Lexical Error:" + " (Line: " + currLine + ", Column: " + (currCol - holder.length()) + ")" + " " + errorMsg + " \"" + holder + "\"");
    	if (currIgnore && nextIgnore)
    		System.out.println();
    }
    
    private String addSpaces(int i)
    {
    	String str = "";
    	for (;i<5;i++)
    	{
    		str += ' ';
    	}
    	return str;
    }
}

