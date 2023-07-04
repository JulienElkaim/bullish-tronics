package io.github.bullishtronics.checkout.core.invoice;

import io.github.bullishtronics.checkout.models.invoice.Invoice;
import io.github.bullishtronics.checkout.models.user.User;

public interface InvoiceService {
    Invoice generate(String orderId, User requester);
}
