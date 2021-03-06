package org.cafebabepy.runtime.module._ast;

import org.cafebabepy.runtime.PyObject;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.module.DefinePyFunction;
import org.cafebabepy.runtime.module.DefinePyType;

import static org.cafebabepy.util.ProtocolNames.__init__;

/**
 * Created by yotchang4s on 2017/08/05.
 */
@DefinePyType(name = "_ast.JoinedStr", parent = {"_ast.expr"})
public class PyJoinedStrType extends AbstractAST {

    public PyJoinedStrType(Python runtime) {
        super(runtime);
    }

    @DefinePyFunction(name = __init__)
    public void __init__(PyObject self, PyObject... args) {
        if (args.length == 0) {
            return;
        }

        self.getFrame().getLocals().put("values", args[0]);
    }

    @Override
    String[] _fields() {
        return new String[]{"values"};
    }
}
