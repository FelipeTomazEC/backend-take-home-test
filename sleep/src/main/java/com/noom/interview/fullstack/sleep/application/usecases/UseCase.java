package com.noom.interview.fullstack.sleep.application.usecases;

public interface UseCase<Command, Output> {
    Output execute(Command command);
}
