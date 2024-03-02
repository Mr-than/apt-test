package plat;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Than
 * @package: PACKAGE_NAME
 * @className: plat.TokenSet
 * @description: TODO
 * @date: 2023/10/21 22:20
 */
@AutoService(Processor.class)
public class TokenSet extends AbstractProcessor {


        private Elements elementUtils;
        private JavacTrees javacTrees;
        private TreeMaker treeMaker;
        private Names names;

        private Trees trees;


        @Override
        public synchronized void init(ProcessingEnvironment processingEnv) {
            this.elementUtils = processingEnv.getElementUtils();

            Context context = ((JavacProcessingEnvironment) processingEnv).getContext();

            this.javacTrees = JavacTrees.instance(processingEnv);
            trees = Trees.instance(processingEnv);//通过trees可以获取到抽象语法书
            this.treeMaker = TreeMaker.instance(context);

            this.names = Names.instance(context);
            super.init(processingEnv);
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            Set<String> s = new HashSet<>();
            s.add(AutoToken.class.getCanonicalName());
            return s;
        }

        @Override
        public SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latestSupported();
        }


        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            TypeElement anno = elementUtils.getTypeElement("plat.AutoToken");
            Set<? extends Element> types = roundEnv.getElementsAnnotatedWith(anno);
            types.forEach(t -> {
                if (t.getKind() == ElementKind.METHOD) {
                    JCTree tree = (JCTree) trees.getTree(t);
                    String value = t.getAnnotation(AutoToken.class).value();
                    TokenAdder tokenAdder = new TokenAdder(treeMaker,names,value);
                    tree.accept(tokenAdder);
                }
            });
            return false;
        }




}
