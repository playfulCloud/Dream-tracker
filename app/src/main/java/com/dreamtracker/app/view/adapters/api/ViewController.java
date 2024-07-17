package com.dreamtracker.app.view.adapters.api;

import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.domain.ports.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ViewController {

  private final ViewService viewService;

  @GetMapping("/views/{view-name}")
  public ResponseEntity<CombinedComponentResponse> getView(
      @PathVariable("view-name") String viewName) {
    var pageResult = viewService.getStatsComponent(viewName);
    return new ResponseEntity<>(pageResult, HttpStatus.OK);
  }
 @PostMapping("/views")
  public ResponseEntity<ViewResponse>createView(@RequestBody ViewRequest viewRequest){
        var createdViewResponse = viewService.createView(viewRequest);
        return new ResponseEntity<>(createdViewResponse,HttpStatus.CREATED);
 }
}
