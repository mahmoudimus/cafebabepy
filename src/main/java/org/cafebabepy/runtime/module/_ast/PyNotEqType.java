package org.cafebabepy.runtime.module._ast;

import org.cafebabepy.annotation.DefineCafeBabePyType;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.module.AbstractCafeBabePyType;

/**
 * Created by yotchang4s on 2017/05/29.
 */
@DefineCafeBabePyType(name = "_ast.NotEq", parent = {"_ast.cmpop"})
public class PyNotEqType extends AbstractCafeBabePyType {

    public PyNotEqType(Python runtime) {
        super(runtime);
    }
}