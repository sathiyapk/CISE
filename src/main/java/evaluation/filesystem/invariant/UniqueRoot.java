package evaluation.filesystem.invariant;

import com.microsoft.z3.*;
import evaluation.filesystem.filesystem;
import invariant.Invariant;

public class UniqueRoot implements Invariant {

	String name;
	
	public UniqueRoot(String name){
		this.name=name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public BoolExpr getInv(Context ctx) throws Z3Exception {
		
		  Sort[] nodes = new Sort[1];
		  nodes[0] =ctx.mkUninterpretedSort(ctx.mkSymbol("Dir"));
		 //setting names
		  Symbol[] namess = new Symbol[1];
		  namess[0] =  ctx.mkSymbol("dir1");

          Expr node1=ctx.mkConst("dir1", ctx.mkUninterpretedSort(ctx.mkSymbol("Dir")) );

		  Expr[] existArgs =  new Expr[2];
	      existArgs[0]=ctx.mkConst("dir1", ctx.mkUninterpretedSort(ctx.mkSymbol("Dir")));
	      existArgs[1]=filesystem.root;
	    		
	      Expr exist= (BoolExpr) ctx.mkApp(filesystem.exists, existArgs)  ;
	    	 
	 	  Expr[] arg= new Expr[3];
	 	  arg[0]=filesystem.root;
	 	  arg[1]=ctx.mkConst("dir1", ctx.mkUninterpretedSort(ctx.mkSymbol("Dir")));
	 	  arg[2]=exist;	
	 	  Expr reachabilityTuple=filesystem.Reachability.mkDecl().apply(arg);
	 	  Expr rootReachability= ctx.mkSetMembership(reachabilityTuple,filesystem.Reachable_set);
 
          BoolExpr inv =ctx.mkForall(nodes, namess, ctx.mkAnd(new BoolExpr []
	    		  {(BoolExpr)  ctx.mkSetMembership(filesystem.root,filesystem.Dir_set), ctx.mkNot(ctx.mkEq(filesystem.root,node1 )),
        		  ctx.mkNot((BoolExpr) rootReachability) }), 1, null, null,  null, null);
        
          System.out.println("Root Uniqueness: " + inv.toString());

		  return inv;
	
	}

}
