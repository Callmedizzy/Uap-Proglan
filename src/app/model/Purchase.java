package app.model;

import app.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Objects;

public class Purchase {
    private String ref;
    private String eventName;
    private String ticketType;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String paymentMethod;
    private String status;
    private String checkInStatus;
    private long totalPaid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentType;
    private String bank;
    private int quantity;
    private LocalDateTime exportedAt;

    public Purchase() {
    }

    public Purchase(Purchase other) {
        this.ref = other.ref;
        this.eventName = other.eventName;
        this.ticketType = other.ticketType;
        this.customerName = other.customerName;
        this.customerEmail = other.customerEmail;
        this.customerPhone = other.customerPhone;
        this.paymentMethod = other.paymentMethod;
        this.status = other.status;
        this.checkInStatus = other.checkInStatus;
        this.totalPaid = other.totalPaid;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.paymentType = other.paymentType;
        this.bank = other.bank;
        this.quantity = other.quantity;
        this.exportedAt = other.exportedAt;
    }

    public Purchase(String ref, String eventName, String ticketType, String customerName, String customerEmail,
                    String customerPhone, String paymentMethod, String status, String checkInStatus, long totalPaid,
                    LocalDateTime createdAt, LocalDateTime updatedAt, String paymentType, String bank, int quantity,
                    LocalDateTime exportedAt) {
        this.ref = ref;
        this.eventName = eventName;
        this.ticketType = ticketType;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.checkInStatus = checkInStatus;
        this.totalPaid = totalPaid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paymentType = paymentType;
        this.bank = bank;
        this.quantity = quantity;
        this.exportedAt = exportedAt;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public long getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(long totalPaid) {
        this.totalPaid = totalPaid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getExportedAt() {
        return exportedAt;
    }

    public void setExportedAt(LocalDateTime exportedAt) {
        this.exportedAt = exportedAt;
    }

    public Purchase copy() {
        return new Purchase(this);
    }

    public String[] toCsvRow() {
        return new String[] {
                ref,
                eventName,
                ticketType,
                customerName,
                customerEmail,
                customerPhone,
                paymentMethod,
                status,
                checkInStatus,
                String.valueOf(totalPaid),
                DateUtil.formatDateTime(createdAt),
                DateUtil.formatDateTime(updatedAt),
                paymentType,
                bank,
                String.valueOf(quantity),
                DateUtil.formatDateTime(exportedAt),
                "",
                "",
                ""
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Purchase)) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        return Objects.equals(ref, purchase.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref);
    }
}

