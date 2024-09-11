package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.user.adapters.api.ActivationRequest;
import com.dreamtracker.app.user.adapters.api.PositionChangeRequest;
import com.dreamtracker.app.user.adapters.api.PositionRequest;
import com.dreamtracker.app.user.adapters.api.PositionResponse;
import com.dreamtracker.app.user.domain.model.Position;

public interface PositionFixtures {

   default Position.PositionBuilder getPosition(){
       return Position.builder();
   }


    default PositionRequest.PositionRequestBuilder getPositionRequest(){
        return PositionRequest.builder();
    }

    default PositionResponse.PositionResponseBuilder getPositionResponse(){
        return PositionResponse.builder();
    }

    default PositionChangeRequest.PositionChangeRequestBuilder getPositionChangeRequest(){
        PositionChangeRequest.PositionChangeRequestBuilder builder = PositionChangeRequest.builder();
        return builder; 
    }
    
    default ActivationRequest.ActivationRequestBuilder getActivationRequest(){
       return ActivationRequest.builder();
    }
}
