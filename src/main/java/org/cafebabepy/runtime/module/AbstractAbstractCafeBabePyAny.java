package org.cafebabepy.runtime.module;

import org.cafebabepy.runtime.AbstractPyObject;
import org.cafebabepy.runtime.CafeBabePyException;
import org.cafebabepy.runtime.PyObject;
import org.cafebabepy.runtime.Python;
import org.cafebabepy.runtime.object.java.PyJavaFunctionObject;

import java.lang.reflect.Method;
import java.util.*;

import static org.cafebabepy.util.ProtocolNames.__call__;

/**
 * Created by yotchang4s on 2017/05/30.
 */
abstract class AbstractAbstractCafeBabePyAny extends AbstractPyObject {

    private volatile List<PyObject> bases;

    AbstractAbstractCafeBabePyAny(Python runtime) {
        super(runtime);
    }

    AbstractAbstractCafeBabePyAny(Python runtime, boolean appear) {
        super(runtime, appear);
    }

    @Override
    public void initialize() {
        defineClass();
        defineClassMembers();
    }

    @Override
    public final boolean isNone() {
        return false;
    }

    @Override
    public final boolean isNotImplemented() {
        return false;
    }

    @Override
    public final boolean isEllipsis() {
        return false;
    }

    @Override
    public List<PyObject> getBases() {
        if (this.bases == null) {
            synchronized (this) {
                if (this.bases == null) {
                    String[] baseNames = getBaseNames();
                    List<PyObject> bases = new ArrayList<>(baseNames.length);

                    for (String baseName : getBaseNames()) {
                        PyObject base = this.runtime.type(baseName, false).orElseThrow(() ->
                                new CafeBabePyException(
                                        "type '" + getName() + "' parent '" + baseName + "' is not found")
                        );

                        bases.add(base);
                    }

                    this.bases = Collections.unmodifiableList(Collections.synchronizedList(bases));
                }
            }
        }

        return this.bases;
    }

    abstract String[] getBaseNames();

    public abstract void defineClass();

    private void defineClassMembers() {
        Class<?> clazz = getClass();

        for (Class<?> c = clazz.getSuperclass(); c != Object.class; c = c.getSuperclass()) {
            defineClassMember(c);
        }

        defineClassMember(clazz);
    }

    private void defineClassMember(Class<?> clazz) {
        // Check duplicate
        Set<String> defineClassMemberNamesSet = new HashSet<>();

        for (Method method : clazz.getMethods()) {
            // Same class method only
            if (clazz != method.getDeclaringClass()) {
                continue;
            }

            DefinePyFunction definePyFunction = method.getAnnotation(DefinePyFunction.class);
            if (definePyFunction == null) {
                continue;
            }

            if (defineClassMemberNamesSet.contains(definePyFunction.name())) {
                throw new CafeBabePyException(
                        "Duplicate '" + definePyFunction.name() + "' function");
            }

            PyJavaFunctionObject f = new PyJavaFunctionObject(
                    getRuntime(),
                    this,
                    definePyFunction.name(),
                    method);

            if (__call__.equals(f.getName())) {
                f.getScope().put(__call__, f);
            }

            getScope().put(f.getName(), f);

            defineClassMemberNamesSet.add(definePyFunction.name());
        }
    }
}
