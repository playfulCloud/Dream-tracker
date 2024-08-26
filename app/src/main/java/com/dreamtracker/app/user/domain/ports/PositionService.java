package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.user.adapters.api.ActivationRequest;
import com.dreamtracker.app.user.adapters.api.PositionChangeRequest;
import com.dreamtracker.app.user.adapters.api.PositionRequest;
import com.dreamtracker.app.user.adapters.api.PositionResponse;

import java.util.UUID;

public  interface PositionService {
    PositionResponse create(PositionRequest positionRequest);
    PositionResponse changePosition(PositionChangeRequest positionChangeRequest);
    PositionResponse changeActivation(ActivationRequest activationRequest);
    PositionResponse findByUserUUID(UUID userUUID);
}
