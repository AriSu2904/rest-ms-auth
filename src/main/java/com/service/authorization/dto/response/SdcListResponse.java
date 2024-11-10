package com.service.authorization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdcListResponse {
    private String id;
    private String nama;
    private String nim;

    @JsonProperty("nama_pt")
    private String namaPt;

    @JsonProperty("sinkatan_pt")
    private String sinkatanPt;

    @JsonProperty("nama_prodi")
    private String namaProdi;
}
