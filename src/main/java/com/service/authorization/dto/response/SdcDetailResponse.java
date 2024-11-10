package com.service.authorization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdcDetailResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("nama_pt")
    private String namaPT;

    @JsonProperty("kode_pt")
    private String kodePT;

    @JsonProperty("kode_prodi")
    private String kodeProdi;

    @JsonProperty("prodi")
    private String prodi;

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("nim")
    private String nim;

    @JsonProperty("jenis_daftar")
    private String jenisDaftar;

    @JsonProperty("id_pt")
    private String idPT;

    @JsonProperty("id_sms")
    private String idSMS;

    @JsonProperty("jenis_kelamin")
    private String jenisKelamin;

    @JsonProperty("jenjang")
    private String jenjang;

    @JsonProperty("status_saat_ini")
    private String statusSaatIni;

    @JsonProperty("tanggal_masuk")
    private String tanggalMasuk;
}
