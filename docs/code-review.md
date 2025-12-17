# Code Review Report

## Temuan
1. Potensi NPE saat mencari data dengan ref null di `PurchaseService.findByRef`.
2. Header CSV berpotensi memiliki BOM sehingga pembacaan header gagal.
3. Validasi input belum ada untuk angka dan format email/telepon.

## Perbaikan
1. Menambahkan guard `ref == null` di `PurchaseService.findByRef`.
2. Menambahkan pembersihan BOM di `PurchaseRepository.get`.
3. Menambahkan `Validation` untuk angka, email, dan telepon, serta digunakan di Form input.
