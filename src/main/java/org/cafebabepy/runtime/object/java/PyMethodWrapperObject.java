package org.cafebabepy.runtime.object.java;

import org.cafebabepy.runtime.PyFunctionObject;
import org.cafebabepy.runtime.PyObject;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.object.AbstractPyObjectObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.cafebabepy.util.ProtocolNames.__call__;

/**
 * Created by yotchang4s on 2018/06/24.
 */
public class PyMethodWrapperObject extends AbstractPyObjectObject implements PyFunctionObject {

    private final PyObject source;

    private final PyFunctionObject function;

    public PyMethodWrapperObject(Python runtime, PyFunctionObject function, PyObject source) {
        super(runtime);

        this.function = function;
        this.source = source;

        getFrame().getLocals().put(__call__, this);
    }

    public PyObject getSource() {
        return this.source;
    }

    public PyObject getFunction() {
        return this.function;
    }

    @Override
    public List<String> getArguments() {
        return this.function.getArguments();
    }

    @Override
    public PyObject call(PyObject... args) {
        return call(args, new LinkedHashMap<>());
    }

    @Override
    public PyObject call(PyObject[] args, LinkedHashMap<String, PyObject> keywords) {
        PyObject[] newArgs = new PyObject[args.length + 1];
        System.arraycopy(args, 0, newArgs, 1, args.length);
        newArgs[0] = this.source;

        return this.function.call(newArgs, keywords);
    }

    @Override
    public PyObject getType() {
        return this.runtime.typeOrThrow("builtins.method-wrapper", false);
    }
}
