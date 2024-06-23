package pl.networkmanager.bilka.basket.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.basket.infrastructure.dto.BasketItemAddRequestDto;

@RestController
@RequestMapping(value = "/api/v1/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketMediator basketMediator;

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestBody BasketItemAddRequestDto basketItemAddRequestDto,
            HttpServletRequest request,
            HttpServletResponse response) {
        return basketMediator.addProduct(basketItemAddRequestDto, request, response);
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestParam String uuid, HttpServletRequest request) {
        return basketMediator.delete(uuid, request);
    }

    @GetMapping
    public ResponseEntity<?> getItems(HttpServletRequest request) {
        return basketMediator.getItems(request);
    }
}
