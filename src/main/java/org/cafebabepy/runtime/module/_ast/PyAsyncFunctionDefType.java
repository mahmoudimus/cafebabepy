package org.cafebabepy.runtime.module._ast;

import org.cafebabepy.runtime.PyObject;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.module.DefinePyFunction;
import org.cafebabepy.runtime.module.DefinePyType;

import static org.cafebabepy.util.ProtocolNames.__init__;

/**
 * Created by yotchang4s on 2018/10/24.
 */
@DefinePyType(name = "_ast.AsyncFunctionDef", parent = {"_ast.stmt"})
public class PyAsyncFunctionDefType extends AbstractAST {

    public PyAsyncFunctionDefType(Python runtime) {
        super(runtime);
    }

    @DefinePyFunction(name = __init__)
    public void __init__(PyObject self, PyObject... args) {
        if (args.length == 0) {
            return;
        }

        self.getFrame().putToLocals("name", args[0]);
        self.getFrame().putToLocals("args", args[1]);
        self.getFrame().putToLocals("body", args[2]);
        self.getFrame().putToLocals("decorator_list", args[3]);
        self.getFrame().putToLocals("returns", args[4]);
    }

    @Override
    String[] _fields() {
        return new String[]{"name", "args", "body", "decorator_list", "returns"};
    }
}
