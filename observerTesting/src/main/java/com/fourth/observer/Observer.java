package com.fourth.observer;

import com.fourth.componentResponse.ComponentResponseContainer;

public interface Observer {
    ComponentResponseContainer update(String name);
}
