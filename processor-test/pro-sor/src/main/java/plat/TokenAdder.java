package plat;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 * @author Than
 * @package: plat
 * @className: LogClearTranslator
 * @description: TODO
 * @date: 2023/10/21 22:31
 */
public class  TokenAdder extends TreeTranslator {
    private TreeMaker treeMaker;
    private Names names;

    private String value;

    public TokenAdder() {
    }

    public TokenAdder(TreeMaker treeMaker, Names names, String value) {
        this.treeMaker = treeMaker;
        this.names = names;
        this.value=value;
    }

    @Override
    public void visitMethodDef(JCTree.JCMethodDecl tree) {

        List<JCTree.JCVariableDecl> params = tree.params;

        List<JCTree.JCVariableDecl> parameters = List.nil();//参数列表

        JCTree.JCExpression jcExpression = memberAccess(value);

        JCTree.JCArrayTypeTree jcArrayTypeTree = treeMaker.TypeArray(jcExpression);

        JCTree.JCVariableDecl param = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER|Flags.VARARGS), names.fromString("token"),jcArrayTypeTree, null);
        param.pos = tree.pos;
        parameters.addAll(params);
        parameters = parameters.append(param);
        tree.params=parameters;
        super.visitMethodDef(tree);
    }



    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(names.fromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, names.fromString(componentArray[i]));
        }
        return expr;
    }
}
