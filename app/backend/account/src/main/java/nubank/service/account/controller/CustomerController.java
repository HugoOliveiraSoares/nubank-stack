package nubank.service.account.controller;

import nubank.service.account.exception.InvalidRequest;
import nubank.service.account.mq.QueueSender;
import nubank.service.account.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import nubank.service.account.dto.CustomerDTO;

@RestController
@RequestMapping("/account/customer")
public class CustomerController {

  @Autowired
  CustomerService customerService;

  @PostMapping(path = "create-new")
  public ResponseEntity<?> post(@RequestBody CustomerDTO dto) throws InvalidRequest {

    var inserted = customerService.create(dto);
    return (inserted != null) ? ResponseEntity.status(204).build() : ResponseEntity.badRequest().build();

  }

  @Autowired
  private QueueSender queueSender;

  @GetMapping(path="enqueue/{msg}")
  public ResponseEntity<?> enqueue(@PathVariable String msg)  {
    queueSender.send(msg);
    return ResponseEntity.ok().build();
  }

}
