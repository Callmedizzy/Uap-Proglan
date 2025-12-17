# Manual Testing Report

## Lingkungan Uji
- OS: Windows
- Data: `data/history.csv`
- Aplikasi: Java Swing (app.Main)

## Skenario Pengujian

### 1. Tambah Data (Create)
- Langkah:
  - Buka menu Input Data.
  - Isi field wajib (Event, Ticket Type, Customer, Email, Phone, Payment Method, Total Paid, Quantity).
  - Klik Simpan.
- Hasil yang diharapkan:
  - Data baru muncul di tabel List Data.
  - File `data/history.csv` bertambah 1 baris.
- Hasil aktual:
  - OK.

### 2. Edit Data (Update)
- Langkah:
  - Buka List Data, pilih satu baris.
  - Klik Edit dan ubah Total Paid atau Status.
  - Klik Simpan.
- Hasil yang diharapkan:
  - Nilai berubah di tabel.
  - File `data/history.csv` ter-update.
- Hasil aktual:
  - OK.

### 3. Hapus Data (Delete)
- Langkah:
  - Buka List Data, pilih satu baris.
  - Klik Hapus dan konfirmasi.
- Hasil yang diharapkan:
  - Baris hilang dari tabel.
  - File `data/history.csv` ter-update.
- Hasil aktual:
  - OK.

### 4. Sorting
- Langkah:
  - Di List Data, pilih Sort: Total (tertinggi) atau Tanggal Terbaru.
- Hasil yang diharapkan:
  - Urutan data berubah sesuai pilihan.
- Hasil aktual:
  - OK.

### 5. Searching
- Langkah:
  - Ketik keyword di Search (nama/email/ref).
- Hasil yang diharapkan:
  - Tabel ter-filter sesuai keyword.
- Hasil aktual:
  - OK.

### 6. Persistensi Data
- Langkah:
  - Lakukan tambah/edit/hapus data.
  - Tutup aplikasi dan jalankan kembali.
- Hasil yang diharapkan:
  - Perubahan tetap tersimpan.
- Hasil aktual:
  - OK.
