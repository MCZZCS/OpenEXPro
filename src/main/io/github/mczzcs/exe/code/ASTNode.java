package io.github.mczzcs.exe.code;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.VMRuntimeException;

public interface ASTNode {
    public void executor(Executor executor)throws VMRuntimeException;
}
