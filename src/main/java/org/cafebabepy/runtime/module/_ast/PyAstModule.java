package org.cafebabepy.runtime.module._ast;

import org.cafebabepy.annotation.DefinePyModule;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.module.AbstractCafeBabePyModule;

/**
 * Created by yotchang4s on 2017/06/04.
 */
@DefinePyModule(name = "_ast")
public class PyAstModule extends AbstractCafeBabePyModule {

    public PyAstModule(Python runtime) {
        super(runtime);
    }
}
