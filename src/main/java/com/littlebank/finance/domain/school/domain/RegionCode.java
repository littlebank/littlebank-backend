package com.littlebank.finance.domain.school.domain;

public enum RegionCode {
    서울특별시(100260),
    부산광역시(100267),
    인천광역시(100269),
    대전광역시(100271),
    대구광역시(100272),
    울산광역시(100273),
    광주광역시(100275),
    세종특별자치시(100274),
    세종시(100274),
    경기도(100276),
    강원도(100278),
    강원특별자치도(100278),
    충청북도(100280),
    충청남도(100281),
    전북특별자치도(100282),
    전라남도(100283),
    경상북도(100285),
    경상남도(100291),
    제주도(100292),
    제주특별자치도(100292);

    private final int code;

    RegionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static int getRegionCode(String regionName) {
        for (RegionCode r : values()) {
            if (regionName.contains(r.name())) {
                return r.code;
            }
        }
        return 0;
    }
}
