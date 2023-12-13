package io.github.mczzcs.compile.code;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.VMRuntimeException;

public interface ASTNode {
    public void executor(Executor executor)throws VMRuntimeException;
}
