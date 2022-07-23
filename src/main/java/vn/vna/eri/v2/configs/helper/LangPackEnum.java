package vn.vna.eri.v2.configs.helper;

import lombok.Getter;

@Getter
public enum LangPackEnum {
  VI_VN("vi-vn"),
  EN_US("en-us");

  LangPackEnum(String name) {
    this.name = name;
  }

  String name;
}
