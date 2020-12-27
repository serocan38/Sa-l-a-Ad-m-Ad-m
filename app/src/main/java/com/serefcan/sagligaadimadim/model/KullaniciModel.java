package com.serefcan.sagligaadimadim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class KullaniciModel {

    @Getter @Setter private String adi;
    @Getter @Setter private String soyadi;
    @Getter @Setter private String id;
    @Getter @Setter private int yas;
    @Getter @Setter private int boy;
    @Getter @Setter private int kilo;
    @Getter @Setter private int adimbuyuklugu;
    @Getter @Setter private String mod;
    @Getter @Setter private int hedefkilo;



}
