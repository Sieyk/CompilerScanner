/**
 * Created by Matthew Muller on 20/08/15.
 */
public class Token {

    public enum TokId {	TEOF,   // End of File Token
        // The 25 keywords come first
        TPROG, TENDK, TARRS, TPROC, TVARP, TVALP, TLOCL, TLOOP, TEXIT, TWHEN, TCALL, TWITH,
        TIFKW, TTHEN, TELSE, TELSF, TINPT, TPRIN, TPRLN, TNOTK, TANDK, TORKW, TXORK, TIDIV, TLENG,
        // then the operators and delimiters
        TLBRK, TRBRK, TLPAR, TRPAR, TSEMI, TCOMA, TDOTT, TASGN, TPLEQ, TMNEQ, TMLEQ, TDVEQ,
        TDEQL, TNEQL, TGRTR, TLEQL, TLESS, TGREQ, TPLUS, TSUBT, TMULT, TDIVD,
        // then the tokens (or pseudo-tokens) with non-null tuple values
        TIDNT, TILIT, TFLIT, TSTRG, TUNDF
    }

    public TokId tokenID;
    public String contents = "";

    Token(){}

    Token(String tokID, int pseudo){
        if(tokID.equalsIgnoreCase("")) {
            tokenID = TokId.TEOF;
        }

        else if(tokID.equalsIgnoreCase("program")) {
            tokenID = TokId.TPROG;
        }

        else if(tokID.equalsIgnoreCase("end")) {
            tokenID = TokId.TENDK;
        }

        else if(tokID.equalsIgnoreCase("arrays")) {
            tokenID = TokId.TARRS;
        }

        else if(tokID.equalsIgnoreCase("procedure")) {
            tokenID = TokId.TPROC;
        }

        else if(tokID.equalsIgnoreCase("var")) {
            tokenID = TokId.TVARP;
        }

        else if(tokID.equalsIgnoreCase("val")) {
            tokenID = TokId.TVALP;
        }

        else if(tokID.equalsIgnoreCase("local")) {
            tokenID = TokId.TLOCL;
        }

        else if(tokID.equalsIgnoreCase("loop")) {
            tokenID = TokId.TLOOP;
        }

        else if(tokID.equalsIgnoreCase("exit")) {
            tokenID = TokId.TEXIT;
        }

        else if(tokID.equalsIgnoreCase("when")) {
            tokenID = TokId.TWHEN;
        }

        else if(tokID.equalsIgnoreCase("if")) {
            tokenID = TokId.TIFKW;
        }

        else if(tokID.equalsIgnoreCase("then")) {
            tokenID = TokId.TTHEN;
        }

        else if(tokID.equalsIgnoreCase("else")) {
            tokenID = TokId.TELSE;
        }

        else if(tokID.equalsIgnoreCase("elsif")) {
            tokenID = TokId.TELSF;
        }

        else if(tokID.equalsIgnoreCase("call")) {
            tokenID = TokId.TCALL;
        }

        else if(tokID.equalsIgnoreCase("with")) {
            tokenID = TokId.TWITH;
        }

        else if(tokID.equalsIgnoreCase("input")) {
            tokenID = TokId.TINPT;
        }

        else if(tokID.equalsIgnoreCase("print")) {
            tokenID = TokId.TPRIN;
        }

        else if(tokID.equalsIgnoreCase("printline")) {
            tokenID = TokId.TPRLN;
        }

        else if(tokID.equalsIgnoreCase("not")) {
            tokenID = TokId.TNOTK;
        }

        else if(tokID.equalsIgnoreCase("and")) {
            tokenID = TokId.TANDK;
        }

        else if(tokID.equalsIgnoreCase("or")) {
            tokenID = TokId.TORKW;
        }

        else if(tokID.equalsIgnoreCase("xor")) {
            tokenID = TokId.TXORK;
        }

        else if(tokID.equalsIgnoreCase("div")) {
            tokenID = TokId.TIDIV;
        }

        else if(tokID.equalsIgnoreCase("length")) {
            tokenID = TokId.TLENG;
        }

        else if(tokID.equalsIgnoreCase("[")) {
            tokenID = TokId.TLBRK;
        }

        else if(tokID.equalsIgnoreCase("]")) {
            tokenID = TokId.TRBRK;
        }

        else if(tokID.equalsIgnoreCase("(")) {
            tokenID = TokId.TLPAR;
        }

        else if(tokID.equalsIgnoreCase(")")) {
            tokenID = TokId.TRPAR;
        }

        else if(tokID.equalsIgnoreCase(";")) {
            tokenID = TokId.TSEMI;
        }

        else if(tokID.equalsIgnoreCase("==")) {
            tokenID = TokId.TDEQL;
        }

        else if(tokID.equalsIgnoreCase(",")) {
            tokenID = TokId.TCOMA;
        }

        else if(tokID.equalsIgnoreCase(".")) {
            tokenID = TokId.TDOTT;
        }

        else if(tokID.equalsIgnoreCase("=")) {
            tokenID = TokId.TASGN;
        }

        else if(tokID.equalsIgnoreCase("+=")) {
            tokenID = TokId.TPLEQ;
        }

        else if(tokID.equalsIgnoreCase("!=")) {
            tokenID = TokId.TNEQL;
        }

        else if(tokID.equalsIgnoreCase("<=")) {
            tokenID = TokId.TLEQL;
        }

        else if(tokID.equalsIgnoreCase(">=")) {
            tokenID = TokId.TGREQ;
        }

        else if(tokID.equalsIgnoreCase("-=")) {
            tokenID = TokId.TMNEQ;
        }

        else if(tokID.equalsIgnoreCase("*=")) {
            tokenID = TokId.TMLEQ;
        }

        else if(tokID.equalsIgnoreCase("/=")) {
            tokenID = TokId.TDVEQ;
        }

        else if(tokID.equalsIgnoreCase("<")) {
            tokenID = TokId.TLESS;
        }

        else if(tokID.equalsIgnoreCase(">")) {
            tokenID = TokId.TGRTR;
        }

        else if(tokID.equalsIgnoreCase("/")) {
            tokenID = TokId.TDIVD;
        }

        else if(tokID.equalsIgnoreCase("+")) {
            tokenID = TokId.TPLUS;
        }

        else if(tokID.equalsIgnoreCase("-")) {
            tokenID = TokId.TSUBT;
        }

        else if(tokID.equalsIgnoreCase("*")) {
            tokenID = TokId.TMULT;
        }

        else{ //pseudo-tokens
        	if (pseudo == 1){
        		tokenID = TokId.TIDNT;
        		contents = tokID;
        	}
        	else if (pseudo == 2){
        		tokenID = TokId.TILIT;
        		contents = tokID;
        	}
			else if (pseudo == 3){
				tokenID = TokId.TFLIT;
				contents = tokID;
			}
			else if (pseudo == 4){
				tokenID = TokId.TSTRG;
				contents = tokID;
			}
			else{
				tokenID = TokId.TUNDF;
				contents = tokID;
			}
        }
    }

    public String getTokenID(){
        return tokenID.name();
    }
    public String getContents(){
        return contents;
    }
    public void setContents(String in){
    	contents = in;
    }
    public boolean EOFChecker(){
    	return (tokenID == TokId.TEOF);
    }
}
