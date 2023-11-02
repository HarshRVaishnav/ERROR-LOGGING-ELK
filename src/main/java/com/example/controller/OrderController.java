package com.example.controller;

import com.example.customresponse.CustomResponse;
import com.example.dto.OrderDto;
import com.example.entity.Order;
import com.example.exception.OrderNotFoundException;
import com.example.service.IOrderService;
import com.fasterxml.jackson.core.JsonParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private String code;

    private Object data;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private ModelMapper modelMapper;

    @SuppressWarnings("finally")
    @GetMapping
    public ResponseEntity<Object> getAllOrder() {
        try {
            List<Order> list = iOrderService.getAllOrder();

            data = list;
            code = "SUCCESS";
        } catch (Exception e) {
            code = "EXCEPTION"; data = null;
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @PostMapping
    public ResponseEntity<Object> placeOrder(@RequestBody OrderDto OrderDto) {
        try {
            Order DtoToEntity = modelMapper.map(OrderDto, Order.class);
            Order orderEntity = iOrderService.createOrder(DtoToEntity);
            OrderDto ordertoDto = modelMapper.map(orderEntity, OrderDto.class);
            data = ordertoDto;
            code = "CREATED";
        } catch (OrderNotFoundException orderNotFoundException) {
            code = "DATA_NOT_CREATED"; data = null;
        } catch (RuntimeException order) {
            code = "RUNTIME_EXCEPTION"; data = null;
        } catch (Exception e) {
            code = "EXCEPTION"; data = null;
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @GetMapping("{orderNumber}")
    public ResponseEntity<Object> showOrderById(@PathVariable Integer orderNumber) {
        try {
            Order orderFound = iOrderService.findOrderById(orderNumber);

            data = orderFound;
            code = "SUCCESS";
        } catch (OrderNotFoundException orderNotFoundException) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException r) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception e) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @DeleteMapping("{orderNumber}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Integer orderNumber) {
        try {
            String deletedOrder = iOrderService.deleteOrder(orderNumber);
            data = deletedOrder;
            code = "SUCCESS";
        } catch (OrderNotFoundException e) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception e) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @DeleteMapping("/cancel/{orderNumber}")             //not working /u-work
    public ResponseEntity<Object> cancelOrder(@PathVariable  Integer orderNumber) {
        try {
       iOrderService.cancelOrder(orderNumber);
            data = "cancel Order";
            code = "SUCCESS";
        } catch (OrderNotFoundException e) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception e) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @PatchMapping("/updateShippingDate/{orderNumber}")
    public ResponseEntity<Object> updateShippingDate(@Valid @PathVariable Integer orderNumber,
                                                     @RequestBody @Valid Order order) {
        try {
            Order updatedDate = iOrderService.updateShippingDate(orderNumber, order);
            data = updatedDate;
            code = "SUCCESS";
        } catch (OrderNotFoundException e) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception r) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @PatchMapping("/updateStatus/{orderNumber}")
    public ResponseEntity<Object> updateStatus(@PathVariable @Valid Integer orderNumber,
                                               @RequestBody @Valid Order order) {
        try {
            Order updatedStatus = iOrderService.updateOrderStatus(orderNumber, order);
            data = updatedStatus;
            code = "SUCCESS";
        } catch (OrderNotFoundException orderNotFoundException) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception exception) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }
}




























































































/*  Converter<Order, OrderDto> populateExistingNumbers = new Converter<Order, OrderDto>() {

      @Override
      public OrderDto convert(MappingContext<Order, OrderDto> context) {
         //This custom converter replaces the one automatically created by ModelMapper,
         //So we have to map each of the Order fields as well.
         context.getDestination().setId(context.getSource().getId());
         context.getDestination().setFirstName(context.getSource().getFirstName());
         context.getDestination().setLastName(context.getSource().getLastName());
         context.getDestination().setTitle(context.getSource().getTitle());
         context.getDestination().setCompany(context.getSource().getCompany());
         context.getDestination().setNotes(context.getSource().getNotes());
         //Add an empty phone number for adding a new number
         context.getDestination().getPhoneNumbers().add(new PhoneNumberDto());
         for (PhoneNumber number : context.getSource().getPhoneNumbers()) {
            context.getDestination().getPhoneNumbers().add(new PhoneNumberDto(number.getId(), number.getType(), number.getNumber()));
         }
         return context.getDestination();
      }
   };

   Converter<OrderDto, Order> handlePhoneNumbersEntered = new Converter<OrderDto, Order>() {

      @Override
      public Order convert(MappingContext<OrderDto, Order> context) {
         //This custom converter replaces the one automatically created by ModelMapper,
         //So we have to map each of the Order fields as well.
         context.getDestination().setId(context.getSource().getId());
         context.getDestination().setFirstName(context.getSource().getFirstName());
         context.getDestination().setLastName(context.getSource().getLastName());
         context.getDestination().setTitle(context.getSource().getTitle());
         context.getDestination().setCompany(context.getSource().getCompany());
         context.getDestination().setNotes(context.getSource().getNotes());

         //if new, we just have to create phone numbers for each number, so deal with that first
         if (context.getSource().getId() == null) {
            for (PhoneNumberDto numberDto : context.getSource().getPhoneNumbers()) {
               context.getDestination().addPhoneNumber(numberDto.getType(), numberDto.getPhoneNumber());
            }
         } else {
            Order existing = OrderRepository.getOne(context.getSource().getId());
            context.getDestination().getPhoneNumbers().clear();

            for (PhoneNumberDto phoneNumDto : context.getSource().getPhoneNumbers()) {
               boolean found = false;
               //For each phone number coming in from the form, check the existing phone numbers
               //from the database.  If there's a match, update the phone number object and add it to the destination
               //phone numbers collection, unless it's deleted and then we leave it out of the collection.
               for (PhoneNumber phoneNumber : existing.getPhoneNumbers()) {
                  if (phoneNumDto.getId() != null && phoneNumDto.getId().longValue() == phoneNumber.getId().longValue()) {
                     found = true;
                     if (!phoneNumDto.isDeleted()) {
                        phoneNumber.setType(phoneNumDto.getType());
                        phoneNumber.setNumber(phoneNumDto.getPhoneNumber());
                        context.getDestination().getPhoneNumbers().add(phoneNumber);
                     }
                     break;
                  }
               }
               //For input phone numbers that aren't in the database, add it to the destination.
               if (!found && !phoneNumDto.getPhoneNumber().isEmpty()) {
                  context.getDestination().addPhoneNumber(phoneNumDto.getType(), phoneNumDto.getPhoneNumber());
               }
            }
         }

         return context.getDestination();
      }

   };
}
*/