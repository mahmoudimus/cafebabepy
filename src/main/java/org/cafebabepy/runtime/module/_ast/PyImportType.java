package org.cafebabepy.runtime.module._ast;

import org.cafebabepy.runtime.PyObject;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.module.DefinePyFunction;
import org.cafebabepy.runtime.module.DefinePyType;

import static org.cafebabepy.util.ProtocolNames.__init__;

/**
 * Created by yotchang4s on 2018/04/27.
 */
@DefinePyType(name = "_ast.Import", parent = {"_ast.stmt"})
public class PyImportType extends AbstractAST {

    public PyImportType(Python runtime) {
        super(runtime);
    }

    @DefinePyFunction(name = __init__)
    public void __init__(PyObject self, PyObject... args) {
        if (args.length == 0) {
            return;
        }

        self.getFrame().putToLocals("names", args[0]);
    }

    @Override
    String[] _fields() {
        return new String[]{"names"};
    }
}
