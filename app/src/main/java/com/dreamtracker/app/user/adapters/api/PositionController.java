package com.dreamtracker.app.user.adapters.api;


import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.DomainPositionService;
import com.dreamtracker.app.user.domain.ports.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1")
@RequiredArgsConstructor
public class PositionController {



    private final PositionService positionService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/position")
    public ResponseEntity<PositionResponse> getPosition(){
        return new ResponseEntity<>(
                domainPositionService.findByUserUUID(currentUserProvider.getCurrentUser()) , HttpStatus.OK);
    }

    @PostMapping("/position")
    public ResponseEntity<PositionResponse> createPosition(@RequestBody PositionRequest positionRequest){
        return new ResponseEntity<>(
                domainPositionService.create(positionRequest), HttpStatus.CREATED);
    }


    @PutMapping("/position-activation")
    public ResponseEntity<PositionResponse> activatePosition(@RequestBody ActivationRequest activationRequest){
        return new ResponseEntity<>(
                domainPositionService.changeActivation(activationRequest), HttpStatus.CREATED);
    }


    @PutMapping("/position-change")
    public ResponseEntity<PositionResponse> changePosition(@RequestBody PositionRequest positionRequest){
        return new ResponseEntity<>(
                domainPositionService.create(positionRequest), HttpStatus.CREATED);
    }

}
