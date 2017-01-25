/**
 * 
 */
package language;

import java.util.ArrayList;

/**
 * @author Drew Buckley
 * 
 * Class for holding java keywords;
 *
 */
public class JavaExclusionList extends ExclusionList {

	
	public JavaExclusionList(){
		//primative types
		this.add("int");
		this.add("long");
		this.add("short");
		this.add("double");
		this.add("float");
		this.add("char");
		this.add("boolean");
		this.add("byte");
		
		//common objects
		this.add("String");
		
		//Memory 
		this.add("new");
		this.add("final");
		this.add("static");
		this.add("this");
		this.add("const");
		this.add("null");
		
		//visibility
		this.add("public");
		this.add("private");
		this.add("protected");
		
		//class structure
		this.add("abstract");
		this.add("class");
		this.add("extends");
		this.add("interface");
		this.add("implements");
		this.add("package");
		this.add("import");
		this.add("throws");
		this.add("enum");
		this.add("void");
		this.add("super");
		this.add("instanceof");
		this.add("native");
		
		
		//Conditionals
		this.add("if");
		this.add("while");
		this.add("else");
		this.add("for");
		this.add("default");
		this.add("try");
		this.add("catch");
		this.add("switch");
		this.add("case");
		this.add("continue");
		this.add("finally");
		this.add("default");
		this.add("false");
		this.add("true");
		
		
		//commands
		this.add("throw");
		this.add("goto");
		this.add("assert");
		this.add("return");
		this.add("do");
		this.add("sychronized");
		this.add("break");
		this.add("strictfp");
		this.add("transient");
		this.add("volatile");
		
		//chars
		this.add("(");
		this.add(")");
		this.add("{");
		this.add("}");
		this.add("=");
		this.add("-");
		this.add("*");
		this.add("+");
		this.add(";");
		this.add("\'");
		this.add("\"");
		this.add(">");
		this.add("<");
		this.add("&");
		this.add("!");
		this.add("==");
		this.add("[");
		this.add("]");
		this.add("/");
		this.add("||");
		this.add("|");
		this.add(">=");
		this.add("<=");
		
		
	}
	/* (non-Javadoc)
	 * @see language.ExclusionList#containsKeyword()
	 */
	@Override
	public boolean containsKeyword(String compare) {
		boolean contains = false;
		for(String e: this){
			if(e.equalsIgnoreCase(compare)){
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	/*
	 * (non-Javadoc)
	 * @see language.ExclusionList#isComment(java.lang.String)
	 */
	public boolean isComment(String line) {
		
		if(line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")){
			return true;
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * @see language.ExclusionList#getCommentChars()
	 */
	public ArrayList<String> getCommentChars() {
		ArrayList<String> comments = new ArrayList<String>();
		comments.add("//");
		comments.add("/*");
		comments.add("*");
		
		return comments ;
	}

}
