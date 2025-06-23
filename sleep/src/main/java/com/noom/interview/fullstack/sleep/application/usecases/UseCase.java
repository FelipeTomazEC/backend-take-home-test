package com.noom.interview.fullstack.sleep.application.usecases;

public interface UseCase<Operation, Output> {
    Output execute(Operation operation);
}
