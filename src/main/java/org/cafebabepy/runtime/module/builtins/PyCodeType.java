package org.cafebabepy.runtime.module.builtins;

import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.module.AbstractCafeBabePyType;
import org.cafebabepy.runtime.module.DefinePyType;

/**
 * Created by yotchang4s on 2018/05/18.
 */
@DefinePyType(name = "builtins.code", appear = false)
public class PyCodeType extends AbstractCafeBabePyType {

    // FIXME code is unknown
    public PyCodeType(Python runtime) {
        super(runtime, false);
    }
}
