package app.service;

import app.model.Purchase;
import app.repository.PurchaseRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class PurchaseService {
    private final PurchaseRepository repository;
    private final List<Purchase> purchases;
    private final List<Purchase> history;

    public PurchaseService(PurchaseRepository repository) {
        this.repository = repository;
        this.purchases = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    public void load() throws IOException {
        purchases.clear();
        purchases.addAll(repository.load());
    }

    public List<Purchase> getAll() {
        return new ArrayList<>(purchases);
    }

    public List<Purchase> getHistory() {
        return new ArrayList<>(history);
    }

    public Purchase findByRef(String ref) {
        if (ref == null) {
            return null;
        }
        for (Purchase purchase : purchases) {
            if (purchase.getRef().equalsIgnoreCase(ref)) {
                return purchase;
            }
        }
        return null;
    }

    public void add(Purchase purchase) throws IOException {
        if (purchase.getRef() == null || purchase.getRef().trim().isEmpty()) {
            purchase.setRef(generateRef());
        }
        if (findByRef(purchase.getRef()) != null) {
            throw new IllegalArgumentException("Ref sudah digunakan.");
        }
        if (purchase.getCreatedAt() == null) {
            purchase.setCreatedAt(LocalDateTime.now());
        }
        purchase.setUpdatedAt(LocalDateTime.now());
        purchases.add(purchase);
        repository.save(purchases);
        history.add(purchase.copy());
    }

    public void update(Purchase purchase) throws IOException {
        for (int i = 0; i < purchases.size(); i++) {
            if (purchases.get(i).getRef().equalsIgnoreCase(purchase.getRef())) {
                purchase.setUpdatedAt(LocalDateTime.now());
                purchases.set(i, purchase);
                repository.save(purchases);
                return;
            }
        }
        throw new IllegalArgumentException("Data tidak ditemukan.");
    }

    public void delete(String ref) throws IOException {
        Purchase target = null;
        for (Purchase purchase : purchases) {
            if (purchase.getRef().equalsIgnoreCase(ref)) {
                target = purchase;
                break;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException("Data tidak ditemukan.");
        }
        purchases.remove(target);
        repository.save(purchases);
    }

    public List<Purchase> getSorted(Comparator<Purchase> comparator) {
        List<Purchase> sorted = new ArrayList<>(purchases);
        sorted.sort(comparator);
        return sorted;
    }

    public void exportTo(Path path, List<Purchase> data) throws IOException {
        PurchaseRepository exporter = new PurchaseRepository(path);
        exporter.save(data);
    }

    public static Comparator<Purchase> sortByCreatedAt() {
        return Comparator.comparing(Purchase::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public static Comparator<Purchase> sortByTotalPaidDesc() {
        return Comparator.comparingLong(Purchase::getTotalPaid).reversed();
    }

    public static Comparator<Purchase> sortByCustomerName() {
        return Comparator.comparing(Purchase::getCustomerName, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Purchase> sortByTicketType() {
        return Comparator.comparing(Purchase::getTicketType, String.CASE_INSENSITIVE_ORDER);
    }

    private String generateRef() {
        String base = "T" + System.currentTimeMillis();
        Random random = new Random();
        String ref = base + (100 + random.nextInt(900));
        while (findByRef(ref) != null) {
            ref = base + (100 + random.nextInt(900));
        }
        return ref;
    }
}

