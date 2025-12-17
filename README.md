# E-Ticket App (Proglan UAP)

## Deskripsi
Aplikasi Java Swing untuk mengelola data pembelian e-ticket. Fitur utama mencakup CRUD, sorting, searching, dan ringkasan history. Data dipersistenkan ke CSV sehingga tetap tersimpan saat aplikasi ditutup.

## Struktur Proyek
- `src/app` kode aplikasi
- `data/history.csv` data utama (CSV)
- `docs/testing.md` laporan pengujian manual
- `docs/code-review.md` laporan code review
- `assets/icons` tempat ikon jika diperlukan

## Cara Menjalankan
### Opsi 1: Jalankan dari IDE
- Buka folder `D:\Uap Proglan` di IDE.
- Jalankan `app.Main`.

### Opsi 2: Jalankan via Command Line (PowerShell)
```
# Dari folder project
Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out @sources.txt
java -cp out app.Main
```

## Fitur Utama
- Dashboard: ringkasan total transaksi dan pendapatan.
- List Data: tabel dengan sorting, searching, edit, delete.
- Input Data: form tambah/edit dengan validasi.
- History/Report: ringkasan dan daftar transaksi yang baru ditambahkan selama aplikasi berjalan.
- Persistensi CSV: data tersimpan di `data/history.csv`.

## Catatan Data
File `data/history.csv` dihasilkan dari file sumber `data/Data e-ticket.csv` (aslinya format Excel) dan sudah dikonversi ke CSV. Aplikasi hanya membaca/menulis `data/history.csv`.
