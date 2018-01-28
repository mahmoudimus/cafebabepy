package org.cafebabepy.runtime.module;

import org.cafebabepy.runtime.CafeBabePyException;
import org.cafebabepy.runtime.PyObject;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.util.StringUtils;

import static org.cafebabepy.util.ProtocolNames.__call__;

/**
 * Created by yotchang4s on 2017/05/30.
 */
public abstract class AbstractCafeBabePyType extends AbstractAbstractCafeBabePyAny {

    private PyObject module;

    private String name;

    private String[] baseNames;

    protected AbstractCafeBabePyType(Python runtime) {
        super(runtime);
    }

    @Override
    final String[] getBaseNames() {
        return this.baseNames;
    }

    @Override
    public void defineClass() {
        Class<?> clazz = getClass();

        DefinePyType definePyType = clazz.getAnnotation(DefinePyType.class);
        if (definePyType == null) {
            throw new CafeBabePyException(
                    "DefinePyModule annotation is not defined " + clazz.getName());
        }

        this.baseNames = definePyType.parent();

        String[] splitStr = StringUtils.splitLastDot(definePyType.name());

        if (StringUtils.isEmpty(splitStr[0])) {
            throw new CafeBabePyException("name '"
                    + definePyType.name()
                    + "' is not found module");
        }

        this.module = this.runtime.moduleOrThrow(splitStr[0]);
        this.name = splitStr[1];
        this.module.getScope().put(this.name, this, definePyType.appear());
    }

    @Override
    public final PyObject getType() {
        return this.runtime.typeOrThrow("builtins.type");
    }

    @Override
    public final PyObject getModule() {
        return this.module;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public final boolean isType() {
        return true;
    }

    @Override
    public final boolean isModule() {
        return false;
    }

    @Override
    public PyObject call(PyObject... args) {
        return this.runtime.typeOrThrow("builtins.type").getScope().get(__call__).orElseThrow(() ->
                new CafeBabePyException("type " + __call__ + " is not foud")
        ).call(this, args);
    }
}
