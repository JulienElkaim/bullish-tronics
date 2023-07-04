package io.github.bullishtronics.checkout.app.web;


import io.github.bullishtronics.checkout.app.security.BasicAuthBased;
import io.github.bullishtronics.checkout.core.invoice.InvoiceService;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.data.adater.InvoiceDtoAdapter;
import io.github.bullishtronics.checkout.io.invoice.InvoiceDto;
import io.github.bullishtronics.checkout.models.invoice.Invoice;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@BasicAuthBased
@RequestMapping("api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final UserService userService;
    private final InvoiceDtoAdapter invoiceDtoAdapter;

    public InvoiceController(InvoiceService invoiceService,
                             InvoiceDtoAdapter invoiceDtoAdapter,
                             UserService userService) {
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.invoiceDtoAdapter = invoiceDtoAdapter;
    }


    @GetMapping(value = "/{orderId}")
    public ResponseEntity<InvoiceDto> generateInvoice(@PathVariable String orderId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);

        Invoice invoice = invoiceService.generate(orderId, requester);
        return ResponseEntity.ok(invoice == null ? null : invoiceDtoAdapter.adapte(invoice));
    }
}
