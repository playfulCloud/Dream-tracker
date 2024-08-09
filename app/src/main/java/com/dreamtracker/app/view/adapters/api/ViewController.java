package com.dreamtracker.app.view.adapters.api;

import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import com.dreamtracker.app.view.domain.ports.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ViewController {

  private final ViewService viewService;
  private final StatsAggregator statsAggregator;

  @GetMapping("/views/stats/{habit-id}")
  public ResponseEntity<CombinedComponentResponse> getCalculatedStats(
       @PathVariable("habit-id") UUID habit ) {
    var pageResult = statsAggregator.getAggregates(habit);
    return new ResponseEntity<>(pageResult, HttpStatus.OK);
  }


  @GetMapping("/views/{view-name}")
  public ResponseEntity<ViewResponse> getViewByName(@PathVariable("view-name") String name){
        var view = viewService.getViewByName(name);
        return new ResponseEntity<>(view,HttpStatus.OK);
  }



 @PostMapping("/views")
  public ResponseEntity<ViewResponse>createView(@RequestBody ViewRequest viewRequest){
        var createdViewResponse = viewService.createView(viewRequest);
        return new ResponseEntity<>(createdViewResponse,HttpStatus.CREATED);
 }


    @PutMapping("/views")
    public ResponseEntity<ViewResponse>updateView(@RequestBody ViewRequest viewRequest){
        var createdViewResponse = viewService.updatedView(viewRequest);
        return new ResponseEntity<>(createdViewResponse,HttpStatus.OK);
    }

 @DeleteMapping("/views/{view-id}")
  public ResponseEntity<Void> deleteView(@PathVariable("view-id") UUID viewUUID){
        var isDeleted = viewService.deleteView(viewUUID);
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
 }
}
